// write a function that takes two positive integers and 
// return the multiplication of them without using the 
// multiplication operator (*)
public class CodingCallenge {
    static public int multiplication(int first, int second){
        boolean isNegetive = false;
        if(second < 0){
            isNegetive = true;
        }
        int Sec = Math.abs(second);
        int answer = first;
        for(int i = 0;i < Sec-1;i++){
            answer += first;
        }

        return !isNegetive?answer:-answer;
    }
    public static void main(String[] args) {
        int answer1 = multiplication(5, 4);
        int answer2 = multiplication(5, -4);
        int answer3 = multiplication(-5, 4);
        int answer4 = multiplication(-5, -4);
        System.out.println(answer1);
        System.out.println(answer2);
        System.out.println(answer3);
        System.out.println(answer4);
    }
}
