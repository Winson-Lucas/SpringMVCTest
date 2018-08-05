package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Sample Department bean to demostrate main excel export features
 * author: Leonid Vysochyn
 */
public class Department {
	private Integer rowNum;
	private String profilo_name;
	private String project_name; 
	private Double reserve_amt;
	private Double reserve_ratio;
	private Date intial_date;
	
	public Department() {
		super();
	}

	public String getProfilo_name() {
		return profilo_name;
	}

	public void setProfilo_name(String profilo_name) {
		this.profilo_name = profilo_name;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public Double getReserve_amt() {
		return reserve_amt;
	}

	public void setReserve_amt(Double reserve_amt) {
		this.reserve_amt = reserve_amt;
	}

	public Double getReserve_ratio() {
		return reserve_ratio;
	}

	public void setReserve_ratio(Double reserve_ratio) {
		this.reserve_ratio = reserve_ratio;
	}

	public Date getIntial_date() {
		return intial_date;
	}

	public void setIntial_date(Date intial_date) {
		this.intial_date = intial_date;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	private String category;
	private String year;
    private String name;
    private Double dv;
    private Date dt;
    private Employee chief;
    private List staff = new ArrayList();
    private List staff2 = new ArrayList();

    
    public Department(String name) {
        this.name = name;
    }

    public Department(String name, Employee chief, List staff) {
        this.name = name;
        this.chief = chief;
        this.staff = staff;
    }

    public static List<Department> generate(int depCount, int employeeCount){
        List<Department> departments = new ArrayList<Department>();
        for(int index = 0; index < depCount; index++){
            Department dep = new Department("Dep " + index);
            dep.setChief( Employee.generateOne("ch" + index));
            dep.setStaff( Employee.generate(employeeCount) );
            departments.add( dep );
        }
        return departments;
    }

    public void addEmployee(Employee employee) {
        staff.add(employee);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getChief() {
        return chief;
    }

    public void setChief(Employee chief) {
        this.chief = chief;
    }

    public List getStaff() {
        return staff;
    }

    public void setStaff(List staff) {
        this.staff = staff;
    }

	public List getStaff2() {
		return staff2;
	}

	public void setStaff2(List staff2) {
		this.staff2 = staff2;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Double getDv() {
		return dv;
	}

	public void setDv(Double dv) {
		this.dv = dv;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public Integer getRowNum() {
		return rowNum;
	}

    
}
