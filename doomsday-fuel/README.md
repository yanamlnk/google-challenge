# Description
Making fuel for the LAMBCHOP's reactor core is a tricky process because of the exotic matter involved. It starts as raw ore, then during processing, begins randomly changing between forms, eventually reaching a stable form. There may be multiple stable forms that a sample could ultimately reach, not all of which are useful as fuel.

Commander Lambda has tasked you to help the scientists increase fuel creation efficiency by predicting the end state of a given ore sample. You have carefully studied the different structures that the ore can take and which transitions it undergoes. It appears that, while random, the probability of each structure transforming is fixed. That is, each time the ore is in 1 state, it has the same probabilities of entering the next state (which might be the same state). You have recorded the observed transitions in a matrix. The others in the lab have hypothesized more exotic forms that the ore can become, but you haven't seen all of them.

Write a function ``solution(m)`` that takes an array of array of nonnegative ints representing how many times that state has gone to the next state and return an array of ints for each terminal state giving the exact probabilities of each terminal state, represented as the numerator for each state, then the denominator for all of them at the end and in simplest form. The matrix is at most 10 by 10. It is guaranteed that no matter which state the ore is in, there is a path from that state to a terminal state. That is, the processing will always eventually end in a stable state. The ore starts in state 0. The denominator will fit within a signed 32-bit integer during the calculation, as long as the fraction is simplified regularly.

For example, consider the matrix ``m``:
```
[
  [0,1,0,0,0,1],  # s0, the initial state, goes to s1 and s5 with equal probability
  [4,0,0,3,2,0],  # s1 can become s0, s3, or s4, but with different probabilities
  [0,0,0,0,0,0],  # s2 is terminal, and unreachable (never observed in practice)
  [0,0,0,0,0,0],  # s3 is terminal
  [0,0,0,0,0,0],  # s4 is terminal
  [0,0,0,0,0,0],  # s5 is terminal
]
```
So, we can consider different paths to terminal states, such as:
```
s0 -> s1 -> s3
s0 -> s1 -> s0 -> s1 -> s0 -> s1 -> s4
s0 -> s1 -> s0 -> s5
```
Tracing the probabilities of each, we find that:
```
s2 has probability 0
s3 has probability 3/14
s4 has probability 1/7
s5 has probability 9/14
```
So, putting that together, and making a common denominator, gives an answer in the form of ``[s2.numerator, s3.numerator, s4.numerator, s5.numerator, denominator]`` which is: [0, 3, 2, 9, 14].

### Test cases
```
Input:
Solution.solution({{0, 2, 1, 0, 0}, {0, 0, 0, 3, 4}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}})
Output:
{7, 6, 8, 21}

Input:
Solution.solution({{0, 1, 0, 0, 0, 1}, {4, 0, 0, 3, 2, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}})
Output:
{0, 3, 2, 9, 14}
```
# Explanation

### Understanding input

We have the following matrix as the input:
```
0, 2, 1, 0, 0
0, 0, 0, 3, 4
0, 0, 0, 0, 0 
0, 0, 0, 0, 0
0, 0, 0, 0, 0
```
Before we start solving the task, we need to understand the input. 
Each row is a state. Each column shows how many times we move from that state to the other. 

We can rewrite the input as below to make it easier for us:

```
    S0 S1 S2 S3 S4
S0  0, 2, 1, 0, 0
S1  0, 0, 0, 3, 4
S2  0, 0, 0, 0, 0 
S3  0, 0, 0, 0, 0
S4  0, 0, 0, 0, 0
```

So when we on state S0, 2 out of 3 times ore transforms to S1. 1 out of 3 times if transforms to S2 state.

```
    S0 S1 S2 S3 S4
S0  0, 2, 1, 0, 0

```

So there is 2/3 chance of getting S1 out of S0. There is 1/3 chance of getting to the terminal state S2.
In terms of probabilities:
```
    S0  S1   S2  S3 S4
S0  0, 2/3, 1/3, 0, 0

```

Say we got S1 ore. Now we have different picture:

```
    S0 S1 S2 S3 S4
S1  0, 0, 0, 3, 4
```
From S1 there is 3/7 chance of gettin S3, and 4/7 chance of getting S4.
```
    S0 S1 S2  S3  S4
S1  0, 0, 0, 3/7, 4/7
```

In out input we have 3 terminal states, S2, S3, S4. How do we know that?
```
    S0 S1 S2 S3 S4
S2  0, 0, 0, 0, 0 
S3  0, 0, 0, 0, 0
S4  0, 0, 0, 0, 0
```
As you see, S2, S3 and S4 does not trasnform to any other state, for example, 0 out 0 times state S2 tranform to any other state.

We can also write it down this way:
```
    S0 S1 S2 S3 S4
S2  0, 0, 1, 0, 0
S3  0, 0, 0, 1, 0
S4  0, 0, 0, 0, 1
```
So, there is 1/1 chance that state S2 transform to state S2. In other words, it stays the same.
We can now transform our matrix so that it will show probabilities:

