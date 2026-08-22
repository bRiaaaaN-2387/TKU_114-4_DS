import java.util.ArrayDeque;
import java.util.Deque;

public class BrowserBackStack {

    // 使用 Deque 作為堆疊 (Stack) 來保存瀏覽歷程
    // 堆疊頂端 (Top) 將永遠代表當前所在的網頁
    private final Deque<String> historyStack;

    public BrowserBackStack() {
        this.historyStack = new ArrayDeque<>();
    }

    // 訪問新網頁：將新網址推入堆疊頂端
    public void visit(String url) {
        historyStack.push(url);
        System.out.println("Action: Visited -> " + url);
    }

    // 返回上一頁：彈出堆疊頂端的網頁，並顯示新的頂端網頁
    public void back() {
        // 確保空堆疊，或只有一頁時不會拋出 NoSuchElementException
        if (historyStack.isEmpty() || historyStack.size() == 1) {
            System.out.println("Action: Back -> Failed (No previous page in history)");
        } else {
            String leftPage = historyStack.pop();
            String currentPage = historyStack.peek();
            System.out.println("Action: Back -> Left [" + leftPage + "], Now at [" + currentPage + "]");
        }
    }

    // 取得當前網頁：查看堆疊頂端但不彈出
    public void current() {
        if (historyStack.isEmpty()) {
            System.out.println("Current Page: [None - Browser is empty]");
        } else {
            System.out.println("Current Page: " + historyStack.peek());
        }
    }

    public static void main(String[] args) {
        BrowserBackStack browser = new BrowserBackStack();

        System.out.println("=== Browser History Simulation ===");

        // 測試操作 1 & 2：在空狀態下呼叫 current() 與 back()，確認不會產生 Exception
        browser.current();
        browser.back();
        System.out.println();

        // 測試操作 3, 4, 5：連續訪問三個網頁
        browser.visit("https://www.google.com");
        browser.visit("https://github.com");
        browser.visit("https://stackoverflow.com");
        System.out.println();

        // 測試操作 6：確認當前網頁是否正確
        browser.current();
        System.out.println();

        // 測試操作 7 & 8：連續返回兩次
        browser.back();
        browser.back();
        System.out.println();

        // 測試操作 9：確認返回後的當前網頁
        browser.current();
        System.out.println();

        // 測試操作 10：嘗試在只剩一頁時返回
        browser.back();
    }
}