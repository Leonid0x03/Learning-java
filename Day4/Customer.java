package Day4;

public class Customer {
    private final int id;
    private String name;
    private int discount;

    public Customer(int id, String name, int discount) {
        this.id = id;
        this.name = name;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}

class Invoice {
    private final int id;
    private Customer customer;
    private double amount;

    public Invoice(int id, Customer customer, double amount) {
        this.id = id;
        this.customer = customer;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customer.getName();
    }

    public double getAmountAfterDiscount() {
        return amount - amount * customer.getDiscount() / 100;
    }
}

class Entry12 {
    public static void main(String[] args) {
        Customer customer1 = new Customer(1000, "Tuan", 20);
        Invoice invoice1 = new Invoice(1000, customer1, 100000);
        System.out.println("Customer name: " + invoice1.getCustomerName());
        System.out.println("Invoice after discount: " + invoice1.getAmountAfterDiscount());
        Customer customer2 = new Customer(2000, "Ha", 30);
        invoice1.setCustomer(customer2);
        System.out.println("Customer name: " + invoice1.getCustomerName());
        System.out.println("Invoice after discount 2: " + invoice1.getAmountAfterDiscount());

    }
}
