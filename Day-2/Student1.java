public class Student1 {
    private final String name;
    private final String address;
    private final double gpa;

    public Student1(String name, String address, double gpa) {
		this.name = name;
		this.address = address;
		this.gpa = gpa;
	}

    @Override
    public String toString() {
        return "Name: " + name + ", address: " + address + ", GPA: " + gpa;
    }

}
