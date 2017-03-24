/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flocking;

/**
 *
 * @author eonx_32
 */
class Vector {
    private double x,y;
    
    public Vector(double x,double y)
    {
        setX(x);
        setY(y);
    }
    
    public Vector(Vector V)
    {
        setX(V.getX());
        setY(V.getY());
    }
    
    public void setX(double x)
    {
        this.x = x;
    }
    
    public void setY(double y)
    {
        this.y = y;
    }
    
    public void setXY(double x,double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public double magnitude()
    {
        //System.out.println(x+" "+y+" "+Math.sqrt(x*x+y*y));
        return Math.sqrt(x*x+y*y);
    }
    
    public void add(final Vector V)
    {
        x+=V.x;
        y+=V.y;
    }
    
    public void sub(final Vector V)
    {
        x-=V.x;
        y-=V.y;
    }
    
    public void mult(double n)
    {
        x*=n;
        y*=n;
    }
    
    public void div(double n)
    {
        x/=n;
        y/=n;
    }
    
    public Vector sum(final Vector V)
    {
        return new Vector(x+V.x,y+V.y);
    }
    
    public Vector subtract(final Vector V)
    {
        return new Vector(x-V.x,y-V.y);
    }
    
    public Vector multiply(double n)
    {
        return new Vector(x*n,y*n);
    }
    
    public Vector divide(double n)
    {
        return new Vector(x/n,y/n);
    }
    
    public void normalize()
    {
        double m = magnitude();
        
        if(m>0){
            div(m);
        }
    }
    
    public void limit(double max)
    {
        if(magnitude()>max){
            normalize();
            mult(max);
        }
    }
}
