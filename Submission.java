import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.lang.String.*;
import java.lang.Math.*;
import java.io.FileWriter;


/* Created by - Neelabh Agrawal
*
* Program to implement CNF_SAT problem using Backtrack Algorithm
*
* Input arguments - <input file name> <output file name>
*       Input file should be in following structure -
*       Line 1: n m sizeC// where n is the number of variables, and m the number of clauses
*       and sizeC the number of literals in the clauses
*       Line 2 to m+1: a list of sizeC positive and negative numbers in the range 1 to n.
*       If the number is negative the literal is negated.
*
* Ouput format - Output file with possible values of solutions
*/

class Submission {
	int n, m, sizeC, countTrue;
	int[][] clauses;
	int[] x, min, clauseValue,max;
	boolean resultFound = false, noMore = false, dontPrint = false;
	String outFile;
	FileWriter writer;
	
	Submission(String in, String out) {
		
		try {
			Scanner scan = new Scanner(new File(in));
			outFile = out;
			n = scan.nextInt();
			m = scan.nextInt();
			sizeC = scan.nextInt();

			/* create a two dimensional array clauses and read all the values from input file*/
			x = new int[n+1];
			clauses = new int[m][sizeC];

			for(int i = 0;i < m;i++) {
				for(int j=0;j<sizeC;j++) {
					clauses[i][j] = scan.nextInt();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		max = new int[m];
		min = new int[m];

		/*get maximum and minimum index of x variables for each clause*/
		getDepths();
		countTrue = 0;
		/*create new file with output file name entered by user*/
		try {
			writer = new FileWriter(outFile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	void backTrackCompute(int depth) {
		if(clauseValue(depth)==m) {
				/*Check if clause value is stastfied
				Mark resultFound as true*/
				resultFound = true;
				/*if number of variable are less than 5 print all results
				else print only 1 result if number of clauese are less than 30*/
				if(n<=5) {
					printResult(depth);
				} else {
					noMore = true;
					if( m<=30) {
						printResult(depth);
					} else {
						dontPrint = true;
					}
				}
			return;
		}
		/*recursion will stop once a solution is found in case number of variables are more than 5*/
		if(promising(depth) && !noMore) {
			x[depth]=1;
			backTrackCompute(depth+1);
			x[depth]=0;
			backTrackCompute(depth+1);
		}
	}



	boolean promising(int varCount) {
		for(int i=0;i<m;i++) {
			if(max[i]==varCount) {
				boolean clauseFalse = true;
				for(int j=0;j<sizeC;j++) {
					if(clauses[i][j]<0) {
						if(x[(0-clauses[i][j])-1]==0)
							clauseFalse = false;
					} else {
						if(x[clauses[i][j]-1]==1)
							clauseFalse = false;
					}
				}
				if(clauseFalse) {
					return false;
				}
			}
		}
		return true;
	}


	int clauseValue(int varCount) {
		countTrue = 0;
		for(int i=0;i<m;i++) {
			if(min[i]<=varCount) {
				for(int j=0;j<sizeC;j++) {
					int c = clauses[i][j];
					if(c<0 && (0-c)<=varCount) {
						if(x[(0-c)-1]==0) {
							countTrue++;
							break;
						}
					} else if(c>0 && c<=varCount) {
						if(x[c-1]==1) {
							countTrue++;
							break;
						}
					}
				}
			}
		}
		return countTrue;
	}


	void getDepths() { 
		for(int i=0;i<m;i++) {
			max[i] = 0;
			min[i] = n;
			for(int j=0;j<sizeC;j++) {
				if(clauses[i][j] < 0) {
					if(min[i] > (0-clauses[i][j])) 
					min[i] = (0-clauses[i][j]);
					if(max[i] < (0-clauses[i][j]))
					max[i] = (0-clauses[i][j]);
				} else {
					if(min[i] > clauses[i][j])
					min[i] = clauses[i][j];
					if(max[i] < clauses[i][j])
					max[i] = clauses[i][j];
				}
			}
		}
	}

	
	void printResult(int size){
		try {
			writer = new FileWriter(outFile, true);
			writer.write(" The solution is\n");

			/*print result in form of variable values*/
			for(int i=0;i<size;i++) {
				writer.write("x[" + (i+1) + "] = " + x[i] + "\n");
			}
			writer.close();
		}catch(IOException exc) {
			System.out.println(exc.getMessage());
		}
	}

	public static void main(String[] args) {
		String output = "";

		long time1 = System.currentTimeMillis();

		Submission sub = new Submission(args[0],args[1]);

		/*if result is not found*/
		sub.backTrackCompute(0);
		if(!sub.resultFound) {
			output = "No satisfying assignment\n";

		/*if result is not found with n>5 and m>30*/
		} else if(sub.resultFound && sub.dontPrint) {
			output = "There is a satisfying assignment\n";
		}

		/*calculate running time*/
		long time2 = System.currentTimeMillis();
		output = output + "Run time is " + (time2 - time1) + " milliseconds";

		/*append the output string to output file*/
		try {
			FileWriter wr = new FileWriter(sub.outFile, true);
			wr.write(output);
			wr.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
