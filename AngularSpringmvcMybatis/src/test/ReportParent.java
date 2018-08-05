package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportParent {
	private List<String> years = new ArrayList<String>();
	private List<RowExcel> lsRrowExcel = new ArrayList<RowExcel>();
	private List<ReportCommon> lsTtl = new ArrayList<>();
	public List<String> getYears() {
		return years;
	}
	public void setYears(List<String> years) {
		this.years = years;
	}
	public List<RowExcel> getLsRrowExcel() {
		return lsRrowExcel;
	}
	public void setLsRrowExcel(List<RowExcel> lsRrowExcel) {
		this.lsRrowExcel = lsRrowExcel;
	}
	public List<ReportCommon> getLsTtl() {
		return lsTtl;
	}
	public void setLsTtl(List<ReportCommon> lsTtl) {
		this.lsTtl = lsTtl;
	}
	
	
	
}
