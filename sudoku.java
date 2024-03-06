package sudoku;

import java.util.Scanner;

public class sudoku {
	static int[][] board = new int[9][9];
	static int zeroes = 0; // this is used to count zeroes in a board
	static int row = 0, col = 0; // initializes variables to zero 
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) 
	{
		do // the do loop will run until there are no more '0's
			//and the board is filled with numbers. 
		{ 
			zeroes = 0;
			readBoard(); //reads the current state of the board from
			
			if (zeroes <= 3) //it runs if there are three or less than
							//three 0's on the board 
				solveBoard();
		}
		while (zeroes < 81); //the loop continues to run until there are
							//no more zeroes
		System.out.print("END"); //shows "END" once the loop stops running. 
	}

	static void readBoard() {
		// this method is used read the current state of the 9x9 sudoku board
		// and counts the number of 0s. 

		zeroes = 0;
		for (int row = 0; row < 9; row++) // for each row
			for (int col = 0; col < 9; col++) // for each column
			{
				board[row][col] = input.nextInt();
				if (board[row][col] == 0)
					{zeroes++;} //increases the number o 0s by 1
			}

	}

	public static void solveBoard() {
		// the board has now been read and we have counted the number of zeroes. 
		// if we are here, we know we have 1, 2, or 3 zeroes.

	//if (zeroes == 0) 
	//	System.out.println("No Missing Values.");	
				
		while (zeroes > 0) { 
			if (zeroes == 1) {
				// this is the type I problem where there is one zero on the board 
				for (row = 0; row < 9; row++) {
					for (col = 0; col < 9; col++) {
						if (board[row][col] == 0) {
							// call findMissing to find the missing value instead of zeroes for 
							//each row and column

							board[row][col] = findMissing(board[row]);
							
							System.out.println("(" + row + "," + col + "," + board[row][col] + ")");
							return;
						}
					}
				}
			}

			else if (zeroes == 2) { //this is the type II problem where there are two zeroes
									//together in the same col
				for (int row = 0; row < 9; row++) {
					for (int col = 0; col < 8; col++) {
						if (board[row][col] == 0) { 
							if (col != 8) { 
								if (board[row][col + 1] == 0) {
									int missing = 0; 	
									int[] column = new int[] { board[0][col], board[1][col], board[2][col],
											board[3][col], board[4][col], board[5][col], board[6][col],
											board[7][col], board[8][col] };
									board[row][col] = findMissing(column); //this assigns the missing number in the row and col 
									System.out.print("(" + row + "," + col + "," + board[row][col] + ")");

									col++; //increasing the col value to find other missing value

									missing = findMissing(new int[] { board[0][col], board[1][col], board[2][col],
											board[3][col], board[4][col], board[5][col], board[6][col],
											board[7][col], board[8][col] });

									board[row][col] = missing;

									System.out.print("(" + row + "," + col + "," + missing + ")\n");

									zeroes -= 2; //decreases the number of empty cells by 2
								} else if (board[row + 1][col] == 0) { //the code executes if the cell below the cell is empty
									int missing = 0;
									int[] rows = new int[] { board[0][col], board[1][col], board[2][col], board[3][col],
											board[4][col], board[5][col], board[6][col], board[7][col],
											board[8][col] };
									board[row][col] = findMissing(rows);
									System.out.print("(" + row + "," + col + "," + board[row][col] + ")");
									row++;
									missing = findMissing(new int[] { board[row][0], board[row][1], board[row][2],
											board[row][3], board[row][4], board[row][5], board[row][6], board[row][7],
											board[row][8] });

									board[row][col] = missing;

									System.out.print("(" + row + "," + col + "," + board[row][col] + ")" + ")\n");

									zeroes -= 2; //reduces the number of empty cells by 2 because 
									 			// now we have filled in two cells with missing number. 
								}
							}
						}
					}
				}
				//if it's three missing values, then it's a type 3 problem
				//where they'll be side by side in L formation.
			} else if (zeroes == 3) 
				;

			int[][] countbyThree = new int[9][3]; //to put 
			int whichboard = 0;
			for (int row = 0; row < 9; row++) {
				for (int col = 0; col < 9; col++) { // 
					if (board[row][col] == 0) {   // checking if any cell is empty
												//if any cell is empty, the program will
												//try to fill it in with a missing number. 
						whichboard = (row / 3) * 3 + (col / 3); //it calculates 3 by 3 sub-grids in row and column

						countbyThree[whichboard][0]++;

						countbyThree[whichboard][1] = row;

						countbyThree[whichboard][2] = col;
					}
				}
			}
			for (int i = 0; i < 9; i++) {
				if (countbyThree[i][0] == 1) {
					row = countbyThree[i][1];
					col = countbyThree[i][2];
					int firstrow = (row / 3) * 3;
					int firstcol = (col / 3) * 3;

					int[] value = new int[] { board[firstrow + 0][firstcol], board[firstrow + 0][firstcol + 1],
							board[firstrow + 0][firstcol + 2], board[firstrow + 1][firstcol],
							board[firstrow + 1][firstcol + 1], board[firstrow + 1][firstcol + 2],
							board[firstrow + 2][firstcol], board[firstrow + 2][firstcol + 1],
							board[firstrow + 2][firstcol + 2] };
					int missing = findMissing(value);
					board[row][col] = missing;
					
					
					System.out.print("(" + row + "," + col + "," + board[row][col] + ")");

					zeroes--;
				}
			}
			
		}
		
	}

	public static int findMissing(int[] A) {
		// the input A is a 9 element array of ints, with each value being between 0 and
		// 9.
		// the goal of this method is to return where the missing value is and what the missing.
		//value is 

		boolean[] found = new boolean[10]; //a boolean array "found" to check which integers are present
										   

		for (int i = 0; i < 9; i++) {
			found[A[i]] = true;
		}
		
		for (int i = 0; i < 10; i++) {
			if (!found[i]) {
				return i;

			}
		}

		return -1; // there is no missing number found in the given array. 
	}

}