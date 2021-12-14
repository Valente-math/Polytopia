package framework.primitives;

import java.awt.Color;
import java.util.Arrays;
import manifolds.smoothmanifolds.Hypersphere;

/**
 * @author Valente Productions
 * @version 1.0
 */
public class Vector implements Comparable
{
    public int DIM;
    public double[] e;
    
    public Vector(double[] w, Color c)
    {
        DIM = w.length;
        e = w;
    }
    
    public Vector(int dim, Color c)
    {
        DIM = dim;
        e = new double[DIM];
        for(int i = 0; i < DIM; i++) e[i] = 0;
    }
    
    public Vector(double[] w)
    {
        DIM = w.length;
        e = w;
    }
    
    public Vector(float[] w)
    {
        DIM = w.length;
        e = new double[DIM];
        for(int i = 0; i < DIM; i++)
            e[i] = w[i];
    }
    
    public Vector(int[] w)
    {
        DIM = w.length;
        e = new double[DIM];
        for(int i = 0; i < DIM; i++) e[i] = w[i];
    }
    
    public Vector(int dim, double val)
    {
        DIM = dim;
        e = new double[DIM];
        Arrays.fill(e, val);
    }
    
    public Vector(int dim)
    {
        this(dim, 0);
    }
    
    public double norm()
    {
        double sum = 0;
        for(int i = 0; i < DIM; i++)
            sum += e[i]*e[i];
        return Math.sqrt(sum);
    }
    
    public Vector normalized()
    {
        return this.scaledBy(1.0/norm());
    }
    
    public static Vector expand(Vector w, int target)
    {
        
        if(target <= w.DIM) return w;
        Vector exp = new Vector(target);//, w.color);
        System.arraycopy(w.e, 0, exp.e, 0, w.DIM);
        for(int i = w.DIM; i < target; i++)
            exp.e[i] = 0; 
        return exp;
    }
    
    public static Vector collapse(Vector w, int target)
    {
        Vector exp = new Vector(target);//, w.color);
        System.arraycopy(w.e, 0, exp.e, 0, target);
        return exp;
    }
    
    public Vector combine(Vector w1, Vector w2)
    {
        int dim = Math.max(w1.DIM, w2.DIM);
        double[] sum = new double[dim];
        Vector W1 = expand(w1, dim);
        Vector W2 = expand(w2, dim);
        for(int i = 0; i < sum.length; i++)
            sum[i] = W1.e[i] + W2.e[i];
        
        return new Vector(sum);
    }
    
    public void add(Vector w)
    {
        if(w.DIM != DIM) return;
        for(int i = 0; i < DIM; i++)
            e[i] += w.e[i];
    }
    
    public Vector plus(Vector w)
    {
        Vector sum = w.clone();
        sum.add(this);
        
        return sum;
    }
    
    public Vector minus(Vector w)
    {
        Vector diff = w.clone();
        diff.scale(-1);
        diff.add(this);
        
        return diff;
    }
    
    public void translate(Vector w)
    {
        if(!w.isEqualTo(new Vector(w.DIM)))
        add(w.scaledBy(-1));
    }
    
    public Vector scaledBy(double factor)
    {
        double[] product = new double[this.DIM];
        for(int i = 0; i < this.e.length; i++)
            product[i] = factor * this.e[i];
        return new Vector(product);//, this.color);
    }
    
    public void scale(double factor)
    {
        for(int i = 0; i < DIM; i++)
            e[i] *= factor; 
    }

    public void shiftBy(double d)
    {
        for(int i = 0; i < DIM; i++)
            e[i] += Math.abs(d);
    }
    
    public Vector shifted(double d)
    {
        Vector w = new Vector(DIM);
        for(int i = 0; i < DIM; i++) w.e[i] = e[i] + d;
        return this;
    }
    
    
    public double distanceTo(Vector w)
    {
        double radicand = 0;
        for(int i = 0; i < DIM; i++)
            radicand += Math.pow(w.e[i]-this.e[i],2);
        return Math.sqrt(radicand);
    }
    
    public long preciseDistanceTo(Vector w)
    {
        long radicand = 0;
        for(int i = 0; i < DIM; i++)
            radicand += ((long)w.e[i]-(long)e[i])*((long)w.e[i]-(long)e[i]);
        return (long)Math.sqrt(radicand);
    }
    
    
    
    private Vector subarray(Vector arr, int pos1, int pos2)
    {
        double[] result = new double[pos2 - pos1 + 1];
        for(int i = 0; i < result.length; i++)
            result[i] = arr.e[pos1 + i];
        
        return new Vector(result);
    }
    
