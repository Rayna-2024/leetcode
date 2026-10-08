import java.util.*;
public class Interview331 {
    public static void main(String[] args) {
        // Scanner input=new Scanner(System.in);
        // String str=input.next();
        // System.out.println("hello world");
        // cdefghijklmnopqrstuvwxyzab
        // cdefghijklmnopqrstuvwxyz    ab
        
        String s = "nmlkjihgfedcbazyxwvutsrqpo";
        int sizeS = 26;
        int sizeFirst = 0;
        boolean isIncre = false;

        for(int i = 0;i < s.length()-1;i++){
            if(s.charAt(i)+1 == s.charAt(i+1)){
                isIncre = true;
                break;
            }
        }

        for(int i = 0;i < s.length()-1;i++){
            if(isIncre && s.charAt(i)+1 == s.charAt(i+1)){
                sizeFirst++;
            }
            else if(!isIncre && s.charAt(i)-1 == s.charAt(i+1)){
                sizeFirst++;
            }
            else{
                sizeFirst++;
                break;
            }
        }
        // sizeFirst--;

        int sizeSecond = sizeS - sizeFirst;
        int plus = 0;

        if(isIncre && s.charAt(0) < s.charAt(25))plus = 2;
        if(!isIncre)plus = 1;

        System.out.println(Math.min(sizeFirst, sizeSecond)+plus);
        System.out.println(sizeFirst);
    }
}