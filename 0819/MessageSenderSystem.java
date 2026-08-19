public class MessageSenderSystem {

    // 1. 訊息發送介面 (Interface)
    public interface MessageSender {
        /**
         * 發送訊息方法
         * @param receiver 接收者 (Email/手機號碼/使用者名稱)
         * @param message  訊息內容
         * @return 發送成功回傳 true，否則回傳 false
         */
        boolean send(String receiver, String message);
    }

    // 2. 實作類別一：Email 發送器 (EmailSender)
    public static class EmailSender implements MessageSender {
        @Override
        public boolean send(String receiver, String message) {
            // 邊界條件：檢查空白 receiver 或 message
            if (isInvalidInput(receiver, message)) {
                return false;
            }
            // 實際發送時顯示的 Console 輸出結果
            System.out.println("[EMAIL OUT] To: <" + receiver.trim() + "> | Content: \"" + message.trim() + "\"");
            return true;
        }
    }

    // 3. 實作類別二：簡訊發送器 (SmsSender)
    public static class SmsSender implements MessageSender {
        @Override
        public boolean send(String receiver, String message) {
            // 邊界條件：檢查空白 receiver 或 message
            if (isInvalidInput(receiver, message)) {
                return false;
            }
            // 實際發送時顯示的 Console 輸出結果
            System.out.println("[SMS OUT] To Phone: " + receiver.trim() + " | Content: \"" + message.trim() + "\"");
            return true;
        }
    }

    // 4. 實作類別三：控制台發送器 (ConsoleSender)
    public static class ConsoleSender implements MessageSender {
        @Override
        public boolean send(String receiver, String message) {
            // 邊界條件：檢查空白 receiver 或 message
            if (isInvalidInput(receiver, message)) {
                return false;
            }
            // 實際發送時顯示的 Console 輸出結果
            System.out.println("[CONSOLE LOG] User: " + receiver.trim() + " | Content: \"" + message.trim() + "\"");
            return true;
        }
    }

    // 通用的輸入驗證輔助方法 (防止 null 或全空白字串)
    private static boolean isInvalidInput(String receiver, String message) {
        return receiver == null || receiver.trim().isEmpty() ||
               message == null || message.trim().isEmpty();
    }

    // 5. 通知服務方法 (只依賴 MessageSender 介面)
    public static boolean notify(MessageSender sender, String receiver, String message) {
        // 先顯示輸入參數，讓你清楚知道丟進去了什麼 Message
        System.out.println("--------------------------------------------------");
        System.out.println("[INPUT] Receiver: " + (receiver == null ? "null" : "\"" + receiver + "\"") + 
                           " | Message: " + (message == null ? "null" : "\"" + message + "\""));

        if (sender == null) {
            System.out.println("[OUTPUT FAILED] Sender interface is null.");
            return false;
        }
        
        // 呼叫介面定義的 send() 方法 (動態繫結)
        boolean success = sender.send(receiver, message);
        if (!success) {
            System.out.println("[OUTPUT FAILED] Transaction rejected due to blank receiver or message.");
        }
        return success;
    }

    // 主程式測試區
    public static void main(String[] args) {
        MessageSender emailSender = new EmailSender();
        MessageSender smsSender = new SmsSender();
        MessageSender consoleSender = new ConsoleSender();

        System.out.println("=== 1. Normal Cases ===");
        notify(emailSender, "alice@example.com", "Your OTP is 123456.");
        notify(smsSender, "+886912345678", "Meeting scheduled at 2 PM.");
        notify(consoleSender, "User_Bob", "System update completed.");

        System.out.println("\n=== 2. Edge Cases (Blank/Null Message) ===");
        // 情況 A: 輸入全空白 Message
        notify(emailSender, "charlie@example.com", "   ");

        // 情況 B: 輸入 null Message
        notify(smsSender, "+886987654321", null);

        // 情況 C: 輸入空白 Receiver
        notify(consoleSender, "   ", "Hello World");
    }
}