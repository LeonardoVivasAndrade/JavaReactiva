package others.clases.reverse_string;

public class Reverse {

    public static void main(String[] args) {
        char[] s = new char[] {'H','E','L','L','O'};
        System.out.println(reverseString(s));
    }

    public static char[] reverseString(char[] s){
        char[] r = new char[s.length];
        for (int i=s.length - 1; i >= 0; i--){
            r[(s.length - i) - 1] = s[i];
        }
        return r;
    }

}
