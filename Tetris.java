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
    	if (mutation == PentominoDatabase.data[pentID].length-1 && x==1)
    		mutation = 0; else
    	if (mutation == 0 && x == -1)
    		mutation = PentominoDatabase.data[pentID].length-1;
    	else    	
    	mutation = mutation + x;
    	int piece[][] = PentominoDatabase.data[pentID][mutation];
    }
    
    public void move(int x) {
    	if (locW+x!=-1 || locW+x != width)
    		locW+=x;
    }
    public void dropDown() {
    	if(fit())locH+=1;
    	else {locH=0;nextPiece();}
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
        ui.setState(field);
 		
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
   	    		if (field[cx][cy] != -1) 
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