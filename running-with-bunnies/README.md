# Description 

You and the bunny workers need to get out of this collapsing death trap of a space station -- and fast! Unfortunately, some of the bunnies have been weakened by their long work shifts and can't run very fast. Their friends are trying to help them, but this escape would go a lot faster if you also pitched in. The defensive bulkhead doors have begun to close, and if you don't make it through in time, you'll be trapped! You need to grab as many bunnies as you can and get through the bulkheads before they close. 

The time it takes to move from your starting point to all of the bunnies and to the bulkhead will be given to you in a square matrix of integers. Each row will tell you the time it takes to get to the start, first bunny, second bunny, ..., last bunny, and the bulkhead in that order. The order of the rows follows the same pattern ``(start, each bunny, bulkhead)``. The bunnies can jump into your arms, so picking them up is instantaneous, and arriving at the bulkhead at the same time as it seals still allows for a successful, if dramatic, escape. (Don't worry, any bunnies you don't pick up will be able to escape with you since they no longer have to carry the ones you did pick up.) You can revisit different spots if you wish, and moving to the bulkhead doesn't mean you have to immediately leave -- you can move to and from the bulkhead to pick up additional bunnies if time permits.

In addition to spending time traveling between bunnies, some paths interact with the space station's security checkpoints and add time back to the clock. Adding time to the clock will delay the closing of the bulkhead doors, and if the time goes back up to ``0`` or a ``positive number`` after the doors have already closed, it triggers the bulkhead to reopen. Therefore, it might be possible to walk in a circle and keep gaining time: that is, each time a path is traversed, the same amount of time is used or added.

Write a function of the form solution(times, time_limit) to calculate the most bunnies you can pick up and which bunnies they are, while still escaping through the bulkhead before the doors close for good. If there are multiple sets of bunnies of the same size, return the set of bunnies with the lowest worker IDs (as indexes) in sorted order. The bunnies are represented as a sorted list by worker ID, with the first bunny being 0. There are at most 5 bunnies, and time_limit is a non-negative integer that is at most 999.

For instance, in the case of
```
[
  [0, 2, 2, 2, -1],  # 0 = Start
  [9, 0, 2, 2, -1],  # 1 = Bunny 0
  [9, 3, 0, 2, -1],  # 2 = Bunny 1
  [9, 3, 2, 0, -1],  # 3 = Bunny 2
  [9, 3, 2, 2,  0],  # 4 = Bulkhead
]
```
and a ``time limit`` of ``1``, the five inner array rows designate the ``starting point``, ``bunny 0``, ``bunny 1``, ``bunny 2``, and the ``bulkhead door exit`` respectively. You could take the path:

```
Start End Delta Time Status
    -   0     -    1 Bulkhead initially open
    0   4    -1    2
    4   2     2    0
    2   4    -1    1
    4   3     2   -1 Bulkhead closes
    3   4    -1    0 Bulkhead reopens; you and the bunnies exit
```

With this solution, you would pick up bunnies ``1`` and ``2``. This is the best combination for this space station hallway, so the solution is ``[1, 2]``.

### Test cases
```
Input:
Solution.solution({{0, 1, 1, 1, 1}, {1, 0, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 1, 1, 0, 1}, {1, 1, 1, 1, 0}}, 3)
Output:
    [0, 1]

Input:
Solution.solution({{0, 2, 2, 2, -1}, {9, 0, 2, 2, -1}, {9, 3, 0, 2, -1}, {9, 3, 2, 0, -1}, {9, 3, 2, 2, 0}}, 1)
Output:
    [1, 2]
```

# Explanation

