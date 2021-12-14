package framework.primitives;

import java.awt.Color;

/**
 * @author Valente Productions
 */
public class Line
{
    public final int DIM;
    public double slope[];
    public Vector v0;
    public Vector v1;
    public String color;
    
    public Line(Vector a, Vector b)
    {
        v0 = a;
        v1 = b;
        DIM = v0.DIM; 
        slope = new double[DIM];
        for(int i = 0; i < DIM; i++)
            slope[i] = v1.e[i]-v0.e[i];
    }

    public Vector f(double t)
    {
        Vector out = new Vector(DIM);
        for(int i = 0; i < DIM; i++)
            out.e[i] = slope[i]*t + v0.e[i];
        return out;
    }
    
    public Vector deltas(Vector vertex)
    {
        if(!vertex.isEqualTo(v0) && !vertex.isEqualTo(v1))
        {
            System.out.println("INCOMPATIBLE VERTEX");
            return null;
        }
        Vector delta = new Vector(DIM);
        if(vertex.isEqualTo(v0))
            for(int i = 0; i < DIM; i++)
                delta.e[i] = v1.e[i] - v0.e[i];
        else if(vertex.isEqualTo(v1))
            for(int i = 0; i < DIM; i++)
                delta.e[i] = v0.e[i] - v1.e[i];
        
        return delta;
    }
    
    public Vector midpoint()
    {
        return v0.plus(v1).scaledBy(0.5);
    }
    
    public Vector deltas()
    {
        Vector delta = new Vector(DIM);
        for(int i = 0; i < DIM; i++)
            delta.e[i] = v1.e[i] - v0.e[i];
        
        return delta;
    }
    
    public float length()
    {
        return (float)v0.distanceTo(v1);
    }
    
    // Rotation by theta through the AxB plane
    public void rotate2D(double theta, int A, int B) 
    {
       v0.rotate2D(theta, A, B);
       v1.rotate2D(theta, A, B);
    }
    
    // Rotation by theta through the AxB plane
    public void rotate(double theta[], int A[]) 
    {
       v0.rotate(theta, A);
       v1.rotate(theta, A);
    }
    
    public void scale(double factor)
    {
        v0.scale(factor);
        v1.scale(factor);
    }
    
    @Override
    public boolean equals(Object obj) 
    {
        return   (toString01().equals(((Line)obj).toString())
                ||toString10().equals(((Line)obj).toString()));
    }
    
    @Override
    public int hashCode() 
    {
        return (v0.hashCode() + v1.hashCode());//this.toString().hashCode();
    }
    
    @Override
    public String toString()
    {
        return toString01();
    }
    
    public String toFormattedString()
    {
        return "{" + v0.toFormattedString() + "," + v1.toFormattedString() + "}";
    }
    
    public String toString01()
    {
        return v0.toStringR() + " <-> " + v1.toStringR();
    }
    
    public String toString10()
    {
        return v1.toStringR() + " <-> " + v0.toStringR();
    }

    @Override
    public Line clone()
    {
        return new Line(v0.clone(), v1.clone());
    }
}
