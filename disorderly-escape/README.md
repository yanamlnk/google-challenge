# Description

Oh no! You've managed to free the bunny workers and escape Commander Lambdas exploding space station, but Lambda's team of elite starfighters has flanked your ship. If you dont jump to hyperspace, and fast, youll be shot out of the sky!

Problem is, to avoid detection by galactic law enforcement, Commander Lambda planted the space station in the middle of a quasar quantum flux field. In order to make the jump to hyperspace, you need to know the configuration of celestial bodies in the quadrant you plan to jump through. In order to do *that*, you need to figure out how many configurations each quadrant could possibly have, so that you can pick the optimal quadrant through which youll make your jump. 

There's something important to note about quasar quantum flux fields' configurations: when drawn on a star grid, configurations are considered equivalent by grouping rather than by order. That is, for a given set of configurations, if you exchange the position of any two columns or any two rows some number of times, youll find that all of those configurations are equivalent in that way -- in grouping, rather than order.

Write a function `solution(w, h, s)` that takes 3 integers and returns the number of unique, non-equivalent configurations that can be found on a star grid `w` blocks wide and `h` blocks tall where each celestial body has `s` possible states. Equivalency is defined as above: any two star grids with each celestial body in the same state where the actual order of the rows and columns do not matter (and can thus be freely swapped around). Star grid standardization means that the width and height of the grid will always be between 1 and 12, inclusive. And while there are a variety of celestial bodies in each grid, the number of states of those bodies is between 2 and 20, inclusive. The solution can be over 20 digits long, so return it as a decimal string.  The intermediate values can also be large, so you will likely need to use at least 64-bit integers.

For example, consider `w=2`, `h=2`, `s=2`. We have a 2x2 grid where each celestial body is either in state `0` (for instance, silent) or state `1` (for instance, noisy).  We can examine which grids are equivalent by swapping rows and columns.

```
00
00
```

In the above configuration, all celestial bodies are "silent" - that is, they have a state of 0 - so any swap of row or column would keep it in the same state.
```
00   00   01   10
01   10   00   00
```
1 celestial body is emitting noise - that is, has a state of 1 - so swapping rows and columns can put it in any of the 4 positions.  All four of the above configurations are equivalent.

```
00   11
11   00
```

2 celestial bodies are emitting noise side-by-side.  Swapping columns leaves them unchanged, and swapping rows simply moves them between the top and bottom.  In both, the *groupings* are the same: one row with two bodies in state 0, one row with two bodies in state 1, and two columns with one of each state.

```
01   10
01   10
```

2 noisy celestial bodies adjacent vertically. This is symmetric to the side-by-side case, but it is different because there's no way to transpose the grid.

```
01   10
10   01
```

2 noisy celestial bodies diagonally.  Both have 2 rows and 2 columns that have one of each state, so they are equivalent to each other.

```
01   10   11   11
11   11   01   10
```

3 noisy celestial bodies, similar to the case where only one of four is noisy.

```
11
11
```

4 noisy celestial bodies.

There are **7** distinct, non-equivalent grids in total, so `solution(2, 2, 2)` would return `7`.

## Test cases

```
Input:
Solution.solution(2, 3, 4)
Output: 430

Input:
Solution.solution(2, 2, 2)
Output: 7
```

# Explanation
We need to return a number of **unique** combinations that can be formed on `W x H` grid using `S` states. Unique is a key word here.
```
(1)   (2)  (3)

01    00   00
00    01   10
```
There 3 examples are not considered unique.
- If we change rows in the first grid, we will get the second grid.
- If we change columns in the second grid, we will get the third.
- If we swap columns and rows in the first grid, we will get the third.

