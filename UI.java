import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// This class takes care of all the graphics to display a certain state
@SuppressWarnings("serial")
public class UI extends JPanel {
	
	// Generates Window
	private JFrame window;
	
	// Display 
	private int high = 0;
	
	// Current Field displayed
	private int[][] state;
	
	// Pixels per size
	private int size;
	private static int z= 0;
	// Current Score
	private int gameScore = 0;
	
	// Data of the Next Piece
	public int TNpentID;
	public int[][] TNpiece = {{ -1, -1, -1 }, { -1, -1, -1 }, { -1, -1, -1 } };
	
	// Stores previous Highscores
	public int[] highscore = {0,0,0,0,0};
	
	//
	public Color defaultColor = new Color(90, 90, 90);
	
	// Constructor with string sets UI for The player
	public UI(int x, int y, int _size, String s) {
		
		
		// 
		high = 1;
		//Sets the Dimensions of the board
		size = _size;
		setPreferredSize(new Dimension(4 * y * size, 250 + x * size));
		

		
		// Initializing Window
		window = new JFrame("Tetris");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.add(this);
		window.pack();
		window.setVisible(true);
		
		window.setBounds((int) 0, 0, 4 * y * size, 250 + z + x * size);
	    if (z== 0)
	    z = 1;
	    else z = 0;
		
		// Sets Gray Color
		window.setBackground(defaultColor);
		
		//Sets up Restart button
		JPanel panel = new JPanel();
		JButton restart = new JButton("RESTART");
		panel.setLayout(null);
		restart.setBounds(330, 660, 100, 30);
		panel.add(restart);
		panel.setBackground(defaultColor);
		ActionListener reset = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    window.setBounds((int) 0, 0, 4 * y * size, 250 + z + x * size);
			    if (z== 0)
			    z = 1;
			    else z = 0;
				
			    reset();
				
			}
		};
		restart.addActionListener(reset);
		restart.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    window.setBounds((int) 0, 0, 4 * y * size, 250 + z + x * size);
			    if (z== 0)
			    z = 1;
			    else z = 0;
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
			    window.setBounds((int) 0, 0, 4 * y * size, 250 + z + x * size);
			    if (z== 0)
			    z = 1;
			    else z = 0;		   
			    }
		});
		restart.setFocusable(false);
		window.add(panel);
		
		// Sets up keylistener 
		KeyListener listener = new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					Tetris.rotate(-1);
					break;
				case KeyEvent.VK_DOWN:
					Tetris.rotate(1);
					break;
				case KeyEvent.VK_LEFT:
					Tetris.move(-1);
					break;
				case KeyEvent.VK_RIGHT:
					Tetris.move(1);
					break;
				case KeyEvent.VK_SPACE:
					Tetris.dropDown();

					break;
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		};
		window.addKeyListener(listener);
		// Make the falling piece drop every second
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						Tetris.dropDown();
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();

		// Generates Empty Board
		state = new int[x][y];
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				state[i][j] = -1;
			}
		}


	}

	// Constructor for the Bot sets everything up
	public UI(int x, int y, int _size) {
		
		// Sets default Dimensions
		size = _size;
		setPreferredSize(new Dimension(4 * y * size, 250 + x * size));

		// Sets up window
		window = new JFrame("Tetris");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.add(this);
		window.pack();
		window.setVisible(true);
		window.setBackground(defaultColor);
		window.setBounds((int) 0, 0, 4 * y * size, 250 + z + x * size);
	    if (z== 0)
	    z = 1;
	    else z = 0;
		
		
		// Generates Empty Board
		state = new int[x][y];
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				state[i][j] = -1;
			}
		}

	}

	// Function to receive in game score
	public void giveScore(int score) {
		gameScore = score;
	}

	// Function to receive in game highscore
	public void giveHighScore(int[] score) {
		highscore = score;
	}

	// Function to receive next piece
	public void giveNPiece(int NpentID, int[][] Npiece) {
		TNpentID = NpentID;
		TNpiece = Npiece;

	}

	// Displays Message of Loss
	public void gameLost() {
		JOptionPane.showMessageDialog(this, "Game Over\nYour Score is : " + gameScore);
		System.exit(0);
	}

	
	// Paint function, called by the system if required for a new frame, uses the
	// state stored by the UI class
	public void paintComponent(Graphics g) {
		Graphics2D localGraphics2D = (Graphics2D) g;
		
		// Displays Tetris
		Font myFont = new Font("ARIAL", Font.BOLD, 40);
		localGraphics2D.setFont(myFont);
		localGraphics2D.setColor(Color.WHITE);
		localGraphics2D.drawString("Tetris", 75, 100);
		
		// Displays Current Score
		Font myFont2 = new Font("ARIAL", Font.BOLD, 20);
		localGraphics2D.setFont(myFont2);
		localGraphics2D.setColor(defaultColor);
		localGraphics2D.fill(new Rectangle2D.Double(330, 450, 100, 100));
		localGraphics2D.setColor(Color.WHITE);
		localGraphics2D.drawString("Score : " + gameScore, 330, 500);

		// Displays Highscore
		
		
		if (high == 1) {
			localGraphics2D.setColor(Color.WHITE);
			localGraphics2D.drawString("Scoreboard Placements:", 330, 550);
			Font myFont3 = new Font("ARIAL",Font.BOLD , 13);
			localGraphics2D.setFont(myFont3);
			for (int i = 0; i < highscore.length; i++) {
				localGraphics2D.drawString("Place "+(i+1)+" : "+Integer.toString(highscore[i]), 330, 570+i*15);
			}
		}
		localGraphics2D.setColor(Color.WHITE);
		
		// Draws Lines
		for (int i = 0; i <= state.length; i++) {
			localGraphics2D.drawLine(20, (i * size) + 180, state[0].length * size + 20, (i * size) + 180);
		}
		for (int i = 0; i <= state[0].length; i++) {
			localGraphics2D.drawLine((i * size) + 20, 0 + 180, i * size + 20, (state.length * size) + 180);
		}

		// draw blocks
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[0].length; j++) {
				localGraphics2D.setColor(GetColorOfID(state[i][j]));
				localGraphics2D.fill(new Rectangle2D.Double(j * size + 1 + 20, i * size + 1 + 180, size - 1, size - 1));
			}
		}

		// Displays Next Piece
		localGraphics2D.setFont(myFont2);
		localGraphics2D.setColor(Color.WHITE);
		localGraphics2D.drawString("Next Piece : ", 330, 200);

		
		// Next piece grid
		localGraphics2D.setColor(Color.WHITE);
		for (int i = 0; i < 6; i++) {
			localGraphics2D.drawLine(i * size + 330, 220, i * size + 330, 220 + 5 * size);
		}
		for (int i = 0; i < 6; i++) {
			localGraphics2D.drawLine(330, i * size + 220, 330 +  5 * size, i * size + 220);
		}
		
		// Next piece fill
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (TNpiece.length > i && TNpiece[i].length > j) {
					if (TNpiece[i][j] == 1) {
						localGraphics2D.setColor(GetColorOfID(TNpentID));
					} else {
						localGraphics2D.setColor(defaultColor);
					}
				} else {
					localGraphics2D.setColor(defaultColor);
				}
				localGraphics2D.fill(new Rectangle2D.Double( j * size +331 ,i * size+221 , size - 1, size - 1));
			}
		}

	}

	// Decodes the ID of a pentomino into a color
	private Color GetColorOfID(int i) {
		switch (i) {
		case 0:
			return new Color(22, 138, 254);
		case 1:
			return new Color(254, 208, 22);
		case 2:
			return new Color(22, 254, 177);
		case 3:
			return Color.GREEN;
		case 4:
			return new Color(183, 47, 134);
		case 5:
			return new Color(212, 139, 223); // Pink
		case 6:
			return new Color(229, 62, 62);
		case 7:
			return new Color(229, 229, 71);
		case 8:
			return new Color(210, 245, 60); // Lime
		case 9:
			return new Color(116, 108, 188); // Purple
		case 10:
			return new Color(62, 51, 155); // Lavender
		case 11:
			return new Color(170, 110, 40); // Brown
		case 12:
			return new Color(128, 0, 0); // Olive
		case 13:
			return new Color(0, 0, 128); // Navy
		case 14:
			return new Color(0, 128, 128); // Teal
		case 15:
			return new Color(255, 215, 180); // Apricot
		case 16:
			return new Color(170, 255, 195); // Mint
		case 17:
			return new Color(128, 0, 0); // Maroon
		case 18:
			return new Color(255, 250, 200); // Beige
		default:
			return defaultColor;

		}
	}

	// This function should be called to update the displayed state (Makes a copy)
	public void setState(int[][] _state, int[][] _state2) {
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[0].length; j++) {
				if (_state[i][j] != 18)
					state[i][j] = _state[i][j];
				else
					state[i][j] = _state2[i][j];
			}
		}

		// Tells the system a frame update is required
		repaint();
	}

	public void reset() {
		
		Tetris.start();
		
	}

}