abstract class Person4{
    private String name;
    private String address;
    public Person4(String name,String address){
        this.name=name;
        this.address=address;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public String getAddress(){
        return address;
    }
    public void display(){
        System.out.println("Name: "+name);
        System.out.println("Address: "+address);
    }
}
class Employee2 extends Person4{
    private int salary;
    public Employee2(String name,String address,int salary){
        super(name,address);
        this.salary=salary;
    }
    public void setSalary(int salary){
        this.salary=salary;
    }
    public int getSalary(){
        return salary;
    }
    @Override
    public void display(){
        System.out.println("Employee name: "+this.getName());
        System.out.println("Employee address: "+this.getAddress());
        System.out.println("Employee salary: "+this.getSalary());
    }
}
class Customer1 extends Person4{
    private int balance;
    public Customer1(String name,String address,int balance){
        super(name,address);
        this.balance=balance;
    }
    public void setBalance(int balance){
        this.balance=balance;
    }
    public int getBalance(){
        return balance;
    }
    @Override
    public void display(){
        System.out.println("Customer name: "+this.getName());
        System.out.println("Customer address: "+this.getAddress());
        System.out.println("Customer balance: "+this.getBalance());
    }
}
class Main4 {
    public static void main(String[] args) {
        Employee2 emp = new Employee2("An", "Ha Noi", 5000);
        Customer1 cus = new Customer1("Binh", "Da Nang", 10000);

        emp.display();
        System.out.println("------");
        cus.display();

        System.out.println("------ Polymorphism ------");
        Person4[] people = { emp, cus };
        for (Person4 p : people) {
            p.display();
            System.out.println();
        }
    }
}