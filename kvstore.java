import java.util.*;

public class kvstore {

    class HitCounter {

        private Deque<Long> queue = new ArrayDeque<>();
        private static final int WINDOW = 300; // 5 minutes

        public void hit() {

            long now = System.currentTimeMillis() / 1000;

            queue.offerLast(now);

            clean(now);
        }

        public double getLoad() {

            long now = System.currentTimeMillis() / 1000;

            clean(now);

            return (double) queue.size() / WINDOW;
        }

        private void clean(long now) {

            while (!queue.isEmpty() && now - queue.peekFirst() >= WINDOW) {
                queue.pollFirst();
            }
        }
    }

    private Map<String, String> store = new HashMap<>();

    private HitCounter putCounter = new HitCounter();
    private HitCounter getCounter = new HitCounter();

    public void put(String key, String value) {

        store.put(key, value);

        putCounter.hit();
    }

    public String get(String key) {

        getCounter.hit();

        return store.get(key);
    }

    public double getPutLoad() {

        return putCounter.getLoad();
    }

    public double getGetLoad() {

        return getCounter.getLoad();
    }

    public static void main(String[] args) throws Exception {

        kvstore store = new kvstore();

        System.out.println("===== Basic KV Test =====");

        store.put("a", "1");
        store.put("b", "2");

        System.out.println(store.get("a"));
        System.out.println(store.get("b"));
        System.out.println(store.get("c"));

        System.out.println("\n===== Load Test =====");

        for (int i = 0; i < 30; i++) {
            store.put("k" + i, "v" + i);
        }

        for (int i = 0; i < 15; i++) {
            store.get("k1");
        }

        System.out.println("Put Avg Per Second: " + store.getPutLoad());
        System.out.println("Get Avg Per Second: " + store.getGetLoad());
    }
}