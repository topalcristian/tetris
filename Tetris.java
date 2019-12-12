import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Tetris {
	// Board location
	public static int locW;
	public static int locH;
	// Data of current Piece used
	public static int pentID;
	public static int mutation;
	public static int[][] piece;
	// Board Sizes
	public static int width = 5;
	public static int height = 15;
	// Current Score
	public static int score = 0;
	public static int[] highscore = {0,0,0,0,0};

	// Field 1 to Save current State
	public static int[][] field = new int[height][width];
	// Field 2 to save colors
	public static int[][] field2 = new int[height][width];
	// Generates Ui
	public static UI ui = new UI(height, width, 40, " ");
	// Next Piece Data
	public static int NpentID;
	public static int Nmutation;
	public static int[][] Npiece;
	
	// Rotation L/R Function (x = 1) -> right, (x = -1) -> left, works by changing mutaition 
	public static void rotate(int x) {
		int stMutation = mutation;
		if (mutation == PentominoDatabase.data[pentID].length - 1 && x == 1)
			mutation = 0;
		else if (mutation == 0 && x == -1)
			mutation = PentominoDatabase.data[pentID].length - 1;
		else
			mutation = mutation + x;
		if (fitInMove(0, 0)) // if new position fit then
		{
			reDraw();
			piece = PentominoDatabase.data[pentID][mutation];
			addPiece();
		} else {
			int[][] tempPiece = PentominoDatabase.data[pentID][mutation];
			if (fitInMove(5 - (tempPiece[0].length) - locW, 0)) // if new position fit then
			{
				locW = 5 - (tempPiece[0].length);
				reDraw();
				piece = PentominoDatabase.data[pentID][mutation];
				addPiece();
			} else {
				mutation = stMutation;
			}
		}
	}

	// Move to the left or to the right the piece 
	public static void move(int x) {
		if (locW + x >= 0 && locW + x < width) {
			if (fitInMove(x, 0)) // if new position feet then
			{
				reDraw();
				locW += x;
				addPiece();
			}
		}
	}

	// Move the Piece down if possible
	public static void dropDown() {
		if (fitInMove(0, 1)) {
			reDraw();
			locH += 1;
			addPiece();
		} else {
			onTable();
			locH = 0;
			checkDelRows();
			ui.giveScore(score);
			nextPiece();
		}
	}

	// Function to distinguish which spaces of the field are already occupied
	public static void onTable() {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				if (field[i][j] == pentID) {
					field2[i][j] = pentID;
					field[i][j] = 18;
				}
			}
		}
	}
	
	// Checks if the Pentomino fits from the selected start point
	public static boolean fitInMove(int x, int y) {
		int[][] pieceTemp = PentominoDatabase.data[pentID][mutation];

		for (int i = 0; i < pieceTemp.length; i++) {
			for (int j = 0; j < pieceTemp[0].length; j++) {
				if (pieceTemp[i][j] == 0) {
					continue;
				}
				int cx = i + locH + y;
				int cy = j + locW + x;
				if (cx < 0 || cy < 0 || cx >= field.length || cy >= field[0].length) {
					return false;
				}
				if (field[cx][cy] > -1 && field[cx][cy] != pentID) {
					return false;
				}
			}
		}
		return true;
	}

	// Checks if there are rows to be deleted and score added
	public static void checkDelRows() {
		boolean lineIsFull = true;
		for (int i = 0; i < height; i++) {
			lineIsFull = true;
			for (int j = 0; j < width; j++) {
				if (field[i][j] == -1) {
					lineIsFull = false;
				}
			}
			if (lineIsFull == true) {
				moveOneRow(i);
				score += 1;
			}
		}
	}

	// Removes the current piece so it can be redrawn in a different position
	public static void reDraw() {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[0].length; j++) {
				if (field[i][j] == pentID) {
					field[i][j] = -1;
				}

			}
		}

	}

	// Moves the field by one row if there are rows to be removed
	public static void moveOneRow(int row) {
		for (int j = row - 1; j > 0; j--) {
			for (int i = 0; i < width; i++) {
				field[j + 1][i] = field[j][i];
				field2[j + 1][i] = field2[j][i];
			}
		}
	}
	
	// Initializes the initial board with empty spaces
	public static void initZero() {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				field[i][j] = -1;
			}
		}
		
	}
	
	// Reads Highscore file
	public static void readHighScore() {
		
		
		File tmpDir = new File("highscore.txt");
		boolean exists = tmpDir.exists();
		if (exists) {
			String[] tr = new String[6];
			BufferedReader reader;

			try {
				reader = new BufferedReader(new FileReader("highscore.txt"));
				String line = reader.readLine();
				int i = 0;
				while (line != null) {
					
					highscore[i] = Integer.parseInt(line);
					line = reader.readLine();
					i++;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			
		}
		
	}
	
	// Starts the Game
	public static int start() {
		
		initZero();
		readHighScore();
		ui.giveHighScore(highscore);
		
		// Generates initital NextPiece
		Random r = new Random();
		NpentID = r.nextInt(PentominoDatabase.data.length);
		Nmutation = r.nextInt(PentominoDatabase.data[NpentID].length);
		Npiece = PentominoDatabase.data[NpentID][Nmutation];
		nextPiece();
		return 1;
	}

	// Generates the Next Piece
	public static void nextPiece() {

		pentID = NpentID;
		mutation = Nmutation;
		piece = Npiece;
		Random r = new Random();
		NpentID = r.nextInt(PentominoDatabase.data.length);
		Nmutation = r.nextInt(PentominoDatabase.data[NpentID].length);
		Npiece = PentominoDatabase.data[NpentID][Nmutation];
		ui.giveNPiece(NpentID, Npiece);
		locW = 0;
		locH = 0;
		if (fitInMove(0, 0)) {
			addPiece();
		} else {
			recordScore();
			ui.gameLost();
		}
	}

	// Store Highscore to the file
	private static void recordScore() {
		for (int i = 0; i < highscore.length; i++) {
			if (highscore[i] <= score) {
				
				for(int j = highscore.length-1; j > i; j--) {
					highscore[j] = highscore[j-1]; 
				}
				highscore[i] = score;
				score = 0;
				break;
			}
			
		}

		File myFoo = new File("highscore.txt");
		FileWriter fooWriter;
		try {
			fooWriter = new FileWriter(myFoo, false);
			for (int i = 0; i < highscore.length; i++) {
				fooWriter.write(highscore[i]+"\n");
			}
			
			fooWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	// Adds the piece to the table
	public static void addPiece() {

		for (int i = 0; i < piece.length; i++) // loop over x position of pentomino
		{
			for (int j = 0; j < piece[0].length; j++) // loop over y position of pentomino
			{
				if (piece[i][j] == 0) {
					continue;
				}
				int cx = i + locH;
				int cy = j + locW;
				field[cx][cy] = pentID;
			}
		}
		ui.setState(field, field2);
	}

	// Main Function
	public static void main(String[] args) {
		int t = 0;
		while (t == 0) {
			t = start();
		}

		System.out.print("end");
	}
}