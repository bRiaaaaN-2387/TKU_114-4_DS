import java.util.Objects;

public class MemberEqualityPractice {

    // 內部類別：圖書館會員 (LibraryMember)
    public static class LibraryMember {
        private String memberId;
        private String name;
        private String email;

        // 建構子 (Constructor)
        public LibraryMember(String memberId, String name, String email) {
            this.memberId = memberId;
            this.name = name;
            this.email = email;
        }

        public String getMemberId() {
            return memberId;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        // 1. 覆寫 toString()
        @Override
        public String toString() {
            return String.format("Member ID: %s | Name: %s | Email: %s", memberId, name, email);
        }

        // 2. 覆寫 equals() - 僅以 memberId 判斷身分
        @Override
        public boolean equals(Object obj) {
            // (1) 參考同一記憶體位址，直接回傳 true
            if (this == obj) {
                return true;
            }

            // (2) 邊界條件：與 null 比較或型態不一致，回傳 false（防範 NullPointerException）
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            // (3) 強制轉型後，比較 memberId
            LibraryMember other = (LibraryMember) obj;
            return Objects.equals(this.memberId, other.memberId);
        }

        // 3. 覆寫 hashCode() - 與 equals() 保持一致，僅使用 memberId 計算
            @Override
                public int hashCode() {
            return Objects.hash(memberId);
}
    }

    // 主程式區
    public static void main(String[] args) {
        // 建立兩個 id 相同，但 email 不同的會員物件
        LibraryMember m1 = new LibraryMember("M001", "Alice", "alice@example.com");
        LibraryMember m2 = new LibraryMember("M007", "Bob", "bob@gmail.com");
        System.out.println("=== 1. Member Information ===");
        System.out.println("m1 -> " + m1);
        System.out.println("m2 -> " + m2);
        System.out.println();

        System.out.println("=== 2. Comparison Results ===");
        // == 比較：比較兩者的記憶體參考位址 (Memory Reference)
        System.out.println("m1 == m2           : " + (m1 == m2));

        // equals() 比較：比較業務邏輯上的身分 (memberId)
        System.out.println("m1.equals(m2)      : " + m1.equals(m2));
        System.out.println();

        System.out.println("=== 3. Edge Case Testing (Null & Type Safety) ===");
        // 邊界條件：與 null 比較，應該回傳 false 且不拋出 Exception
        System.out.println("m1.equals(null)    : " + m1.equals(null));
        
        // 與不同類別的物件比較
        System.out.println("m1.equals(\"M001\") : " + m1.equals("M001"));
    }
}