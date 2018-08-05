package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.zip.ZipOutputStream;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test {

	private static String mergecellslist = "C:/Users/Winson/Desktop/jxl/test2.xlsx";
	private static String mergecellslistd = "C:/Users/Winson/Desktop/jxl/out_test2.xlsx";

	public static void main(String[] args) throws Exception {
//		test3();
//		testmultiSheet();
//		testzip();
//		testcopy();
//		testxf();
//		testlarge1();
//		testlarge2();
//		testFormular();
		/*for(int i = 0; i<100; i++){
			Double dd = Math.random();
			System.out.println((int)((dd*9+1)*1000000));
		}*/
		
		double ori = 1;
		double end = 2;
		int year = 10;
		double rate = 0.01;
		double chkend = 1;
		while(chkend < end){
			chkend = ori * Math.pow((1+rate), year);
			rate +=  0.01;
		}
		System.out.println(rate);
		System.out.println(Math.pow(3, 2));
	}

	
	private static void testFormular() throws ParsePropertyException, InvalidFormatException, IOException {
	/*	 List departmens = new ArrayList();
         // initilize list of departments in some way
         Map beans = new HashMap();
         departmens.add(2);
         departmens.add(5);
         departmens.add(8);
         beans.put("dd", departmens);
		 XLSTransformer transformer = new XLSTransformer();
         transformer.transformXLS("C:/Users/Winson/Desktop/jxl/testformular.xlsx", beans, mergecellslistd);
         System.out.println("f");*/
		

		Workbook  wb = new XSSFWorkbook(new FileInputStream("C:/Users/Winson/Desktop/jxl/testformular.xlsx"));
		Sheet sh = wb.getSheetAt(0);
		String[] strs = new String[]{"aa","bb","cc","dd","ee","ff","gg","hh","ii"};
        DataValidationHelper dvHelper = sh.getDataValidationHelper();
        DataValidationConstraint constrain = dvHelper.createExplicitListConstraint(strs);
        CellRangeAddressList adl = new CellRangeAddressList(0, 10, 0, 0);
        DataValidation val = dvHelper.createValidation(constrain, adl);
        val.setSuppressDropDownArrow(true);
        val.setShowErrorBox(true);;
        sh.addValidationData(val);
		
        System.out.println(sh.getLastRowNum());
        setCellVal(sh, 0, 0, "a");
        setCellVal(sh, 0, 4, 2);
        setCellVal(sh, 0, 20, 3);
        setCellVal(sh, 1000, 4, 3);
        setCellVal(sh, 0, 1, 1112.10);
        System.out.println(sh.getLastRowNum());
        setCellVal(sh, 1, 1, 112502.10);
        System.out.println(sh.getLastRowNum());
        setCellVal(sh, 3, 1, 1502);
		Cell cf = getCell(sh, 0, 2);
		Cell cf2 = getCell(sh, 1, 2);
		sh.getDataValidations();
		cf2.setCellFormula(cf.getCellFormula().replace(1+"", 1+1+""));
		wb.setForceFormulaRecalculation(true);
		FileOutputStream destOutputStream = new FileOutputStream("C:/Users/Winson/Desktop/jxl/out_testformular.xlsx");
		wb.write(destOutputStream);
		 System.out.println(sh.getLastRowNum());
		destOutputStream.close();
		System.out.println("f");
	}
	
	private static void setSameFormular(int r, int c){
		
	}
	
	private static void setCellVal(Sheet sh, int r, int c, Object v){
		if(v instanceof Integer){
			getCell(sh, r, c).setCellValue((Integer)v);
		}else if(v instanceof Double){
			getCell(sh, r, c).setCellValue((Double)v);
		}else{
			getCell(sh, r, c).setCellValue((String)v);
		}
		
	}

	private static Cell getCell(Sheet sh, int r, int c){
		Row row = sh.getRow(r);
		if(row == null){
			System.out.println("Emp Row "+r);
			row = sh.createRow(r);
		}
		Cell cell = row.getCell(c);
		if(cell == null){
			System.out.println("Emp Cell "+r+" : "+c);
			cell = row.createCell(c);
		}
		return cell;
	}

	private static void testlarge2() throws Exception {

		String multime = "3";
		String lenght = "3000";
		
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 11030);
		mp.put("asset_status", "0");
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 12120);
		mp.put("asset_status", "1");
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 0.21);
		mp.put("C", 0.11);
		mp.put("asset_status", "2");
		ls.add(mp);
		int i = 0;
		double ii = (Double.valueOf(multime))*10000;
		Date date0 = new Date();
		for(;i<ii;i++){
			mp = new HashMap<String, Object>();
			mp.put("A", "A"+i);
			mp.put("B", i);
			mp.put("C", i%6*0.1);
			mp.put("asset_status", "");
			for(int x=0; x<100; x++){
				mp.put("A"+x, "a"+x);
			}
			ls.add(mp);
		}
