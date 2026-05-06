package Day5;

import java.util.ArrayList;
import java.util.List;

public class Main2 {

     public static void main(String[] args) {
        // Tạo danh sách nhân viên
        List<Employee4> employees = new ArrayList<>();

        employees.add(new FullTimeEmployee1("NV001", "Nguyễn Văn A",
                "a@company.com", 15_000_000, 1.2));
        employees.add(new FullTimeEmployee1("NV002", "Trần Thị B",
                "b@company.com", 17_000_000, 1.5));
        employees.add(new PartTimeEmployee1("NV003", "Lê Văn C",
                "c@company.com", 120, 50_000));
        employees.add(new PartTimeEmployee1("NV004", "Phạm Thị D",
                "d@company.com", 90, 50_000));

        // In danh sách + lương
        System.out.println("===== DANH SÁCH NHÂN VIÊN =====");
        for (Employee4 emp : employees) {
            emp.displayInfo();
            System.out.println();
        }

        // Tính tổng quỹ lương
        double totalSalary = 0;
        for (Employee4 emp : employees) {
            totalSalary += emp.calculateSalary();
        }

        // Tìm nhân viên lương cao nhất
        Employee4 highestPaid = employees.get(0);
        for (Employee4 emp : employees) {
            if (emp.calculateSalary() > highestPaid.calculateSalary()) {
                highestPaid = emp;
            }
        }

        // In thống kê
        System.out.println("===== THỐNG KÊ =====");
        System.out.printf("Tổng quỹ lương: %,.0f VNĐ%n", totalSalary);
        System.out.printf("Lương cao nhất: %s (%,.0f VNĐ)%n",
                highestPaid.name, highestPaid.calculateSalary());
    }

}

interface Payable {
    double calculateSalary();
}

abstract class Employee4 implements Payable {
    private final String id;
    protected  String name;
    private final String email;

    public Employee4(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public void displayInfo() {
        System.out.println("[" + id + "] " + name + " - " + email);
        System.out.printf("   → Lương: %,.0f VNĐ (%s)%n",
                calculateSalary(), getType());
    }

    public abstract String getType();

}

// ============== FULL-TIME EMPLOYEE ==============

class FullTimeEmployee1 extends Employee4 {
    private double baseSalary;
    private double kpiBonus;

    public FullTimeEmployee1(String id, String name, String email,
            double baseSalary, double kpiBonus) {
        super(id, name, email);
        this.baseSalary = baseSalary;
        this.kpiBonus = kpiBonus;
    }

    @Override
    public double calculateSalary() {
        return baseSalary * kpiBonus;
    }

    @Override
    public String getType() {
        return "Full-time";
    }

}

    // ============== PART-TIME EMPLOYEE ==============

class PartTimeEmployee1 extends Employee4 {
    private int hoursWorked;
    private double hourlyRate;

    public PartTimeEmployee1(String id, String name, String email,
                            int hoursWorked, double hourlyRate) {
        super(id, name, email);
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculateSalary() {
        return hoursWorked * hourlyRate;
    }

    @Override
    public String getType() {
        return "Part-time";
    }
}
