package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	private static final String DEFAULT_SHEET_NAME = "sheet";

	/**
	 * 导出无动态表头的Excel文件
	 * <p>
	 * 参考重载的有动态表头注释
	 * </p>
	 * 
	 * @param destOutputStream
	 * @param templateInputStream
	 * @param data
	 * @param dataKey
	 * @param maxRowPerSheet
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void generateExcelByTemplate(OutputStream destOutputStream,
			InputStream templateInputStream, List data, String dataKey,
			int maxRowPerSheet) throws Exception {
		generateExcelByTemplate(destOutputStream, templateInputStream, null,
				null, data, dataKey, maxRowPerSheet);
	}

	/**
	 * 通过Excel模版生成Excel文件
	 * <p>
	 * 创建Excel模版，变量类似JSP tag风格。 例如：
	 * <ul>
	 * <li>无动态表头
	 * 
	 * <pre>
	 * 序号   名称  规格  创建时间    价格
	 * &lt;jx:forEach items="${vms}" var="vm"&gt;
	 * ${vm.id} ${vm.name} ${vm.scale} ${vm.created} ${vm.price}
	 * &lt;/jx:forEach&gt;
	 * </pre>
	 * 
	 * </li>
	 * <li>有动态表头
	 * 
	 * <pre>
	 * 项目/数量/时间    &lt;jx:forEach items="${dates}" var="date"&gt;    ${date} &lt;/jx:forEach&gt;
	 * &lt;jx:forEach items="${itemsx}" var="item"&gt;            
	 * ${item.name}    &lt;jx:forEach items="${item.counts}" var="count"&gt; ${count}    &lt;/jx:forEach&gt;
	 * &lt;/jx:forEach&gt;
	 * </pre>
	 * 
	 * </li>
	 * </ul>
	 * 调用该方法则生成对应的Excel文件。
	 * </p>
	 * <p>
	 * 注意：dataKey不能是items, items是保留字，如果用items则会提示：Collection is
	 * null并抛出NullPointerException
	 * </p>
	 * 
	 * @param destOutputStream
	 *            Excel输出流
	 * @param templateInputStream
	 *            Excel模版输入流
	 * @param header
	 *            动态表头
	 * @param headerKey
	 *            表头的变量
	 * @param data
	 *            数据项
	 * @param dataKey
	 *            数据项变量
	 * @param maxRowPerSheet
	 *            每个sheet最多行数
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void generateExcelByTemplate(OutputStream destOutputStream,
			InputStream templateInputStream, List header, String headerKey,
			List data, String dataKey, int maxRowPerSheet) throws Exception {

		List<List> splitData = null;
		@SuppressWarnings("unchecked")
		Map<String, List> beanMap = new HashMap();
		List<String> sheetNames = new ArrayList<String>();
		if (data.size() > maxRowPerSheet) {
			splitData = splitList(data, maxRowPerSheet);
			sheetNames = new ArrayList<String>(splitData.size());
			for (int i = 0; i < splitData.size(); ++i) {
				sheetNames.add(DEFAULT_SHEET_NAME + i);
			}
		} else {
			splitData = new ArrayList<List>();
			sheetNames.add(DEFAULT_SHEET_NAME + 0);
			splitData.add(data);
		}
		if (null != header) {
			beanMap.put(headerKey, header);
		}
		XLSTransformer transformer = new XLSTransformer();
		Workbook workbook = transformer
				.transformMultipleSheetsList(templateInputStream, splitData,
						sheetNames, dataKey, beanMap, 0);
		workbook.write(destOutputStream);
		templateInputStream.close();
		destOutputStream.close();
	}

	/**
	 * 导出无动态表头的Excel文件，目标文件和模版文件均为文件路径
	 * <p>
	 * 参考重载的有动态表头注释
	 * </p>
	 * 
	 * @param destFilePath
	 * @param templateFilePath
	 * @param data
	 * @param dataKey
	 * @param maxRowPerSheet
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void generateExcelByTemplate(String destFilePath,
			String templateFilePath, List data, String dataKey,
			int maxRowPerSheet) throws Exception {
		generateExcelByTemplate(destFilePath, templateFilePath, null, null,
				data, dataKey, maxRowPerSheet);
	}

	/**
	 * 导出有动态表头的Excel文件，目标文件和模版文件均为文件路径
	 * <p>
	 * 参考重载的有动态表头注释
	 * </p>
	 * 
	 * @param destFilePath
	 * @param templateFilePath
	 * @param header
	 * @param headerKey
	 * @param data
	 * @param dataKey
	 * @param maxRowPerSheet
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void generateExcelByTemplate(String destFilePath,
			String templateFilePath, List header, String headerKey, List data,
			String dataKey, int maxRowPerSheet) throws Exception {
		generateExcelByTemplate(new FileOutputStream(destFilePath),
				new FileInputStream(templateFilePath), header, headerKey, data,
				dataKey, maxRowPerSheet);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<List> splitList(List data, int maxRowPerSheet) {
		List<List> splitData = new ArrayList<List>();
		List sdata = null;
		for (int i = 0; i < data.size(); ++i) {
			if (0 == i % maxRowPerSheet) {
				if (null != sdata) {
					splitData.add(sdata);
				}
				sdata = new ArrayList(maxRowPerSheet);
			}
			sdata.add(data.get(i));
		}
		if (0 != maxRowPerSheet % data.size()) {
			splitData.add(sdata);
		}

		return splitData;
	}
	
	public static void fileToZip(Workbook wb, ZipOutputStream zos, String fileName, int i, int last) throws IOException{
		try{
			ZipEntry zipEntry = new ZipEntry(fileName+"_"+last+"-"+i+".xlsx");
			zos.putNextEntry(zipEntry);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			wb.write(baos);
			baos.flush();
			zos.write(baos.toByteArray());
		}finally{
			wb.close();
		}
	}
	
	static Map<String, Integer> vmp = new HashMap<String, Integer>();
	static{
		vmp.put("asset_status", 0);
		vmp.put("A", 1);
		 vmp.put("B", 2);
		 int i = 0;
		 for(;i<100;i++){
			 int x = i+3;
			 vmp.put("A"+x, x);
		 }
	} 
	
	public static void exportLargeExcel(InputStream is, OutputStream os, List<Map<String, Object>> ls, int startRow)
			throws Exception {
		XSSFWorkbook wb = null;
		SXSSFWorkbook swb = null;
		try{
			if(isExportEmpty(ls, is, os, null)){
				return;
			}
			wb = new XSSFWorkbook(is);
			swb = new SXSSFWorkbook(wb);
			setExcelVal(swb, ls, startRow);
		}finally{
			if(swb != null){
				swb.write(os);
				swb.dispose();
				swb.close();
			}
			if(wb != null) wb.close();
			if(is != null) is.close();
			if(os != null) os.close();
		}
	}
	
	private static void setExcelVal(Workbook swb, List<Map<String, Object>> ls, int startRow) {
		Sheet sheet = swb.getSheetAt(0);
		CellStyle style = swb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.TAN.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);
		for (Map<String, Object> m : ls) {
			Row row = sheet.getRow(startRow);
			if (row == null) {
				row = sheet.createRow(startRow);
			}
			for (Entry<String, Object> e : m.entrySet()) {
				String key = e.getKey();
				Integer c = vmp.get(key);
				if (c == null) {
					continue;
				}
				Cell cell = row.getCell(c);
				if (cell == null) {
					cell = row.createCell(c);
				}
				CellStyle setstyle = style; 
				Object v = e.getValue();
				if (v != null) {
					//to do 红黄绿灯
					if("asset_status".equals(key)){
						if(!"".equals(v)){
							setstyle = swb.createCellStyle();
							if("0".equals(v)){
								setstyle.setFillForegroundColor(IndexedColors.YELLOW.index);
							}else if("1".equals(v)){
								setstyle.setFillForegroundColor(IndexedColors.RED.index);
							}else if("2".equals(v)){
								setstyle.setFillForegroundColor(IndexedColors.BLUE.index);
							}
							setstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							setstyle.setBorderBottom(BorderStyle.THIN);
							v = "";
						}
					}
					if (v instanceof String) {
						cell.setCellValue((String) v);
					} else if (v instanceof Integer) {
						cell.setCellValue((Integer) v);
					} else {
						cell.setCellValue((Double) v);
					}
				}
				cell.setCellStyle(setstyle);
			}
			startRow++;
		}
	}

	public static void exportLargeExcel
	(List<Map<String, Object>> ls, int length, String fileName, InputStream is, OutputStream os, int startRow) throws Exception{
		ZipOutputStream zos = null;
		try{
			zos = new ZipOutputStream(os);
			if(isExportEmpty(ls, is, os, zos)){
				return;
			}
			int size = ls.size();
			if(size <= length){
				exportZip(is, zos, fileName, size, 1, ls, startRow);
			}else{
				int r = size%length;
				int n = (size - r)/length;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) > -1 ) {
				    baos.write(buffer, 0, len);
				}
				baos.flush();
				for(int i = 1; i<=n; i++){
					int fromIndex = (i - 1)*length;
					int toIndex = i*length;
					InputStream is1 = new ByteArrayInputStream(baos.toByteArray()); 
					exportZip(is1, zos, fileName, toIndex, fromIndex+1, ls.subList(fromIndex, toIndex), startRow);
				}
				if(r != 0){
					int fromIndex = size - r;
					int toIndex = size;
					InputStream is1 = new ByteArrayInputStream(baos.toByteArray()); 
					exportZip(is1, zos, fileName, toIndex, fromIndex+1, ls.subList(fromIndex, toIndex), startRow);
				}
			}
			zos.flush();
			zos.close();
		}finally{
			if(is != null) is.close();
			if(os != null) os.close();
		}
	}
	
	private static void exportZip(InputStream is, ZipOutputStream zos,
			String fileName, int toIndex, int fromIndex, List<Map<String, Object>> ls,
			int startRow) throws Exception {
		XSSFWorkbook swb = new XSSFWorkbook(is);
		setExcelVal(swb, ls, startRow);
		fileToZip(swb, zos, fileName, toIndex, fromIndex);
	}

	private static boolean isExportEmpty(List<Map<String, Object>> ls, InputStream is, OutputStream os, ZipOutputStream zos) throws Exception {
		if(ls == null || ls.isEmpty()){
			XSSFWorkbook wb = new XSSFWorkbook(is);
			if(zos == null){
				wb.write(os);
			}else{
				fileToZip(wb, zos, "宽表", 0, 0);
				zos.flush();
				zos.close();
			}
			wb.close();
			return true;
		}
		return false;
	}

	/**
	 * 将存放在sourceFilePath目录下的源文件,打包成fileName名称的ZIP文件,并存放到zipFilePath。
	 * @param sourceFilePath 待压缩的文件路径
	 * @param zipFilePath	 压缩后存放路径
	 * @param fileName		 压缩后文件的名称
	 * @return flag
	 */
	public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		
		if(sourceFile.exists() == false) {
			System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath + " 不存在. <<<<<<");
		} else {
			try {
				File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
				if(zipFile.exists()) {
					System.out.println(">>>>>> " + zipFilePath + " 目录下存在名字为：" + fileName + ".zip" + " 打包文件. <<<<<<");
				} else {
					File[] sourceFiles = sourceFile.listFiles();
					if(null == sourceFiles || sourceFiles.length < 1) {
						System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath + " 里面不存在文件,无需压缩. <<<<<<");
					} else {
						fos = new FileOutputStream(zipFile);
						zos = new ZipOutputStream(new BufferedOutputStream(fos));
						byte[] bufs = new byte[1024*10];
						for(int i=0;i<sourceFiles.length;i++) {
							// 创建ZIP实体,并添加进压缩包
							ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
							zos.putNextEntry(zipEntry);
							// 读取待压缩的文件并写进压缩包里
							fis = new FileInputStream(sourceFiles[i]);
							bis = new BufferedInputStream(fis,1024*10);
							int read = 0;
							while((read=bis.read(bufs, 0, 1024*10)) != -1) {
								zos.write(bufs, 0, read);
							}
						}
						flag = true;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				// 关闭流
				try {
					if(null != bis) bis.close();
					if(null != zos) zos.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		
		return flag;
	}
}
