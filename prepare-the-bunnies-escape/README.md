# Description
You're awfully close to destroying the LAMBCHOP doomsday device and freeing Commander Lambda's bunny workers, but once they're free of the work duties the bunnies are going to need to escape Lambda's space station via the escape pods as quickly as possible. Unfortunately, the halls of the space station are a maze of corridors and dead ends that will be a deathtrap for the escaping bunnies. Fortunately, Commander Lambda has put you in charge of a remodeling project that will give you the opportunity to make things a little easier for the bunnies. Unfortunately (again), you can't just remove all obstacles between the bunnies and the escape pods - at most you can remove one wall per escape pod path, both to maintain structural integrity of the station and to avoid arousing Commander Lambda's suspicions.

You have maps of parts of the space station, each starting at a work area exit and ending at the door to an escape pod. The map is represented as a matrix of ``0s`` and ``1s``, where ``0s`` are passable space and ``1s`` are impassable walls. The door out of the station is at the top left (0,0) and the door into an escape pod is at the bottom right (w-1,h-1).

Write a function ``solution(map)`` that generates the length of the shortest path from the station door to the escape pod, where you are allowed to remove one wall as part of your remodeling plans. The path length is the total number of nodes you pass through, counting both the entrance and exit nodes. The starting and ending positions are always passable (0). The map will always be solvable, though you may or may not need to remove a wall. The height and width of the map can be from 2 to 20. Moves can only be made in cardinal directions; no diagonal moves are allowed.

### Test cases
```
Input:
Solution.solution({{0, 1, 1, 0}, {0, 0, 0, 1}, {1, 1, 0, 0}, {1, 1, 1, 0}})
Output:
7

Input:
Solution.solution({{0, 0, 0, 0, 0, 0}, {1, 1, 1, 1, 1, 0}, {0, 0, 0, 0, 0, 0}, {0, 1, 1, 1, 1, 1}, {0, 1, 1, 1, 1, 1}, {0, 0, 0, 0, 0, 0}})
Output:
11
```

# Explanation
So we need to return the length of the shortest path INCLUDING nodes of start and end.
There will always be the way, so no need to check if maze is passable or not. Also it is worth noting, and if ``m.length == m[0].length == 2``, we can immediately return result ``3`` without checking.
No diagonal moves, which means we have only 4 directions to move.

# Solution
The whole solution consists of 4 parts:
1. Decide the best case scenario
2. Find the length of the path
3. Check if removing one wall will change the length
4. Find the best length

### 1. Decide the best case scenario
Best case scenario is the shortest possible length for given map. Since we can move only in 4 directions, the best case will be

``m.length + m[0].length - 1``

If at some point of finding the best path we reach length that equals to best case scenario, we can immediately stop the program and return this result, because we will not get better for sure.

