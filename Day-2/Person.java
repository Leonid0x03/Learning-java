class Person{
	private final int id;
	private final String name;
	private final int age;
	private final String address;
	public Person(int id, String name, int age, String address) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.address = address;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public String getAddress() {
		return address;
	}

	public static void main(String[] args) {
		Person p = new Person(1001, "Quynh", 24, "Ha Noi");
		System.out.println("Id: " + p.getId());
		System.out.println("Name: " + p.getName());
		System.out.println("Age: " + p.getAge());
		System.out.println("Address: " + p.getAddress());
	}
}