### Input
Input is similar to [Doomsday Fuel](https://github.com/yanamlnk/google-challenge/tree/main/doomsday-fuel), we have the matrix of times, where we can see how many time it takes to move from one place to another.

```
[ 
  [0, 2, 2, 2, -1],  # 0 = Start 
  [9, 0, 2, 2, -1],  # 1 = Bunny 0
  [9, 3, 0, 2, -1],  # 2 = Bunny 1
  [9, 3, 2, 0, -1],  # 3 = Bunny 2
  [9, 3, 2, 2,  0],  # 4 = Bulkhead
]
```
To move from start to Bunny0, we need to spend 2 time units (as in node [0, 1]. But to move from Bunny0 back to start, we need to spend 9 time units ([1, 0]). We also have nodes with "-1" in it - those are spaces where we can add time (1 - (-1) = 2).

### Output
We need to return how many bunnies we will be able to take in the best case scenario. In example above, we can either take only Bunny0, or we can take Bunny1 + Bunny2, so better result is [1, 2].

If there are several possible options, return one with the lowest workers' IDs.
Example:
```
0, 1, 1, 1, 1, 
1, 0, 1, 1, 1, 
1, 1, 0, 1, 1, 
1, 1, 1, 0, 1, 
1, 1, 1, 1, 0
``` 

With time limit ``3`` we have several options:
```
[0, 1]
[1, 2]
[0, 2]
```
Since we need to return the lowest workers' IDs, the first option will be the best fit.

### Algorithms
We will have at most 5 bunnies to save, so we can use simple ``brute force``. I have used 2 algoritms in my solution:
1. Floyd-Warshall Algorithm
2. DFS

### 1. Floyd-Warshall Algorithm
[Floyd-Warshall Algorithm](https://www.programiz.com/dsa/floyd-warshall-algorithm) is about finding the best option to go from point A to point B. In this algorithms we check what path is better: going from A to B directly, or choosing to go through intermediate node, so path would be A -> C -> B (spoiler, it is often better to go through intermediate point), or A -> D -> B, etc. 

There is one more thing about this algorithm: with help of it we can detect negative cycles. 
Negative cycle - is when we are gaining, not loosing, time going through some path where negative cycle is. We got negative cycle when total sum of distance or time in some part of the path is negative. Please check implementation of this algorithm [here](https://www.geeksforgeeks.org/detecting-negative-cycle-using-floyd-warshall/).
Basically negative cycle means, that we can get to it and iterate as many times as needed to gain as much time as needed, and once we have enough time we can take all the bunnies easily.

### 2. DFS
[Depth First Search](https://www.programiz.com/dsa/graph-dfs) is a [recursive](https://www.geeksforgeeks.org/introduction-to-recursion-data-structure-and-algorithm-tutorials/) algorithm that helps you move through all nodes of the graph. It uses Stack Data Structure (unlile Queue, where we have principle First In First Out, Stack is First In Last Out, for Last In First Out)

The DFS algorithm works as follows:
- Start by putting any one of the graph's vertices on top of a stack.
- Take the top item of the stack and add it to the visited list.
- Create a list of that vertex's adjacent nodes. Add the ones which aren't in the visited list to the top of the stack.
- Keep repeating steps 2 and 3 until the stack is empty.

You can also find implementatuon [here](https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/).

# Solution
1. Implement Floyd-Warshall algorithm on input matrix. For the rest of the solution you need to work with new matrix, as it simplifies next steps (because this new matrix will already show best time between 2 locations with intermediate nodes accounted), so you can either change the input itself, or create copy of input, implement algorithm on the copy, and continue working with new matrix. I have used [this](https://www.geeksforgeeks.org/detecting-negative-cycle-using-floyd-warshall/) implementation.
2. Check if there is negative cycle.
- if yes -> ``return`` array of all bunnies' IDs available in input array.
3. If no negative cycle, implement DFS algorithm at new matrix from step 1. The most inportant change to the general implementation of this algorithm is ``time_limit``. 
- if you already on last node AND time_limit >= 0 -> it means that you have already check the path up to the end and managed to get away on time, so this path is one of our possible solution.
- if you are on last node AND time_limit < 0, it is incorrect solution and we need to try to find another way
4. return SORTED result.
