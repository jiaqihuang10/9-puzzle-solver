/* NinePuzzle.java
   CSC 225 - Spring 2017

   Student Name: Jiaqi Huang (V00862966)
   Date: April 02, 2017

*/

import java.util.Scanner;
import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Arrays;
import java.util.Stack;

public class NinePuzzle{
	//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
	public static final int NUM_BOARDS = 362880;
	public static final LinkedList[] G = construct_graph();
	public static boolean[] visited = new boolean[NUM_BOARDS];
	public static int[] prev_visited = new int[NUM_BOARDS];//to save the vertex visited right before certain vertex using their index

	public static LinkedList[] construct_graph(){
		LinkedList[] G = new LinkedList[NUM_BOARDS];

		for(int k = 0; k < NUM_BOARDS; k++) {
			//get the board for index i
			int[][]B = getBoardFromIndex(k);
			LinkedList<Integer> list = new LinkedList<Integer>();

			//get the index of 0
			int i = 0;
			int j = 0;


			for(int a = 0; a < 3; a++) {
				for (int b = 0; b < 3;b++) {
					if (B[a][b] == 0) {
						i = a;
						j = b;
						break;
					}
				}
			}
			//get the adjacent vertices
			// use indicator  from 0 to 3 to indicate which tile to move
			//System.out.println("i: " + i + " j: " + j);

			if (i > 0) {
				// get the board created by moving tile[i-1,j] down one row
				int[][] B1 = getAdjBoard(B,i,j,0);
				int index = getIndexFromBoard(B1);
				// add the vertex to G
				list.addLast(index);
			}

			if (i < 2) {
				// get the board created by moving tile [i+1, j]
				int[][] B2 = getAdjBoard(B,i,j,1);
				int index = getIndexFromBoard(B2);
				// add the vertex to G
				list.addLast(index);
			}
			if (j > 0) {
				//get the board created by moving tile [i,j-1] to the right
				int[][] B3 = getAdjBoard(B,i,j,2);
				int index = getIndexFromBoard(B3);
				// add the vertex to G
				list.addLast(index);
			}
			if (j < 2) {
				// get the board created by moving tile [i,j+1] to the left
				int[][] B4 = getAdjBoard(B,i,j,3);
				int index = getIndexFromBoard(B4);
				// add the vertex to G
				list.addLast(index);
			}
			G[k] = list;

			//printList(G[k],k);
		}
		return G;
	}

	public static int[][] getAdjBoard(int[][] B, int i, int j, int dir) {
		int[][] adj_B = new int[3][3];
		//copy B to adj_B
		for (int a = 0; a < 3;a++) {
			for (int b = 0; b < 3;b++) {
				adj_B[a][b] = B [a][b];
			}
		}

		if (dir == 0) {
			// dir = 0, move [i-1,j] down one row
			adj_B[i][j] = adj_B[i-1][j];
			adj_B[i-1][j] = 0;

		} else if (dir == 1) {
		// dir = 1, move [i+1,j] up one row
			adj_B[i][j] = adj_B[i+1][j];
			adj_B[i+1][j] = 0;
		} else if (dir == 2) {
		// dir = 2, move [i,j-1] to the right
			adj_B[i][j] = adj_B[i][j-1];
			adj_B[i][j-1] = 0;
		} else if (dir == 3) {
		// dir = 3, move [i,j+1] to the left
			adj_B[i][j] = adj_B[i][j+1];
			adj_B[i][j+1] = 0;
		} else {
			return adj_B;
		}

		return adj_B;
	}

	public static boolean BFS (int vertex, int goal) {
		//initialize the visited & prev_visited array
		Arrays.fill(visited,false);
		Arrays.fill(prev_visited,-1);

		//queue is an interface in java
		LinkedList<Integer> q = new LinkedList<Integer>();
		q.add(vertex);
		visited[vertex] = true;

		while(!q.isEmpty()) {
			//dequeue
			int v = q.removeFirst();
			//add adj configs(w) into queue,mark v as their prev visited vertex
			ListIterator<Integer> iter = G[v].listIterator();
			//printList(G[v]);
			while(iter.hasNext()) {
				int w = iter.next();
				if (!visited[w]) {
					visited[w] = true;
					prev_visited[w] = v;

					if (w == goal){
						return true;
					}
					// w enqueue
					q.add(w);
				}
			}
		}
		return false;
	}

	public static void printPrev () {
		for (int i = 0; i < NUM_BOARDS;i++) {
			if (prev_visited[i] != -1)
				System.out.println("vertex:	" + i + "	parent:	"+ prev_visited[i]);
		}
	}

