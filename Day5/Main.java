package Day5;

public class Main {
     public static void main(String[] args) {
        Airplane plane = new Airplane("Boeing 747", 900);

        plane.info();  
        plane.start(); 
        plane.takeOff();
        plane.fly(); 
    }

}

interface Flyable {
    void fly();
    default void takeOff(){
           System.out.println("Cất cánh...");
    }
}

abstract class Vehicle{
    protected  String brand;
    protected  int speed;

    public Vehicle(String brand , int speed){
        this.brand = brand;
        this.speed = speed;
    }

    abstract void start();

    public void infor(){
         System.out.println("Hãng: " + brand + " | Tốc độ: " + speed + " km/h");
    }
}

class Airplane extends Vehicle implements Flyable{
    public Airplane(String brand, int  speed){
        super(brand, speed);
    }

    @Override
    public void start(){
         System.out.println(brand + " khởi động động cơ...");
    }

    @Override
    public void fly(){
         System.out.println(brand + " đang bay " + speed + " km/h");
    }

    public void info(){
        super.infor();
    }
}