//		ls = null;
		Date date1 = new Date();
		System.out.println("start................"+((date1.getTime()-date0.getTime())/1000));
		InputStream is = new FileInputStream(new File(
				mergecellslist));
		
		FileOutputStream os = new FileOutputStream("C:\\Users\\Winson\\Desktop\\www\\宽表.zip");
		 ExcelUtil.exportLargeExcel(ls, Integer.valueOf(lenght), "宽表", is, os, 3);
		 
          Date date2 = new Date();
          System.out.println("complete................"+((date2.getTime()-date1.getTime())/1000));
  		
	
	
	}
	
	private static void testlarge1() throws Exception {

		String multime = "3";
		
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 11030);
		mp.put("asset_status", "0");
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 12120);
		mp.put("asset_status", "1");
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 0.21);
		mp.put("C", 0.11);
		mp.put("asset_status", "2");
		ls.add(mp);
		int i = 0;
		double ii = (Double.valueOf(multime))*10000;
		Date date0 = new Date();
		for(;i<ii;i++){
			mp = new HashMap<String, Object>();
			mp.put("A", "A"+i);
			mp.put("B", i);
			mp.put("C", i%6*0.1);
			mp.put("asset_status", "");
			for(int x=0; x<100; x++){
				mp.put("A"+x, "a"+x);
			}
			ls.add(mp);
		}
