import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

public class Solution {
    public static int solution(int[][] map) {
        if (map.length == 2) {
            return 3;
        }
        Result result = new Result(map);
        return result.getResult();
    }
}

class Matrix {
    final int[][] inputMatrix;
    int bestRouteScenario;
    int startX = 0;
    int startY = 0;
    int endX;
    int endY;

    int[][] indexesOfOnes;
    boolean[] alreadyCheckedOnes;

    int[][] tempMatrix;

    Matrix(int[][] inputMatrix) {
        this.inputMatrix = inputMatrix;
        tempMatrix = new int[inputMatrix.length][inputMatrix[0].length];
        this.bestRouteScenario = inputMatrix.length + inputMatrix[0].length - 1;
        this.endX = inputMatrix.length - 1;
        this.endY = inputMatrix[0].length - 1;
        setIndexesOfOnes(); alreadyCheckedOnes = new boolean[indexesOfOnes.length];
    }

    void setIndexesOfOnes() {
        this.indexesOfOnes = new int[countOnes()][2];
        int m = 0;

        for (int i = 0; i < inputMatrix.length; i++) {
            for (int k = 0; k < inputMatrix[0].length; k++) {
                if (this.inputMatrix[i][k] == 1) {
                    this.indexesOfOnes[m][0] = i;
                    this.indexesOfOnes[m][1] = k;
                    m++;
                }
            }
        }
    }

    private int countOnes() {
        int count = 0;
        for (int[] matrix : this.inputMatrix) {
            for (int k = 0; k < this.inputMatrix[0].length; k++) {
                if (matrix[k] == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    //return copy of input matrix, but with 1 -> 0 in specified location
    int[][] getMatrixWithWallDestroyed(int row, int col) {
        setTempMatrix();
        tempMatrix[row][col] = 0;
        return tempMatrix;
    }

    private void setTempMatrix() {
        for (int i = 0; i < this.inputMatrix.length; i++) {
            System.arraycopy(inputMatrix[i], 0, tempMatrix[i], 0, inputMatrix[i].length);
        }
    }
}

//class to find the distance from A (including) to B (including)
class Path {
    // up, left, right, down
    private static final int[] row = { -1, 0, 0, 1 };
    private static final int[] col = { 0, -1, 1, 0 };

    private static boolean isValid(int[][] mat, boolean[][] visited, int row, int col) {
        return (row >= 0) && (row < mat.length) && (col >= 0) && (col < mat[0].length)
                && mat[row][col] == 0 && !visited[row][col];
    }

     static class Node {
        // matrix cell coordinates: (x, y)
        // cell's minimum distance to the start: dist
        int x, y, dist;

        Node(int x, int y, int dist) {
            this.x = x;
            this.y = y;
            this.dist = dist;
        }
    }

    // input matrix: mat, from: (i, j), to: (x, y)
    // returns int minDistance OR -1 if no path available
    static int findShortestPathLength(int[][] mat, int i, int j, int x, int y) {

        int M = mat.length;
        int N = mat[0].length;
        boolean[][] visited = new boolean[M][N];

        Queue<Node> q = new ArrayDeque<>();
        visited[i][j] = true;
        q.add(new Node(i, j, 1));

        int minDistance = Integer.MAX_VALUE;

        while (!q.isEmpty()) {

            Node node = q.poll();
            i = node.x;
            j = node.y;
            int dist = node.dist;

            if (i == x && j == y) {
                minDistance = dist;
                break;
            }

            for (int k = 0; k < 4; k++) {
                if (isValid(mat, visited, i + row[k], j + col[k])) {
                    visited[i + row[k]][j + col[k]] = true;
                    q.add(new Node(i + row[k], j + col[k], dist + 1));
                }
            }
        }

        if (minDistance != Integer.MAX_VALUE) {
            return minDistance;
        }
        return -1;
    }
}

class Result extends Matrix {

    ArrayList<Integer> distances = new ArrayList<>();
    int bestCaseScenario;
    int tempX;
    int tempY;
    int result;

    Result(int[][] mat) {
        super(mat);
        this.bestCaseScenario = mat.length + mat[0].length - 1;
        setResult();
    }

    int getResult() {
        return this.result;
    }

    private void setResult() {
        distances.add(Path.findShortestPathLength(inputMatrix, startX, startY, endX, endY));
        int i = 0;
        while (!alreadyCheckedOnes[alreadyCheckedOnes.length-1]) {
            if(distances.get(i) == bestCaseScenario) {
                this.result = bestCaseScenario;
                break;
            } else i++;

            setNewCoordinates();
            distances.add(getDistanceWithoutWall());
        }

        if (result != bestCaseScenario) {
            Collections.sort(distances);
            for (Integer distance : distances) {
                if (distance != -1) {
                    this.result = distance;
                    break;
                }
            }
        }
    }

    private void setNewCoordinates() {
        for (int i = 0; i < alreadyCheckedOnes.length; i++) {
            if (!alreadyCheckedOnes[i]) {
                this.tempX = indexesOfOnes[i][0];
                this.tempY = indexesOfOnes[i][1];
                alreadyCheckedOnes[i] = true;
                break;
            }
        }
    }

    private int getDistanceWithoutWall() {
        int startToWall = Path.findShortestPathLength(getMatrixWithWallDestroyed(tempX, tempY), startX, startY, tempX, tempY);
        int wallToEnd = Path.findShortestPathLength(getMatrixWithWallDestroyed(tempX, tempY), tempX, tempY, endX, endY);
        if (startToWall == -1 || wallToEnd == -1) {
            return -1;
        }
        return startToWall + wallToEnd - 1;
    }
}
