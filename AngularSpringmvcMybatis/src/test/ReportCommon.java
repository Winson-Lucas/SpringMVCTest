package test;

public class ReportCommon {
	private String rank_category;
	private String category1;
	private String category2;
	private String category3;
	
	private Double invest_amt;
	private Double invest_amt_ratio;
	private Double pe_cv_ps_fw;
	private Double pe_cv_ps_fw_ratio;
	
	public ReportCommon(){
		
	}
	
	public ReportCommon(String rank_category, String category1,
			String category2, String category3, Double invest_amt,
			Double invest_amt_ratio, Double pe_cv_ps_fw,
			Double pe_cv_ps_fw_ratio) {
		super();
		this.rank_category = rank_category;
		this.category1 = category1;
		this.category2 = category2;
		this.category3 = category3;
		this.invest_amt = invest_amt;
		this.invest_amt_ratio = invest_amt_ratio;
		this.pe_cv_ps_fw = pe_cv_ps_fw;
		this.pe_cv_ps_fw_ratio = pe_cv_ps_fw_ratio;
	}
	public String getRank_category() {
		return rank_category;
	}
	public void setRank_category(String rank_category) {
		this.rank_category = rank_category;
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
	public Double getInvest_amt() {
		return invest_amt;
	}
	public void setInvest_amt(Double invest_amt) {
		this.invest_amt = invest_amt;
	}
	public Double getInvest_amt_ratio() {
		return invest_amt_ratio;
	}
	public void setInvest_amt_ratio(Double invest_amt_ratio) {
		this.invest_amt_ratio = invest_amt_ratio;
	}
	public Double getPe_cv_ps_fw() {
		return pe_cv_ps_fw;
	}
	public void setPe_cv_ps_fw(Double pe_cv_ps_fw) {
		this.pe_cv_ps_fw = pe_cv_ps_fw;
	}
	public Double getPe_cv_ps_fw_ratio() {
		return pe_cv_ps_fw_ratio;
	}
	public void setPe_cv_ps_fw_ratio(Double pe_cv_ps_fw_ratio) {
		this.pe_cv_ps_fw_ratio = pe_cv_ps_fw_ratio;
	}
	
	
}
