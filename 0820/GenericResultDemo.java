public class GenericResultDemo {

    // 1. 泛型類別 (Generic Class) Result<T>
    public static class Result<T> {
        private final boolean success;
        private final String message;
        private final T data;

        // 私有建構子，透過靜態工廠方法建立物件
        private Result(boolean success, String message, T data) {
            this.success = success;
            this.message = (message == null) ? "" : message.trim();
            this.data = data;
        }

        // 成功時的靜態建立方法
        public static <T> Result<T> success(T data, String message) {
            return new Result<>(true, message, data);
        }

        // 失敗時的靜態建立方法 (資料安全帶入 null)
        public static <T> Result<T> failure(String message) {
            return new Result<>(false, message, null);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        // 取出資料方法 (不用任何強制轉型 (Type Cast))
        public T getData() {
            return data;
        }

        @Override
        public String toString() {
            return String.format("Result [Success: %-5b | Message: %-25s | Data: %s]",
                    success, "\"" + message + "\"", (data == null ? "null" : data.toString()));
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        System.out.println("=== 1. Testing Result<String> ===");
        // 成功案例：包裹 String 資料
        Result<String> strSuccess = Result.success("Token_ABC_12345", "Authentication successful.");
        // 取出資料無需任何 (String) 強制轉型！
        String token = strSuccess.getData(); 
        System.out.println("Success Result : " + strSuccess);
        System.out.println("Extracted Data : " + token);

        // 失敗案例：data 為 null
        Result<String> strFailure = Result.failure("Invalid password entered.");
        System.out.println("Failure Result : " + strFailure);
        System.out.println("Extracted Data : " + strFailure.getData()); // 印出 null
        System.out.println();


        System.out.println("=== 2. Testing Result<Integer> ===");
        // 成功案例：包裹 Integer 資料
        Result<Integer> intSuccess = Result.success(200, "HTTP Request OK.");
        // 取出資料直接為 int/Integer，完全無需 (Integer) 轉型，且可直接做算術運算
        int statusCode = intSuccess.getData(); 
        System.out.println("Success Result : " + intSuccess);
        System.out.println("Extracted Status Code + 5 = " + (statusCode + 5));

        // 失敗案例：data 為 null
        Result<Integer> intFailure = Result.failure("Connection timed out.");
        System.out.println("Failure Result : " + intFailure);
        System.out.println("Extracted Data : " + intFailure.getData()); // 印出 null
        System.out.println();


        System.out.println("=== 3. Compile-Time Type Safety Validation ===");
        // 驗證編譯期型態安全（若取消註解下面這行，Java 編譯器會直接報錯，無法通過編譯）：
        // Result<Integer> wrongType = Result.success("This is a String", "Test"); // ❌ Compile Error!
        System.out.println("Compile-time safety verified: Mismatched types are blocked before runtime.");
    }
}