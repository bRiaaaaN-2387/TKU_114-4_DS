import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Q07_RequestPipeline {
    
    public static boolean isBalanced(String text) {
        if (text == null) {
            return false;
        }
        if (text.isEmpty()) {
            return true;
        }
        
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : text.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty()) {
                    return false;
                }
                char top = stack.pop();
                if ((c == ')' && top != '(') ||
                    (c == ']' && top != '[') ||
                    (c == '}' && top != '{')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }
    
    private static String takeUrgentCheckpoint(Deque<String> queue) {
        return queue.pollFirst();
    }

    public static java.util.List<String> process(String[] commands) {
        List<String> result = new ArrayList<>();
        if (commands == null) {
            return result;
        }
        
        Deque<String> normalQueue = new ArrayDeque<>();
        Deque<String> urgentQueue = new ArrayDeque<>();
        
        for (String cmd : commands) {
            if (cmd == null || cmd.trim().isEmpty()) {
                continue;
            }
            String trimmed = cmd.trim();
            
            if (trimmed.startsWith("NORMAL ")) {
                normalQueue.offerLast(trimmed.substring(7).trim());
            } else if (trimmed.startsWith("URGENT ")) {
                urgentQueue.offerLast(trimmed.substring(7).trim());
            } else if (trimmed.equals("PROCESS")) {
                if (!urgentQueue.isEmpty()) {
                    result.add(takeUrgentCheckpoint(urgentQueue));
                } else if (!normalQueue.isEmpty()) {
                    result.add(normalQueue.pollFirst());
                } else {
                    result.add("EMPTY");
                }
            }
            // 忽略其他格式錯誤的指令
        }
        return result;
    }
}