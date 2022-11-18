import java.math.BigInteger;
import java.util.ArrayList;

public class Solution {
    // result = X / G
    //      G = w! * h!
    //      X = sum(Cw(i) * Ch(j) * s^exp(i, j))
    //      Cw -> all integer partitions of w, (i) -> number of specific partition, C - conjugacy class size
    //      Ch -> all integer partitions of h, (j) -> number of specific partition, C - conjugacy class size
    //      exp(i, j) -> sum (gcd(i(a), j(b)), where i(a) -> element of specific partition i, j(b) -> element of specific partition j

    public static String solution(int w, int h, int s) {
          BigInteger G = factorial(BigInteger.valueOf(w)).multiply(factorial(BigInteger.valueOf(h)));
          ArrayList<Partition> partitionsW = makeIntegerPartition(w);
          ArrayList<Partition> partitionsH = makeIntegerPartition(h);
          BigInteger X = BigInteger.ZERO;
          BigInteger C; BigInteger S;

          int sizeW = partitionsW.size();
          int sizeH = partitionsH.size();

          for (int i = 0; i < sizeW; i++) {
              for (int j = 0; j < sizeH; j++) {
                  C = partitionsW.get(i).conjugacySize.multiply(partitionsH.get(j).conjugacySize);
                  S = BigInteger.valueOf(s).pow(power(partitionsW.get(i), partitionsH.get(j)));
                  X = X.add(C.multiply(S));
              }
          }

          return X.divide(G).toString();
    }

    private static int power(Partition one, Partition two) {
        int sizeOne = one.partition.size();
        int sizeTwo = two.partition.size();
        int sum = 0;

        for (int i = 0; i < sizeOne; i++) {
            for (int j = 0; j < sizeTwo; j++) {
                sum += gcd(one.partition.get(i), two.partition.get(j));
            }
        }
        return sum;
    }

    private static int gcd(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcd(n2, n1 % n2);
    }

    private static BigInteger factorial(BigInteger number) {
        if (number.compareTo(BigInteger.valueOf(2)) <= 0) {
            return number;
        }
        return number.multiply(factorial(number.subtract(BigInteger.ONE)));
    }

    private static ArrayList<Partition> makeIntegerPartition(int number) {
        ArrayList<Partition> result = new ArrayList<>();
        int[] p = new int[number];
        int k = 0;
        p[k] = number;

        while (true) {
            result.add(new Partition(number, getArray(p, k+1)));

            int rem_val = 0;
            while (k >= 0 && p[k] == 1) {
                rem_val += p[k];
                k--;
            }

            if (k < 0) {
                break;
            }

            p[k]--;
            rem_val++;

            while (rem_val > p[k]) {
                p[k+1] = p[k];
                rem_val = rem_val - p[k];
                k++;
            }

            p[k+1] = rem_val;
            k++;
        }

        return result;
    }

    private static ArrayList<Integer> getArray(int[] p, int n) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(p[i]);
        }
        return result;
    }

    private static class Partition {
        BigInteger originalNumber;
        ArrayList<Integer> partition;
        BigInteger conjugacySize;

        Partition(int original, ArrayList<Integer> list) {
            this.originalNumber = new BigInteger(String.valueOf(original));
            this.partition = new ArrayList<>(list);
            this.conjugacySize = setConjugacySize(partition);
        }

        private BigInteger setConjugacySize(ArrayList<Integer> list) {
            if (list.size() == 1) {
                return factorial(originalNumber).divide(originalNumber);
            }
            BigInteger numerator = factorial(originalNumber);
            BigInteger denominator = BigInteger.ONE;
            Integer number = list.get(0);
            double counter = 1;
            int temp;
            for (int i = 1; i <= list.size(); ) {
                if (i == list.size()) {
                    temp = (int) Math.pow(number.doubleValue(), counter) * (int) factorialD(counter);
                    denominator = denominator.multiply(BigInteger.valueOf(temp));
                    break;
                }
                if (list.get(i).equals(number)) {
                    if (i + 1 != list.size()) {
                        counter++;
                        i++;
                    } else {
                        counter++;
                        temp = (int) Math.pow(number.doubleValue(), counter) * (int) factorialD(counter);
                        denominator = denominator.multiply(BigInteger.valueOf(temp));
                        break;
                    }
                } else {
                    temp = (int) Math.pow(number.doubleValue(), counter) * (int) factorialD(counter);
                    denominator = denominator.multiply(BigInteger.valueOf(temp));
                    counter = 1;
                    number = list.get(i);
                    i++;
                }
            }
            return numerator.divide(denominator);
        }

        private static double factorialD(double n) {
            if (n <= 2) {
                return n;
            }
            return n * factorialD(n - 1);
        }
    }
}
