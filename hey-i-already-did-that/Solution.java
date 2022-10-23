import java.util.ArrayList;
import java.util.Arrays;

class Solution {
      public static int solution(String n, int b) {
        ArrayList<String> listOfIDs = new ArrayList<>();
        listOfIDs.add(n);
        int result = 0;
        boolean algoIsCorrect = true;
        String x, y, z;
        while (algoIsCorrect) {
            x = descending(n);
            y = ascending(n);
            z = subtract(x, y, b);
            if (z.equals(n) || z.equals("0")) {
                result = 1;
                algoIsCorrect = false;
            }
            if (IDIsDuplicate(listOfIDs, z)) {
                result = listOfIDs.size() - listOfIDs.indexOf(z);
                algoIsCorrect = false;
            } else {
                listOfIDs.add(z);
                n = z;
            }
        }
        return result;
    }

    private static String subtract(String x, String y, int b) {
        String result = Integer.toString(Integer.parseInt(x, b) - Integer.parseInt(y, b), b);
        if (result.length() < x.length() && !result.equals("0")) {
            return modifyWithZeros(result, x.length() - result.length());
        }
        return result;
    }

    private static String ascending(String n) {
        char[] array = n.toCharArray();
        Arrays.sort(array);
        return new String(array);
    }

    private static String descending(String n) {
        return new StringBuilder(ascending(n)).reverse().toString();
    }

    private static String modifyWithZeros (String input, int zeros) {
        StringBuilder result = new StringBuilder();
        while(zeros != 0) {
            result.append("0");
            zeros--;
        }
        result.append(input);
        return result.toString();
    }

    private static boolean IDIsDuplicate (ArrayList<String> list, String element) {
        for (String s : list) {
            if (s.equals(element)) {
                return true;
            }
        }
        return false;
    }
}
