package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestExcel {

	static String fileout = "C:\\Users\\Winson\\Desktop\\jxl\\testexp_out.xlsx";
	
	public static void main(String[] args) throws Exception {
		//read();
//		test();
		setToExcelVal(sampel);
		System.out.println("finish");
//		System.out.println(getCellVal("C:/Users/Winson/Desktop/www - 副本.xlsx", "Sheet1", 0, 7));
		
	}
	
	public static String getCellVal(String file, String sheet, int row, int col) throws Exception{
		Workbook workbook = WorkbookFactory.create(new FileInputStream(new File(file)));
		Sheet s = workbook.getSheet(sheet);
		if(s == null){
			return null;
		}
		Row r = s.getRow(row);
		if(r == null){
			r = s.createRow(row);
		}
		Cell c = r.getCell(col);
		if(c == null){
			c = r.createCell(col);
		}
		
		return (String)getCellVal(c, String.class);
	}
	
	static String sampel = "sample";
	
	static Integer objectList = -1;
	static Integer startRow = -2;
	static Integer sheet = -3;
	static Integer file = -4;
	static Integer startCol = -5;
	
	static SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	static Map<String, Map<Integer, Object>> mmp = new HashMap<String, Map<Integer, Object>>();
	
	static Map<Integer, Object> sampMp = new HashMap<Integer, Object>();
	static {
		ObjectExcel o1 =new ObjectExcel();
		o1.setB(1d);
		ObjectExcel o2 =new ObjectExcel();
		o2.setB(2d);
		ObjectExcel o3 =new ObjectExcel();
		o3.setB(3d);
		
	/*	Map<String, Object> o1 = new HashMap<String, Object>();
		Map<String, Object> o2 = new HashMap<String, Object>();
		Map<String, Object> o3 = new HashMap<String, Object>();*/
		
		/*o1.put("c", "C");
		o1.put("d", "D");
		o1.put("e", "E");
		
		o2.put("d", "DD");
		o2.put("f", "FF");
		
		o3.put("a", "AAA");
		o3.put("g", "GGG");
		o3.put("h", "HHH");*/
		
		sampMp.put(objectList, putList(o1, o2, o3));
		sampMp.put(startRow, 2);
		sampMp.put(sheet, "sheet1");
		sampMp.put(file, "C:/Users/Winson/Desktop/wwwTest2.xlsx");
		sampMp.put(startCol, 0);
		sampMp.put(0, "a");
		sampMp.put(1, "b");
		sampMp.put(2, "c");
		sampMp.put(3, "d");
		sampMp.put(4, "e");
		sampMp.put(5, "f");
		sampMp.put(6, "g");
		
		o1.setC("zz");
		o2.setC("xx");
		o3.setC("tt");
		
		o1.setB(11133.12d);
		o2.setB(-21333d);
		o3.setB(null);
		
		o1.setF(11333.33d);
		o2.setF(12d);
		o3.setF(0d);
		
		o1.setG(new Date());
		
		sampMp.put(objectList, putList(o1, o2, o3));
		sampMp.put(startRow, 2);
		sampMp.put(sheet, "sheet1");
		sampMp.put(file, "C:\\Users\\Winson\\Desktop\\jxl\\testexp.xlsx");
		sampMp.put(startCol, 0);
		
		mmp.put(sampel, sampMp);
		
	}
	
	public static List<Object> putList(Object... objects){
		List<Object> list = new ArrayList<Object>();
		for(Object o : objects){
			list.add(o);
		}
		return list;
	}
	
	private static void test() throws Exception{
//		@SuppressWarnings("unchecked")
//		List<ObjectExcel> lso = (List<ObjectExcel>)getFromExcelVal(sampel);
//		for(ObjectExcel o : lso){
//			System.out.println("------------------");
//			System.out.println(o.getA());
//			System.out.println(o.getB());
//			System.out.println(o.getC());
//			System.out.println(o.getD());
//			System.out.println(o.getE());
//			System.out.println(o.getF());
//			System.out.println(o.getG());
//			if(o.getG() != null){
//				TimeZone zone = TimeZone.getTimeZone("CTT");
//                smf.setTimeZone(zone);
//				String date = smf.format(o.getG());
//				System.out.println(smf.getTimeZone()+" yyyy-MM-dd : "+date);
//			}
//			o.setF(o.getF()==null?0:o.getF()+1d);
//			System.out.println("$$$$$$$$$$$$$$$$$$$$$");
//		}
		setToExcelVal(sampel);
		System.out.println("finish");
	}
	
	
	@SuppressWarnings("unchecked")
	private static void setToExcelVal(String temName) throws Exception {
		Map<Integer, Object> excelMp = mmp.get(temName);
		if(excelMp == null){
			return;
		}
		XSSFWorkbook wb = 
				new XSSFWorkbook(new File((String)excelMp.get(file)));
//				WorkbookFactory.create(new FileInputStream(new File((String)excelMp.get(file))));
		SXSSFWorkbook workbook = new SXSSFWorkbook(wb);
		Sheet s = workbook.getSheet((String)excelMp.get(sheet));
		s.setForceFormulaRecalculation(true);
		int sc = (Integer)excelMp.get(startCol);
		int sr = (Integer)excelMp.get(startRow);
		List<Object> listObj = (List<Object>)excelMp.get(objectList);
		for(Object o : listObj){
			Row r = s.getRow(sr);
			if(r == null){
				System.out.println("create row "+sr);

				r = s.createRow(sr);
			}
			sr++;
			for(Integer col : excelMp.keySet()){
				if(col >= 0){
					if(col < sc){
						String val = (String)excelMp.get(col);
						Cell cell = r.getCell(col);
						if(cell == null){
							System.out.println("create row "+col);
							cell = r.createCell(col);
						}
						setCellVal(cell, val);
					}else{
						String field = (String)excelMp.get(col);
						Cell cell = r.getCell(col);
						if(cell == null){
							cell = r.createCell(col);
						}
						Object value = null;
						if(o instanceof Map){
							value = ((Map<String, Object>)o).get(field);
						}else{
							Field f = o.getClass().getDeclaredField(field);
							f.setAccessible(true);
							value = f.get(o);
						}
					
						CellStyle cs = workbook.createCellStyle();
						cs.setBorderBottom(BorderStyle.THIN);
						cs.setBorderRight(BorderStyle.THIN);
						cell.setCellStyle(cs);
						if(value != null){
							setCellVal(cell, value);
							if(value instanceof Date){
								 
								 cs.setDataFormat( 
										 workbook.createDataFormat().getFormat("yyyy-mm-dd")
				                );  
							}else if(value instanceof Double){
								 cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
//								 cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
							}
						}
					}
				}
			}
		}
		
		
		System.out.println(s.getLastRowNum());
		
		
		workbook.write(new FileOutputStream(fileout));
		
	}


	private static void setCellVal(Cell cell, Object v) {
		if(v == null){
			return;
		}
		if(cell == null){
			return;
		}
		if(v instanceof Double){
			cell.setCellValue((Double)v);
		}else if(v instanceof Date){
			cell.setCellValue((Date)v);
		}else{
			cell.setCellValue((String)v);
		} 
	}


	@SuppressWarnings("unchecked")
	public static List<? extends Object> getFromExcelVal(String temName) throws Exception {
		Map<Integer, Object> excelMp = mmp.get(temName);
		if(excelMp == null){
			return null;
		}
		
		Workbook workbook = WorkbookFactory.create(new FileInputStream(new File((String)excelMp.get(file))));
		Sheet s = workbook.getSheet((String)excelMp.get(sheet));
		int sc = (Integer)excelMp.get(startCol);
		int sr = (Integer)excelMp.get(startRow);
		List<Object> listObj = (List<Object>)excelMp.get(objectList);
		for(Object o : listObj){
			Row r = s.getRow(sr++);
			if(r == null){
				continue;
			}
			for(Integer col : excelMp.keySet()){
				if(col >= sc){
					String field = (String)excelMp.get(col);
					Field f = o.getClass().getDeclaredField(field);
					f.setAccessible(true);
					Cell cell = r.getCell(col);
					f.set(o, getCellVal(cell, f.getType()));
				}
			}
		}
		return listObj;
	}


	public static Object getCellVal(Cell cell, Class<?> c){
		Object value = null;
		if(cell == null){
			return value;
		}
		
		if(cell.getCellType() == Cell.CELL_TYPE_BLANK){
			return value;
		}else if(cell.getCellType() == Cell.CELL_TYPE_ERROR){
			return value;
		}else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				value =  cell.getDateCellValue();
			}else{
				value = cell.getNumericCellValue();
			}
		}else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
			value = cell.getStringCellValue();
		}else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
