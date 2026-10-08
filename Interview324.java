import java.util.*;
public class Interview324 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long a = in.nextLong();
        // System.out.print(a);
        String s = String.valueOf(a);
        int res = backtrack(0, s, 0, 0);
        System.out.println(res);


    }

    public static int backtrack(int startIndex, String s, long current, long sum){
        if(startIndex == s.length()){
            sum += current;
            return isPrime(sum)?1:0;
        }
        int digit = s.charAt(startIndex)-'0';

        int res = backtrack(startIndex+1, s, current*10+digit, sum);
        if(startIndex>0){
            res += backtrack(startIndex+1, s, digit, sum+current);
        }
        return res;

    }

    public static boolean isPrime(long num){
        if(num<2)return false;
        for(int i = 2;i*i<=num;i++){
            if(num%i==0)return false;
        }
        return true;
    }
}