```
    S0  S1  S2    S3   S4
S0  0, 2/3, 1/3,  0,   0
S1  0,  0,   0,  3/7, 4/7
S2  0,  0,   1,   0,   0
S3  0,  0,   0,   1,   0
S4  0,  0,   0,   0,   1 
```
### Understanding output

We need to return array of probabilities of getting the terminal state. In this case, since there is 3 terminal states, we need to return array ``{S2.numerator, S3.numerator, S4.numerator, common.denominator}``

### Getting result

Let's come back to our example input:
```
    S0  S1  S2    S3   S4
S0  0, 2/3, 1/3,  0,   0
S1  0,  0,   0,  3/7, 4/7
S2  0,  0,   1,   0,   0
S3  0,  0,   0,   1,   0
S4  0,  0,   0,   0,   1 
```

We can get S2 only from S0. So probability of getting S2 is 1/3
To get S3, we first need to get S1. Probability of S1 is 2/3, probability of getting S3 from S1 is 3/7, so total probability of getting S3 is 2/3 * 3/7 = 2*3 / 3*7 = 6/21
Same goes for S4, 2/3 * 4/7 = 8/21

So, P(S2) = 1/3, P(S3) = 6/21, P(S4) = 8/21.
Common denominator will be 21, so we should transform P(S2) = 1/3 = 1 * (21/3) / 21 = 7/21
Our result = {7, 6, 8, 21}

But that is pretty ideal set up here. Lets say we have input as below:
```
    S0 S1 S2 S3 S4
S0  0, 2, 1, 0, 0
S1  1, 0, 1, 3, 4
S2  0, 0, 0, 0, 0 
S3  0, 0, 0, 0, 0
S4  0, 0, 0, 0, 0
```
We got changes here. Now 1/9 times S1 will transform back to S0. And S2 can now transform from S0 and from S1.

So now we can have different scenarios of getting S2:
S0 -> S2
S0 -> S1 -> S2
S0 -> S1 -> S0 -> S2
S0 -> S1 -> S0 -> S1 -> S2

We can't simply multiply everything, because then probability will be wrong.
We need different approach, and it is **Absorbing Markov Chains Solution**.

