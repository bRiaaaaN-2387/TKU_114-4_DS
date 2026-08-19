public class EmployeeConstructorChain {

    // 1. 抽象父類別 (EmployeeBase)
    public static abstract class EmployeeBase {
        private String id;
        private String name;

        // 父類別建構子
        public EmployeeBase(String id, String name) {
            // 輸出自己類別的名稱，觀察建構子執行順序
            System.out.println("[Constructor Execution] -> EmployeeBase");
            this.id = (id == null || id.trim().isEmpty()) ? "Unknown_ID" : id.trim();
            this.name = (name == null || name.trim().isEmpty()) ? "Unknown_Name" : name.trim();
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        // 抽象方法：計算薪資 (由子類別實作)
        public abstract double calculatePay();

        @Override
        public String toString() {
            return String.format("ID: %s | Name: %-8s | Pay: $%.1f", id, name, calculatePay());
        }
    }

    // 2. 正職員工子類別 (FullTimeEmployee)
    public static class FullTimeEmployee extends EmployeeBase {
        private double monthlySalary; // 月薪

        public FullTimeEmployee(String id, String name, double monthlySalary) {
            // 必須使用 super(...) 呼叫父類別建構子
            super(id, name);
            System.out.println("[Constructor Execution] -> FullTimeEmployee");
            
            // 邊界條件：負數薪資自動轉為 0
            this.monthlySalary = (monthlySalary < 0) ? 0.0 : monthlySalary;
        }

        @Override
        public double calculatePay() {
            return monthlySalary;
        }
    }

    // 3. 兼職員工子類別 (PartTimeEmployee)
    public static class PartTimeEmployee extends EmployeeBase {
        private double hourlyRate; // 時薪
        private double hoursWorked; // 時數

        public PartTimeEmployee(String id, String name, double hourlyRate, double hoursWorked) {
            // 必須使用 super(...) 呼叫父類別建構子
            super(id, name);
            System.out.println("[Constructor Execution] -> PartTimeEmployee");

            // 邊界條件：負數時薪或時數自動轉為 0
            this.hourlyRate = (hourlyRate < 0) ? 0.0 : hourlyRate;
            this.hoursWorked = (hoursWorked < 0) ? 0.0 : hoursWorked;
        }

        @Override
        public double calculatePay() {
            return hourlyRate * hoursWorked;
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        System.out.println("=== 1. Instantiating FullTimeEmployee ===");
        // 建立正職員工物件 (測試正常薪資 $50000)
        FullTimeEmployee ft = new FullTimeEmployee("FT-001", "Alice", 50000);
        System.out.println("Object Created: " + ft + "\n");

        System.out.println("=== 2. Instantiating PartTimeEmployee ===");
        // 建立兼職員工物件 (測試正常時薪 $200, 時數 80)
        PartTimeEmployee pt = new PartTimeEmployee("PT-001", "Bob", 200, 80);
        System.out.println("Object Created: " + pt + "\n");

        System.out.println("=== 3. Edge Case Testing (Negative values forced to 0) ===");
        FullTimeEmployee ftNegative = new FullTimeEmployee("FT-002", "Charlie", -30000);
        PartTimeEmployee ptNegative = new PartTimeEmployee("PT-002", "David", -180, -40);
        System.out.println("Negative Salary FT: " + ftNegative);
        System.out.println("Negative Rate/Hours PT: " + ptNegative);
    }
}