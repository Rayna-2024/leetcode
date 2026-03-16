// Implement rate limiter class. Input is the limit of the number of requests per second, 
// When a caller calls the available function, judge if the requests breaches the limit. if so,
//  return false; otherwise return true;

import java.util.*;

class RateLimiterNew{

    private Map<String, Queue<Long>> map;
    private int limit;

    public RateLimiterNew(int limit){
        map = new HashMap<>();
        this.limit = limit;
    }

    public static class RateLimiterTask implements Runnable{
        private RateLimiterNew rateLimiternew;
        public RateLimiterTask(RateLimiterNew rateLimiter){
            this.rateLimiternew = rateLimiter;
        }

        @Override
        public void run(){
            for(int i = 0;i < 5;i++){
                boolean res = rateLimiternew.allow("A");

                System.out.println(Thread.currentThread().getName() + "->" + res);
                // try{
                //     Thread.sleep(200);
                // }
                // catch(InterruptedException e){
                //     e.printStackTrace();
                // }
            }

        }
    }
    public synchronized boolean allow(String callId){
        Long curTime = System.currentTimeMillis();

        if(!map.containsKey(callId)){
            map.put(callId, new LinkedList<>());
        }

        Queue<Long> queue = map.get(callId);

        while(!queue.isEmpty() && queue.peek() < curTime - 1000){
            queue.poll();
        }
        if(limit > queue.size()){
            queue.offer(curTime);
            return true;
        }

        // 下面这个try catch可以删掉的
        try{
            Thread.sleep(200);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        return false;
    }
    public static void main(String[] args) throws InterruptedException{
        RateLimiterNew limiter = new RateLimiterNew(3);

        // System.out.println(limiter.allow("A")); // true
        // System.out.println(limiter.allow("A")); // true
        // System.out.println(limiter.allow("A")); // false

        // System.out.println(limiter.allow("B")); // true
        // System.out.println(limiter.allow("B")); // true
        // Thread.sleep(2000);
        // System.out.println(limiter.allow("B")); // true

        // multiple threads
        RateLimiterTask task = new RateLimiterTask(limiter);

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();

    }
}