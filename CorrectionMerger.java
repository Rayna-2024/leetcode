import java.util.*;

public class CorrectionMerger {

    enum Changes {
        DELETE, DEDUP, UPDATE, UPPERCASE, LOWERCASE
    }

    static class Correction {
        int start;
        int end;
        int priority;
        Changes changes;

        // 为了比较稳定性，给每个原始 correction 一个 id
        int id;

        public Correction(int start, int end, int priority, Changes changes) {
            this.start = start;
            this.end = end;
            this.priority = priority;
            this.changes = changes;
        }

        public int originalLength() {
            return end - start + 1;
        }

        @Override
        public String toString() {
            return "(" + start + ", " + end + ", " + priority + ", " + changes + ")";
        }
    }

    static class Segment {
        int start;
        int end;
        Correction source; // 记录这个输出段来自哪个原始 correction

        Segment(int start, int end, Correction source) {
            this.start = start;
            this.end = end;
            this.source = source;
        }

        @Override
        public String toString() {
            return "(" + start + ", " + end + ", " + source.priority + ", " + source.changes + ")";
        }
    }

    public static List<Correction> correctionMerge(List<Correction> input) {
        List<Correction> result = new ArrayList<>();
        if (input == null || input.isEmpty()) return result;

        // 1) 过滤非法区间，并分配 id
        List<Correction> corrections = new ArrayList<>();
        int uid = 0;
        for (Correction c : input) {
            if (c == null) continue;
            if (c.start > c.end) continue; // 非法区间直接跳过
            c.id = uid++;
            corrections.add(c);
        }
        if (corrections.isEmpty()) return result;

        // 2) 建事件：start 加入，end+1 移除
        Map<Integer, List<Correction>> addMap = new HashMap<>();
        Map<Integer, List<Correction>> removeMap = new HashMap<>();
        TreeSet<Integer> points = new TreeSet<>();

        for (Correction c : corrections) {
            addMap.computeIfAbsent(c.start, k -> new ArrayList<>()).add(c);
            removeMap.computeIfAbsent(c.end + 1, k -> new ArrayList<>()).add(c);

            points.add(c.start);
            points.add(c.end + 1);
        }

        List<Integer> sortedPoints = new ArrayList<>(points);

        // 3) 优先队列：堆顶永远是“当前最优 correction”
        PriorityQueue<Correction> pq = new PriorityQueue<>((a, b) -> {
            // priority 高的更优
            if (a.priority != b.priority) {
                return Integer.compare(b.priority, a.priority);
            }

            // priority 相同，原始长度短的更优
            int lenA = a.originalLength();
            int lenB = b.originalLength();
            if (lenA != lenB) {
                return Integer.compare(lenA, lenB);
            }

            // 下面是稳定性 tie-breaker，题目没定义，但工程上最好有
            if (a.start != b.start) {
                return Integer.compare(a.start, b.start);
            }
            return Integer.compare(a.id, b.id);
        });

        Set<Integer> activeIds = new HashSet<>();

        List<Segment> segments = new ArrayList<>();

        // 4) 扫描每个基本区间 [x, nextX - 1]
        for (int i = 0; i < sortedPoints.size() - 1; i++) {
            int x = sortedPoints.get(i);
            int nextX = sortedPoints.get(i + 1);

            // 先处理在 x 开始/结束的区间
            if (removeMap.containsKey(x)) {
                for (Correction c : removeMap.get(x)) {
                    activeIds.remove(c.id);
                }
            }

            if (addMap.containsKey(x)) {
                for (Correction c : addMap.get(x)) {
                    activeIds.add(c.id);
                    pq.offer(c);
                }
            }

            // 清理堆顶失效元素
            while (!pq.isEmpty() && !activeIds.contains(pq.peek().id)) {
                pq.poll();
            }

            // [x, nextX - 1] 是一个 active 集合恒定的闭区间
            if (!pq.isEmpty() && x <= nextX - 1) {
                Correction winner = pq.peek();
                segments.add(new Segment(x, nextX - 1, winner));
            }
        }

        // 5) 合并连续且 winner 是同一个原 correction 的段
        List<Segment> merged = new ArrayList<>();
        for (Segment seg : segments) {
            if (merged.isEmpty()) {
                merged.add(seg);
            } else {
                Segment last = merged.get(merged.size() - 1);
                if (last.end + 1 == seg.start && last.source.id == seg.source.id) {
                    last.end = seg.end;
                } else {
                    merged.add(seg);
                }
            }
        }

        // 6) 转回 List<Correction>
        for (Segment seg : merged) {
            result.add(new Correction(seg.start, seg.end, seg.source.priority, seg.source.changes));
        }

        return result;
    }

    // ===== test =====
    public static void main(String[] args) {
        List<Correction> input = Arrays.asList(
                new Correction(1, 10, 1, Changes.UPPERCASE),
                new Correction(3, 6, 1, Changes.LOWERCASE)
        );

        List<Correction> ans = correctionMerge(input);
        for (Correction c : ans) {
            System.out.println(c);
        }

        System.out.println("------");

        List<Correction> input2 = Arrays.asList(
                new Correction(1, 10, 1, Changes.UPPERCASE),
                new Correction(3, 6, 1, Changes.LOWERCASE),
                new Correction(4, 8, 1, Changes.DELETE)
        );

        List<Correction> ans2 = correctionMerge(input2);
        for (Correction c : ans2) {
            System.out.println(c);
        }
    }
}