Now when we understand the task, let move to the solution.
This problem requires math, but fortunately there is a formula for that.
Before digging in, I recommend to quickly read about [Group Theory](https://brilliant.org/wiki/group-theory-introduction/) to better understand the subject, or you can immediately go to [Burnside's lemma](https://brilliant.org/wiki/burnsides-lemma/) reading as it is what we are going to use.

This explanation is based on [this](https://medium.com/@chris.bell_/google-foobar-as-a-non-developer-level-5-a3acbf3d962b) article. 

Our formula that we will use:

<img width="172" alt="image" src="https://user-images.githubusercontent.com/90959658/202734126-f771f85d-ae06-4011-84a7-a5796af7d587.png">

My math is a bit rusty (and even if I've read articles on group theory, my knowledge is still very superficial), so I will go very straightforward with my explanations.

1) <img width="69" alt="image" src="https://user-images.githubusercontent.com/90959658/202742183-c5abe997-e9ea-4c3b-b48d-79bc415746de.png"> is what we need to find, it is our result.

2) <img width="40" alt="image" src="https://user-images.githubusercontent.com/90959658/202742410-7e92527c-5602-4d85-b5fe-307a85d39ce3.png"> means that basically we need to divide the rest of the formula by <img width="31" alt="image" src="https://user-images.githubusercontent.com/90959658/202742564-c78ab5e2-060d-47d7-8603-1820928509fb.png">.

<img width="120" alt="image" src="https://user-images.githubusercontent.com/90959658/202742810-243cc74a-c575-467b-84c7-440b9efa0f00.png">

3) Before going to the next part of the formula (<img width="82" alt="image" src="https://user-images.githubusercontent.com/90959658/202747257-2bfdbaf8-c307-4b01-8d42-fde49fe766f9.png">), we need to get [integer partitions](https://www.whitman.edu/mathematics/cgt_online/book/section03.03.html) for `w` and `h`. 

If `w` = 2, then its partitions `Yw` are:
```
2
1  1
```
If `h` = 5, then `Yh`:
```
5
4  1
3  2
3  1  1
2  2  1
2  1  1  1
1  1  1  1  1
```

4) Now we can elaborate on the remaining part of the formula:

<img width="411" alt="image" src="https://user-images.githubusercontent.com/90959658/202746649-cd005865-76b0-467e-bc18-564d2ad07836.png">

- `y1` is partition from `Yh` as per partitions above, `y2` is partition from set `Yw`.
- `Cy1` and `Cy2` are conjugacy class sizes for each particular partition.
- `i` is particular number from partition `y1`, similarly `j` is number from partition `y2`.

So, if we have `h` = 3, then 
```
Yh = [ {3}
       {2 1}
       {1 1 1} ]
```
Then `y1` can be for example
```
{2 1}
```
Then `i` can be `2`.

Speaking of **conjugacy size**. For example, we have number `23` with partition `{3  3  3  3  2  2  2  1  1  1  1  1}`. So we have four `3s`, three `2s` and five `1s`. Then conjugacy size of this partition is:

<img width="257" alt="image" src="https://user-images.githubusercontent.com/90959658/202760272-2e5506ea-f014-45fd-ba89-c880fe6f665d.png">

So now when we have all elements for our formula, we can move to actual code.

_P.S. <img width="38" alt="image" src="https://user-images.githubusercontent.com/90959658/202751900-a2f1fe0d-198d-47e3-b619-ea4139e5d098.png"> is a sum, simply speaking, we will need to iterate through all `y1` in `Yh`, perform on each `y1` formula mentioned on the right side of <img width="34" alt="image" src="https://user-images.githubusercontent.com/90959658/202752266-cfc7391f-a163-48da-b839-7afb79257f80.png">, and add up each result of the formula. When we have two <img width="34" alt="image" src="https://user-images.githubusercontent.com/90959658/202752660-89350b09-e360-4a61-aa5e-683efddc838c.png">, it simply means we need two loops :)._

# Solution
1) Start with writing functional methods that you will need for your code:
- [method](https://www.baeldung.com/java-calculate-factorial) that takes double and returns factorial of this double.
- method that takes BigInteger and returns factorical of this BigInteger.
- [method](https://www.baeldung.com/java-greatest-common-divisor) that takes 2 integers and returns their GCD.
- there is already built-in [method](https://www.tutorialspoint.com/java/math/biginteger_gcd.htm) for GCD of 2 BigIntegers.

2) Calculate `G` as per step 2 in Explanation.

3) Write method for integer partition. I used [this](https://www.geeksforgeeks.org/generate-unique-partitions-of-an-integer/) code. Instead of printing values, I saved each array as the object of static class `Partition`, which takes ArrayList `partition` and int `originalNumber` to constructor. Inside constructor there is also method `setConjugacySize` which takes ArrayList `partition`, int `originalNumber` and returns conjugacy size value as per step 4 in explanation. All `Partition` objects I save to one ArrayList. 

4) Make partitions for `w` and `h`.

5) Write method to count power of `s`. It my code it takes 2 objects of `Partition` class (`one` and `two`), iterate through their ArrayLists `partition` and sum GCD of numbers from 2 lists. 

```
power(Partition one, Partition two)
    sum = 0
    
    // partition -> ArrayList field in Partition class
    array1 = one.partition
    array2 = two.partition
    
    for a in array1
        for b in array2
            sum += gcd(a, b)
```

6) Iterate through 2 loops: `partitionsOfW` and `partitionsOfH`. 

```
X = 0;
         
for i in partitionsOfH
    for j in partitionsOfW
        C = i.conjugacySize * j.conjugacySize
        S = s^power(i, j)
        X += (C * S)        
```

7) Return the result of `X / G`. Don't forget `toString()`
