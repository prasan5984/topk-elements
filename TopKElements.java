import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class TopKElements {

    public static final int DEFAULT_N = 1000000;
    public static final int DEFAULT_ITERATIONS = 10;

    static Stats findTopKUsingMaxHeap(List<Integer> input, int k) {
        long t1 = System.nanoTime();
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(Collections.reverseOrder());
        queue.addAll(input);
        long t2 = System.nanoTime();

        Integer[] output = new Integer[k];
        for (int i = 0; i < k; i++) {
            output[i] = queue.poll();
        }
        long t3 = System.nanoTime();
        return new Stats((t2 - t1), (t3 - t2), input.size());

    }

    static Stats findTopKUsingMinHeap(List<Integer> input, int k) {
        long t1 = System.nanoTime();
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int i = 0; i < k; i++) {
            queue.add(input.get(i));
        }
        long t2 = System.nanoTime();

        int count = 0;
        for (int i = k; i < input.size(); i++) {
            if (queue.peek() < input.get(i)) {
                queue.poll();
                queue.add(input.get(i));
                count++;
            }
        }
        long t3 = System.nanoTime();
        return new Stats((t2 - t1), (t3 - t2), count);

    }

    public static void main(String[] args) {
        int n = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_N;
        System.out.printf("Test for Array size: %d\n", n);
        System.out.printf("%-10s%-20s%-20s%-20s%-20s\n", "k", "MinHeapIterations", "MinHeapTime(ms)", "MaxHeapTime(ms)", "MaxTime/MinTime");

        for (int k = 1; k <= n; k *= 10) {
            Stats[] maxHeapResult = new Stats[DEFAULT_ITERATIONS];
            Stats[] minHeapResult = new Stats[DEFAULT_ITERATIONS];
            for (int i = 0; i < DEFAULT_ITERATIONS; i++) {

                List<Integer> input = IntStream.range(0, n)
                                               .boxed()
                                               .collect(Collectors.toList());
                Collections.shuffle(input);
                maxHeapResult[i] = findTopKUsingMaxHeap(input, k);
                System.gc();
                minHeapResult[i] = findTopKUsingMinHeap(input, k);
                System.gc();
            }
            StringBuilder stringBuilder = new StringBuilder().append(String.format("%-10d", k));
            appendResultLine(stringBuilder, maxHeapResult, minHeapResult);
            System.out.println((stringBuilder.toString()));
        }
    }

    private static void appendResultLine(StringBuilder builder, Stats[] maxHeapStats, Stats[] minHeapStats) {
        Stats minHeapAvg = getAverageStats(minHeapStats);
        builder.append(String.format("%-20.0f", minHeapAvg.iterationCount));
        appendTimeStats(builder, minHeapAvg);
        Stats maxAvg = getAverageStats(maxHeapStats);
        appendTimeStats(builder, maxAvg);
        builder.append(String.format("%-20.2f", (maxAvg.heapifyTime + maxAvg.iterationTime) / (minHeapAvg.heapifyTime + minHeapAvg.iterationTime)));
    }

    private static void appendTimeStats(StringBuilder builder, Stats stats) {
        builder/*.append(String.format("%2f", stats.heapifyTime / Math.pow(10, 6)))
               .append(String.format("%2f", stats.iterationTime / Math.pow(10, 6)))*/
               .append(String.format("%-20.2f", (stats.heapifyTime + stats.iterationTime) / Math.pow(10, 6)));
    }


    private static Stats getAverageStats(Stats[] stats) {
        double heapTime = 0, iterationTime = 0, iterationCount = 0;
        for (int i = 0; i < stats.length; i++) {
            heapTime += stats[i].heapifyTime;
            iterationTime += stats[i].iterationTime;
            iterationCount += stats[i].iterationCount;
        }
        return new Stats(heapTime / stats.length, iterationTime / stats.length, iterationCount / stats.length);
    }


    private static class Stats {
        double heapifyTime;
        double iterationTime;
        double iterationCount;

        public Stats(double heapifyTime, double iterationTime, double iterationCount) {
            this.heapifyTime = heapifyTime;
            this.iterationTime = iterationTime;
            this.iterationCount = iterationCount;
        }

    }
}


