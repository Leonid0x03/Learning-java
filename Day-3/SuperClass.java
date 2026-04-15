class SuperClass{
	int x = 10;
}

class SubClass extends SuperClass{
	int x = 20;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}

 class Entry5 {
	public static void main(String[] args) {
		SuperClass a = new SubClass();
		System.out.println(a.x);
        System.out.println(((SubClass)a).x);
	}
}
