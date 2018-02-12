# 9-Puzzle Solver
To provide test inputs with standard input, run the program with

 `java NinePuzzle`

To terminate the input, use Ctrl-D (which signals EOF).

To read test inputs from a file (e.g. boards.txt), run the program with
 java NinePuzzle boards.txt

The input format for both input methods is the same. Input consists
of a series of 9-puzzle boards, with the '0' character representing the
empty square. For example, a sample board with the middle square empty is

 ```
 1 2 3
 4 0 5
 6 7 8
 ```

And a solved board is

 ```
 1 2 3
 4 5 6
 7 8 0
 ```

An input file can contain an unlimited number of boards; each will be
processed separately.
