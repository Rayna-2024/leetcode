// Implement PubSub subscription class and subject class
// A subscription class is basically a lambda function which takes one Input.
// A subject can subscirbe to multiple subscriptions. 
// When calling the next function of a subject with a parameter, 
// it calls all the subscriptions of this subject with that parameter. Also, can unsubscribe using
// the ubsubscribe function of subscription object.
// 如果有多个subject呢
// Subject        → 发布消息的人
// Subscriber     → 订阅消息的人

import java.util.*;

public class PubSub3 {
    public interface Subscriber {
        void sendMessage(String msg);
    }

    public static class Subscription{
        Subject subject;

        // This does not create an interface object. 
        // It just declares a reference variable of type Subscriber.
        Subscriber subscriber;
        public Subscription(Subject subject, Subscriber subscriber){
            this.subject = subject;
            this.subscriber = subscriber;
        }

        public void callFunction(String msg){
            subscriber.sendMessage(msg);
        }

        public void unsubscribe(){
            this.subject.remove(this);
        }

    }

    public static class Subject{

        List<Subscription> subs;
        public Subject(){
            subs = new ArrayList<>();

        }

        public Subscription subscribe(Subscriber subscriber){
            Subscription sub = new Subscription(this, subscriber);
            subs.add(sub);
            return sub;
        }
        public void publish(String msg){
            for(Subscription sub:subs){
                sub.callFunction(msg);
            }
        }
        public void remove(Subscription subscripetion){
            subs.remove(subscripetion);
        }

    }

    public static void main(String[] args) {

        Subject subject = new Subject();

        // Subscription s1 = subject.subscribe(new Subscriber() {
        //     @Override
        //     public void onMessage(String msg) {
        //         System.out.println("Subscriber1: " + msg);
        //     }
        // });

        // 按照提议直接用lambada
        Subscription s1 = subject.subscribe(msg->System.out.println("Subscriber1: " + msg));

        Subscription s2 = subject.subscribe(new Subscriber() {
            @Override
            public void sendMessage(String msg) {
                System.out.println("Subscriber2: " + msg);
            }
        });

        subject.publish("hello");

        s1.unsubscribe();

        subject.publish("world");
    }
}
