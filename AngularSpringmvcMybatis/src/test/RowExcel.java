package test;

import java.util.ArrayList;
import java.util.List;

public class RowExcel {
	private String keyRow;
	private String category1;
	private String category2;
	private String category3;
	private List<ReportCommon> cat = new ArrayList<ReportCommon>(); 
	private List<RowExcel> LsRow = new ArrayList<>();
	private List<ReportCommon> lsTtl = new ArrayList<>();
	public String getKeyRow() {
		return keyRow;
	}
	public void setKeyRow(String keyRow) {
		this.keyRow = keyRow;
	}
	public String getCategory1() {
		return category1;
	}
	public void setCategory1(String category1) {
		this.category1 = category1;
	}
	public String getCategory2() {
		return category2;
	}
	public void setCategory2(String category2) {
		this.category2 = category2;
	}
	public String getCategory3() {
		return category3;
	}
	public void setCategory3(String category3) {
		this.category3 = category3;
	}
	public List<ReportCommon> getCat() {
		return cat;
	}
	public void setCat(List<ReportCommon> cat) {
		this.cat = cat;
	}
	public List<RowExcel> getLsRow() {
		return LsRow;
	}
	public void setLsRow(List<RowExcel> lsRow) {
		LsRow = lsRow;
	}
	public List<ReportCommon> getLsTtl() {
		return lsTtl;
	}
	public void setLsTtl(List<ReportCommon> lsTtl) {
		this.lsTtl = lsTtl;
	}
	
	
}
