// Implement rate limiter class. Input is the limit of the number of requests per second, 
// When a caller calls the available function, judge if the requests breaches the limit. if so,
//  return false; otherwise return true;

import java.util.*;

class RateLimiter {
    private int limit;
    private Map<String, Queue<Long>> map;

    public RateLimiter(int limit) {
        this.limit = limit;
        this.map = new HashMap<>();
    }

    public synchronized boolean allow(String callerId) {
        long now = System.currentTimeMillis();

        // 如果这个 caller 第一次出现，给它新建一个队列
        if (!map.containsKey(callerId)) {
            map.put(callerId, new LinkedList<>());
        }

        Queue<Long> queue = map.get(callerId);

        // 把 1 秒以前的请求删掉
        while (!queue.isEmpty() && now - queue.peek() >= 1000) {
            queue.poll();
        }

        // 看当前 1 秒内请求数是否达到上限
        if (queue.size() < limit) {
            queue.offer(now);
            return true;
        }

        return false;
    }
    public static void main(String[] args) throws InterruptedException{

        // RateLimiter limiter = new RateLimiter(2);

        // System.out.println(limiter.allow("A")); // true
        // System.out.println(limiter.allow("A")); // true
        // System.out.println(limiter.allow("A")); // false

        // System.out.println(limiter.allow("B")); // true
        // System.out.println(limiter.allow("B")); // true
        // Thread.sleep(2000);
        // System.out.println(limiter.allow("B")); // false


        // todo 写多线程测试
    }
}