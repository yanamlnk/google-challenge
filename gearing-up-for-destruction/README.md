# Description

As Commander Lambda’s personal assistant, you’ve been assigned the task of configuring the LAMBCHOP doomsday device’s axial orientation gears. It should be pretty simple — just add gears to create the appropriate rotation ratio. But the problem is, due to the layout of the LAMBCHOP and the complicated system of beams and pipes supporting it, the pegs that will support the gears are fixed in place.

The LAMBCHOP’s engineers have given you lists identifying the placement of groups of pegs along various support beams. You need to place a gear on each peg (otherwise the gears will collide with unoccupied pegs). The engineers have plenty of gears in all different sizes stocked up, so you can choose gears of any size, from a radius of 1 on up. Your goal is to build a system where the last gear rotates at twice the rate (in revolutions per minute, or rpm) of the first gear, no matter the direction. Each gear (except the last) touches and turns the gear on the next peg to the right.

Given a list of distinct positive integers named ``pegs`` representing the location of each peg along the support beam, write a function ``solution(pegs)`` which, if there is a solution, returns a list of two positive integers a and b representing the numerator and denominator of the first gear’s radius in its simplest form in order to achieve the goal above, such that ``radius = a/b``. The ratio a/b should be greater than or equal to 1.
Not all support configurations will necessarily be capable of creating the proper rotation ratio, so if the task is impossible, the function ``solution(pegs)`` should return the list [-1, -1].

For example, if the pegs are placed at [4, 30, 50], then the first gear could have a radius of 12, the second gear could have a radius of 14, and the last one a radius of 6. Thus, the last gear would rotate twice as fast as the first one. In this case, pegs would be [4, 30, 50] and ``solution(pegs)`` should return [12, 1].

The list ``pegs`` will be given sorted in ascending order and will contain at least 2 and no more than 20 distinct positive integers, all between 1 and 10000 inclusive.

### Test cases

```
Input:
Solution.solution({4, 17, 50})
Output:
{-1, -1}

Input:
Solution.solution({4, 30, 50})
Output:
{12, 1}
```

# Explanation
The most important information we get from the description:
- **System**:
  - We have simple gear train system. If you want to better understand how to find ratio in gear train, you can read [this short article](https://www.omnicalculator.com/physics/gear-ratio). In general: ``gear ratio = (radius of input gear)/(radius of output gear)``.
  - So if we want the last gear to rotate twice as fast as the first, no matter the direction, then ``radiusOfLastGear = radiusOfFirstGear / 2``.
  - As for location of each peg - imagine those like positions on some linear scale of your choosing (in my case it is easier to imagine centimeter ruler). So the ``distance between two positions A and B (where B > A) = B - A``.

- **Input**:
  - We will have input only with positive integers (so no need to check for incorrect input in this case).
  - The input will already be in ascending order - so no need to sort.
  - Input will consist of <=20 integers (meaning that there is no need for tight optimization), where max number <=10000 (which is inside integer range).

- **Output**:
  - All gears in the system must have radius >=1. Considering the fact that the last gear radius must be at least 1, and the first gear must be twice as big, we can say that first gear radius must be >=2. Also, if one of the gears in our gear train appears to have radius <1, it is automatically incorrect solution, even if first and last gears have correct radius.
  - You need to return the value of the first gear's radius in a form [a, b], where radius = a / b.
  
# Solution
We can divide our solution into 2 main parts:
1. Getting correct first gear radius
2. Turning double into fraction

### 1. Getting correct first gear radius
I must be completely honest, even though I wrote the code myself, I took the formula for the solution from another participant. You can check the whole explanation to the formula [here](https://pratickroy.medium.com/my-google-foobar-journey-6e46034b835f), but long story short:

```
r0 = x * (Sum(EvenIndexedDistances) — Sum(OddIndexedDistances))

where
n - number of pegs
r0 - radius of first gear
x = 2/3 if n is even
x = 2 if n is odd
```

<sub>When I was searching for the description of this challenge, I have also bumped into another formula [here](https://gist.github.com/jlacar/e66cd25bac47fab887bffbc5e924c2f5).</sub>

Once we have first gear radius, we can check if it is correct:
- if firstGearRadius < 2 -> return [-1, -1]
- we must check the rest of the gears if they are meeting the condition of >=1.

```
distance d = Peg1 - Peg0
r0 + r1 = d, then if gear train is correct:
d - r0 >= 1

d = Peg2 - Peg1
r1 + r2 = d, then if gear train is correct:
d - r1 >= 1

...
```

### 2. Turning double into fraction
And one more time, I have borrowed algorithm from the same [source](https://pratickroy.medium.com/my-google-foobar-journey-6e46034b835f).

The idea is that we are trying to find the closest approximate fraction with some tolerance.

```
For example, our double is 0.75.
We are choosing 2 whole numbers, between which we can fit our number.
In this case:
0/1 < 0.75 < 1/1

What we do next, is simply add numerators and denominators, so
0+1 / 1+1 = 1/2

We now compare this number to our double. Since 1/2 < 0.75, then we have:

1/2 < 0.75 < 1/1

Again, 1+1 / 2+1 = 2/3
It is one more time lower that our number, so

2/3 < 0.75 < 1/1

Again, 2+1 / 3+1 = 3/4

And we got our fraction: 0.75 = 3/4
```

When do we need tolerance? When we have more complex numbers like 3.142857, and we need to avoid getting fraction 3142857/1000000. 

Say our tolerance is 0.000001. Using the steps above, we get to the fraction 22/7.

22 / 7 = 3.14285714.

3.14285714 - 3.142857 = 0.00000014 < 0.000001, so we can stop there and say that our result is 22/7
