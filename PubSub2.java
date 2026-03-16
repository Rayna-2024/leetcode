
import java.util.*;


public class PubSub2 {
    // Functional Interface
    // 因为 Subscriber 是一个 Functional Interface（只有一个方法的接口），
    // Java 可以自动把 lambda 映射到那个唯一的方法。
    interface Subscriber {
        void onMessage(String message);
    }

    static class Subscription {

        private Subscriber subscriber;
        private Subject subject;

        public Subscription(Subscriber subscriber, Subject subject) {
            this.subscriber = subscriber;
            this.subject = subject;
        }

        public void notify(String msg) {
            subscriber.onMessage(msg);
        }

        public void unsubscribe() {
            subject.remove(this);
        }
    }

    static class Subject {

        private List<Subscription> subs = new ArrayList<>();

        public Subscription subscribe(Subscriber subscriber) {

            Subscription sub = new Subscription(subscriber, this);
            subs.add(sub);

            return sub;
        }

        public void publish(String msg) {

            for (Subscription sub : subs) {
                sub.notify(msg);
            }
        }

        public void remove(Subscription sub) {
            subs.remove(sub);
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
            public void onMessage(String msg) {
                System.out.println("Subscriber2: " + msg);
            }
        });

        subject.publish("hello");

        s1.unsubscribe();

        subject.publish("world");
    }
}
