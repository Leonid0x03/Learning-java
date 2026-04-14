class Animal1 {
    public void sound() {
        System.out.println("some sound");
    }
}

class Dog1 extends Animal1 {
    @Override
    public void sound() {
        System.out.println("woof woof");
    }

    public void play() {
        System.out.println("The dog is playing");
    }
}

class DownCasting {
    public static void main(String[] args) {
        Animal1 animal = new Dog1();
        Dog1 dog = (Dog1) animal;
        dog.play();
    }
}