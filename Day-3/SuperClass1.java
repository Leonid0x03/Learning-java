public class SuperClass1 {
    int x = 10;

    public void display() {
        System.out.println(x);
    }
}

class SubClass1 extends SuperClass1 {
    int x = 20;

    @Override
    public void display() {
        System.out.println(x);
    }
}

class Entry6 {
    public static void main(String[] args) {
        SuperClass1 a = new SubClass1();
        System.out.println(a.x);
        a.display();
    }
}
