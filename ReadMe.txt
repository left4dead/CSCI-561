Name: Arpit Bansal
EMail: arpitban@usc.edu



Compile and Execute:

--------------------

First compile using Compile Command: 		javac MinMax.java

Then Execute using Execute Command : 		java MinMax



Description:

------------

Classes:

========

1) MinMax.java: This class contains the main method which calls the MinMax algorithm basend on a boolean parameter which decides whether it is a pruning algorithm or not. The definition of the methods is given in Go.java
2) PlayerNode.java: This class contains the node structure for player.

3) ChosenGameState.java: This calss is used for backtracking to print the best strategy. This class contains a structure to store the best state so far by storing the player and it's child.

4) Go.java: This class defines several methods which are further used in implementation of MinMax algorithm. Further understanding of these methods can be found in the comments in the class.



Inputs:
=======
1) input.txt: This is an input file which represents the initial board configuration. The format for this file is <row index, column index, player>. The purpose for this file is to change the initial configuration just by updating the file rather than changing the code. Put this file in the same directory where the class files has been placed otherwise it could throw an exception trace.

High Level Data Structures:

===========================

PlayerNode[][] board- An array of arraylist of type "GraphNode" to represent the graph in form of adjacency list



Flow of the Program:

====================

->The main function creates the board by calling a constructor of Go.java and sets the pruning variable to true or false.

->Then the instance of Go.java class calls the method named GetMinMax to start the execution of algorithm.
->The algorithm is run two times in order to coompare the running time of MinMax algorithm and MinMax with Alpha Beta algorithm.
