import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebsiteLinkGraph {
    // 使用 Map 儲存每個網頁的有向連結 (Directed Adjacency List)
    private final Map<String, List<String>> outgoing = new LinkedHashMap<>();

    // 新增網頁節點
    public void addPage(String url) {
        if (url == null || url.isBlank()) return;
        outgoing.putIfAbsent(url.trim(), new ArrayList<>());
    }

    // 新增有向連結 (Directed Edge)
    public boolean addLink(String from, String to) {
        if (!outgoing.containsKey(from) || !outgoing.containsKey(to)) return false;
        
        List<String> links = outgoing.get(from);
        if (links.contains(to)) return false; // 避免重複連結
        
        links.add(to);
        return true;
    }

    // 取得指定網頁的 outgoing links
    public List<String> getOutgoingLinks(String url) {
        return outgoing.getOrDefault(url, List.of());
    }

    // 計算指定網頁的 incoming count (In-degree)
    public int getIncomingCount(String url) {
        if (!outgoing.containsKey(url)) return 0;
        
        int count = 0;
        // 走訪所有網頁的 outgoing 集合，檢查是否包含目標 url
        for (List<String> links : outgoing.values()) {
            if (links.contains(url)) {
                count++;
            }
        }
        return count;
    }

    // 尋找無 incoming 頁面 (In-degree 為 0，通常為孤兒頁面或隱藏入口)
    public List<String> getNoIncomingPages() {
        List<String> result = new ArrayList<>();
        for (String url : outgoing.keySet()) {
            if (getIncomingCount(url) == 0) {
                result.add(url);
            }
        }
        return result;
    }

    // 尋找無 outgoing 頁面 (Out-degree 為 0，即死胡同頁面)
    public List<String> getNoOutgoingPages() {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : outgoing.entrySet()) {
            if (entry.getValue().isEmpty()) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public static void main(String[] args) {
        WebsiteLinkGraph graph = new WebsiteLinkGraph();
        
        // 初始化網頁節點
        List.of("Home", "About", "Products", "Contact", "HiddenPage", "DeadEndSite")
            .forEach(graph::addPage);

        // 建立有向連結
        graph.addLink("Home", "About");
        graph.addLink("Home", "Products");
        graph.addLink("Home", "Contact");
        graph.addLink("About", "Home");
        graph.addLink("Products", "Contact");
        graph.addLink("HiddenPage", "Home"); 
        
        System.out.println("Home outgoing links=" + graph.getOutgoingLinks("Home"));
        System.out.println("Contact incoming count=" + graph.getIncomingCount("Contact"));
        
        System.out.println("\npages with no incoming=" + graph.getNoIncomingPages());
        System.out.println("pages with no outgoing=" + graph.getNoOutgoingPages());
    }
}