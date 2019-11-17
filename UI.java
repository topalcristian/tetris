import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

// This class takes care of all the graphics to display a certain state
@SuppressWarnings("serial")
public class UI extends JPanel
{
    private JFrame window;
    private int[][] state;
    private int size;

    // Constructor: sets everything up
    public UI(int x, int y, int _size)
    {
        size = _size;
        setPreferredSize(new Dimension( y * size,x * size));

        window = new JFrame("Pentomino");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(this);
        window.pack();
        window.setVisible(true);
        Tetris t = new Tetris();
        window.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					t.rotate(1);
					break;
				case KeyEvent.VK_DOWN:
					t.rotate(-1);
					break;
				case KeyEvent.VK_LEFT:
					t.move(1);
					break;
				case KeyEvent.VK_RIGHT:
					t.move(-1);
					break;
				case KeyEvent.VK_SPACE:
					t.dropDown();
					t.score += 1;
					break;
				} 
			}
			
			public void keyReleased(KeyEvent e) {
			}
		});
		
		// Make the falling piece drop every second
		new Thread() {
			@Override public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						t.dropDown();
					} catch ( InterruptedException e ) {}
				}
			}
		}.start();
	
    
    
    
    
        state = new int[x][y];
        for (int i = 0; i < state.length; i++)
        {
            for (int j = 0; j < state[i].length; j++)
            {
                state[i][j] = -1;
            }
        }
        
        
    }

    // Paint function, called by the system if required for a new frame, uses the state stored by the UI class
    public void paintComponent(Graphics g)
    {
        Graphics2D localGraphics2D = (Graphics2D) g;

        localGraphics2D.setColor(Color.LIGHT_GRAY);
        localGraphics2D.fill(getVisibleRect());

        // draw lines
        localGraphics2D.setColor(Color.GRAY);
        for (int i = 0; i <= state.length; i++)
        {
            localGraphics2D.drawLine(0 , (i * size), state[0].length * size,(i * size));
        }
        for (int i = 0; i <= state[0].length; i++)
        {
            localGraphics2D.drawLine( (i * size),0,  i * size,(state.length * size));
        }

        // draw blocks
        for (int i = 0; i < state.length; i++)
        {
            for (int j = 0; j < state[0].length; j++)
            {
                localGraphics2D.setColor(GetColorOfID(state[i][j]));
                localGraphics2D.fill(new Rectangle2D.Double( j * size + 1,i * size + 1, size - 1, size - 1));
            }
        }
        

    }

    // Decodes the ID of a pentomino into a color
    private Color GetColorOfID(int i)
    {
        switch (i)  { 
         case 0:  return Color.BLUE;
         case 1:  return Color.ORANGE;
         case 2:  return Color.CYAN;
         case 3:  return Color.GREEN;
         case 4:  return Color.MAGENTA;
         case 5:  return Color.PINK;
         case 6:  return Color.RED;
         case 7:  return Color.YELLOW;
         case 8:  return Color.YELLOW;
         case 9:  return new Color(255,250,240);
         case 10:  return new Color(250,235,215);
         case 11:  return new Color(81,47,47);
         case 12:  return new Color(81,47,23);
         case 13:  return new Color(0, 150, 128);
         case 14:  return new Color(0, 150, 102);
         case 15:  return new Color(51, 200,128);
         case 16:  return new Color(51, 200,102);
         case 17:  return new Color(0, 100, 0);
         default: return Color.LIGHT_GRAY;
	}
    }

    // This function should be called to update the displayed state (Makes a copy)
    public void setState(int[][] _state)
    {
        for (int i = 0; i < state.length; i++)
        {
            for (int j = 0; j < state[0].length; j++)
            {
                state[i][j] = _state[i][j];
            }
        }

        // Tells the system a frame update is required
        repaint();
    }
}
