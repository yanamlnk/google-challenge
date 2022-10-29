import java.util.ArrayList;
import java.util.Arrays;

public class Solution {
    static ArrayList<Integer> dfsResult;
    static int sizeOfResult;
    static int[][] floydWarshallMatrix;

    public static int[] solution(int[][] times, int times_limit) {

        if (times.length <= 2 || (times.length != times[0].length)) {
            return new int[] {};
        }

        setFloydWarshallMatrix(times);
        if (isNegCycle()) {
            int[] negativeCycleResult = new int[times.length - 2];
            for (int i = 0; i < negativeCycleResult.length; i++) {
                negativeCycleResult[i] = i;
            }
            return negativeCycleResult;
        }

        dfsResult = new ArrayList<>();
        sizeOfResult = 0;
        boolean[] visited = new boolean[floydWarshallMatrix.length];
        visited[0] = true;
        for (int i = 1; i < floydWarshallMatrix.length; i++) {
            dfs(i, floydWarshallMatrix, (times_limit - floydWarshallMatrix[0][i]), new ArrayList<>(), visited);
        }

        if (dfsResult.size() == 0) {
            return new int[] {};
        }

        int[] result = dfsResult.stream().mapToInt(i -> i).toArray();
        Arrays.sort(result);

        return result;
    }

    private static void dfs(int currentNode, int[][] matrix, int currentTime, ArrayList<Integer> tempResult, boolean[] visited) {
        //to check if it is end already and there is no time left + check if this corridor was already explored + check if size already full
        if (currentNode == matrix.length - 1 && currentTime < 0) {
            return;
        } else if (visited[currentNode]) {
            return;
        } else if (sizeOfResult == matrix.length - 2) {
            return;
        } else if (currentTime <= -999) {
            return;
        }

        if (currentNode == matrix.length - 1 && currentTime >= 0) {
            if (tempResult.size() > sizeOfResult) {
                dfsResult = new ArrayList<>(tempResult);
                sizeOfResult = dfsResult.size();
            }
        }

        visited[currentNode] = true;
        tempResult.add(currentNode - 1);
        for (int nextNode = 1; nextNode < matrix.length; nextNode++) {
            if (currentNode == nextNode) {
                continue;
            }
            dfs(nextNode, matrix, (currentTime - matrix[currentNode][nextNode]), tempResult, visited);
        }
        tempResult.remove(tempResult.size() - 1);
        visited[currentNode] = false;
    }

    private static void setFloydWarshallMatrix(int[][] input) {
        floydWarshallMatrix = new int[input.length][input[0].length];
        int i, j, k;
        for (i = 0; i < input.length; i++) {
            for (j = 0; j < input[0].length; j++) {
                floydWarshallMatrix[i][j] = input[i][j];
            }
        }
        for (k = 0; k < floydWarshallMatrix.length; k++) {
            for (i = 0; i < floydWarshallMatrix.length; i++) {
                for (j = 0; j < floydWarshallMatrix.length; j++) {
                    if (floydWarshallMatrix[i][k] + floydWarshallMatrix[k][j] < floydWarshallMatrix[i][j]) {
                        floydWarshallMatrix[i][j] = floydWarshallMatrix[i][k] + floydWarshallMatrix[k][j];
                    }
                }
            }
        }
    }

    private static boolean isNegCycle() {
        for (int i = 0; i < floydWarshallMatrix.length; i++)
            if (floydWarshallMatrix[i][i] < 0)
                return true;

        return false;
    }
}
