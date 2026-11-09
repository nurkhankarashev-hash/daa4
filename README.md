# Assignment 4. Patient Zero

Student ID: 254828  
Name: Karashev Nurkhan  
Group: SE-2540

This project compares two DNA strains, searches for a marker in a genome, and finds which people should be tested in an infection tree. It is written in Java.

## Program output

Results for my student ID (254828):

```text
Mutations: 103
Shared core: 1177
Marker hits: 6
First hit: 1087
Border length: 4
Shared words: 422
Tests needed: 27
Cheapest testing: 126
Longest chain: 28
```

Lab console screenshot:

![Lab console](output/outbreak.png)

## Questions

### Mission 1 — Mutations

`d[i][j]` is the minimum number of changes needed to turn the first `i` letters of A into the first `j` letters of B. For each cell, I check three choices: delete a letter, insert a letter, or replace a letter. I take the smallest result. If the last letters are the same, the diagonal step adds 0 instead of 1.

### Mission 2 — Shared core

A subsequence keeps the order of letters, but the letters do not have to be next to each other. A substring is a continuous part of a string. That is why the longest common subsequence can be longer than the longest common substring.

### Mission 3 — The marker

The prefix function tells us the length of the longest prefix that is also a suffix for each part of the marker. KMP uses this information after a mismatch instead of moving the text pointer backwards.

My marker is `AAGTCGAAGT`. Its border length is 4 because `AAGT` is both the beginning and the end of the marker. KMP found 6 occurrences in my genome, and the first starts at position 1087.

### Mission 4 — Shared words

Rabin-Karp calculates a hash for each 12-letter window. With a rolling hash, the next hash is calculated from the previous one without reading the whole window again. Two different words can have the same hash, so I still compare the actual letters when hashes match. If there are many hash collisions, the algorithm becomes slower because it makes more comparisons.

### Mission 5 — Whom to test

For each person, the tree DP stores two costs: the minimum total cost if this person is tested, and the minimum total cost if this person is not tested. If a person is tested, a child can be tested or not tested. If a person is not tested, every child must be tested to cover the links.

After calculating the costs, the program goes through the tree again to choose the people to test. A greedy choice does not always work because a person with many links can have a high testing cost. The longest chain is found by keeping the two longest paths down from each person.

## AI use

I used ChatGPT to help write and check the Java methods and to explain the algorithms and README questions. I reviewed the results with the provided examples and my own input data. I did not use external code sources.

## Timing (Mission 7)

These are the times from `java -cp out Bench` on my computer. The numbers are in milliseconds and are averages of 3 runs.

| Algorithm | Input size | Time (ms) |
|---|---:|---:|
| Naive search | 20,000 | 147.8 |
| Naive search | 40,000 | 234.8 |
| Naive search | 80,000 | 501.9 |
| KMP | 20,000 | 7.6 |
| KMP | 40,000 | 3.6 |
| KMP | 80,000 | 1.3 |
| Edit distance | 500 | 18.2 |
| Edit distance | 1,000 | 25.5 |
| Edit distance | 2,000 | 53.4 |
| Edit distance | 4,000 | 228.7 |

Naive search gets slower as the text becomes longer. KMP is much faster because it does not go backwards in the text. Its measured times can change because Java warms up and optimizes the program. Edit distance becomes more expensive when both strings grow, because it fills a table with a cell for every pair of positions.

## Complexity

Here `n` and `m` are the lengths of the input strings (or text and marker), `k` is the word length (12 in this assignment), `h` is the number of marker hits, `c` is the number of candidate word comparisons, and `v` is the number of people.

| Algorithm | Time | Space |
|---|---|---|
| Edit distance | O(n * m) | O(n * m) |
| Longest common subsequence | O(n * m) | O(n * m) |
| Prefix function and KMP | O(n + m) | O(m + h) |
| Rabin-Karp shared words | O(n + m + c * k), worst O(n * m * k) | O(m) |
| Tree DP and longest chain | O(v) | O(v) |

Edit distance and LCS fill a two-dimensional table. KMP reads the text once and stores the prefix function and matching positions. Rabin-Karp stores hashes of windows from B and checks matching words. Tree DP visits each person and infection link a constant number of times.

## How to run

From the project folder:

```bash
java tools/MakeVariant.java 254828 data
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
java tools/Check.java
java -cp out Bench
```

Open `output/outbreak.html` in a browser and save a screenshot as `output/outbreak.png`.

The `data` folder should contain `strains.txt`, `genome.txt`, `markers.txt`, and `tree.txt` generated using my student ID.
