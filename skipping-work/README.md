# Description

Commander Lambda is all about efficiency, including using her the bunny workers for manual labor. But no one's been properly monitoring the labor shifts for a while and they've gotten quite mixed up. You've been given the task of fixing them, but after you wrote up new shifts you realized that some bunny workers had been transferred to a different area and aren't available for their assigned shifts. Manually sorting through each shift list to compare against work area lists will take forever -- remember, Commander Lambda loves efficiency!

Given two almost identical lists of worker IDs x and y where one of the lists contains an additional ID, write a function ``solution(x, y)`` that compares the lists and returns the additional ID.

For example, given the lists ``x`` = [13, 5, 6, 2, 5] and ``y`` = [5, 2, 5, 13], the function ``solution(x, y)`` would return 6 because the list ``x`` contains the integer 6 and the list ``y`` doesn't. Given the lists ``x`` = [14, 27, 1, 4, 2, 50, 3, 1] and ``y`` = [2, 4, -4, 3, 1, 1, 14, 27, 50], the function ``solution(x, y)`` would return -4 because the list ``y`` contains the integer -4 and the list ``x`` doesn't.

In each test case, the lists ``x`` and ``y`` will always contain ``n`` non-unique integers where ``n`` is at least 1 but never more than 99, and one of the lists will contain an additional unique integer which should be returned by the function. The same ``n`` non-unique integers will be present on both lists, but they might appear in a different order like in the examples above. Commander Lambda likes to keep the numbers short, so every worker ID will be between -1000 and 1000.

### Test cases
```
Input:

Solution.solution({13, 5, 6, 2, 5}, {5, 2, 5, 13})

Output:
6

Input:

Solution.solution({14, 27, 1, 4, 2, 50, 3, 1}, {2, 4, -4, 3, 1, 1, 14, 27, 50})

Output:
4
```

# Explanation
This task is pretty straightforward. You need to find and return extra number from 2 arrays.
Input will only consists of numbers in a range [-1000; 1000], so there is no need for BigInteger or long types.

# Solution
Unfortunately I have deleted the code I used to pass this test, but I do remember steps I have taken.
1. Sort both arrays in ascending order with ``Arrays.sort()``.
2. Using 2 for-loops, compare objects of both arrays:
```
if array1[i] == array2[i] -> i++

else -> return array1.length > array2.length ? array1[i] : array2[i]
```
Logic is simple - longer array is the array with extra element.
