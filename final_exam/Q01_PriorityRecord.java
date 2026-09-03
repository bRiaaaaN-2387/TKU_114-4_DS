import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Q01_PriorityRecord {

    public record Job(String id, int priority, long sequence) {}

    public static List<String> processOrder(List<Job> jobs) {
        // 處理 null 或 empty 的輸入，回傳全新的 empty List
        if (jobs == null || jobs.isEmpty()) {
            return new ArrayList<>();
        }

        // 定義排序規則：priority (小至大) -> sequence (小至大) -> id (字典序)
        Comparator<Job> comparator = Comparator
                .comparingInt(Job::priority)
                .thenComparingLong(Job::sequence)
                .thenComparing(Job::id);

        PriorityQueue<Job> queue = new PriorityQueue<>(comparator);

        // 將非 null 的 job 加入 PriorityQueue，不修改原始輸入 List
        for (Job job : jobs) {
            if (job != null) {
                queue.offer(job);
            }
        }

        // 依序取出最高優先級的 Job ID
        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            result.add(queue.poll().id());
        }

        return result;
    }

    public static void main(String[] args) {
        List<Job> normalJobs = List.of(
            new Job("J1", 2, 100),
            new Job("J2", 1, 200),
            new Job("J3", 1, 100),
            new Job("J4", 2, 100) // 測試與 J1 priority 和 sequence 相同，預期 J1 先出
        );

        System.out.println("--- normal case ---");
        System.out.println("result=" + processOrder(normalJobs));

        System.out.println("\n--- list with null elements ---");
        List<Job> jobsWithNull = new ArrayList<>();
        jobsWithNull.add(new Job("J5", 3, 50));
        jobsWithNull.add(null);
        jobsWithNull.add(new Job("J6", 0, 10));
        System.out.println("result=" + processOrder(jobsWithNull));

        System.out.println("\n--- empty list case ---");
        System.out.println("result=" + processOrder(new ArrayList<>()));

        System.out.println("\n--- null input case ---");
        System.out.println("result=" + processOrder(null));
    }
}