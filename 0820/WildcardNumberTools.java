import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WildcardNumberTools {

    // Calculates average of a numeric list (supports List<Integer>, List<Double>, etc.)
    public static double average(List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Number val : values) {
            sum += val.doubleValue();
        }
        return sum / values.size();
    }

    // Finds maximum value in a numeric list (supports List<Integer>, List<Double>, etc.)
    public static double maximum(List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return Double.NaN;
        }

        double max = values.get(0).doubleValue();
        for (int i = 1; i < values.size(); i++) {
            double current = values.get(i).doubleValue();
            if (current > max) {
                max = current;
            }
        }
        return max;
    }

    // Adds range of integers [start, end] into a target list (supports List<Integer>, List<Number>, etc.)
    public static void addRange(List<? super Integer> target, int start, int end) {
        if (target == null || start > end) {
            return;
        }

        for (int i = start; i <= end; i++) {
            target.add(i);
        }
    }

    public static void main(String[] args) {
        // Test average and maximum with List<Integer> and List<Double>
        List<Integer> intList = Arrays.asList(10, 20, 30, 40);
        List<Double> doubleList = Arrays.asList(1.5, 3.5, 2.0);
        List<Integer> emptyList = new ArrayList<>();

        System.out.println("=== Test average() ===");
        System.out.println("Integer Average: " + average(intList));
        System.out.println("Double Average: " + average(doubleList));
        System.out.println("Empty List Average: " + average(emptyList));

        System.out.println("\n=== Test maximum() ===");
        System.out.println("Integer Max: " + maximum(intList));
        System.out.println("Double Max: " + maximum(doubleList));
        System.out.println("Empty List Max: " + maximum(emptyList));

        System.out.println("\n=== Test addRange() ===");
        List<Number> numList = new ArrayList<>();
        addRange(numList, 5, 8);
        System.out.println("Added [5, 8] to List<Number>: " + numList);

        List<Object> objList = new ArrayList<>();
        addRange(objList, 10, 8); // start > end
        System.out.println("Added [10, 8] (invalid range) to List<Object>: " + objList);
    }
}