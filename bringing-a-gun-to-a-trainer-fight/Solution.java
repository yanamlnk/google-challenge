import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Solution {
    static int roomX, roomY, playerX, playerY, trainerX, trainerY, laserDistance;

    static int copiesOfX;
    static int copiesOfY;

    public static int solution(int[] dimensions, int[] your_position, int[] trainer_position, int distance) {
        roomX = dimensions[0]; roomY = dimensions[1];
        playerX = your_position[0]; playerY = your_position[1];
        trainerX = trainer_position[0]; trainerY = trainer_position[1];
        laserDistance = distance;

        copiesOfX = playerX + laserDistance / roomX + 1;
        copiesOfY = playerY + laserDistance / roomY + 1;

        Map<Point, Point> allPoints = new HashMap<>();
        for (int i = -copiesOfX; i <= copiesOfX; i++) {
            for (int j = -copiesOfY; j <= copiesOfY; j++) {
                Point playerPosition = new Point(mirror(i, roomX, playerX), mirror(j, roomY, playerY), playerX, playerY, false);
                Point playerBlock = allPoints.get(playerPosition);
                if (playerBlock == null || playerBlock.distance > playerPosition.distance) {
                    allPoints.put(playerPosition, playerPosition);
                }

                Point trainerPosition = new Point(mirror(i, roomX, trainerX), mirror(j, roomY, trainerY), playerX, playerY, true);
                Point trainerBlock = allPoints.get(trainerPosition);
                if (trainerBlock == null || trainerBlock.distance > trainerPosition.distance) {
                    allPoints.put(trainerPosition, trainerPosition);
                }
            }
        }

        return (int) allPoints.values().stream().filter(p -> p.distance <= laserDistance).filter(p -> p.trainer).count();
    }

    private static int mirror(int i, int roomDimensions, int playerPosition) {
        if (i % 2 == 0) {
            return (i * roomDimensions) - playerPosition;
        } else return (i-1) * roomDimensions + playerPosition;
    }

    private static class Point {
        int x, y;
        double distance;
        int[] bearer;
        boolean trainer;

        Point (int x, int y, int originalX, int originalY, boolean trainer) {
            this.x = x;
            this.y = y;
            this.trainer = trainer;

            this.bearer = new int[]{x - originalX, y - originalY};
            this.distance = Math.hypot(bearer[0], bearer[1]);

            int gcd = gcd(bearer[0], bearer[1]);
            if (gcd != 0) {
                this.bearer[0] /= gcd;
                this.bearer[1] /= gcd;
            }
        }

        private int gcd(int a, int b) {
            if (b == 0)
                return Math.abs(a);
            return gcd(b, a % b);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            return prime + Arrays.hashCode(bearer);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Point other = (Point) obj;
            if (!Arrays.equals(bearer, other.bearer))
                return false;
            return true;
        }
    }
}
