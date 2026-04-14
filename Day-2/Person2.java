public class Person2 {
    private String name;

    public Person2(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Student2 extends Person2 {
    public Student2(String name) {
        super(name);
    }

    public static void main(String[] args) {
        Student2 s1 = new Student2("Trung");
        System.out.println(s1.getName());
    }
}
