import java.util.ArrayList;
import java.util.Arrays;

public class Solution {
    public static int[] solution(int[][] m) {
        if (m.length < 2) {
            return new int[] {1, 1};
        }
        CorrectMatrix arrangedMatrix = new CorrectMatrix(m);
        int terminalIndex = getTerminalIndex(arrangedMatrix.getCorrectMatrix());
        SolutionMatrix solution = new SolutionMatrix(arrangedMatrix.getCorrectMatrix(), terminalIndex);
        return getResult(solution.getSolutionMatrix());
    }

    private static int getTerminalIndex (int[][] m) {
        for (int i = 0; i < m.length; i++) {
            if(MathOperations.getDenominator(m, i) == 0) {
                return i;
            }
        }
        return 0;
    }

    static int[] getResult(double[][] m) {
        int[] result = new int[m[0].length+1];
        int[] denominators = new int[m[0].length];
        int[] temp;

        for (int i = 0; i < m[0].length; i++) {
            temp = MathOperations.makeDoubleToFraction(m[0][i]);
            result[i] = temp[0];
            denominators[i] = temp[1];
        }
        result[result.length - 1] = MathOperations.commonLCM(denominators);
        for (int i = 0; i < result.length - 1; i++) {
            result[i] = (result[result.length-1] / denominators[i]) * result[i];
        }

        return result;
    }
}

class SolutionMatrix {
    //     | Q | R |
    // P = |-------|
    //     | O | I |

    // |  R * (I - Q)^-1  |
    // |------------------|
    // |         I        |
    double[][] matrix;
    double[][] matrixI;
    double[][] matrixR;
    double[][] matrixQ;
    double[][] matrixF;
    double[][] solutionMatrix;

    SolutionMatrix(int[][] m, int terminalIndex) {
        standardiseMatrix(m); setMatrixR(terminalIndex);
        setMatrixQ(terminalIndex); setMatrixI();
        setMatrixF(); setSolutionMatrix();
    }

    private void standardiseMatrix(int[][] m) {

        this.matrix = new double[m.length][m[0].length];
        for (double[] doubles : this.matrix) {
            Arrays.fill(doubles, 0);
        }

        for (int i = 0; i < m.length; i++) {
            if (MathOperations.getDenominator(m, i) == 0) {
                m[i][i] = 1;
            }
            for (int k = 0; k < m[i].length; k++) {
                this.matrix[i][k] = (double) m[i][k] / MathOperations.getDenominator(m, i);
            }
        }
    }

    private void setMatrixI() {

        this.matrixI = new double[matrixQ.length][matrixQ.length];
        for (double[] doubles : matrixI) {
            Arrays.fill(doubles, 0);
        }
        for (int i = 0; i < matrixI.length; i++) {
            matrixI[i][i] = 1;
        }
    }

    private void setMatrixR(int terminalIndex) {
        this.matrixR = new double[terminalIndex][this.matrix.length - terminalIndex];
        for (int i = 0; i < terminalIndex; i++) {
            if (matrix.length - terminalIndex >= 0)
                System.arraycopy(this.matrix[i], terminalIndex, this.matrixR[i], 0, matrix.length - terminalIndex);
        }
    }

    private void setMatrixQ(int terminalIndex) {
        this.matrixQ = new double[terminalIndex][terminalIndex];
        for (int i = 0; i < terminalIndex; i++) {
            System.arraycopy(this.matrix[i], 0, this.matrixQ[i], 0, terminalIndex);
        }
    }

    private void setMatrixF() {
        this.matrixF = MathOperations.inverseMatrix(MathOperations.subtractMatrices(matrixI, matrixQ));
    }

    private void setSolutionMatrix() {
        this.solutionMatrix = MathOperations.multiplyMatrices(this.matrixF, this.matrixR);
    }

    double[][] getSolutionMatrix() {
        return this.solutionMatrix;
    }
}

class MathOperations {

    static int getDenominator(int[][] m, int index) {
        int result = 0;
        for (int i = 0; i < m[index].length; i++) {
            result += m[index][i];
        }
        return result;
    }

    static double[][] subtractMatrices(double[][] m1, double[][] m2) {
        double[][] result = new double[m1.length][m1[0].length];
        for (int i = 0; i < m1.length; i ++) {
            for (int k = 0; k < m1[i].length; k++) {
                result[i][k] = m1[i][k] - m2[i][k];
            }
        }
        return result;
    }

