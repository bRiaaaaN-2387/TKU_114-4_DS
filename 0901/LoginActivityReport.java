import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginActivityReport {
    // 模擬登入紀錄結構
    public record LoginEvent(String account, String ip) {}

    public static void analyzeLogins(List<LoginEvent> events) {
        if (events == null || events.isEmpty()) return;

        // 使用 HashMap 統計每個帳號的登入次數
        Map<String, Integer> loginCounts = new HashMap<>();
        // 使用 HashMap 與 HashSet 統計每個帳號的不重複登入 IP
        Map<String, Set<String>> uniqueIps = new HashMap<>();

        for (LoginEvent event : events) {
            if (event.account() == null || event.account().isBlank()) continue;
            
            String account = event.account().trim();
            String ip = event.ip() != null ? event.ip().trim() : "unknown";

            // 累加登入次數
            loginCounts.merge(account, 1, Integer::sum);
            
            // 將 IP 加入對應帳號的 HashSet 中，自動去除重複
            uniqueIps.computeIfAbsent(account, k -> new HashSet<>()).add(ip);
        }

        System.out.println("--- anomalous login report ---");
        boolean foundAnomaly = false;

        // 走訪統計結果，找出異常行為 (例如：登入超過 3 次，或使用超過 1 個不同 IP)
        for (Map.Entry<String, Integer> entry : loginCounts.entrySet()) {
            String account = entry.getKey();
            int count = entry.getValue();
            Set<String> ips = uniqueIps.get(account);
            
            if (count > 3 || ips.size() > 1) {
                foundAnomaly = true;
                System.out.printf("account=%s total_logins=%d unique_ips=%d ip_list=%s%n",
                        account, count, ips.size(), ips);
            }
        }

        if (!foundAnomaly) {
            System.out.println("no anomalous activity detected.");
        }
    }

    public static void main(String[] args) {
        List<LoginEvent> logs = List.of(
            new LoginEvent("userA", "192.168.1.10"),
            new LoginEvent("userA", "192.168.1.10"),
            new LoginEvent("userB", "10.0.0.5"),
            new LoginEvent("userB", "10.0.0.12"), // 異常：不同 IP
            new LoginEvent("userC", "172.16.0.8"),
            new LoginEvent("userA", "192.168.1.10"),
            new LoginEvent("userA", "192.168.1.10")  // 異常：次數大於 3
        );

        analyzeLogins(logs);
    }
}