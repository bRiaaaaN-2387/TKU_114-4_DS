public class ScoreRangeBst {

    // 定義樹的節點，包含分數與學號
    static class Node {
        int score;
        String studentId;
        Node left, right;

        public Node(int score, String studentId) {
            this.score = score;
            this.studentId = studentId;
        }
    }

    private Node root;

    /**
     * 插入資料
     * 使用 Score + StudentId 作為複合鍵來維持 BST 的結構
     */
    public void insert(int score, String studentId) {
        root = insertRec(root, score, studentId);
    }

    private Node insertRec(Node node, int score, String studentId) {
        // 如果節點為空，直接建立新節點
        if (node == null) {
            return new Node(score, studentId);
        }

        // 1. 先比較分數
        if (score < node.score) {
            node.left = insertRec(node.left, score, studentId);
        } else if (score > node.score) {
            node.right = insertRec(node.right, score, studentId);
        } else {
            // 2. 如果分數相同，則比較學號 (studentId)
            if (studentId.compareTo(node.studentId) < 0) {
                node.left = insertRec(node.left, score, studentId);
            } else if (studentId.compareTo(node.studentId) > 0) {
                node.right = insertRec(node.right, score, studentId);
            }
            // 若學號也完全相同，則視為重複資料不處理（或視需求進行更新）
        }
        return node;
    }

    /**
     * 範圍查詢
     * 找出所有分數介於 minScore 與 maxScore 之間的資料並印出
     */
    public void queryRange(int minScore, int maxScore) {
        System.out.println("查詢分數範圍 [" + minScore + " ~ " + maxScore + "] 的結果：");
        queryRangeRec(root, minScore, maxScore);
        System.out.println("--------------------------------------------------");
    }

    private void queryRangeRec(Node node, int minScore, int maxScore) {
        if (node == null) {
            return;
        }

        // 優化遍歷：如果當前節點分數大於 minScore，代表左子樹還有可能有符合範圍的資料
        if (node.score > minScore) {
            queryRangeRec(node.left, minScore, maxScore);
        }

        // 如果當前節點分數落在範圍內，則進行處理（此處為印出）
        if (node.score >= minScore && node.score <= maxScore) {
            System.out.println("學號: " + node.studentId + ", 分數: " + node.score);
        }

        // 優化遍歷：如果當前節點分數小於 maxScore，代表右子樹還有可能有符合範圍的資料
        if (node.score < maxScore) {
            queryRangeRec(node.right, minScore, maxScore);
        }
    }

    // 測試主程式
    public static void main(String[] args) {
        ScoreRangeBst bst = new ScoreRangeBst();

        // 插入測試資料 (包含同分情況)
        bst.insert(85, "A1001");
        bst.insert(92, "A1002");
        bst.insert(85, "A1003"); // 同分，學號不同
        bst.insert(78, "A1004");
        bst.insert(95, "A1005");
        bst.insert(85, "A0999"); // 同分，學號不同，預期排在 A1001 前面
        bst.insert(88, "A1006");

        // 查詢範圍：80 到 90 分
        bst.queryRange(80, 90);

        // 查詢範圍：90 到 100 分
        bst.queryRange(90, 100);
    }
}