public class TransportFareSystem {

    // 1. 抽象父類別 (Abstract Class)
    public static abstract class Transport {
        private String routeName;

        public Transport(String routeName) {
            this.routeName = (routeName == null || routeName.trim().isEmpty()) ? "Unknown Route" : routeName.trim();
        }

        public String getRouteName() {
            return routeName;
        }

        // 抽象方法：計算票價 (由子類別實作)
        public abstract double calculateFare(int distance);
    }

    // 2. 子類別：公車 (Bus)
    public static class Bus extends Transport {
        private static final double BASE_FARE = 15.0; // 基本底價 $15

        public Bus(String routeName) {
            super(routeName);
        }

        // 覆寫 calculateFare: 公車採段號/里程計費 (超過 10 公里每公里加 2 元)
        @Override
        public double calculateFare(int distance) {
            if (distance <= 0) return 0.0;
            if (distance <= 10) {
                return BASE_FARE;
            }
            return BASE_FARE + (distance - 10) * 2.0;
        }
    }

    // 3. 子類別：計程車 (Taxi)
    public static class Taxi extends Transport {
        private static final double START_FARE = 85.0; // 起跳價 $85 (前 1.25 公里)

        public Taxi(String routeName) {
            super(routeName);
        }

        // 覆寫 calculateFare: 計程車跳表計費 (超過 1.25 公里後每公里加 30 元)
        @Override
        public double calculateFare(int distance) {
            if (distance <= 0) return 0.0;
            if (distance <= 1) {
                return START_FARE;
            }
            return START_FARE + (distance - 1) * 30.0;
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        // 設定測試距離
        int testDistance = 15; 

        // 1. 使用抽象類別陣列 Transport[] 保存不同的子類別物件 (多型)
        Transport[] transports = new Transport[] {
            new Bus("Route 307 (Short Bus)"),
            new Bus("Route 299 (Long Bus)"),
            new Taxi("City Taxi #01"),
            new Taxi("Airport Taxi #02")
        };

        System.out.println("=== Transport Fare System ===");
        System.out.println("Target Distance: " + testDistance + " km\n");

        // 2. 多型呼叫 (Polymorphic Call)
        // 完全不使用 instanceof 判斷型態，直接呼叫被覆寫的 calculateFare()
        for (Transport t : transports) {
            double fare = t.calculateFare(testDistance);
            System.out.println("Route: " + String.format("%-25s", t.getRouteName()) + 
                               " | Calculated Fare: $" + String.format("%.1f", fare));
        }
    }
}