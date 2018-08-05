package test;

/* ====================================================================
        Licensed to the Apache Software Foundation (ASF) under one or more
        contributor license agreements.  See the NOTICE file distributed with
        this work for additional information regarding copyright ownership.
        The ASF licenses this file to You under the Apache License, Version 2.0
        (the "License"); you may not use this file except in compliance with
        the License.  You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
        ==================================================================== */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * A rudimentary XLSX -> CSV processor modeled on the
 * POI sample program XLS2CSVmra from the package
 * org.apache.poi.hssf.eventusermodel.examples.
 * As with the HSSF version, this tries to spot missing
 * rows and cells, and output empty entries for them.
 * <p/>
 * Data sheets are read using a SAX parser to keep the
 * memory footprint relatively small, so this should be
 * able to read enormous workbooks.  The styles table and
 * the shared-string table must be kept in memory.  The
 * standard POI styles table class is used, but a custom
 * (read-only) class is used for the shared string table
 * because the standard POI SharedStringsTable grows very
 * quickly with the number of unique strings.
 * <p/>
 * For a more advanced implementation of SAX event parsing
 * of XLSX files, see {@link XSSFEventBasedExcelExtractor}
 * and {@link XSSFSheetXMLHandler}. Note that for many cases,
 * it may be possible to simply use those with a custom
 * {@link SheetContentsHandler} and no SAX code needed of
 * your own!
 */
public class XLSX2CSV {
    /**
     * Uses the XSSF Event SAX helpers to do most of the work
     * of parsing the Sheet XML, and outputs the contents
     * as a (basic) CSV.
     */
    private class SheetToCSV implements SheetContentsHandler {
        private boolean firstCellOfRow = false;
        private int currentRow = -1;
        private int currentCol = -1;

        private void outputMissingRows(int number) {
            for (int i = 0; i < number; i++) {
                for (int j = 0; j < minColumns; j++) {
                    output.append(',');
                }
                output.append('\n');
            }
        }

        @Override
        public void startRow(int rowNum) {
            // If there were gaps, output the missing rows
            outputMissingRows(rowNum - currentRow - 1);
            // Prepare for this row
            firstCellOfRow = true;
            currentRow = rowNum;
            currentCol = -1;
        }

        @Override
        public void endRow(int rowNum) {
            // Ensure the minimum number of columns
            for (int i = currentCol; i < minColumns; i++) {
                output.append(',');
            }
            output.append('\n');
        }

        @Override
        public void cell(String cellReference, String formattedValue,
                         XSSFComment comment) {
            if (firstCellOfRow) {
                firstCellOfRow = false;
            } else {
                output.append(',');
            }

            // gracefully handle missing CellRef here in a similar way as XSSFCell does
            if (cellReference == null) {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }

            // Did we miss any cells?
            int thisCol = (new CellReference(cellReference)).getCol();
            int missedCols = thisCol - currentCol - 1;
            for (int i = 0; i < missedCols; i++) {
                output.append(',');
            }
            currentCol = thisCol;

            // Number or string?
            try {
                Double.parseDouble(formattedValue);
                output.append(formattedValue);
            } catch (NumberFormatException e) {
                output.append('"');
                output.append(formattedValue);
                output.append('"');
            }
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {
            // Skip, no headers or footers in CSV
        }
    }


    ///////////////////////////////////////

    private final OPCPackage xlsxPackage;

    /**
     * Number of columns to read starting with leftmost
     */
    private final int minColumns;

    /**
     * Destination for data
     */
    private final PrintStream output;

    /**
     * Creates a new XLSX -> CSV converter
     *
     * @param pkg        The XLSX package to process
     * @param output     The PrintStream to output the CSV to
     * @param minColumns The minimum number of columns to output, or -1 for no minimum
     */
    public XLSX2CSV(OPCPackage pkg, PrintStream output, int minColumns) {
        this.xlsxPackage = pkg;
        this.output = output;
        this.minColumns = minColumns;
    }

