# Description

Uh-oh -- you've been cornered by one of Commander Lambdas elite bunny trainers! Fortunately, you grabbed a beam weapon from an abandoned storeroom while you were running through the station, so you have a chance to fight your way out. But the beam weapon is potentially dangerous to you as well as to the bunny trainers: its beams reflect off walls, meaning you'll have to be very careful where you shoot to avoid bouncing a shot toward yourself!

Luckily, the beams can only travel a certain maximum distance before becoming too weak to cause damage. You also know that if a beam hits a corner, it will bounce back in exactly the same direction. And of course, if the beam hits either you or the bunny trainer, it will stop immediately (albeit painfully). 

Write a function `solution(dimensions, your_position, trainer_position, distance)` that gives an array of 2 integers of the `width` and `height` of the room, an array of 2 integers of your `x` and `y` coordinates in the room, an array of 2 integers of the trainer's `x` and `y` coordinates in the room, and returns an integer of the number of distinct directions that you can fire to hit the elite trainer, given the maximum distance that the beam can travel.

The room has integer dimensions `[1 < x_dim <= 1250, 1 < y_dim <= 1250]`. You and the elite trainer are both positioned on the integer lattice at different distinct positions `(x, y)` inside the room such that `[0 < x < x_dim, 0 < y < y_dim]`. Finally, the maximum distance that the beam can travel before becoming harmless will be given as an integer `1 < distance <= 10000`.

For example, if you and the elite trainer were positioned in a room with dimensions `[3, 2]`, your_position `[1, 1]`, trainer_position `[2, 1]`, and a maximum shot distance of `4`, you could shoot in seven different directions to hit the elite trainer (given as vector bearings from your location): `[1, 0], [1, 2], [1, -2], [3, 2], [3, -2], [-3, 2], and [-3, -2]`. As specific examples, the shot at bearing `[1, 0]` is the straight line horizontal shot of distance `1`, the shot at bearing `[-3, -2]` bounces off the left wall and then the bottom wall before hitting the elite trainer with a total shot distance of `sqrt(13)`, and the shot at bearing `[1, 2]` bounces off just the top wall before hitting the elite trainer with a total shot distance of `sqrt(5)`.

## Test cases
```
Input:
Solution.solution([3,2], [1,1], [2,1], 4)
Output: 7

Input:
Solution.solution([300,275], [150,150], [185,100], 500)
Output: 9
```

# Explanation
Great explanation was already written [here](https://peter-ak.github.io/2020/05/10/Brining_a_gun_to_a_guard_fight.html). But I will still write in my words :)

Here we have coordinate system task. 

<img width="343" alt="image" src="https://user-images.githubusercontent.com/90959658/202672165-dcb3f689-bc23-492c-b82e-09243a11519f.png">

Instead of trying to find all possible angles of shots, we can go another way:
1) mirror the room
2) mirror player's and trainer's points

<img width="412" alt="image" src="https://user-images.githubusercontent.com/90959658/202674186-88bc39c6-a0fd-4571-9cf7-fc7036269f16.png">

The shot will bounce back to trainer, if the shot reaches the mirrored trainer's point. So we can mirror given room `n` times and find out how many mirrored trainers will be shot from player's original position. What we need to take into account:
1) distance. If distance to mirrored trainer will be higher than maximum laser distance - we can omit counting this trainer as he is unreacheable. To count distance we use **Distance Formula**.
<img width="475" alt="image" src="https://user-images.githubusercontent.com/90959658/202677956-7af03b62-9d7f-4b3e-a222-604af991ead1.png">

2) obstacles. We will not be able to reach the trainer if there is player or trainer in the way.
Like in example below, Player will not reach Trainer2, because Trainer is in the way. Similarly, Trainer3 is unreachable, because Player3 is an obstacle.

<img width="623" alt="image" src="https://user-images.githubusercontent.com/90959658/202679928-a4bebba4-b1f5-4b88-a4ca-94afbee89198.png">

# Solution 

1) Find out how many times room should be mirrored:
```
        copiesOfX = playerX + laserDistance / roomX + 1;
        copiesOfY = playerY + laserDistance / roomY + 1;
```
If you are working with doubles, you can use ``Math.ceil()`` function.

2) Get all players' and trainers' mirrored points:
- iterate through 2 loops: 
    1) from -copiesOfX to copiesOfX
    2) from -copiesOfY to copiesOfY
- get mirrored points for players and trainers. I have written below method for this:
```
    private static int mirror(int i, int roomDimensions, int playerPosition) {
        if (i % 2 == 0) {
            return (i * roomDimensions) - playerPosition;
        } else return (i-1) * roomDimensions + playerPosition;
    }
```
So if I want to get coordinates for player, `i` = `copiesOfX`, `j` = `copiesOfY`:

```
x = mirror(i, roomX, playerX)
y = mirror(j, roomY, playerY)
```
- save all of mirrored coordinates in the preffered way. I have created static class `Point` with constructor that takes new `x`, `y` and `boolean trainer` to check whether it is a player or a trainer. Each `Point` was added to HashMap `allPoints`.

P.S. it is up to you to choose the best way to store all mirrored points. For the first time I have used ArrayList and I failed a lot of tests, because this task is also about optimization. For maximum values my program was working really slow. I changed to HashMap. Interesting answer on why HashMap is faster than ArrayList in this case is [here](https://stackoverflow.com/questions/18862997/using-arraylist-or-hashmap-for-better-speed#:~:text=The%20ArrayList%20has%20O(n,for%20large%20values%20of%20n)).

3) Get distance and bearing of each point:
Each object of `Point` class also has 2 fields: `double distance` and `int[] bearer`. 

Bearing:
```
bearer = new int[]{x - playerX, y - playerY};
```

Distance:
```
distance = Math.hypot(bearer[0], bearer[1]);
```

You can also simplify bearing by dividing both numbers of the array by GCD of those 2 numbers.

4) Before adding new `Point` to `allPoints`, check if there are already some points with the same bearing. If yes, compare distances. Add the one with lower distance.

5) All that is left is to filter `allPoints` and leave only those points where `trainer == true` and `distance <= laserDistance`. Result will be the number of points that are left after you filter them.
