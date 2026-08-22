import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class TextEditorHistory {

    // 使用兩個 Deque 作為 undo 與 redo 的堆疊 (Stack)
    private final Deque<String> undoStack;
    private final Deque<String> redoStack;

    public TextEditorHistory() {
        this.undoStack = new ArrayDeque<>();
        this.redoStack = new ArrayDeque<>();
    }

    // 新增操作：將新文字推入 undoStack，並且清空 redoStack
    public void type(String text) {
        undoStack.push(text);
        redoStack.clear();
        System.out.println("Action: Type -> '" + text + "'");
        printState();
    }

    // 復原操作：將 undoStack 頂端的資料彈出，並推入 redoStack
    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Action: Undo -> Failed (Nothing to undo)");
        } else {
            String undoneText = undoStack.pop();
            redoStack.push(undoneText);
            System.out.println("Action: Undo -> Reverted '" + undoneText + "'");
        }
        printState();
    }

    // 重做操作：將 redoStack 頂端的資料彈出，並推回 undoStack
    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Action: Redo -> Failed (Nothing to redo)");
        } else {
            String redoneText = redoStack.pop();
            undoStack.push(redoneText);
            System.out.println("Action: Redo -> Applied '" + redoneText + "'");
        }
        printState();
    }

    // 輔助方法：輸出當前文字內容與堆疊狀態
    private void printState() {
        // 使用 descendingIterator 從底部 (最舊) 讀取到頂部 (最新)，來重建完整文件內容
        StringBuilder currentDocument = new StringBuilder();
        Iterator<String> iterator = undoStack.descendingIterator();
        while (iterator.hasNext()) {
            currentDocument.append(iterator.next());
        }

        System.out.println("Current Text : [" + currentDocument.toString() + "]");
        System.out.println("Undo Stack   : " + undoStack); // 顯示格式: [最新, ..., 最舊]
        System.out.println("Redo Stack   : " + redoStack);
        System.out.println("--------------------------------------------------");
    }

    public static void main(String[] args) {
        System.out.println("=== Text Editor Undo/Redo System ===\n");
        TextEditorHistory editor = new TextEditorHistory();

        // 1. 測試空堆疊情況下的 Undo 與 Redo
        System.out.println("--- Testing Empty Stacks ---");
        editor.undo();
        editor.redo();

        // 2. 測試連續新增操作
        System.out.println("--- Typing Actions ---");
        editor.type("Java");
        editor.type(" is");
        editor.type(" awesome!");

        // 3. 測試 Undo 操作
        System.out.println("--- Undo Actions ---");
        editor.undo(); // 復原 " awesome!"
        editor.undo(); // 復原 " is"

        // 4. 測試 Redo 操作
        System.out.println("--- Redo Actions ---");
        editor.redo(); // 重做 " is"

        // 5. 測試新增操作會清空 redoStack 的特性
        System.out.println("--- Typing after Undo (Clears Redo Stack) ---");
        editor.type(" powerful."); // 此時原本 redo 堆疊中的 " awesome!" 應該被清空

        // 6. 再次測試 Redo，確認是否已被清空
        System.out.println("--- Redo after new Typing ---");
        editor.redo();
    }
}