    public static Vector join(Vector arr1, Vector arr2)
    {
        double[] result = new double[arr1.DIM + arr2.DIM];
        int index = 0;
        
        for(int i = 0; i < arr1.DIM; i++, index++) result[index] = arr1.e[i];
        for(int i = 0; i < arr2.DIM; i++, index++) result[index] = arr2.e[i];
        
        return new Vector(result);
    }
    
    public Vector swap(Vector arr, int pos1, int pos2)
    {
        double[] result = new double[arr.DIM];
        
        for(int i = 0; i < arr.DIM; i++)
            if((i != pos1)&&(i != pos2)) result[i] = arr.e[i];
        
        result[pos1] = arr.e[pos2];
        result[pos2] = arr.e[pos1];
        
        return new Vector(result);
    }
    
    public void swap(int pos1, int pos2)
    {
        double hold = e[pos1];
        e[pos1] = e[pos2];
        e[pos2] = hold;
    }
    
    // Rotation by theta through the AxB plane
    public void rotate2D(double theta, int A, int B) 
    {
       double x = e[A];
       double y = e[B];
       e[A] = x*Math.cos(theta) - y*Math.sin(theta);
       e[B] = x*Math.sin(theta) + y*Math.cos(theta);
    }
    
    public void rotate(double[] rot, int[] A) 
    {
       Hypersphere sphere = new Hypersphere(A.length);
       double[] part = new double[A.length];
       for(int i = 0; i < A.length; i++) part[i] = e[A[i]];
       Vector params = sphere.getVectorInverse(part);
       for(int i = 1; i < params.DIM; i++) params.e[i] += rot[i-1];
       double[] result = sphere.getVector(params.e).e;
       for(int i = 0; i < A.length; i++) e[A[i]] = result[i];
    }
    
    /*public void rotate3D(double dtheta, double dphi)
    {
        if(v.length != 3) {System.out.println("Invalid rotation"); return;}
        
        double x = v[0]; 
        double y = v[1]; 
        double z = v[2];
        
        //System.out.println(this);
        double ro = Math.sqrt(x*x + y*y + z*z);
        double theta = arctan(y, x);
        double phi = arctan(z, Math.sqrt(x*x + y*y));
        
        v[0] = ro*Math.cos(theta + dtheta)*Math.cos(phi + dphi);
        v[1] = ro*Math.sin(theta + dtheta)*Math.cos(phi + dphi);
        v[2] = ro*Math.sin(phi + dphi);
    }*/

    
    
    public double dot(Vector u)
    {
        Vector w = u.clone();
        if(w.DIM > DIM) w = collapse(w, DIM);
        else if(w.DIM < DIM) w = expand(w, DIM);
        
        double result = 0;
        for(int i = 0; i < DIM; i++)
            result += this.e[i]*w.e[i];
        
        return result;
    }
    
    public boolean isEqualTo(Vector w)
    {
        if(w.DIM != DIM) return false;
        for(int i = 0; i < DIM; i++)
            if(w.e[i] != e[i])  
                return false;
        
        return true;
    }
    
    public boolean isEqualToR(Vector w)
    {
        if(w.DIM != DIM) return false;
        for(int i = 0; i < DIM; i++)
            if(round(this.e[i]) != round(w.e[i]))
                return false;
        
        return true;
    }

    @Override
    public boolean equals(Object obj) 
    {
        return this.toString().equals(obj.toString());
    }
    
    @Override
    public int hashCode() 
    {
        return this.toString().hashCode();
    }
    
    @Override
    public String toString()
    {
        return toStringR();
    }
    
    public String toFormattedString()
    {
        return "{" + toString() + "}";
    }
    
    public String toStringNR() // NOT ROUNDED
    {
        if(DIM <= 0) return "";
        String toPrint = "" + e[0]; //"(" + e[0];
        for(int i = 1; i < DIM; i++)
            toPrint += ("," + e[i]);
        toPrint += ""; //")";
        return toPrint;
    }
    
    public String toStringR() // ROUNDED
    {
        if(DIM <= 0) return "";
        String toPrint = "" + round(e[0]); 
        for(int i = 1; i < DIM; i++)
            toPrint += ("," + round(e[i]));
        toPrint += ""; 
        return toPrint;
    }
    
    public String toStringP() // PLAIN 
    {
        String toPrint = "";
        for(int i = 0; i < DIM; i++)
            toPrint += round(e[i]) + " ";
        return toPrint;
    }
    
    

    @Override
    public Vector clone()
    {
        double[] copy = new double[e.length];
        System.arraycopy(e, 0, copy, 0, DIM);
        return new Vector(copy);//, color);
    }

    @Override
    public int compareTo(Object o) {
        
        if(this.toString().equals(o.toString())) return 0;
        else return -1;
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public float round(double x)
    {
        return round(x, 4);
    }
    
    public float round(double x, int precision)
    {
        float p = (float)Math.pow(10, precision);
        return Math.round(x*p)/p;
    }
    
}