    /**
     * Parses and shows the content of one sheet
     * using the specified styles and shared-strings tables.
     *
     * @param styles
     * @param strings
     * @param sheetInputStream
     */
    public void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            SheetContentsHandler sheetHandler,
            InputStream sheetInputStream)
            throws IOException, ParserConfigurationException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(
                    styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void process()
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            String sheetName = iter.getSheetName();
            this.output.println();
            this.output.println(sheetName + " [index=" + index + "]:");
            processSheet(styles, strings, new SheetToCSV(), stream);
            stream.close();
            ++index;
        }
    }

    public static void main(String[] args) throws Exception {
      /*  if (args.length < 1) {
            System.err.println("Use:");
            System.err.println("  XLSX2CSV <xlsx file> [min columns]");
            return;
        }*/
    	long i = System.currentTimeMillis();
        File xlsxFile = new File("C:\\Users\\Winson\\Desktop\\jxl\\testformular.xlsx");
        if (!xlsxFile.exists()) {
            System.err.println("Not found or not a file: " + xlsxFile.getPath());
            return;
        }
        
        /*int minColumns = -1;
        if (args.length >= 2)
            minColumns = Integer.parseInt(args[1]);

        // The package open is instantaneous, as it should be.
        OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ); */
        InputStream in = new FileInputStream(xlsxFile);
        OPCPackage p = OPCPackage.open(in);
        //XLSX2CSV xlsx2csv = new XLSX2CSV(p, System.out, minColumns);
        ReadExcel xlsx2csv = new ReadExcel(p, 2, 10, "Sheet1");
        xlsx2csv.process();
        p.close();
        long i1 = System.currentTimeMillis();
        List<Map<Integer, String>> ls = xlsx2csv.getLs();
        String errorMsg = validate(ls, "1", "3001");
        ls = null;
        if(errorMsg != null){
        	System.out.println(errorMsg);
        }
        
        System.out.println("读取时间: "+(i1- i)+"ms");
        long i2 = System.currentTimeMillis();
        System.out.println("处理时间: "+(i2 - i1)+"ms");
        System.out.println("处理时间: "+(i2 - i)+"ms");
    }

    private static final String PORTFOLIO_NAME = "profilo_name";
    private static final String PROJECT_CODE = "project_name";
    private static final String MACRO_SITUATION = "category";
    private static final String RESERVE_AMT = "reserve_amt";
    private static final String RESERVE_RATIO = "reserve_ratio";
    		
    
    public static Map<Integer, String> mpObj = new HashMap<Integer, String>();
    static{
    	mpObj.put(-1, "rowNum");
    	mpObj.put(0, PORTFOLIO_NAME);
    	mpObj.put(1, PROJECT_CODE);
    	mpObj.put(2, MACRO_SITUATION);
    	mpObj.put(3, "year");
    	mpObj.put(4, RESERVE_AMT);
    	mpObj.put(5, RESERVE_RATIO);
    	mpObj.put(7, "intial_date");
    	mpObj.put(9, "name");
    }
    public static Map<String, String> nameMap = new HashMap<String, String>();
    static{
    	nameMap.put(PROJECT_CODE, "项目编码");
    	nameMap.put(MACRO_SITUATION, "宏观情景");
    	nameMap.put(RESERVE_AMT, "准备金额");
    	nameMap.put(RESERVE_RATIO, "准备金率");
    	nameMap.put("intial_date", "初始日期");
    }
    
    public static String validate(List<Map<Integer, String>> ls, String sourceTable, String submit_company){
    	String errorMsg = null;
    	if(ls != null){
    		Map<String, List<Map<Integer, String>>> lsMp = new HashMap<String, List<Map<Integer, String>>>(); 
        	for(Map<Integer, String> mp : ls){
        		String rowNum = "第"+mp.get(-1)+"行:";
        		String delDRow = "，请更正或删除此行数据";
        		System.out.print(rowNum);
        		for(Entry<Integer, String> entry : mp.entrySet()){
        			System.out.print(" "+entry.getKey()+":"+entry.getValue()+" ");
        		}
        		System.out.println();
        		if(mp.size() == 1){
        			continue;
        		}
        		errorMsg = hasEmpty(mp, false, PROJECT_CODE, MACRO_SITUATION);
        		if(errorMsg != null){
        			return rowNum+errorMsg+delDRow;
        		}
        		errorMsg = hasEmpty(mp, true, RESERVE_AMT, RESERVE_RATIO);
        		if(errorMsg != null){
        			return rowNum+errorMsg+delDRow;
        		}
        		errorMsg = chkFieldVal(mp, MACRO_SITUATION, "乐观", "基准", "不利");
        		if(errorMsg != null){
        			return rowNum+errorMsg+delDRow;
        		}
        		errorMsg = chkDupVal(ls, mp);
        		if(errorMsg != null){
        			return rowNum+errorMsg+delDRow;
        		}
        		
        		String pc = getMapVal(mp, PROJECT_CODE);
            	List<Map<Integer, String>> lsm = lsMp.get(pc);
            	if(lsm == null){
            		lsm = new ArrayList<Map<Integer, String>>();
            		lsMp.put(pc, lsm);
            	}
            	lsm.add(mp);
            	
            	
        		
        		/*if(lsDepartMent != null){
        			Department dm = (Department)getMapObj(mp, mpObj, Department.class);
            		if(dm != null){
            			lsDepartMent.add(dm);
            		}
        		}*/
        	}
        	
        	for(Entry<String, List<Map<Integer, String>>> entry : lsMp.entrySet()){
        		
        	}
        }
    	return errorMsg;
    }
    
    private static String chkDupVal(List<Map<Integer, String>> ls,
			Map<Integer, String> mp) {
    	String errorMsg = null;
    	String rowNum = mp.get("-1");
    	String pn = getMapVal(mp, PORTFOLIO_NAME);
    	String pc = getMapVal(mp, PROJECT_CODE);
    	String ms = getMapVal(mp, MACRO_SITUATION);
    	String key = pc;
    	if(pn != null){
    		key = pn+"_"+pc;
    	}
    	key += "_"+ms;
    	for(Map<Integer, String> smp : ls){
    		String srowNum = smp.get(-1);
    		if(srowNum == null || srowNum.equals(rowNum)){
    			continue;
    		}
    		String spn = getMapVal(smp, PORTFOLIO_NAME);
        	String spc = getMapVal(smp, PROJECT_CODE);
        	String sms = getMapVal(smp, MACRO_SITUATION);
        	String skey = spc;
        	if(spn != null){
        		skey = spn+"_"+spc;
        	}
        	skey += "_"+sms;
        	if(key.equals(skey)){
        		return "有重复记录";
        	}
    		if(pc.equals(spc)){
    			if(pn == null && spn != null){
    				return "存在相同项目编码，但组合名称不为空的数据";
    			}
    		}
    	}
    	return errorMsg;
	}

	private static String chkFieldVal(Map<Integer, String> mp, String chkField,
			String... chkvals) {
    	String v = getMapVal(mp, chkField);
    	chkField = nameMap.get(chkField);
    	for(String cv : chkvals){
			if(cv.equals(v)){
				return null;
			}
		}
    	return chkField+"不在值域范围";
	}

	private static Object getMapObj(Map<Integer, String> mp,
			Map<Integer, String> mpObj, Class<?> c) {
    	try {
			Object o = c.newInstance();
			for(Entry<Integer, String> entry : mpObj.entrySet()){
				String v = mp.get(entry.getKey());
				if(v != null){
					setFieldVal(o, entry.getValue(), v);
				}
			}
			return o;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    private static void setFieldVal(Object o, String field, Object val){
    	if(val == null){
    		return;
    	}
    	Field f = getField(o.getClass(), field);
    	if(f != null){
    		try {
    			f.setAccessible(true);
    			if(f.getType() == Double.class){
    				if(field.toLowerCase().contains("_ratio")){
    					val = NumberFormat.getPercentInstance().parse((String)val).doubleValue();
    				}else{
    					val = NumberFormat.getNumberInstance().parse((String)val).doubleValue();
    				}
    				
    			}else if(f.getType() == Date.class){
    				SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
    				val = sf.parse((String)val);
    			}else if(f.getType() == Integer.class){
    				val = NumberFormat.getIntegerInstance().parse((String)val).intValue();
    			}
				f.set(o, val);
				f.setAccessible(false);
			} catch (IllegalArgumentException | IllegalAccessException | ParseException e) {
				e.printStackTrace();
			}
    	}
    }
    
    public static Field getField(Class<?> c, String field){
    	Field f = null;
    	try {
			f = c.getDeclaredField(field);
		} catch (NoSuchFieldException | SecurityException e) {
			Class<?> sc = c.getSuperclass();
			if(sc != null){
				f = getField(sc, field);
			} 
		}
    	return f;
    }
    
	public static String hasEmpty(Map<Integer, String> mp, boolean isNumber, String... cols){
    	String errorMsg = null;
    	String msg = "必须有值";
    	if(isNumber){
    		msg = msg+"并不能为0";
    	}
    	for(String col : cols){
    		String v = getMapVal(mp, col);
    		col = nameMap.get(col);
    		if(v == null){
    			return col+msg;
    		}
    		if(isNumber){
    			try{
    				if(v.contains("/")){
    					return col+"值无效";
    				}
    				NumberFormat nf = NumberFormat.getInstance();
    				Number dv = nf.parse(v);
    				if(dv.doubleValue() == 0){
    	        		return col+msg;
    	        	}
    			}catch(ParseException e){
        			e.printStackTrace();
        			return col+"值无效";
    			}
        	}
    	}
    	
    	return errorMsg;
    }

	public static String getMapVal(Map<Integer, String> mp, String col){
		String v = null;
		for(Entry<Integer, String> entry : mpObj.entrySet()){
			if(col.equals(entry.getValue())){
				v = mp.get(entry.getKey());
				break;
			}
		}
		return v;
	}
	
}


/*这样，在Mybatis.xml中就可以指定对role_id的引用：

@Override
    public List getDynamicPermission(String[] role_ids, String employeeid) {
        List permissions = null;
        Map params = new HashMap();
        params.put("employeeid", employeeid);
        params.put("role_ids", role_ids);
        permissions = permissionDao.getDynamicPermission(params);
        return permissions;
    }
select id="getDynamicPermission" resultType="Permission" flushCache="true"
        SELECT *
        FROM permission
        WHERE
        permission.permissionid IN (
        SELECT permission_employee.permissionid FROM permission_employee WHERE
        permission_employee.employeeid=#{employeeid}
        )
        if test="role_ids != null"
            OR
            permission.permissionid IN (
            SELECT permission_role.permissionid FROM permission_role WHERE permission_role.roleid IN
            foreach item="idItem" collection="role_ids" open="(" separator="," close=")"
                #{idItem}
            /foreach
            )
        /if
    /select*/
