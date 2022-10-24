import java.math.BigInteger;

public class Solution {
    public static String solution(String x, String y) {
        Result input = new Result(x, y);
        return input.getResult();
    }
}

class Input {
    BigInteger number1;
    BigInteger number2;
    BigInteger lowerNumber;
    BigInteger higherNumber;
    //n1 < n2

    Input(String n1, String n2) {
        this.number1 = new BigInteger(n1);
        this.number2 = new BigInteger(n2);
    }

    void rearrangeNumbers() {
        this.lowerNumber = number1.min(number2);
        this.higherNumber = number1.max(number2);
        this.number1 = this.lowerNumber;
        this.number2 = this.higherNumber;
    }
}

class Impossible extends Input {

    static String result = "impossible";

    Impossible(String n1, String n2) {
        super(n1, n2);
    }

    boolean isImpossible() {
        if (number1.equals(number2) && !number1.equals(BigInteger.ONE)) {
            return true;
        } else return number1.compareTo(BigInteger.ONE) < 0 || number2.compareTo(BigInteger.ONE) < 0;
    }
}

class Result extends Impossible {

    String result;
    BigInteger counter = BigInteger.ZERO;
    BigInteger[] coefficient;

    Result(String n1, String n2) {
        super(n1, n2); setResult();
    }

    void setResult() {
        while(true) {
            rearrangeNumbers();

            if (number1.equals(BigInteger.ONE) && number2.equals(BigInteger.ONE)) {
                break;
            } else if (isImpossible()) {
                this.result = Impossible.result;
                break;
            } else if (number1.equals(BigInteger.ONE)) {
                this.counter = counter.add(number2.subtract(BigInteger.ONE));
                break;
            }

            coefficient = number2.divideAndRemainder(number1);
            counter = counter.add(coefficient[0]);
            number2 = number2.subtract(number1.multiply(coefficient[0]));
        }

        if (result == null) {
            this.result = counter.toString();
        }
    }

    String getResult() {
        return this.result;
    }
}