//			FormulaEvaluator eva = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			if(cell.getCachedFormulaResultType() == 0){
				value = cell.getNumericCellValue();
			}else{
				value = cell.getStringCellValue();
			}
		}
		
		if(value == null || ("NaN").equals(value) || "#DIV/0!".equals(value)){
			return null;
		}
		
		if(c == null){
			return value;
		}
		
		if(value instanceof Double){
			if(c.equals(String.class)){
				value = (value+"").replace(".0", "");
			}else if(c.equals(Date.class)){
				return null;
			}
		}else if(value instanceof Date){
			if(c.equals(String.class)){
				value = value+"";
			}else if(c.equals(Double.class)){
				return null;
			}
		}else if(value instanceof String){
			if(c.equals(Double.class)){
				value = getDouble((String)value);
			}else if(c.equals(Date.class)){
				value = getDate((String)value);
			}
		}
		
		return value;
		
	}
	
	public static Double getDouble(String str){
	  try{
		 return Double.parseDouble(str);
	  }catch(NumberFormatException e){
		  return null;
	  }
	}

	public static Date getDate(String str){
	  try{
		  SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		  return fmt.parse(str);
	  }catch(ParseException e){
		  return null;
	  }
	}
	
	/*private static void read() throws IOException, BiffException,
			RowsExceededException, WriteException, InvalidFormatException {
		File f = new File("C:/Users/Winson/Desktop/www.xls");

		org.apache.poi.ss.usermodel.Workbook wb= WorkbookFactory.create(new InputStream() {
			
			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		
		org.apache.poi.ss.usermodel.Sheet s = wb.getSheet("1");
		s.getRow(1).getCell(1).getCellStyle();
		
		int type = HSSFCell.CELL_TYPE_STRING;
		
		// 打开要修改的xls文件
		jxl.Workbook rw = jxl.Workbook.getWorkbook(f);
		Sheet rs = rw.getSheet(0);
		Cell c = rs.getCell(0, 2);
		System.out.println("xxx: "+c.getContents());
		c = rs.getCell(3, 1);
		System.out.println("yyy: "+c.getContents());
		System.out.println("yyy: "+c.getCellFormat().getBackgroundColour().getDescription());
		jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(f, rw);

		// 读取第一张工作表
		jxl.write.WritableSheet ws = wwb.getSheet(0);

		Label label = new Label(0, 2, "周星驰");
		jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#,###.##"); 
        jxl.write.WritableCellFormat wcf = new jxl.write.WritableCellFormat(nf); 
        jxl.write.Number n = new jxl.write.Number(2, 1, 1232.451, wcf); 
		ws.addCell(label);
		ws.addCell(n);
		// 写入Excel对象
		wwb.write();

		// 关闭可写入的Excel对象
		wwb.close();

		// 关闭只读的Excel对象
		rw.close();

	}*/
	public void zipMethod(HttpServletResponse response) throws IOException{
		//文件名称
	    String[] names={"one.jpg","two.jpg","three.jpg","four.jpg"};
	    //四个文件流
	    FileInputStream input1 = new FileInputStream(new File("文件路径"));
	    FileInputStream input2 = new FileInputStream(new File("文件路径"));
	    FileInputStream input3 = new FileInputStream(new File("文件路径"));
	    FileInputStream input4 = new FileInputStream(new File("文件路径"));
	    FileInputStream[] inputs={input1,input2,input3,input4};
	    //ZIP打包图片
	    File zipFile = new File("压缩文件存放路径");
	    byte[] buf = new byte[1024];
	    int len;
	    ZipOutputStream zout=new ZipOutputStream(new FileOutputStream(zipFile));
	    for (int i = 0; i < inputs.length; i++) {  
	        FileInputStream in =inputs[i];  
	        zout.putNextEntry(new ZipEntry(names[i]));    
	        while ((len = in.read(buf)) > 0) {  
	            zout.write(buf, 0, len);  
	        }  
	        zout.closeEntry();  
	        in.close();  
	    }
	    zout.close();
	    
	    
	    //下载图片
	    FileInputStream zipInput =new FileInputStream(zipFile);
	    OutputStream out = response.getOutputStream();
	    response.setContentType("application/octet-stream");
	    response.setHeader("Content-Disposition", "attachment; filename=images.zip");
	    while ((len=zipInput.read(buf))!= -1){  
	        out.write(buf,0,len);  
	    }
	    zipInput.close();
	    out.flush();
	    out.close();
	    //删除压缩包
	    zipFile.delete();
	}
	
	  /**
     * 下载
     */
    static void download(Vector<String> downloadList){
        // 线程池
        ExecutorService pool = null;
        HttpURLConnection connection = null;
        //循环下载
        try {
            for (int i = 0; i < downloadList.size(); i++) {
                pool = Executors.newCachedThreadPool();
                final String url = downloadList.get(i);
                String filename = getFilename(downloadList.get(i));
                System.out.println("正在下载第" + (i+1) + "个文件，地址：" + url);
                Future<HttpURLConnection> future = pool.submit(new Callable<HttpURLConnection>(){
                    @Override
                    public HttpURLConnection call() throws Exception {
                        HttpURLConnection connection = null;
                        connection = (HttpURLConnection) new URL(url).openConnection(); 
                        connection.setConnectTimeout(10000);//连接超时时间
                        connection.setReadTimeout(10000);// 读取超时时间
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestMethod("GET");
                        //断点续连,每次要算出range的范围,请参考Http 1.1协议
                        //connection.setRequestProperty("Range", "bytes=0");
                        connection.connect();
                        return connection;
                    }
                });
                connection = future.get();
                System.out.println("下载完成.响应码:"+ connection.getResponseCode());
                // 写入文件
                writeFile(new BufferedInputStream(connection.getInputStream()), URLDecoder.decode(filename,"UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection)
                connection.disconnect();
            if (null != pool) 
                pool.shutdown();
        }
    }
     
    /**
     * 通过截取URL地址获得文件名
     * 注意：还有一种下载地址是没有文件后缀的，这个需要通过响应头中的
     * Content-Disposition字段 获得filename，一般格式为："attachment; filename=\xxx.exe\"
     * @param url
     * @return
     */
    static String getFilename(String url){
        return ("".equals(url) || null == url) ? "" : url.substring(url.lastIndexOf("/") + 1,url.length());
    }
     
    /**
     * 写入文件
     * @param inputStream
     */
    static void writeFile(BufferedInputStream bufferedInputStream,String filename){
        //创建本地文件
        File destfileFile = new File("d:\\temp\\download\\"+ filename);
        if (destfileFile.exists()) {
            destfileFile.delete();
        }
        if (!destfileFile.getParentFile().exists()) {
            destfileFile.getParentFile().mkdir();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destfileFile);
            byte[] b = new byte[1024];
            int len = 0;
            // 写入文件
            System.out.println("开始写入本地文件.");
            while ((len = bufferedInputStream.read(b, 0, b.length)) != -1) {
                System.out.println("正在写入字节：" + len);
                fileOutputStream.write(b, 0, len);
            }
            System.out.println("写入本地文件完成.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                if (null != bufferedInputStream)
                    bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
