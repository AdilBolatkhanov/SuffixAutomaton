

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class SuffixAutomaton {
    public static State[] suffixAutoma;   //suffix Automaton
    public static int curSize, last;    //current size and last - the state corresponding to the entire string at the moment
    public static HashSet<Integer> res = new HashSet<>();      //Result of finding positions of occurrences

    public static class State {
        int len, link;
        HashMap<Character, Integer> next = new HashMap<>();  //lis of transitions or edges

        boolean is_clon;
        int first_pos;
        ArrayList<Integer> inv_link = new ArrayList<>();

        public State() {
        }

    };

    public static void main(String[] args) throws IOException {
        String s1 = "gogomangoog";
        String s2 = "go";

        printAllPositionsOfOccur(s1, s2);
        System.out.println(lcs(s1, s2));


//        PrintWriter output = new PrintWriter(new File("occur.txt"));
//        int i;
//        for ( i = 10; i < 1000;i++){
//            double dt = Double.MAX_VALUE;
//            long startTime = System.nanoTime();
//            String s1 = getAlphaNumericString(i);
//            String s2 = getAlphaNumericString(i - i/5);
//            printAllPositionsOfOccur(s1,s2);
//            long endTime = System.nanoTime();
//            dt = Math.min(dt, (double)(endTime - startTime) * 1e-9);
//            String s = i + " "+ dt+ "\n";
//            output.print(s);
//        }
//        output.close();

    }
    // function to generate a random string of length n
    static String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }



    // initializes a suffix automaton (creating a suffix automaton with a single state)
    public static void suffAutomat_Init() {
        curSize = last = 0;
        if (suffixAutoma[0] == null) {
            suffixAutoma[0] = new State();
        }
        suffixAutoma[0].len = 0;
        suffixAutoma[0].link = -1;
        ++curSize;

    }

    //adds the next character to the end of the current line, rebuilding the machine accordingly
    public static void suffAutomat_Extend(char c) {
        int cur = curSize++;
        if (suffixAutoma[cur] == null) {
            suffixAutoma[cur] = new State();
        }
        suffixAutoma[cur].first_pos = suffixAutoma[last].len - 1;
        suffixAutoma[cur].len = suffixAutoma[last].len + 1;
        int p;
        for (p = last; p != -1 && !suffixAutoma[p].next.containsKey(c); p = suffixAutoma[p].link) {
            suffixAutoma[p].next.put(c, cur);
        }

        if (p == -1) {
            suffixAutoma[cur].link = 0;
        } else {
            int q = suffixAutoma[p].next.get(c);
            if (suffixAutoma[p].len + 1 == suffixAutoma[q].len) {
                suffixAutoma[cur].link = q;
            } else {
                int clone = curSize++;
                if (suffixAutoma[clone] == null) {
                    suffixAutoma[clone] = new State();
                }
                suffixAutoma[clone].is_clon = true;
                suffixAutoma[clone].len = suffixAutoma[p].len + 1;
                suffixAutoma[clone].next.putAll(suffixAutoma[q].next);
                suffixAutoma[clone].link = suffixAutoma[q].link;
                suffixAutoma[clone].first_pos = suffixAutoma[q].first_pos;
                for (; p != -1 && suffixAutoma[p].next.get(c) == q; p = suffixAutoma[p].link) {
                    suffixAutoma[p].next.remove(c);
                    suffixAutoma[p].next.put(c, clone);
                }
                suffixAutoma[q].link = suffixAutoma[cur].link = clone;
            }
        }
        last = cur;
    }

    //Prints all occurrence positions of the s2 in s1
    public static void printAllPositionsOfOccur(String s1, String s2){
        suffixAutoma = new State[(2 * s1.length())];
        suffAutomat_Init();
        for (int i = 0; i < s1.length(); i++) {
            suffAutomat_Extend(s1.charAt(i));
        }
        for (int v = 1; v < curSize; ++v) {
            suffixAutoma[suffixAutoma[v].link].inv_link.add(v);
        }
        State elem = suffixAutoma[0];
        int a = 0;
        for (int i = 0; i < s2.length(); i++) {
            if (elem.next.containsKey(s2.charAt(i))) {
                a = elem.next.get(s2.charAt(i));
                elem = suffixAutoma[a];
            } else {
                a = -1;
                break;
            }
        }
        if (a != -1) {
            output_all_occurences(a, s2.length());
        }
        System.out.println("Number of occurences: "+ res.size());
        if (res.size() == 0){
            return;
        }
        int n = res.size();
        Integer[] b;
        b = res.toArray(new Integer[n]);
        Arrays.sort(b);
        System.out.println("Positions of occurences: ");
        for (int i = 0; i < n; i++) {
            System.out.print(b[i] + " ");
        }
    }

    //main function that finds all occurrences
    public static void output_all_occurences(int v, int P_length) {
        if (!suffixAutoma[v].is_clon) {
            res.add(suffixAutoma[v].first_pos - P_length + 2);
        }
        for (int i = 0; i < suffixAutoma[v].inv_link.size(); ++i) {
            output_all_occurences(suffixAutoma[v].inv_link.get(i), P_length);
        }
    }

    //finds the longest common substring between two strings
    public static String lcs(String S, String T){
        suffixAutoma = new State[(2 * S.length())];
        suffAutomat_Init();
        for (int i =0 ; i < S.length(); i++){
            suffAutomat_Extend(S.charAt(i));
        }
        int v = 0, l = 0, best = 0, bestpos=0;
        for (int i =0; i < T.length(); i++){
            while (v != 0 && !suffixAutoma[v].next.containsKey(T.charAt(i))){
                v = suffixAutoma[v].link;
                l = suffixAutoma[v].len;
            }
            if (suffixAutoma[v].next.containsKey(T.charAt(i))){
                v = suffixAutoma[v].next.get(T.charAt(i));
                l++;
            }
            if (l > best){
                best = l;
                bestpos = i;
            }
        }
        return T.substring(bestpos - best  +1, bestpos  +1 );
    }
}








