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

import java.awt.Point;

public interface MouseListener {
    
    public Vector getMouseLocation();
    
}
