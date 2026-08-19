public class DeviceInspectionSystem {

    // 1. 父類別 (Device)
    public static class Device {
        private String deviceId;

        public Device(String deviceId) {
            this.deviceId = (deviceId == null || deviceId.trim().isEmpty()) ? "Unknown_ID" : deviceId.trim();
        }

        public String getDeviceId() {
            return deviceId;
        }

        // 設備基礎自我診斷方法
        public void runDiagnostic() {
            System.out.println("[DIAGNOSTIC] Running basic diagnostic for Device ID: " + deviceId);
        }
    }

    // 2. 子類別：筆記型電腦 (Laptop)
    public static class Laptop extends Device {
        public Laptop(String deviceId) {
            super(deviceId);
        }

        @Override
        public void runDiagnostic() {
            System.out.println("[DIAGNOSTIC] Laptop [" + getDeviceId() + "]: Checking CPU temperature and Battery health.");
        }
    }

    // 3. 子類別：印表機 (Printer)
    public static class Printer extends Device {
        public Printer(String deviceId) {
            super(deviceId);
        }

        @Override
        public void runDiagnostic() {
            System.out.println("[DIAGNOSTIC] Printer [" + getDeviceId() + "]: Checking ink levels and paper alignment.");
        }

        // Printer 獨有的特有方法
        public void cleanPrintHead() {
            System.out.println("  ==> [PRINTER ACTION] Executing print head cleaning for Printer [" + getDeviceId() + "]...");
        }
    }

    // 4. 子類別：路由器 (Router)
    public static class Router extends Device {
        public Router(String deviceId) {
            super(deviceId);
        }

        @Override
        public void runDiagnostic() {
            System.out.println("[DIAGNOSTIC] Router [" + getDeviceId() + "]: Testing signal stability and bandwidth response.");
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        // 1. 使用 Device[] 保存至少四個物件 (多型陣列)
        Device[] devices = new Device[] {
            new Laptop("LAPTOP-01"),
            new Printer("PRINTER-01"),
            new Router("ROUTER-01"),
            new Printer("PRINTER-02")
        };

        System.out.println("=== Starting Device Inspection & Diagnostic System ===");

        for (Device d : devices) {
            // 2. 多型執行：所有設備皆執行 runDiagnostic()
            d.runDiagnostic();

            // 3. 安全型態判斷：使用 Pattern Matching for instanceof
            // 自動判斷是否為 Printer，若是，則自動繫結至變數 p，完全不需手動撰寫傳統的 (Printer) 轉型！
            if (d instanceof Printer p) {
                p.cleanPrintHead();
            }
            System.out.println("--------------------------------------------------");
        }
    }
}