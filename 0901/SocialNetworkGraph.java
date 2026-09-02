import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SocialNetworkGraph {
    // 使用 LinkedHashMap 與 LinkedHashSet 保持插入順序
    private final Map<String, Set<String>> network = new LinkedHashMap<>();

    // 新增使用者 (Vertex)
    public boolean addUser(String user) {
        if (user == null || user.isBlank()) return false;
        return network.putIfAbsent(user.trim(), new LinkedHashSet<>()) == null;
    }

    // 建立好友關係 (Undirected Edge)
    public boolean addFriend(String user1, String user2) {
        if (!network.containsKey(user1) || !network.containsKey(user2)) return false;
        if (user1.equals(user2)) return false;
        
        boolean changed = network.get(user1).add(user2);
        network.get(user2).add(user1); // 雙向新增
        return changed;
    }

    // 解除好友關係
    public boolean removeFriend(String user1, String user2) {
        if (!network.containsKey(user1) || !network.containsKey(user2)) return false;
        
        boolean changed = network.get(user1).remove(user2);
        network.get(user2).remove(user1); // 雙向移除
        return changed;
    }

    // 尋找共同好友 (交集)
    public List<String> mutualFriends(String user1, String user2) {
        if (!network.containsKey(user1) || !network.containsKey(user2)) {
            return List.of();
        }
        
        Set<String> common = new LinkedHashSet<>(network.get(user1));
        common.retainAll(network.get(user2));
        return new ArrayList<>(common);
    }

    // 尋找孤立使用者 (沒有任何好友)
    public List<String> isolatedUsers() {
        List<String> isolated = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : network.entrySet()) {
            if (entry.getValue().isEmpty()) {
                isolated.add(entry.getKey());
            }
        }
        return isolated;
    }

    public static void main(String[] args) {
        SocialNetworkGraph graph = new SocialNetworkGraph();
        
        for (String user : List.of("Alice", "Bob", "Charlie", "David", "Eve")) {
            graph.addUser(user);
        }

        graph.addFriend("Alice", "Bob");
        graph.addFriend("Alice", "Charlie");
        graph.addFriend("Bob", "Charlie");
        graph.addFriend("David", "Charlie");
        
        System.out.println("Alice and David mutual friends=" + graph.mutualFriends("Alice", "David"));
        System.out.println("isolated users=" + graph.isolatedUsers());

        System.out.println("--- removing Charlie and David friendship ---");
        graph.removeFriend("Charlie", "David");
        System.out.println("isolated users=" + graph.isolatedUsers());
    }
}