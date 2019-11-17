import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Scanner;

public class Tetris
{
	public static int locW;
	public static int locH;
	public static int pentID;
	public static int mutation;
	public static int[][] piece;
    public static int width = 5;
    public static int height = 12;
    public static int score = 0;
    public static int[][] field = new int[height][width];
    public static UI ui = new UI( height, width, 50);
    public void rotate(int x) {
   	int stMutation=mutation;
   	if (mutation == PentominoDatabase.data[pentID].length-1 && x==1)
   		mutation = 0; else
   	if (mutation == 0 && x == -1)
   		mutation = PentominoDatabase.data[pentID].length-1;
   	else    	
   	mutation = mutation + x;
   	if (fitInMove(0,0)) // if new position feet then
		{
   		piece = PentominoDatabase.data[pentID][mutation];
   		reDraw();
		}
   	else {
   		mutation = stMutation;
   	}
   	
   } 
    
	public void move(int x) {
		if (locW + x >= 0 && locW + x < width) {
			if (fitInMove(x,0)) // if new position feet then
			{
				locW += x;
				reDraw();
			}
		}
	}
    
	public void dropDown() {
		if (fitInMove(0,1)) { 
			locH += 1;
			reDraw();
		} else {
			locH = 0;
			checkDelRows();
			nextPiece();
		}
	}
	
	public static boolean fitInMove(int x, int y)
	{
    	int[][] pieceTemp = PentominoDatabase.data[pentID][mutation];

	    for(int i = 0; i < pieceTemp.length; i++) 
	    {
	    	for(int j = 0; j < pieceTemp[0].length; j++) 
	    	{
	    		if (pieceTemp[i][j] == 0 ) 
	    		{
	    			continue;
	    		}
	    		int cx = i  + locH + y;
	    		int cy = j  + locW + x;
	    		if (cx < 0 || cy < 0 || cx >= field.length || cy >= field[0].length) 
	    		{
	    			return false;
	    		}
	    		if (field[cx][cy] > -1 && field[cx][cy] != pentID ) 
	    		{
	    			return false;
	    		}
	    	}
	    }
	    return true;
	}
    

    public void checkDelRows() {
    	boolean lineIsFull = true;
    	 for (int i = height - 1; i >= 0; i--) {
    		 lineIsFull = true;
    		  for (int j = 0; j < width; j++) {
    			  if (field[i][j] == -1) {

                      lineIsFull = false;
                  }

              }
			  if (lineIsFull == true)
				  moveOneRow(i);
    	}
    }
    
    public void reDraw() {
    	for(int i = 0; i < field.length; i++)
        {
        	for(int j = 0; j < field[i].length; j++)
        	{
        		if(field[i][j] == pentID) field[i][j] = -1;
        		
        	}
        }
    	addPiece();
    }
    
	public void  moveOneRow(int row) {
		for (int j = row-1; j > 0; j--) {
			for (int i = 0; i < width; i++) {
				field[j+1][i] = field[j][i];
			}
		}
	}
    
    
    public static int start(){

        for(int i = 0; i < field.length; i++)
        {
        	for(int j = 0; j < field[i].length; j++)
        	{
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
        addPiece();
 		
    }

    private static int characterToID(char character) {
    	int pentID = -1; 
    	if (character == 'X') {
    		pentID = 0;
    	} else if (character == 'I') {
    		pentID = 1;
    	} else if (character == 'Z') {
    		pentID = 2;
    	} else if (character == 'T') {
    		pentID = 3;
    	} else if (character == 'U') {
    		pentID = 4;
     	} else if (character == 'V') {
     		pentID = 5;
     	} else if (character == 'W') {
     		pentID = 6;
     	} else if (character == 'Y') {
     		pentID = 7;
    	} else if (character == 'L') {
    		pentID = 8;
    	} else if (character == 'P') {
    		pentID = 9;
    	} else if (character == 'N') {
    		pentID = 10;
    	} else if (character == 'F') {
    		pentID = 11;
    	} 
    	return pentID;
    }

    // Checks if the Pentomino fits from the selected start point


    public static boolean fit()
   	{
       	int[][] pieceTemp = PentominoDatabase.data[pentID][mutation];
       	int ii = -1, jj = -1;

       	for(int i = 0; i < pieceTemp.length; i++) 
       	{
   	    	for(int j = 0; j < pieceTemp[0].length; j++) 
   	    	{
   	    		if (pieceTemp[i][j] == 1 && ii == -1) 
   	    		{
   	    			ii = i;
   	    			jj = j; 
   	    			break;
   	    		}
   	    	}
   	    }

   	    for(int i = 0; i < pieceTemp.length; i++) 
   	    {
   	    	for(int j = 0; j < pieceTemp[0].length; j++) 
   	    	{
   	    		if (pieceTemp[i][j] == 0 ) 
   	    		{
   	    			continue;
   	    		}
   	    		int cx = i - ii + locW;
   	    		int cy = j - jj + locH;
   	    		if (cx < 0 || cy < 0 || cx >= field.length || cy >= field[0].length) 
   	    		{
   	    			return false;
   	    		}
   	    		if (field[cx][cy] != -1 || field[cx][cy] != pentID ) 
   	    		{
   	    			return false;
   	    		}
   	    	}
   	    }
   	    return true;
   	}

    public static void addPiece()
    {

        for(int i = 0; i < piece.length; i++) // loop over x position of pentomino
        {
            for (int j = 0; j < piece[0].length; j++) // loop over y position of pentomino
            {
            	if (piece[i][j] == 0) 
            	{
            		continue;
            	}
            	int cx = i + locH;
            	int cy = j + locW;
            	field[cx][cy] = pentID;
            }
        }
        ui.setState(field);
    }


    public static void main(String[] args)
    {
        int t = 0;
    	while (t == 0)
    	{
    		t = start();
    	}

        System.out.print("end");
    }
}