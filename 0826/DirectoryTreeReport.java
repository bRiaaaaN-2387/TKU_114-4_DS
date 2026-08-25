import java.util.ArrayList;
import java.util.List;

// 檔案系統節點 (可表示 File 或 Directory)
class FileNode {
    String name;
    boolean isDirectory;
    long size;
    List<FileNode> children;

    // 目錄的建構子
    public FileNode(String name) {
        this.name = name;
        this.isDirectory = true;
        this.size = 0; // 目錄初始大小為 0，需透過走訪計算
        this.children = new ArrayList<>();
    }

    // 檔案的建構子
    public FileNode(String name, long size) {
        this.name = name;
        this.isDirectory = false;
        this.size = size;
        this.children = null; // 檔案沒有子節點
    }

    // 加入子節點
    public void addChild(FileNode child) {
        if (this.isDirectory) {
            this.children.add(child);
        } else {
            System.out.println("Error: Cannot add child to a file.");
        }
    }
}

// 統計資料封裝類別
class TreeStats {
    int totalNodes = 0;
    int fileCount = 0;
    int dirCount = 0;
    FileNode maxFile = null;
}

public class DirectoryTreeReport {

    private FileNode root;

    public DirectoryTreeReport(FileNode root) {
        this.root = root;
    }

    // 1. 後序走訪計算目錄容量，並收集統計資料
    public void generateReport() {
        if (root == null) {
            System.out.println("The file system is empty.");
            return;
        }

        TreeStats stats = new TreeStats();
        System.out.println("--- Calculating Directory Sizes (Postorder) ---");
        calculateSizePostorder(root, stats);
        System.out.println("-----------------------------------------------\n");

        int h = calculateHeight(root);

        System.out.println("=== File System Statistics Report ===");
        System.out.println("Total Nodes    : " + stats.totalNodes);
        System.out.println("Directory Count: " + stats.dirCount);
        System.out.println("File Count     : " + stats.fileCount);
        System.out.println("Tree Height    : " + h);
        
        if (stats.maxFile != null) {
            System.out.println("Largest File   : " + stats.maxFile.name + " (" + stats.maxFile.size + " bytes)");
        } else {
            System.out.println("Largest File   : None");
        }
        System.out.println("Root Total Size: " + root.size + " bytes");
        System.out.println("=====================================");
    }

    // 核心遞迴：後序走訪 (Postorder)
    private long calculateSizePostorder(FileNode node, TreeStats stats) {
        if (node == null) return 0;

        stats.totalNodes++;

        if (node.isDirectory) {
            stats.dirCount++;
            long totalSize = 0;
            
            // 遞迴處理所有 Children (Left/Right 的概念延伸)
            for (FileNode child : node.children) {
                totalSize += calculateSizePostorder(child, stats);
            }
            
            // 處理 Node 本身：更新目錄總大小
            node.size = totalSize;
            System.out.println("Directory: " + node.name + " | Calculated Size: " + node.size);
            return totalSize;
            
        } else {
            // 若為檔案，更新 file count 與 max file
            stats.fileCount++;
            if (stats.maxFile == null || node.size > stats.maxFile.size) {
                stats.maxFile = node;
            }
            return node.size;
        }
    }

    // 2. 計算樹的高度
    // 依教材定義：空樹為 -1，單一節點 (Leaf) 為 0
    private int calculateHeight(FileNode node) {
        if (node == null) return -1;
        
        // 若是檔案或空目錄，高度為 0
        if (!node.isDirectory || node.children.isEmpty()) return 0;

        int maxChildHeight = -1;
        for (FileNode child : node.children) {
            maxChildHeight = Math.max(maxChildHeight, calculateHeight(child));
        }
        
        return 1 + maxChildHeight;
    }

    // 測試主程式
    public static void main(String[] args) {
        // 建立檔案系統樹
        FileNode rootDir = new FileNode("root");
        
        FileNode docsDir = new FileNode("documents");
        FileNode picsDir = new FileNode("pictures");
        FileNode sysDir = new FileNode("system");

        // 加入檔案
        docsDir.addChild(new FileNode("resume.pdf", 1024));
        docsDir.addChild(new FileNode("hw.docx", 2048));
        
        picsDir.addChild(new FileNode("vacation.jpg", 4096));
        picsDir.addChild(new FileNode("profile.png", 512));
        
        sysDir.addChild(new FileNode("config.sys", 128));
        
        // 建立層級關係
        rootDir.addChild(docsDir);
        rootDir.addChild(picsDir);
        rootDir.addChild(sysDir);
        
        // 建立深層結構
        FileNode secretDir = new FileNode("secrets");
        secretDir.addChild(new FileNode("passwords.txt", 64));
        docsDir.addChild(secretDir);

        // 執行報表
        DirectoryTreeReport report = new DirectoryTreeReport(rootDir);
        report.generateReport();
    }
}