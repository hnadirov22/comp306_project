package objects;

public class Patient {
	private int id;
	private String gender;
	private String name;
	private int age;
	private String surname;
	private String password;
	
	
	public Patient(int id, String gender, String name, int age, String surname, String password) {
		super();
		this.id = id;
		this.gender = gender;
		this.name = name;
		this.age = age;
		this.surname = surname;
		this.password = password;
	}


	public int getId() {
		return id;
	}


	public String getGender() {
		return gender;
	}


	public String getName() {
		return name;
	}


	public int getAge() {
		return age;
	}


	public String getSurname() {
		return surname;
	}


	public String getPassword() {
		return password;
	}
	
	
	
	
	
	
		

}
