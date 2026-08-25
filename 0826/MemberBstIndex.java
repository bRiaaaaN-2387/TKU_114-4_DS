import java.util.ArrayList;
import java.util.List;

// 1. 領域物件 (Domain Object)
class Member {
    final int memberId; // Key 應在物件存續期間保持不變[cite: 1]
    String name;
    String email;

    public Member(int memberId, String name, String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: Email cannot be blank.");
        }
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "[ID: " + memberId + " | Name: " + name + " | Email: " + email + "]";
    }
}

// 2. BST 樹節點
class MemberNode {
    Member data;
    MemberNode left;
    MemberNode right;

    public MemberNode(Member data) {
        this.data = data;
    }
}

// 3. 索引系統
public class MemberBstIndex {
    
    private MemberNode root;

    // --- 新增會員 (Add) ---
    public boolean add(Member member) {
        if (member == null) return false;
        
        if (root == null) {
            root = new MemberNode(member);
            return true;
        }

        MemberNode current = root;
        while (true) {
            if (member.memberId == current.data.memberId) {
                System.out.println("Error: Member ID " + member.memberId + " already exists.");
                return false; // 拒絕重複的 ID[cite: 1]
            }
            if (member.memberId < current.data.memberId) {
                if (current.left == null) {
                    current.left = new MemberNode(member);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new MemberNode(member);
                    return true;
                }
                current = current.right;
            }
        }
    }

    // --- 尋找會員 (Find) ---
    public Member find(int memberId) {
        MemberNode current = root;
        while (current != null) {
            if (memberId == current.data.memberId) {
                return current.data;
            }
            current = memberId < current.data.memberId ? current.left : current.right;
        }
        return null;
    }

    // --- 更新 Email (Update Email) ---
    public boolean updateEmail(int memberId, String newEmail) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            System.out.println("Error: New email cannot be blank.");
            return false;
        }

        Member member = find(memberId);
        if (member == null) {
            System.out.println("Error: Member ID " + memberId + " not found.");
            return false;
        }

        member.email = newEmail;
        System.out.println("Success: Email updated for Member " + memberId);
        return true;
    }

    // --- 移除會員 (Remove) ---
    public boolean remove(int memberId) {
        if (find(memberId) == null) return false; // 先確認是否存在[cite: 1]
        root = removeHelper(root, memberId);
        return true;
    }

    private MemberNode removeHelper(MemberNode node, int memberId) {
        if (memberId < node.data.memberId) {
            node.left = removeHelper(node.left, memberId);
        } else if (memberId > node.data.memberId) {
            node.right = removeHelper(node.right, memberId);
        } else {
            // Case 1 & 2: Leaf 或 One-Child
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            // Case 3: Two-Children (尋找右子樹最小值來替換)[cite: 1]
            MemberNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = removeHelper(node.right, successor.data.memberId);
        }
        return node;
    }

    private MemberNode minimumNode(MemberNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // --- 中序報表 (Inorder Report) ---
    public void printInorderReport() {
        System.out.println("=== Member List (Sorted by ID) ===");
        if (root == null) {
            System.out.println("[No members found]");
        } else {
            inorderHelper(root);
        }
        System.out.println("==================================");
    }

    private void inorderHelper(MemberNode node) {
        if (node == null) return;
        inorderHelper(node.left);
        System.out.println(node.data);
        inorderHelper(node.right);
    }

    // --- 測試主程式 ---
    public static void main(String[] args) {
        MemberBstIndex index = new MemberBstIndex();
        
        System.out.println("--- 1. Add Members ---");
        index.add(new Member(105, "Alice", "alice@example.com"));
        index.add(new Member(102, "Bob", "bob@example.com"));
        index.add(new Member(108, "Charlie", "charlie@example.com"));
        index.add(new Member(101, "David", "david@example.com"));
        
        // 測試重複 ID 防呆
        index.add(new Member(105, "Alice Clone", "clone@example.com"));
        
        // 測試建立 Email 為空的 Member (預期會拋出 Exception)
        try {
            index.add(new Member(110, "Eve", "   "));
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Exception: " + e.getMessage());
        }
        System.out.println();

        System.out.println("--- 2. Update Email ---");
        // 正常更新
        index.updateEmail(102, "bob.new@example.com"); 
        // 測試更新為空白 Email
        index.updateEmail(105, ""); 
        // 測試更新不存在的會員
        index.updateEmail(999, "ghost@example.com"); 
        System.out.println();

        System.out.println("--- 3. Find Member ---");
        System.out.println("Find 108: " + index.find(108));
        System.out.println("Find 999: " + index.find(999));
        System.out.println();

        System.out.println("--- 4. Remove Member ---");
        // 移除存在的會員 (測試 Root 以外的節點)
        System.out.println("Remove 102: " + index.remove(102));
        System.out.println();

        System.out.println("--- 5. Final Report ---");
        index.printInorderReport();
    }
}