package objects;

public class Doctor {
	private int id;
	private String departmentName;
	private String name;
	private String surname;
	private int age;
	
	
	public Doctor(int id, String departmentName, String name, String surname, int age) {
		super();
		this.id = id;
		this.departmentName = departmentName;
		this.name = name;
		this.surname = surname;
		this.age = age;
	}


	public int getId() {
		return id;
	}


	public String getDepartmentName() {
		return departmentName;
	}


	public String getName() {
		return name;
	}


	public String getSurname() {
		return surname;
	}


	public int getAge() {
		return age;
	}
	
	
	
	
	

}
