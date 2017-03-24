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

import java.util.ArrayList;
import java.util.Random;
import static flocking.Flock.WIDTH;
import static flocking.Flock.HEIGHT;
import static java.awt.MouseInfo.getPointerInfo;
import java.awt.Point;
import javax.swing.JFrame;


class Boid {
    private static final Random rand;
    private Vector position; //Current Position of Boid
    private Vector velocity; //Current Velocity of Boid
    private Vector acceleration; //Acceleration acquired by a boid in each unit of time 
    private static final double MAXFORCE; //Maximum Force that can act on a boid
    private static final double MAXSPEED; //Maximum Speed that a boid can attain
    private static final double TWO_PI;
    
    
    static{
        rand = new Random();
        TWO_PI = 2*3.14159f;
        MAXSPEED = 2f;
        MAXFORCE = 0.03f;
    }
    
    Boid(double x,double y)
    {
        acceleration = new Vector(0,0);
        position = new Vector(x,y);
        
        //Initialize Boid with a random velocity;
        double course = TWO_PI*rand.nextDouble();
        velocity = new Vector(Math.cos(course),Math.sin(course)); 
    }
    
    public void run(ArrayList<Boid> boids,Vector bigFishLocation)
    {
        // For each iteration apply the three parameters of flocking 
        // Separation , Cohesion and Alignment 
        flock(boids,bigFishLocation);
        
        // Udate the Velocity  and location of the Boid 
        update();
        
        //If a Boid goes out of frame update the position
        borders();
    }
    
    public void applyForce(Vector force)
    {
        //Calculate the acceleration produced due to the applied force
        acceleration.add(force);
    }
    
    public void flock(ArrayList<Boid> boids,Vector bigFishLocation)
    {
        Vector sep = separation(boids,bigFishLocation); // Separation
        Vector ali = alignment(boids);                  // Alignment
        Vector coh = cohesion(boids);                   // Cohesion
        
        // Arbitrarily weight these forces
        sep.mult(1.5);
        ali.mult(1.0);
        coh.mult(1.0);
        
        // Add the force vectors to acceleration
        applyForce(sep);
        applyForce(ali);
        applyForce(coh);
    }
    
    // Method to update position
    public void update()
    {
        velocity.add(acceleration);
        velocity.limit(MAXSPEED);
        position.add(velocity);
        // Reset accelertion to 0 each cycle
        acceleration.mult(0);
    }
    
    // Separation
    // Method checks for nearby boids or predators and steers away
    private Vector separation(ArrayList<Boid> boids,Vector bigFishLocation)
    {
        //Required Separation between boids to avoid collision between them
        double desiredSeparation = 35f;
        //Required distance to be maintained to avoid predators/obstacles
        double desiredSeparationFromPredator = 50f;
        
        Vector goal = new Vector(0,0);
        int count = 0;
        
        for(Boid boid:boids){
            double d = position.subtract(boid.getPosition()).magnitude();
            
            if(d>0 && d<desiredSeparation){
                // Calculate vector pointing away from neighbor
                Vector diff = position.subtract(boid.getPosition());
                diff.normalize();   // Weight by distance
                diff.div(d);
                goal.add(diff);
                count++;            // Keep track of how many
            }
        }
        
        double d = position.subtract(bigFishLocation).magnitude();
            
        if(d>0 && d<desiredSeparationFromPredator){
            // Calculate vector pointing away from neighbor
            Vector diff = position.subtract(bigFishLocation);
            diff.normalize();
            diff.div(d);
            diff.mult(2);       //Multiply by a factor to give effect of a predator
            goal.add(diff);
            count++;
        }    
        
        if(count>0)
            goal.div(count);
        
        if(goal.magnitude()>0){
            // Implement Reynolds: Steering = Desired - Velocity
            goal.normalize();
            goal.mult(MAXSPEED);
            goal.sub(velocity);
            goal.limit(MAXFORCE);
        }
        
        return goal;
    }
    // Alignment
    // For every nearby boid in the system, calculate the average velocity
    private Vector alignment(ArrayList<Boid> boids)
    {
        double neighbourDist = 50f;
        Vector goal = new Vector(0,0);
        int count = 0;
        
        for(Boid boid:boids){
            double d = position.subtract(boid.getPosition()).magnitude();
            
            if(d>0 && d<neighbourDist){
                goal.add(boid.getVelocity());
                count++;
            }
        }
        
        if(count>0){
            goal.div(count);
            goal.normalize();
            goal.sub(velocity);
            goal.limit(MAXFORCE);
            return goal;
        }
        else return new Vector(0,0);
    }
    
    // Cohesion
    // For the average position (i.e. center) of all nearby boids
    // Calculate steering vector towards that position
    private Vector cohesion(ArrayList<Boid> boids)
    {
        double neighbourDist = 50f;
        Vector goal = new Vector(0,0);
        int count = 0;
        
        for(Boid boid:boids){
            double d = position.subtract(boid.getPosition()).magnitude();
            
            if(d>0 && d<neighbourDist){
                goal.add(boid.getPosition());
                count++;
            }
        }
        
        if(count>0){
            goal.div(count);
            return seek(goal);
        }
        else return new Vector(0,0);
    }
    
    
    // A method that calculates and applies a steering force towards a goal
    private Vector seek(Vector goal)
    {
        goal.sub(getPosition());
        goal.normalize();
        goal.mult(MAXSPEED);
        Vector steer = goal.subtract(velocity);
        steer.limit(MAXFORCE);  // Limit to maximum steering force
        
        return steer;
    }
    
    
    // Wraparound
    public void borders()
    {
        double x = position.getX();
        double y = position.getY();
        if (x < 0) x = WIDTH;
        if (y < 0) y = HEIGHT;
        if (x > WIDTH) x = 0;
        if (y > HEIGHT) y = 0;
        position.setXY(x,y);
    }
    
    public Vector getPosition()
    {
        return position;
    }
    
    public Vector getVelocity()
    {
        return velocity;
    }
    
    public double getCourse()
    {
        // Get current direction of velocity
        return Math.atan2(velocity.getY(),velocity.getX())+Math.toRadians(90);
    }
}