    static double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
            }
        }

        return result;
    }

    private static double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
        double cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        }
        return cell;
    }

    static int commonLCM(int[] m) {
        int number1 = m[0];
        int commonLcm = 0;
        for (int i = 1; i < m.length; i++) {
            commonLcm = lcm(number1, m[i]);
            number1 = commonLcm;
        }
        return commonLcm;
    }

    private static int lcm(int number1, int number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        int absNumber1 = Math.abs(number1);
        int absNumber2 = Math.abs(number2);
        int absHigherNumber = Math.max(absNumber1, absNumber2);
        int absLowerNumber = Math.min(absNumber1, absNumber2);
        int lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    static int[] makeDoubleToFraction(double number) {
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

    static double[][] inverseMatrix(double[][] a) {
        int n = a.length;
        double[][] x = new double[n][n];
        double[][] b = new double[n][n];
        int[] index = new int[n];
        for (int i = 0; i < n; i++) {
            b[i][i] = 1;
        }
        gaussian(a, index);

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    b[index[j]][k] -= a[index[j]][i] * b[index[i]][k];
                }
            }
        }

        for (int i = 0; i < n; i++) {
            x[n - 1][i] = b[index[n - 1]][i]/a[index[n - 1]][n - 1];
            for (int j = n - 2; j >= 0; j--) {
                x[j][i] = b[index[j]][i];
                for (int k = j + 1; k < n; k++) {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

    private static void gaussian(double[][] a, int[] index) {
        int n = index.length;
        double[] c = new double[n];

        for (int i = 0; i < n; i++) {
            index[i] = i;
        }

        for (int i = 0; i < n; i++) {
            double c1 = 0;
            for (int j = 0; j < n; j++) {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        int k = 0;
        for (int j = 0; j < n - 1; j++) {
            double pi1 = 0;
            for (int i = j; i < n; i++) {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }

            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i = j + 1; i < n; i++) {
                double pj = a[index[i]][j] / a[index[j]][j];

                a[index[i]][j] = pj;

                for (int l = j + 1; l < n; l++) {
                    a[index[i]][l] -= pj * a[index[j]][l];
                }
            }
        }
    }
}

class CorrectMatrix {
    int[][] correctMatrix;
    int[][] sampleCorrectMatrix;
    ArrayList<Integer> orderOfCorrectMatrix = new ArrayList<>();

    CorrectMatrix(int[][] m) {
        setOrderOfCorrectMatrix(m);
        setSampleCorrectMatrix(m);
        setCorrectMatrix();
    }

    private void setCorrectMatrix () {
        this.correctMatrix = new int[sampleCorrectMatrix.length-1][sampleCorrectMatrix[0].length-1];
        for (int i = 0; i < correctMatrix.length; i++) {
            System.arraycopy(sampleCorrectMatrix[i + 1], 1, this.correctMatrix[i], 0, correctMatrix[i].length);
        }
    }

    private ArrayList<Integer> terminalIndexes (int[][] m) {
        ArrayList <Integer> temp = new ArrayList<>();
        for (int i = 0; i < m.length; i++) {
            if (MathOperations.getDenominator(m, i) == 0) {
                temp.add(i);
            }
        }

        return temp;
    }

    private void setOrderOfCorrectMatrix (int[][] m) {
        ArrayList <Integer> terminal = terminalIndexes(m);
        for (int i = 0; i < m.length; i++) {
            this.orderOfCorrectMatrix.add(i);
        }
        for (Integer integer : terminal) {
            this.orderOfCorrectMatrix.remove(integer);
        }
        this.orderOfCorrectMatrix.addAll(terminal);
    }

    private void setSampleCorrectMatrix (int[][] m) {
        this.sampleCorrectMatrix = new int[m.length + 1][m[0].length + 1];

        sampleCorrectMatrix[0][0] = -1;
        for (int i = 1; i < sampleCorrectMatrix.length; i++) {
            sampleCorrectMatrix[0][i] = this.orderOfCorrectMatrix.get(i-1);
            sampleCorrectMatrix[i][0] = this.orderOfCorrectMatrix.get(i-1);
        }

        for (int i = 1; i < sampleCorrectMatrix.length; i++) {
            for (int k = 1; k < sampleCorrectMatrix[i].length; k++) {
                sampleCorrectMatrix[i][k] = m[sampleCorrectMatrix[i][0]][sampleCorrectMatrix[0][k]];
            }
        }
    }

    int[][] getCorrectMatrix() {
        return this.correctMatrix;
    }
}