### 2. Find the length of the path
I have used [Breadth–first search (BFS)](https://www.techiedelight.com/breadth-first-search/) for this problem. You can see its implementation for this kind of task [here](https://www.techiedelight.com/lee-algorithm-shortest-path-in-a-maze/). 

**Explanation of the algorithm***
<sub>If you are aware of Backtracking and BFS algorithms and how they differ, if you now what is Queue data structure - you can easily go ahead and skip this explanation. I didn't have such knowledge when I was writing code for this task, so I spend extra time to understand the concepts behind it.</sub>

We can also use **backtracking**, in other words, make program to go through all possible routes one by one, then compare them all, and return the length of the best one. BFS is different because it is going through all possible routes simultaneously, moving by one step in each of the routes at the same time. Once some of the routes reaches the destination, it is automatically considered as the best route and the program stops (because this route got to the destination first). 
<sub>That is why this algorithm requires a lot of memory, but since our maximum length of ``m`` is 20, we can use it here.</sub>

So if we have the following:

```
            O - O - O - O
         /  
start - O - O - O - O - O - O - -  end
         \                          |
            O - O - O - O - O - O - O
```

**Backtracking** will proccess it in several steps:

1.

```
            2 - 3 - 4 - 5
         /  
start - 1 - O - O - O - O - O - -  end
         \                          |
            O - O - O - O - O - O - O
```
This route hasn't reached destination.

2.

```
            O - O - O - O
         /  
start - 1 - 2 - 3 - 4 - 5 - 6 - -  end
         \                          |
            O - O - O - O - O - O - O
```
This route reaches the destination in 6 steps. In backtracking number of steps is already the length of the route, so length of this path is 6.

3.

```
            O - O - O - O
         /  
start - 1 - 0 - O - O - O - O - -  end
         \                          |
            2 - 3 - 4 - 5 - 6 - 7 - 8
```
This route reaches the destination in 8 steps, length 8.

Then we compare those routes where the destination was reached and return the length of the best, which is 6.

Now **BFS**:

```
Distance: 1   2   3   4    5    6

              2 - 5 - 8 -  11
           /  
  start - 1 - 3 - 6 - 9 -  12 - 14 - -  end
           \                             |
              4 - 7 - 10 - 13 -  O - O - O
```
So, instead of going route by route as in backtracking, we move in 3 different routes simultaneously.
1st step is similar to all 3 routes, and it is located in distance ``1`` to the start.
2nd, 3rd and 4th steps are taken in 3 different routes, but all of these steps have same distance to the start - ``2``.
Same goes for 5th, 6th and 7th steps in distance ``3``, and so on.

It continues until step 14. There is no more step 15, even though those steps would have same distance to the start. But program stops at step 14, because at this point it reaches the destination, so this route is considered the best because it came to the end first.

Now we check what distance step 14 has to the start. And we got the answer - distance is 6.

BFS can move through all routes at the same time with help of **Queue data structure**. You can read more about it [here](https://www.geeksforgeeks.org/queue-data-structure/), but what is the most important about this data structure:
- it uses **FIFO** order (First In First Out)(_unlike Stack data structure, where Last In First Out_), basically the same as in real-world queue. The first in the queue is the first to be served.
- **enqueue** - put an element to the queue to the last place
- **dequeue** - take element from the queue from the first place

If we check the example above, we can see that each of the nodes is processed as per queue structure:

```
Enqueue 1st cell.
	Queue: [1]
Dequeue first element (1) of queue to process it.
	Queue: []
Enqueue 2nd, 3rd and 4th cell
	Queue: [2, 3, 4]
Dequeue first element (2).
	Queue: [3, 4]
Enqueue 5th element
	Queue [3, 4, 5]
Dequeue first element (3)
	Queue [4, 5]
Enqueue 6th element
	Queue [4, 5, 6]
...and so on.
```

Now there is a problem with this queue. We enqueue cells and process them, but we do not know what distance they have to the start.
So it is better to enqueue not the cell, but some object. In my solution there is ``static class Node``, and it has 3 fields: x, y (for position) and dist.
In our example above, we can add object of Node with 2 fields: number and distance.
So, our 1st cell will be node1(1, 1). 2nd -> node2(2, 2). 3rd -> node3(3, 2). 14th -> node14(14, 6) 

Example above has linear structure, so we moved in one direction only (going backwards was also possible, though unnecessary), but when we are speaking about maze, we can move in 4 different directions. We need to track somehow, which cells we have visited and counted, and which are still open for exploration.

For this purpose we need to create one more copy of the same size as our maze, filled with boolean value ``false``. Once we visited the cell, we change the value at the same location to ``true``. In that case, everytime we move, we can see if this cell was already visited or not.

**Now back to our task.**
1. Create queue, enqueue starting point. Since we need to count starting node, the distance of start is already set as ``1``.
2. Dequeue first node and process it.
  - if this node == destination node -> return distance of this node.
3. From this node, check 4 directions. Enqueue each node to which we can move from our current node. Each of this cell must have distance of ``(currentNodeDistance + 1)``. Mark each of enqueued cell as visited in boolean matrix.
4. Return to step 2 while queue is not empty and until destination node is reached. (_If destination is not reached and queue is empty, it means that there is no route to the destination_).

## 3. Check if removing one wall will change the length
There are basically 2 ways to do it:
1. manually remove each of 1s one by one and find path again and again for new matrices with one wall removed.
2. instead of creating new matrix, we can work with existing one, but instead of finding path from start to the end, we find path to the wall, and then find path from that wall to the end. The sum of these 2 paths will be the path with wall removed (just don't forget to subtract 1 to avoid double counting for the wall). So we can iterate through each of the walls, and find the best path.
```
path-with-wall-removed = path-from-start-to-wall + path-from-wall-to-end - 1
```
<sub>When I was writing this, I noticed that I overcomplicate my solution and do both ways :)</sub>

## 4. Find the best length
1. Create ArrayList
2. Add length of the path without wall removed
  - if best case scenario reached -> return this value
3. Add lengths of the paths with each of the wall removed
  - if best case scenario reached -> return this value
4. Once all walls were processed, return the lowest value of the ArrayList
