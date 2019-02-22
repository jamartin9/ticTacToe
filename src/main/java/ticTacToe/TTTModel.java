package ticTacToe;

import java.util.Random;
import java.util.Scanner;

public class TTTModel {

	public static final Scanner INPUT = new Scanner(System.in);
	private final boolean SMART = true;
	private int count,maxPly = 4;
	private char squares[][];

	public TTTModel(int dim) {
		squares = new char[dim][dim];
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				squares[i][j] = '.';
			}
		}
	}

	public TTTModel() {
		this(3);
	}

	public void play() {
		char player = 'X';

		int rows = squares.length;
		int cols = rows;

		for (int moves = 0; moves < rows * cols; moves++) {

			if (gameOver()) {
				return;
			}
			if (player == 'X') {
				playBestMove(SMART);
				player = 'O';
			} else {
				System.out.println(this);
				System.out.print("Enter row : ");
				int row = INPUT.nextInt();
				System.out.print("Enter column : ");
				int col = INPUT.nextInt();
				if (squares[row][col] == '.') {
					squares[row][col] = 'O';
					player = 'X';
				} else {
					System.out.println("Please retry!");
				}
			}
		}

	}

	public void playBestMove(boolean smart) {
		int score;
		int bestScore = -2;
		int bestRow = -1;
		int bestCol = -1;
		int dim = squares.length;
		count = 0;
		// Do the dry run
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (squares[i][j] == '.') {
					squares[i][j] = 'X';
					if (smart) {
						score = minimaxForO(0);
					} else {
						score = score();
					}
					if (score > bestScore) {
						bestScore = score;
						bestRow = i;
						bestCol = j;
					}
					squares[i][j] = '.';
				} // choice loop
			} // inner loop
		}// outer loop
		squares[bestRow][bestCol] = 'X';

		System.out.println("Inspected " + count + " states");
	}

	private int minimaxForO(int depth) {
		int score = score();
		int bestScore = 2;
		count++;
		if (gameOver()||depth >maxPly)
			return score;

		int dim = squares.length;

		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (squares[i][j] == '.') {
					squares[i][j] = 'O';
					score = minimaxForX(depth+1);

					if (score < bestScore) {
						bestScore = score;
					}
					squares[i][j] = '.';
					if (bestScore == -1) {
						return bestScore;
					}
				}
			}
		}
		return bestScore;
	}

	private int minimaxForX(int depth) {
		int score = score();
		int bestScore = -2;
		count++;
		if (gameOver()||depth>maxPly)
			return score;

		int dim = squares.length;

		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (squares[i][j] == '.') {
					squares[i][j] = 'X';
					score = minimaxForO(depth+1);
					if (score > bestScore) {
						bestScore = score;
					}
					squares[i][j] = '.';
					if(bestScore==1){
						return bestScore;
					}
				}
			}
		}
		return bestScore;
	}

	public void computerPlayFirst() {
		Random rnd = new Random();
		int i = rnd.nextInt(squares.length);
		int j = rnd.nextInt(squares.length);
		squares[i][j] = 'X';
	}

	public boolean gameOver() {
		int dim = squares.length;
		if (score() != 0) {
			return true;
		}

		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (squares[i][j] == '.') {
					return false;
				}

			}
		}
		return true;
	}

	public int score() {

		int lineScore;
		int dim = squares.length;
		char[] diagL = new char[dim];
		char[] diagR = new char[dim];
		char[] line = new char[dim];

		for (int i = 0; i < dim; i++) {
			// Check each row
			lineScore = scoreLine(squares[i]);
			if (lineScore != 0) {
				return lineScore;
			}

			// Check each column
			for (int j = 0; j < dim; j++) {
				line[j] = squares[j][i];
			}
			lineScore = scoreLine(line);
			if (lineScore != 0) {
				return lineScore;
			}
			diagL[i] = squares[i][i];
			int k = (dim - 1) - i;
			diagR[i] = squares[i][k];
		}

		lineScore = scoreLine(diagR);
		if (lineScore != 0) {
			return lineScore;
		}
		return scoreLine(diagL);
	}

	private int scoreLine(char[] line) {
		String result = new String(line);

		if (result.matches("[O]+"))
			return -1;
		if (result.matches("[X]+"))
			return 1;
		return 0;
	}

	public void playerSelectSquare(int row, int col) {
		if (squares[row][col] == '.') {
			squares[row][col] = 'O';
		}
	}

	public String toString() {
		String result = "";

		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares.length; j++) {
				result += squares[i][j] + " ";
			}
			result += "\n";
		}

		return result;
	}

	public void resetSquares() {
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares.length; j++) {
				squares[i][j] = '.';
			}
		}
	}

	// Accessor methods
	public char getSquare(int i, int j) {
		return squares[i][j];
	}

	public int getDimension() {
		return squares.length;
	}
}
