# Description
You're so close to destroying the LAMBCHOP doomsday device you can taste it! But in order to do so, you need to deploy special self-replicating bombs designed for you by the brightest scientists on Bunny Planet. There are two types: Mach bombs (``M``) and Facula bombs (``F``). The bombs, once released into the LAMBCHOP's inner workings, will automatically deploy to all the strategic points you've identified and destroy them at the same time.

But there's a few catches. First, the bombs self-replicate via one of two distinct processes:
- Every Mach bomb retrieves a sync unit from a Facula bomb; for every Mach bomb, a Facula bomb is created;
- Every Facula bomb spontaneously creates a Mach bomb.

For example, if you had 3 Mach bombs and 2 Facula bombs, they could either produce 3 Mach bombs and 5 Facula bombs, or 5 Mach bombs and 2 Facula bombs. The replication process can be changed each cycle.

Second, you need to ensure that you have exactly the right number of Mach and Facula bombs to destroy the LAMBCHOP device. Too few, and the device might survive. Too many, and you might overload the mass capacitors and create a singularity at the heart of the space station - not good!

And finally, you were only able to smuggle one of each type of bomb - one Mach, one Facula - aboard the ship when you arrived, so that's all you have to start with. (Thus it may be impossible to deploy the bombs to destroy the LAMBCHOP, but that's not going to stop you from trying!)

You need to know how many replication cycles (generations) it will take to generate the correct amount of bombs to destroy the LAMBCHOP. Write a function ``solution(M, F)`` where ``M`` and ``F`` are the number of Mach and Facula bombs needed. Return the fewest number of generations (as a string) that need to pass before you'll have the exact number of bombs necessary to destroy the LAMBCHOP, or the string "impossible" if this can't be done! ``M`` and ``F`` will be string representations of positive integers no larger than 10^50. For example, if ``M`` = "2" and ``F`` = "1", one generation would need to pass, so the solution would be "1". However, if ``M`` = "2" and ``F`` = "4", it would not be possible.

### Test cases

```
Input:
Solution.solution({"4", "7"})
Output:
4

Input:
Solution.solution({"2", "1"})
Output:
1
```

# Explanation
Firstly, we have 2 processes, and they **can't** happen simultaneously:
1. Each M bomb creates F bomb.
2. Each F bomb creates M bomb.

Those processes can happen in any order, so we can go 3 times through 1st process, and then 10 times through 2nd, or 20 times through first, and 0 through 2nd, or we can go 1st -> 2nd -> 1st -> 2nd. The point is, order is not important.

Since those processes can't happen simultaneously, it is safe to say that it is impossible to get ``n`` M-bombs and ``k`` F-bombs, if ``n == k`` (except if ``n == k == 1``, in that case we can immediately return ``0``)

So, if we take our test input {"3", "7"} and move step by step to understand the process:
```

M	F
1	1	Generation 0, initial state
  <-(1)		2nd process, so each F bomb create M bomb
2	1	Generation 1, we now have 2 (1+1) M bombs and 1 F bomb
  <-(1)		2nd process again
3	1	Generation 2, M = 2+1, F = 1
  (3)->		1st process, each M bomb create F bomb
3	4	Generation 3, M = 3, F = 1+3
  (3)->		1st process again
3	7	Generation 4, M = 3, F = 4+3 - desired state

```
So we need 4 generation to get to the desired state

Secondly, we will deal with huge numbers here (<= 10^50), so I have used BigInteger for this task. [Here}(https://www.geeksforgeeks.org/biginteger-class-in-java/) you can read more about this class.

Lastly, out initial state is ``M = 1``, ``F = 1``.
Input represents desired state -> how many bombs we need to have in the end.
Output is how many steps we need to take from initial state to reach desired state.

# Solution
Let's say we have input ``{"13", "4"}``.
To solve this problem, we can move backwards:
```
generation = 0

M	F
13	4
  13-4=9	generation++ [=1]
9	4
  9-4=5		generation++ [=2]
5	4
  5-4=1		generation++ [=3]
1	4
  4-1=3		generation++ [=4]
1	3
  3-1=2		generation++ [=5]
1	2
  2-1=1		generation++ [=6]
1	1
```
And we get 6 generations.
So, we always need to **subtract lower number from higher, and count each calculation as +1 generation**. In this scenario it really doesn't matter for which bomb type exactly we are calculating at each step, the only thing that is important is which number is higher and which is lower.

But what if our input is ``["10^50"; "3456"]``? It will take a lot of time to calculate, and execution time is limited. We need to optimize it somehow.

We can see how many times lower number fit in higher number.
In example above, ``13 / 4 = 3.25`` -> so number ``4`` can fit in ``13`` 3 whole times.
Then, ``generation+=3``, and
``13 - 4*3 = 13 - 12 = 1``

Now we get to the step where M = 1, F = 4.
4/1 = 4, which will give us the result of 7 generation instead of 6(``generation+=4``; ``generation == 7``). We can either subtract 1 from the final result OR we can subtract 1 from higher number immediately.

```
So, if M == 1 || F == 1
generation += (HigherNumber - 1)
```

In our case, M == 1, then
generation = generation + (F - 1) = 3 + 3 = 6

If in the end we will not get to the initial state ["1", "1"], then we return "impossible"

```
M	F
4	2

 4/2 = 2
4 - (2*2)

0	2 -> impossible
```

**So we take the following steps to solve this challenge**:
``generation = 0``
1. Asign and (re)arrange ``n1`` and ``n2``, where ``n1 < n2``
  - if ``n1`` == 1 -> ``generation += (n2-1)`` -> return ``generation``
  - else if ``n1 == n2`` -> return ``"impossible"``
  - else if ``n1 < 1`` -> return ``"impossible"``
2. ``temp = n2 / n1`` (save only whole number, ignore remainder)
3. ``generation += temp``
4. ``n2 = n2 - (n1 * temp)``
5. Come back to step 1 (with new n2 from step 4)
