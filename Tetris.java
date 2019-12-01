import java.util.Random;

public class Tetris {
	public static int locW;
	public static int locH;
	public static int pentID;
	public static int mutation;
	public static int[][] piece;
	public static int width = 5;
	public static int height = 12;
	public static int score = 0;
	public static int[][] field = new int[height][width];
	public static int[][] field2 = new int[height][width];
	public static UI ui = new UI(height, width, 50);

	public void rotate(int x) {
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
			int [][]tempPiece = PentominoDatabase.data[pentID][mutation];
			if (fitInMove(0 - tempPiece[0].length/2, 0)) // if new position fit then
			{
				locW = locW - tempPiece[0].length/2;  
				reDraw();
				piece = PentominoDatabase.data[pentID][mutation];
				addPiece();
			}
			else {
			mutation = stMutation;
		}
		}
	}

	public void move(int x) {
		if (locW + x >= 0 && locW + x < width) {
			if (fitInMove(x, 0)) // if new position feet then
			{
				reDraw();
				locW += x;
				addPiece();
			}
		}
	}

	public void dropDown() {
		if (fitInMove(0, 1)) {
			reDraw();
			locH += 1;
			addPiece();
		} else {
			onTable();
			locH = 0;
			checkDelRows();
			nextPiece();
		}
	}

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

	public void checkDelRows() {
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
				score += 5;
			}
		}
	}

	public void reDraw() {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[0].length; j++) {
				if (field[i][j] == pentID) {
 					field[i][j] = -1;
				}

			}}

	}

	public void moveOneRow(int row) {
		for (int j = row - 1; j > 0; j--) {
			for (int i = 0; i < width; i++) {
				field[j + 1][i] = field[j][i];
				field2[j + 1][i] = field2[j][i];
			}
		}
	}

	public static int start() {

		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				field[i][j] = -1;
			}
		}

		nextPiece();

		return 1;
	}

	public static void nextPiece() {

		Random r = new Random();
		pentID = r.nextInt(PentominoDatabase.data.length);
		mutation = r.nextInt(PentominoDatabase.data[pentID].length);
		piece = PentominoDatabase.data[pentID][mutation];
		locW = 0;
		locH = 0;
		if (fitInMove(0, 0)) {
			addPiece();
		} else {
			ui.gameLost();
		}
	}

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

	public static void main(String[] args) {
		int t = 0;
		while (t == 0) {
			t = start();
		}

		System.out.print("end");
	}
}