//		ls = null;
		Date date1 = new Date();
		System.out.println("start................"+((date1.getTime()-date0.getTime())/1000));
		InputStream is = new FileInputStream(new File(
				mergecellslist));
		
		FileOutputStream os = new FileOutputStream(mergecellslistd);
		 ExcelUtil.exportLargeExcel(is, os, ls, 3);
		 
          Date date2 = new Date();
          System.out.println("complete................"+((date2.getTime()-date1.getTime())/1000));
  		
	
	}

	private static void testxf() throws Exception {
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 11030);
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 12120);
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 0.21);
		mp.put("C", 0.11);
		ls.add(mp);
		int i = 0;
		int ii = 3*10000;
		Date date0 = new Date();
		for(;i<ii;i++){
			mp = new HashMap<String, Object>();
			mp.put("A", "A"+i);
			mp.put("B", i);
			mp.put("C", i%6*0.1);
			for(int x=0; x<100; x++){
				mp.put("A"+x, "a");
			}
			ls.add(mp);
		}
		Date date1 = new Date();
		System.out.println("start................"+((date1.getTime()-date0.getTime())/1000));
		InputStream is = new BufferedInputStream(new FileInputStream(new File(
				mergecellslist)));
		 int rowaccess=300;//内存中缓存记录行数
		 
		 Map<String, Integer> vmp = new HashMap<String, Integer>();
		 vmp.put("A", 0);
		 vmp.put("B", 1);
		 vmp.put("C", 2);
		 i = 0;
		 for(;i<100;i++){
			 int x = i+3;
			 vmp.put("A"+x, x);
		 }
		 
		 XSSFWorkbook wb = new XSSFWorkbook(is);
          /*keep 100 rowsin memory,exceeding rows will be flushed to disk*/
          SXSSFWorkbook swb = new SXSSFWorkbook(wb, rowaccess); 
          int r = 3;
          SXSSFSheet sheet = swb.getSheetAt(0);
          CellStyle style = swb.createCellStyle();
    	  style.setFillForegroundColor(IndexedColors.TAN.index);
    	  style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    	  style.setBorderBottom(BorderStyle.THIN);
          for(Map<String, Object> m : ls){
        	  SXSSFRow row =  sheet.getRow(r);
        	  if(row == null){
        		  System.out.println(r);
        		  row = sheet.createRow(r);
        	  }
        	  for(Entry<String, Object> e : m.entrySet()){
        		  Integer c = vmp.get(e.getKey());
        		  if(c == null){
        			  continue;
        		  }
        		  SXSSFCell cell = row.getCell(c);
        		  if(cell == null){
        			  cell = row.createCell(c);
        		  }
        		  cell.setCellStyle(style);
        		  Object v = e.getValue();
        		  if(v != null){
        			  if(v instanceof String){
        				  cell.setCellValue((String)e.getValue());
        			  }else if(v instanceof Integer){
        				  cell.setCellValue((Integer)e.getValue());
        			  }else{
        				  cell.setCellValue((Double)e.getValue());
        			  }
        		  }
        		 
        	  }
        	 r++; 
          }

          /*写数据到文件中*/
          FileOutputStream os = new FileOutputStream(mergecellslistd);    
          swb.write(os);
          os.close();	
          swb.dispose();
          Date date2 = new Date();
          System.out.println("complete................"+((date2.getTime()-date1.getTime())/1000));
  		
	}
	
	
	private static void testzip() {
		 String sourceFilePath = "C:\\Users\\Winson\\Desktop\\jxl";  
	        String zipFilePath = "C:\\Users\\Winson\\Desktop\\www";  
	        String fileName = "lp20120301";  
	        boolean flag = ExcelUtil.fileToZip(sourceFilePath, zipFilePath, fileName);  
	        if(flag) {  
	            System.out.println(">>>>>> 文件打包成功. <<<<<<");  
	        } else {  
	            System.out.println(">>>>>> 文件打包失败. <<<<<<");  
	        }  
	}

	private static void testcopy() throws Exception {
		Map beans = new HashMap();
		
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 11030);
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 12120);
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 0.21);
		mp.put("C", 0.11);
		ls.add(mp);
		int i = 0;
		int last = 0;
		double ii = 5*10000;
		Date date0 = new Date();
		OutputStream os = new FileOutputStream("C:\\Users\\Winson\\Desktop\\www\\宽表.zip");
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(os));
		for(;i<ii;i++){
			mp = new HashMap<String, Object>();
			mp.put("A", "A"+i);
			mp.put("B", i);
			mp.put("C", i%6*0.1);
			for(int x=0; x<100; x++){
				mp.put("A"+x, "a");
			}
			ls.add(mp);
			if(i == 0){
				continue;
			}
			if(i%3000 == 0){
				if(i == 3000){
					beans.put("report", ls);
					InputStream is = new BufferedInputStream(new FileInputStream(new File(
							mergecellslist)));
					XLSTransformer transformer = new XLSTransformer();
					ExcelUtil.fileToZip(transformer.transformXLS(is, beans), zos, "宽表", i, last);
					is.close();
					ls = new ArrayList<Map<String, Object>>();
					
				}else{
					beans.put("report", ls);
					InputStream is = new BufferedInputStream(new FileInputStream(new File(
							mergecellslist)));
					XLSTransformer transformer = new XLSTransformer();
					ExcelUtil.fileToZip(transformer.transformXLS(is, beans), zos, "宽表", i, last);
					is.close();
					ls = new ArrayList<Map<String, Object>>();
				}
				last = i;
				Date date1 = new Date();
				System.out.println("start...."+i+"_"+((date1.getTime()-date0.getTime())/1000));
			}
		}
		if(!ls.isEmpty()){
			beans.put("report", ls);
			InputStream is = new BufferedInputStream(new FileInputStream(new File(
					mergecellslist)));
			XLSTransformer transformer = new XLSTransformer();
			ExcelUtil.fileToZip(transformer.transformXLS(is, beans), zos, "宽表", i, last);
			is.close();
			ls = new ArrayList<Map<String, Object>>();
		}
		zos.flush();
		zos.close();
		Date date2 = new Date();
	    System.out.println("complete................"+((date2.getTime()-date0.getTime())/1000));
		
	
	}

	private static void testmultiSheet() throws Exception{
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 11030);
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 12120);
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 0.21);
		mp.put("C", 0.11);
		ls.add(mp);
		int i = 0;
		int ii = 2*10000;
		Date date0 = new Date();
		for(;i<ii;i++){
			mp = new HashMap<String, Object>();
			mp.put("A", "A"+i);
			mp.put("B", i);
			mp.put("C", i%6*0.1);
			for(int x=0; x<100; x++){
				mp.put("A"+x, "a");
			}
			ls.add(mp);
		}
		Date date1 = new Date();
		System.out.println("start................"+((date1.getTime()-date0.getTime())/1000));
		ExcelUtil.generateExcelByTemplate(mergecellslistd,  mergecellslist, ls, "report", 3000);
		Date date2 = new Date();
	    System.out.println("complete................"+((date2.getTime()-date1.getTime())/1000));
		
	}
	
	private static void test3() throws Exception {
		Map beans = new HashMap();
		
		String d = "2016-01";
		String d1 = "2016-02";
		String fp = "第一阶段";
		String sp = "第二阶段";
		String cat1 = "低风险债券";
		String cat2 = "零售固收";
		List<ReportCommon> l = new ArrayList<>();
		ReportCommon com = new ReportCommon(fp+"_"+cat1, fp, cat1, null, 2d, null, 3d, null);
		l.add(com);
		com = new ReportCommon(sp+"_"+cat1, sp, cat1, null, 1d, null, 3d, null);
		l.add(com);
		
		List<ReportCommon> l2 = new ArrayList<>();
		ReportCommon com1 = new ReportCommon(sp+"_"+cat2, sp, cat2, null, 10d, null, 2d, null);
		l2.add(com1);
		com1 = new ReportCommon(sp+"_"+cat1, sp, cat1, null, 5d, null, 2d, null);
		l2.add(com1);
		
		ReportParent parent = setReport(d, d1, l, l2);
		beans.put("report", parent);
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 11030);
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 12120);
		ls.add(mp);
		
		mp = new HashMap<String, Object>();
		mp.put("A", "A");
		mp.put("B", 0.21);
		mp.put("C", 0.11);
		ls.add(mp);
		int i = 0;
		int ii = 1*10000;
		Date date0 = new Date();
		for(;i<ii;i++){
			mp = new HashMap<String, Object>();
			mp.put("A", "A"+i);
			mp.put("B", i);
			mp.put("C", i%6*0.1);
			for(int x=0; x<100; x++){
				mp.put("A"+x, "a");
			}
			ls.add(mp);
		}
		Date date1 = new Date();
		System.out.println("start................"+((date1.getTime()-date0.getTime())/1000));
		beans.put("report", ls);
		InputStream is = new BufferedInputStream(new FileInputStream(new File(
				mergecellslist)));
		XLSTransformer transformer = new XLSTransformer();
		Workbook resultWorkbook = transformer.transformXLS(is, beans);
		is.close();
		OutputStream os = new FileOutputStream(mergecellslistd);
		resultWorkbook.write(os);
       os.flush();
       os.close();
       Date date2 = new Date();
       System.out.println("complete................"+((date2.getTime()-date1.getTime())/1000));
	}

	private static ReportParent setReport(String d, String d1,
			List<ReportCommon>  lscom, List<ReportCommon> lscom1) {
		ReportParent parent = new ReportParent();
		//年度
		List<String> years = new ArrayList<String>();
		parent.setYears(years); 
		years.add(d);
		if(d1 != null){
			String dd = d +" - "+d1;
			years.add(d1);
			years.add(dd);
		}
		//查出所有排序
		List<String> rankCats = new ArrayList<>();
		List<String> cats1 = new ArrayList<>();
		List<String> cats2 = new ArrayList<>();
		
		for(ReportCommon c : lscom){
			String rc = c.getRank_category();
			String c1 = c.getCategory1();
			if(c1 == null){
				if(cats1.contains(rc)){
					continue;
				}
				rankCats.add(rc);
			}else{
				if(c1.contains(c1)){
					continue;
				}
				cats1.add(c1);
			}
			
		}
		
		if(d1 != null){
			for(ReportCommon c : lscom1){
				String rc = c.getRank_category();
				if(rankCats.contains(rc)){
					continue;
				}
				rankCats.add(rc);
			}
		}
		//总计
		parent.setLsTtl(getTtlVal(d1, lscom, lscom1));
		
		//按最细维度设置
		int index = 0;
		if(!cats1.isEmpty()){
			index = 1;
		}
		parent.setLsRrowExcel
		(getLsRow(rankCats, 1, parent.getLsTtl().get(0), parent.getLsTtl().get(1), d1, lscom, lscom1));
		
		return parent;
	}

	private static List<RowExcel> 
	getLsRow(List<String> cats, int index, 
			ReportCommon ttl, ReportCommon ttl1, 
			String d1, List<ReportCommon>  lscom, List<ReportCommon> lscom1) {
		List<RowExcel> ls = new ArrayList<>();
		for(String rc : cats){
			RowExcel rx = new RowExcel();
			String r_c = "";
			String c1 = "";
			String c2 = "";
			List<ReportCommon> lr = new ArrayList<ReportCommon>();
			ReportCommon r = new ReportCommon();
			for(ReportCommon c : lscom){
				if(rc.equals(c.getRank_category())){
					r = c;
					setRatio(ttl, r);
					c1 = r.getCategory1();
					c2 = r.getCategory2();
					r_c = r.getRank_category();
					break;
				}
			}
			lr.add(r);
			if(d1 != null){
				ReportCommon r1 = new ReportCommon();
				for(ReportCommon c : lscom1){
					if(rc.equals(c.getRank_category())){
						r1 = c;
						setRatio(ttl1, r1);
						c1 = r1.getCategory1();
						c2 = r1.getCategory2();
						r_c = r.getRank_category();
						break;
					}
				}
				lr.add(r1);
				lr.add(getDif(r, r1));
			}
			rx.setKeyRow(rc);
			rx.setCategory1(c1);
			rx.setCategory2(c2);
			rx.setCat(lr);
			ls.add(rx);
		}
		return ls;
	}

	private static List<ReportCommon> getTtlVal
	(String d1, List<ReportCommon>  lscom, List<ReportCommon> lscom1) {
		// 总计
		List<ReportCommon> ttlLs = new ArrayList<ReportCommon>();
		Double ia = 0d;
		Double iar = 1d;
		Double pcf = 0d;
		Double pcfr = 0d;

		for (ReportCommon c : lscom) {
			ia = addDouble(ia, c.getInvest_amt());
			pcf = addDouble(pcf, c.getPe_cv_ps_fw());
			c.setPe_cv_ps_fw_ratio(divDouble(c.getPe_cv_ps_fw(),
					c.getInvest_amt()));
		}
		pcfr = divDouble(pcf, ia);
		ReportCommon ttl = new ReportCommon("TOTAL", null, null, null, ia, iar,
				pcf, pcfr);
		ttlLs.add(ttl);
		ReportCommon ttl1 = new ReportCommon();
		if (d1 != null) {
			ia = 0d;
			pcf = 0d;
			pcfr = 0d;
			for (ReportCommon c : lscom1) {
				ia = addDouble(ia, c.getInvest_amt());
				pcf = addDouble(pcf, c.getPe_cv_ps_fw());
				c.setPe_cv_ps_fw_ratio(divDouble(c.getPe_cv_ps_fw(),
						c.getInvest_amt()));
			}
			pcfr = divDouble(pcf, ia);
			ttl1 = new ReportCommon("TOTAL", null, null, null, ia, iar, pcf,
					pcfr);
			ttlLs.add(ttl1);
			ttlLs.add(getDif(ttl, ttl1));
		}
		return ttlLs;
	}

	private static void setRatio(ReportCommon ttl, ReportCommon r) {
		r.setInvest_amt_ratio(divDouble(r.getInvest_amt(), ttl.getInvest_amt())); 
	}

	private static Double addDouble(Double d, Double d1) {
		if(d == null){
			d = 0d;
		}
		
		if(d1 == null){
			d1 = 0d;
		}
		
		return d + d1;
	}

	private static ReportCommon getDif(ReportCommon r, ReportCommon r1) {
		ReportCommon dif1 = new ReportCommon();
		dif1.setRank_category(r.getRank_category());
		dif1.setInvest_amt(subDouble(r.getInvest_amt(), r1.getInvest_amt()));
		dif1.setInvest_amt_ratio(subDouble(r.getInvest_amt_ratio(), r1.getInvest_amt_ratio()));
		dif1.setPe_cv_ps_fw(subDouble(r.getPe_cv_ps_fw(), r1.getPe_cv_ps_fw()));
		dif1.setPe_cv_ps_fw_ratio(subDouble(r.getPe_cv_ps_fw_ratio(), r1.getPe_cv_ps_fw_ratio()));
		return dif1;
	}
	
	private static Double subDouble(Double d, Double d1){
		if(d == null){
			d = 0d;
		}
		
		if(d1 == null){
			d1 = 0d;
		}
		
		return d - d1;
	}
	
	private static Double divDouble(Double d, Double d1){
		if(d == null){
			d = 0d;
		}
		
		if(d1 == null || d1 == 0d){
			return null;
		}
		return d/d1;
	}
}
