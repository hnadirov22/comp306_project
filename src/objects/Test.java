package objects;

public class Test {
	private int id;
	private int patient_id;
	private int appointment_id;
	private String description;
	private double billing;
	
	
	public Test(int id, int patient_id, int appointment_id, String description, double billing) {
		super();
		this.id = id;
		this.patient_id = patient_id;
		this.appointment_id = appointment_id;
		this.description = description;
		this.billing = billing;
	}


	public int getId() {
		return id;
	}


	public int getPatient_id() {
		return patient_id;
	}


	public int getAppointment_id() {
		return appointment_id;
	}


	public String getDescription() {
		return description;
	}


	public double getBilling() {
		return billing;
	}
	
	
	
	
	
	

}
