package objects;

public class Test {
	private int id;
	private int patientID;
	private int appointmentID;
	private String description;
	private double billing;
	
	public Test(int id, int patientID, int appointmentID, String description, double billing) {
		this.id = id;
		this.patientID = patientID;
		this.appointmentID = appointmentID;
		this.description = description;
		this.billing = billing;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}

	public int getAppointmentID() {
		return appointmentID;
	}

	public void setAppointmentID(int appointmentID) {
		this.appointmentID = appointmentID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getBilling() {
		return billing;
	}

	public void setBilling(double billing) {
		this.billing = billing;
	}
	
	
	
}