	public static void printList(LinkedList<Integer> list, int v) {
		System.out.println("vertex: " +v);
		ListIterator<Integer> iter = list.listIterator();
		while(iter.hasNext()) {
			int w = iter.next();
			System.out.print(w + " -> ");
		}
		System.out.println();
	}

	public static void printShortestPath(int start, int goal){
		//use a stack to save the path from goal to start
		Stack<Integer> shortest_path = new Stack<Integer>();

		//push the vertices from goal to start onto the stack
		shortest_path.push(goal);
		int prev = prev_visited[goal];

		while(prev != start) {
			shortest_path.push(prev);
			prev = prev_visited[prev];
		}
		shortest_path.push(start);

		//print the shortest path by popping vertices from the stack
		while(!shortest_path.isEmpty()) {
			int v = shortest_path.pop();
			int[][] B = getBoardFromIndex(v);
			printBoard(B);
		}
	}
	/*  SolveNinePuzzle(B)
		Given a valid 9-puzzle board (with the empty space represented by the
		value 0),return true if the board is solvable and false otherwise.
		If the board is solvable, a sequence of moves which solves the board
		will be printed, using the printBoard function below.
	*/
	public static boolean SolveNinePuzzle(int[][] B){

		/* ... Your code here ... */
		int vertex = getIndexFromBoard(B);
		int[][] goal_B = {{1,2,3},{4,5,6},{7,8,0}};
		int goal = getIndexFromBoard(goal_B);

		if (BFS(vertex,goal)) {
			printShortestPath(vertex,goal);
			return true;
		}

		return false;
	}

	/*  printBoard(B)
		Print the given 9-puzzle board. The SolveNinePuzzle method above should
		use this method when printing the sequence of moves which solves the input
		board. If any other method is used (e.g. printing the board manually), the
		submission may lose marks.
	*/
	public static void printBoard(int[][] B){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				System.out.printf("%d ",B[i][j]);
			System.out.println();
		}
		System.out.println();
	}

	/* Board/Index conversion functions
	   These should be treated as black boxes (i.e. don't modify them, don't worry about
	   understanding them). The conversion scheme used here is adapted from
		 W. Myrvold and F. Ruskey, Ranking and Unranking Permutations in Linear Time,
		 Information Processing Letters, 79 (2001) 281-284.
	*/
	public static int getIndexFromBoard(int[][] B){
		int i,j,tmp,s,n;
		int[] P = new int[9];
		int[] PI = new int[9];
		for (i = 0; i < 9; i++){
			P[i] = B[i/3][i%3];
			PI[P[i]] = i;
		}
		int id = 0;
		int multiplier = 1;
		for(n = 9; n > 1; n--){
			s = P[n-1];
			P[n-1] = P[PI[n-1]];
			P[PI[n-1]] = s;

			tmp = PI[s];
			PI[s] = PI[n-1];
			PI[n-1] = tmp;
			id += multiplier*s;
			multiplier *= n;
		}
		return id;
	}

	public static int[][] getBoardFromIndex(int id){
		int[] P = new int[9];
		int i,n,tmp;
		for (i = 0; i < 9; i++)
			P[i] = i;
		for (n = 9; n > 0; n--){
			tmp = P[n-1];
			P[n-1] = P[id%n];
			P[id%n] = tmp;
			id /= n;
		}
		int[][] B = new int[3][3];
		for(i = 0; i < 9; i++)
			B[i/3][i%3] = P[i];
		return B;
	}



	public static void main(String[] args){
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */


		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}

		int graphNum = 0;
		double totalTimeSeconds = 0;

		//Read boards until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading board %d\n",graphNum);
			int[][] B = new int[3][3];
			int valuesRead = 0;
			for (int i = 0; i < 3 && s.hasNextInt(); i++){
				for (int j = 0; j < 3 && s.hasNextInt(); j++){
					B[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < 9){
				System.out.printf("Board %d contains too few values.\n",graphNum);
				break;
			}
			System.out.printf("Attempting to solve board %d...\n",graphNum);
			long startTime = System.currentTimeMillis();
			boolean isSolvable = SolveNinePuzzle(B);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;

			if (isSolvable)
				System.out.printf("Board %d: Solvable.\n",graphNum);
			else
				System.out.printf("Board %d: Not solvable.\n",graphNum);
		}
		graphNum--;
		System.out.printf("Processed %d board%s.\n Average Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>1)?totalTimeSeconds/graphNum:0);

	}


}
