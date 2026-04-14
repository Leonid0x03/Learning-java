class Animal {
	public void sound() {
		System.out.println("some sound");
	}
}

class Cat extends Animal {
        @Override
	public void sound() {
		System.out.println("meow meow");
	}
}

class Dog extends Animal {
        @Override
	public void sound() {
		System.out.println("woof woof");
	}
}

 class UpCasting {
	public static void main(String[] args) {
		// Up-casting
		Animal animal1 = new Cat();
		animal1.sound();
		// Up-casting
		Animal animal2 = new Dog();
		animal2.sound();
	}
}