package objects;

public class Operation {
	
	private int id;
	private int patient_id;
	private int doctor_id;
	private String descript;
	private double billing;
	private String doctor_name;
	private String doctor_sname;
	

	public Operation(int id, int patient_id, int doctor_id, String descript, double billing, String doctor_name, String doctor_sname) {
		super();
		this.id = id;
		this.patient_id = patient_id;
		this.doctor_id = doctor_id;
		this.descript = descript;
		this.billing = billing;
		this.doctor_name = doctor_name;
		this.doctor_sname = doctor_sname;
	}

	public int getId() {
		return id;
	}

	public int getPatient_id() {
		return patient_id;
	}

	public int getDoctor_id() {
		return doctor_id;
	}

	public String getDescript() {
		return descript;
	}

	public double getBilling() {
		return billing;
	}
		
	public String getDoctor_name() {
		return doctor_name;
	}

	public String getDoctor_sname() {
		return doctor_sname;
	}
	
	
	
	
	

	

}