### Absorbing Markov Chains Solution
You can read great explanation of what it is along with examples [here](https://math.libretexts.org/Bookshelves/Applied_Mathematics/Applied_Finite_Mathematics_(Sekhon_and_Bloom)/10%3A_Markov_Chains/10.04%3A_Absorbing_Markov_Chains). 
- worth noting that there is no need to check whether our matrix is Absorbing Markov Chain. As per task description, we will always get to terminal (=absorbing) state from non-terminal (non-absorbing) state, and we will always have at least one terminal state. 

Here I will just focus on the formula and how to apply it in our case.
In short, you can solve this using below formula:

```
         | Q | R |
     P = |-------|
         | O | I |
```
```

    |  R * (I - Q)^-1  |
    |------------------|
    |         I        |
```

Now then, let's solve out rest input step by step:
```
0, 1, 0, 0, 0, 1
4, 0, 0, 3, 2, 0
0, 0, 0, 0, 0, 0
0, 0, 0, 0, 0, 0
0, 0, 0, 0, 0, 0 
0, 0, 0, 0, 0, 0
```

1. Turn this matrix into probability matrix:
```
 0,  1/2, 0,  0,   0, 1/2
4/9,  0,  0, 3/9, 2/9, 0
 0,   0,  1,  0,   0,  0
 0,   0,  0,  1,   0,  0
 0,   0,  0,  0,   1,  0  
 0,   0,  0,  0,   0,  1
```

2. Divide this matrix into submatrices (different matrices explained [in this video](https://www.youtube.com/watch?v=bj_O4edCwgc&list=PLX2gX-ftPVXWgcF0WATMDr-AfvfaYjJZ3&index=25&ab_channel=MichelvanBiezen)). But shortly speaking how to do it:
- put lines between last non-terminal state and first terminal state. In out case it is horizontal line between rows 1 and 2, and vertical line between columns 1 and 2:
```
 0,  1/2, |  0,  0,   0, 1/2
4/9,  0,  |  0, 3/9, 2/9, 0
----------------------------
 0,   0,  |  1,  0,   0,  0              | Q | R |
 0,   0,  |  0,  1,   0,  0		 |-------|
 0,   0,  |  0,  0,   1,  0		 | O | I |
 0,   0,  |  0,  0,   0,  1
```
I - matrix for terminal states only
Q - matrix with probabilities of turning from non-terminal to non-terminal
R - matrix with probabilities of turning from non-terminal to terminal

3. Now let's solve out matrix using below:
To understand better you can check [this video](https://www.youtube.com/watch?v=72Ipee3ueUs&list=PLX2gX-ftPVXWgcF0WATMDr-AfvfaYjJZ3&index=26&ab_channel=MichelvanBiezen)
```
    |  R * (I - Q)^-1  |
    |------------------|
    |         I        |
```

- **I - Q**. To subtract matrices, we need to subtract each of the element of the two matrices. Read more [here](https://www.mathstips.com/matrix-subtraction/). Trick is, you can't subtract matrices of 2 different sizes, so the best option here would be to resize matrix ``I`` since it will not cause any loss of information.
We can either make it bigger or smaller in the following way:

```
Input    Bigger   Smaller

1 0 0    1 0 0 0    1 0 
0 1 0    0 1 0 0    0 1
0 0 1    0 0 1 0
         0 0 0 1
```

Back to our task:
```
|1 0 0 0|
|0 1 0 0|  - |  0  1/2| = | 1 0| - | 0  1/2| = | 1-0   0-1/2| = | 1  -1/2|
|0 0 1 0|    | 4/9  0 |   | 0 1|   |4/9  0 |   |0-4/9   1-0 |   |-4/9  1 |
|0 0 0 1|
```
- **inverse the result of subtraction. So now we need to raise our new matrix to the power -1. How to do it for 3X3 matrix to can check [here](https://www.mathsisfun.com/algebra/matrix-inverse-minors-cofactors-adjugate.html)(the process is simpler for 2x2 matrix)

After inversion we got result:
```
| 9/7  9/14|
| 4/7  9/7 |
```
- **Multiply by R**. You can check how to do it [here](https://www.cuemath.com/algebra/multiplication-of-matrices/)

```
|0  0   0  1/2|  * |9/7  9/14| = |0  3/14  1/7  9/14|
|0 3/9 2/9  0 |    |4/7  9/7 |   |0  3/7   2/7   2/7|
```

How to read this?
Rows are **from**, colums are **to**.
```
     S2  S3    S4   S5
S0  |0  3/14  1/7  9/14|
S1  |0  3/7   2/7   2/7|
```

We are interested only in probabilities of S0 state (i.e. the probability of getting terminal state FROM initial state S0.)

So result is 
```
     S2  S3    S4   S5
S0   0  3/14  1/7  9/14

{0, 3, 2, 9, 14}
```

### Turn matrix into correct form
One more thing, our example was already in "perfect" form, but lets say we have the following input:

```
    S0 S1 S2 S3
S0  0, 1, 3, 0
S1  0, 0, 0, 0
S2  1, 0, 0, 1
S3  0, 0, 0, 0
```
Terminal states here are S1 and S3. We will not be able to divide this matrix into 4 matrices immediately. So we well need to rearrange our matrix to the correct form as in
```
| Q | R |
|-------|
| O | I |
```
You can check [this video](https://www.youtube.com/watch?v=UuZU3LUBalQ&list=PLX2gX-ftPVXWgcF0WATMDr-AfvfaYjJZ3&index=24&ab_channel=MichelvanBiezen) to undertand how to rearrange this matrix correctly.

Final result will be
```
   S0 S2 S1 S3
S0 0  3  1  0
S2 1  0  0  1
S1 0  0  0  0
S3 0  0  0  0
```
# Solution
1. Rearrange  matrix so it will have correct form.
- create ArrayList and add numbers of terminal states (where sumOfTheRow == 0)
- create new ArrayList for correct order sample. 1) Add all numbers 2) remove from the list all terminal number from previous step 3) add those terminal numbers to the end of this list.
- create SampleMatrix with size of ``input matrix + 1``. 
``SampleMatrix[0][0] = -1``
``SampleMatrix[0][i] = correctOrderArray[i-1]``
``SampleMatrix[i][0] = correctOrderArray[i-1]`` 
- iterate through SampleMatrix starting from 1 (not 0) and do the following:
```
SampleMatrix[i][k] = inputMatrix[SampleMatrix[i][0]][SampleMatrix[0][k]];
```
We refer to the input matrix using indexes we take from 0 row and column of sample matrix (which we filled according to the correct order)
- create new correct matrix with size ``sample matrix - 1`` and copy everything from sample matrix except 0 row and column


2. Turn input into new double probability matrix. 
- count sum of all numbers in the row
- in each cell of the row -> get fraction of ``cell[row][column]`` / ``sumOfTheRow``
- if sum of the row is 0, it means state is terminal. Change ``cell[row][row]`` to ``1``.

3. Divide matrix to submatrices
- get index of the first terminal state.
- matrix Q -> copy of input matrix up until ``row[terminal index-1]`` and ``column[terminal index - 1]``
- matrix R -> copy of input matrix until ``row[terminal index - 1]`` from column[terminal index]
- matrix I -> create the matrix of same size as Q, fill it with ``0``, and change cell[i][i] to ``1``. 
4. Get solution matrix based on formula above. For this I have created separate class with all math methods (for subtraction, multiplication and inversion).
5. Now we are interested only in ``0`` row of our solution matrix.
- create array ``result`` with ``solutionMatrix size + 1``
- create array ``temp``
- create array ``denominators`` with ``solutionMatrix size``
- iterate through 0 row of solution matrix. Turn each double into fraction (I have used same method as in task [Gearing Up For Destruction](https://github.com/yanamlnk/google-challenge/tree/main/gearing-up-for-destruction). Save results to temp array.
- ``result[i] = temp[0]``
- ``denominators[i] = temp[1]``
- after you process all solution matrix, get commonLCM for the ``denominators`` array and save the result to array ``result[result.length-1]``
- now raise all numerators to the common denominator:
``result[i] = (result[result.length-1] / denominators[i]) * result[i];``
