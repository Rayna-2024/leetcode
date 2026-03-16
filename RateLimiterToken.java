// Implement rate limiter class. Input is the limit of the number of requests per second, 
// When a caller calls the available function, judge if the requests breaches the limit. if so,
//  return false; otherwise return true;
// 需要实现refill token
// 限流令牌桶/

import java.util.*;

class RateLimiterToken{

    static int limit;
    public RateLimiterToken(int limit){
        this.limit = limit;
    }
    static class TokenBucket{
        int capacity;
        double refillRate;
        double tokens;
        long lastRefillTimestamp;
        public TokenBucket(int capacity, double refillRate){
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = capacity;
            this.lastRefillTimestamp = System.nanoTime();
        }
        public boolean allow(){
            refill();

            if(tokens<1){
                return false;
            }
            else{
                tokens -= 1;
                return true;
            }
        }
        private void refill(){
            long curTime = System.nanoTime();

            double refillToken = (curTime-lastRefillTimestamp)/1e9 * refillRate;
            tokens = Math.min(capacity, tokens + refillToken);
            lastRefillTimestamp = curTime;
        }
    }

    public static void main(String[] args) throws InterruptedException{
        TokenBucket tokenBucket = new TokenBucket(5, 5);
        

        System.out.println(tokenBucket.allow()); // true
        System.out.println(tokenBucket.allow()); // true
        System.out.println(tokenBucket.allow()); // false

        System.out.println(tokenBucket.allow()); // true
        System.out.println(tokenBucket.allow()); // true
        // Thread.sleep(1000);
        System.out.println(tokenBucket.allow()); // true
    }
}