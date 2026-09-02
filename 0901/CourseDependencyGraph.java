import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseDependencyGraph {
    // 儲存每門課 (Vertex) 與其後續課程 (Outgoing Edges)
    private final Map<String, List<String>> outgoing = new LinkedHashMap<>();

    // 新增課程
    public void addCourse(String course) {
        if (course == null || course.isBlank()) return;
        outgoing.putIfAbsent(course.trim(), new ArrayList<>());
    }

    // 新增相依關係 (prereq 為 course 的先修課)
    public boolean addDependency(String prereq, String course) {
        if (!outgoing.containsKey(prereq) || !outgoing.containsKey(course)) return false;
        
        List<String> subsequent = outgoing.get(prereq);
        if (subsequent.contains(course)) return false; // 避免重複加入
        
        subsequent.add(course);
        return true;
    }

    // 取得後續課程 (Outgoing Edges)
    public List<String> getSubsequent(String course) {
        return outgoing.getOrDefault(course, List.of());
    }

    // 取得先修課 (Incoming Edges)
    public List<String> getPrerequisites(String course) {
        List<String> prereqs = new ArrayList<>();
        if (!outgoing.containsKey(course)) return prereqs;
        
        // 走訪所有課程，若其後續課程包含目標課程，則為先修課
        for (Map.Entry<String, List<String>> entry : outgoing.entrySet()) {
            if (entry.getValue().contains(course)) {
                prereqs.add(entry.getKey());
            }
        }
        return prereqs;
    }

    // 計算 out-degree
    public int outDegree(String course) {
        return getSubsequent(course).size();
    }

    // 計算 in-degree
    public int inDegree(String course) {
        return getPrerequisites(course).size();
    }

    // 輸出指定課程的相依性報告
    public void printReport(String course) {
        if (!outgoing.containsKey(course)) {
            System.out.println("course not found: " + course);
            return;
        }
        System.out.println("--- course: " + course + " ---");
        System.out.println("prerequisites (in=" + inDegree(course) + ")=" + getPrerequisites(course));
        System.out.println("subsequent (out=" + outDegree(course) + ")=" + getSubsequent(course));
    }

    public static void main(String[] args) {
        CourseDependencyGraph graph = new CourseDependencyGraph();
        
        List.of("Intro to CS", "Data Structures", "Algorithms", "Operating Systems", "Database")
            .forEach(graph::addCourse);

        // 設定先修關係
        graph.addDependency("Intro to CS", "Data Structures");
        graph.addDependency("Data Structures", "Algorithms");
        graph.addDependency("Data Structures", "Operating Systems");
        graph.addDependency("Data Structures", "Database");
        
        graph.printReport("Data Structures");
        System.out.println();
        graph.printReport("Algorithms");
        System.out.println();
        graph.printReport("Intro to CS");
    }
}