import java.util.*;
public class interview42 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int groupNum = scanner.nextInt();
        while(groupNum>0){
            int nums = scanner.nextInt();
            // int[] dict = new int[26];
            // for(int i = 0;i < nums;i++){
            //     String curS = scanner.next();
            //     char curC = curS.charAt(0);3
            //     dict[curC - 'a']++;
            // }
            scanner.nextLine();
            String s = scanner.nextLine();
            // System.out.println(s);
            String[] strs = s.split(" ");
            Arrays.sort(strs, (x, y)->(x+y).compareTo(y+x));
            // char[] charArray = s.toCharArray();
            // for(int i = 0;i < charArray.length;i++){
            //     if(charArray[i] == ' '){
            //         continue;
            //     }
            //     else{
            //         dict[charArray[i] - 'a']++;
            //     }
            // }
            StringBuilder res = new StringBuilder();
            // for(int i = 0;i < 26;i++){
            //     int freq = dict[i];
            //     for(int j = 0;j < freq;j++){
            //         res.append((char)(i+'a'));
            //     }
            // }
            for(String curS:strs){
                res.append(curS);
            }
            System.out.println(res.toString());
            groupNum--;
        }
    }
}
