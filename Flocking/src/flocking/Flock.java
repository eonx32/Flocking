/**
 * An implementation of Craig Reynold's Boids program to simulate
 * the flocking behavior of birds. Each boid steers itself based on
 * rules of avoidance, alignment, and coherence.
 *
 * Addition of Mouse pointer to steer away the flock.
 *  
 * Source : https://processing.org/examples/flocking.html
 *
 * @author eonx_32
 */

package flocking;

import java.awt.Graphics;
import java.awt.Graphics2D;
import static java.awt.MouseInfo.getPointerInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Flock extends JPanel implements MouseListener {
    
    private ArrayList<Boid> boids;
    private int[] xt,yt;
    private static final int N = 3;
    private static final int TIME = 1;   // Time between each iteration
    private static final int SIZE = 150; // Number of Boids in the System
    
    //Coordinates to draw a triangle
    private static final int[] X = new int[] {0, 3, 6};
    private static final int[] Y = new int[] {0, -12, 0};
    
    public static final int WIDTH = 1150;   // Width of frame
    public static final int HEIGHT = 700;   // Height of frame
    private static JFrame frame;
    
    Flock()
    {
        boids = new ArrayList<Boid>();
        for(int i=0;i<SIZE;i++)
            boids.add(new Boid(WIDTH/2,HEIGHT/2));
        
        xt = new int[N];
        yt = new int[N];
        
        repaint();
    }
    
    @Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
                
            for(Boid boid:boids){
                double theta = boid.getCourse();
                Vector position = boid.getPosition();
                double cos = Math.cos(theta);
                double sin = Math.sin(theta); 
            
                for(int i=0;i<N;i++){
                    xt[i] = (int)(X[i]*cos - Y[i]*sin) + (int)position.getX();
                    yt[i] = (int)(X[i]*sin + Y[i]*cos) + (int)position.getY();
                }
                
		g2d.drawPolygon(xt, yt, 3);
            }
	}
    
    public void run()
    {
        Vector bigFishLocation = getMouseLocation();
        
        for(Boid boid:boids)
            boid.run(boids,bigFishLocation);
        
        repaint();
    }
    
    @Override
    public Vector getMouseLocation() {
        
        Point point1,point2;
        point1 = getPointerInfo().getLocation();
        point2 = frame.getLocationOnScreen();
        
        //Get relative position of Mouse Pointer with respect to window position
        double x = point1.getX() - point2.getX();
        double y = point1.getY() - point2.getY();
        return new Vector(x,y);
    }
    
    public static void main(String[] args) throws InterruptedException
    {
        frame = new JFrame("Flocking");
	Flock flock = new Flock();
	frame.add(flock);
	frame.setSize(WIDTH, HEIGHT);
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        //Simulate the Flock 
	while (true) {
            flock.run();
            Thread.sleep(TIME);
	}
    }
}
