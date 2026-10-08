// 题目描述： 给定一个非空的整数数组 nums 和一个整数 k，返回数组中出现频率最高的 k 个元素。
// 示例： 输入: nums = [1,1,1,2,2,3], k = 2 输出: [1, 2] (或 [2, 1])
import java.util.*;
public class Interview323 {
    static class Number{
        int value;
        int freq;
        Number(int value, int freq){
            this.value = value;
            this.freq = freq;
        }
    }
    public static void main(String[] args) {
        int[] nums = new int[]{1, 1, 1, 2, 2, 3};
        int k = 2;
        // {{1:3}, {2, 2}, {3, 1}}
        Map<Integer, Integer> map = new HashMap<>();
        for(int curNum : nums){
            if(!map.containsKey(curNum)){
                map.put(curNum, 1);
            }
            else{
                map.put(curNum, map.get(curNum)+1);
            }
        }
        // int size = map.size();
        // int[] res = new int[size];
        // {{1:3}, {2:2}, {3:1}}
        PriorityQueue<Number> minHeap = new PriorityQueue<>((a, b)->Integer.compare(a.freq, b.freq));
        // int i = 0;
        for(Map.Entry<Integer, Integer> entry: map.entrySet()){
            Number curNumber = new Number(entry.getKey(), entry.getValue());
            minHeap.add(curNumber);
        }
        while(minHeap.size()>k){
            minHeap.poll();
        }
        for(int i = 0;i <= minHeap.size();i++){
            int a = minHeap.poll().value;
            System.out.println(a);
        }

    }
}
