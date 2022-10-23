import java.util.Arrays;

public class Solution {

    public static int[] solution(int[] pegs) {
        if (!isGearsCorrect(pegs, getFirstRadius(pegs))) {
            return new int[] {-1, -1};
        }
        return makeDoubleToFraction(getFirstRadius(pegs));
    }

    private static boolean isEven (int number) {
            return number%2 == 0;
        }

    private static int sumOfDistances (int[] pegs) {
        int[] distances = new int [pegs.length - 1];
        int sumOfEven = 0;
        int sumOfOdd = 0;
        for (int i = 0; i < distances.length; i++) {
            distances[i] = pegs[i+1] - pegs[i];
            if (isEven(i)) {
                sumOfEven += distances[i];
            } else {
                sumOfOdd += distances[i];
            }
        }
        return sumOfEven - sumOfOdd;
    }

    private static double getFirstRadius (int[] pegs) {
        double radius = 0;
        if (isEven(pegs.length)){
            radius = 2d/3d * sumOfDistances(pegs);
        } else {
            radius = 2 * sumOfDistances(pegs);
        }
        return radius;
    }

    private static boolean isGearsCorrect(int[] pegs, double firstGear) {
        if (firstGear < 2) {
            return false;
        }
        double gearRadius = firstGear;
        for (int i = 0; i < pegs.length - 1; i++) {
            gearRadius = (pegs[i+1] - pegs[i]) - gearRadius;
            if (gearRadius < 1) {
                return false;
            }
        }
        return true;
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private static int[] makeDoubleToFraction(double number) {
        double tolerance = 0.000001;

        int minNumerator = (int) Math.floor(number);
        int minDenominator = 1;
        int maxNumerator = (int) Math.ceil(number);
        int maxDenominator = 1;

        double approximateResult;
        int approximateNumerator;
        int approximateDenominator;

        do {
            approximateNumerator = minNumerator + maxNumerator;
            approximateDenominator = minDenominator + maxDenominator;
            approximateResult = (double) approximateNumerator / approximateDenominator;

            if (approximateResult > number) {
                maxNumerator = approximateNumerator;
                maxDenominator = approximateDenominator;
            } else {
                minNumerator = approximateNumerator;
                minDenominator = approximateDenominator;
            }
        } while (Math.abs(approximateResult - number) > tolerance);

        int gcd = gcd(approximateNumerator, approximateDenominator);
        return new int[] {approximateNumerator/gcd, approximateDenominator/gcd};
    }
}
