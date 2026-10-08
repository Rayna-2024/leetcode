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
import java.util.function.Consumer;

public class PubSub {
    static class Subscription {

        // Functional Interface
        // 因为 Subscriber 是一个 Functional Interface（只有一个方法的接口），
        // Java 可以自动把 lambda 映射到那个唯一的方法。

        // Consumer<T> is a functional interface in Java used to 
        // represent an operation that accepts a single input and 
        // performs some processing on it without returning a value.

        // 为什么要用interface或者consumer？
        //Java cannot store functions directly, so we use a functional interface 
        // like Consumer<T> to represent a piece of behavior that takes an input 
        // and performs an action.
        private Consumer<String> callback;
        private Subject subject;

        public Subscription(Consumer<String> callback, Subject subject) {
            this.callback = callback;
            this.subject = subject;
        }

        public void call(String msg) {
            callback.accept(msg);
        }

        public void unsubscribe() {
            subject.remove(this);
        }
    }

    static class Subject {

        private List<Subscription> subs = new ArrayList<>();

        public Subscription subscribe(Consumer<String> callback) {

            Subscription sub = new Subscription(callback, this);

            subs.add(sub);

            return sub;
        }

        public void publish(String msg) {

            for (Subscription sub : subs) {
                sub.call(msg);
            }
        }

        public void remove(Subscription sub) {
            subs.remove(sub);
        }
    }

    public static void main(String[] args) {
        Subject subject = new Subject();

        Subscription s1 = subject.subscribe(k -> {
            System.out.println("Sub1: " + k);
        });

        Subscription s2 = subject.subscribe(k -> {
            System.out.println("Sub2: " + k);
        });

        subject.publish("hello");

        s1.unsubscribe();

        subject.publish("world");
    }
}

