 class Student {
     String name;
    int age;
    public Student (String name , int age){
        this.name = name;
        this.age = age;
    }

    public void show(){
        System.out.println("Name:" + name);
        System.out.println("Age:" + age);
    }

}

class Entry_1 {
    public static void main(String[] args) {
    Student s1 = new Student("Ha",27);
    Student s2 = new Student("Trinh", 23);
    s1.show();
    s2.show();
    }
}
