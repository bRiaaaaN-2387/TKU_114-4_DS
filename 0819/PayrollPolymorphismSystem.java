public class PayrollPolymorphismSystem {

    // 1. 抽象父類別 (Employee)
    public static abstract class Employee {
        private String id;
        private String name;

        public Employee(String id, String name) {
            this.id = (id == null || id.trim().isEmpty()) ? "Unknown_ID" : id.trim();
            this.name = (name == null || name.trim().isEmpty()) ? "Unknown_Name" : name.trim();
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        // 抽象方法：計算實領薪資 (由子類別各自實作)
        public abstract double calculatePay();

        @Override
        public String toString() {
            return String.format("ID: %-6s | Name: %-8s | Role: %-15s | Calculated Pay: $%.1f",
                    id, name, getClass().getSimpleName(), calculatePay());
        }
    }

    // 2. 子類別一：月薪制員工 (SalariedEmployee)
    public static class SalariedEmployee extends Employee {
        private double monthlySalary;

        public SalariedEmployee(String id, String name, double monthlySalary) {
            super(id, name);
            this.monthlySalary = (monthlySalary < 0) ? 0.0 : monthlySalary;
        }

        @Override
        public double calculatePay() {
            return monthlySalary;
        }
    }

    // 3. 子類別二：時薪制員工 (HourlyEmployee)
    public static class HourlyEmployee extends Employee {
        private double hourlyRate;
        private double hoursWorked;

        public HourlyEmployee(String id, String name, double hourlyRate, double hoursWorked) {
            super(id, name);
            this.hourlyRate = (hourlyRate < 0) ? 0.0 : hourlyRate;
            this.hoursWorked = (hoursWorked < 0) ? 0.0 : hoursWorked;
        }

        @Override
        public double calculatePay() {
            return hourlyRate * hoursWorked;
        }
    }

    // 4. 子類別三：業務/抽成制員工 (CommissionEmployee)
    public static class CommissionEmployee extends Employee {
        private double baseSalary;  // 底薪
        private double totalSales;  // 總銷售額
        private double commissionRate; // 抽成比例 (例如 0.05 代表 5%)

        public CommissionEmployee(String id, String name, double baseSalary, double totalSales, double commissionRate) {
            super(id, name);
            this.baseSalary = (baseSalary < 0) ? 0.0 : baseSalary;
            this.totalSales = (totalSales < 0) ? 0.0 : totalSales;
            this.commissionRate = (commissionRate < 0) ? 0.0 : commissionRate;
        }

        @Override
        public double calculatePay() {
            return baseSalary + (totalSales * commissionRate);
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        // 1. 使用多型陣列 Employee[] 儲存各種不同計薪方式的員工
        Employee[] employees = new Employee[] {
            new SalariedEmployee("E001", "Alice", 60000.0),                     // 月薪: $60,000
            new HourlyEmployee("E002", "Bob", 200.0, 160.0),                     // 時薪: 200 * 160 = $32,000
            new CommissionEmployee("E003", "Charlie", 30000.0, 500000.0, 0.08), // 業務: 30000 + (500000 * 0.08) = $70,000
            new SalariedEmployee("E004", "David", 45000.0),                     // 月薪: $45,000
            new HourlyEmployee("E005", "Eve", 250.0, 120.0)                      // 時薪: 250 * 120 = $30,000
        };

        System.out.println("=== 1. All Employee Payroll Details ===");
        double totalPayroll = 0.0;
        Employee topEarner = employees[0];

        // 2. 透過多型計算薪資總額與最高薪資員工
        for (Employee emp : employees) {
            double currentPay = emp.calculatePay();
            
            // 累加總薪資
            totalPayroll += currentPay;

            // 尋找最高薪資員工
            if (currentPay > topEarner.calculatePay()) {
                topEarner = emp;
            }

            // 印出個人詳細薪資資訊
            System.out.println(emp);
        }

        System.out.println("\n=== 2. Payroll Summary & Statistics ===");
        System.out.printf("Total Payroll Expense : $%.1f\n", totalPayroll);
        System.out.println("Highest Paid Employee  : " + topEarner.getName() + " (" + topEarner.getId() + ")");
        System.out.printf("Highest Salary Amount  : $%.1f\n", topEarner.calculatePay());
    }
}