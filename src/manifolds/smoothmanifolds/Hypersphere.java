package manifolds.smoothmanifolds;

import framework.primitives.Matrix;
import abstracts.SmoothManifold;
import framework.*;
import java.awt.Color;
import framework.primitives.Vector;

/**
 *
 * @author Valente Productions
 */
public class Hypersphere extends SmoothManifold
{
    public Hypersphere(int DIM)
    {
        super(DIM, DIM-1, 0, 2*Math.PI);
        R = 5; // resolution
        if(DIM > 4) R = 5;
    }

    public Vector MAP(double[] theta, double radius)
    {
        return MAP(theta).scaledBy(radius);
    }
    
    public Vector MAP(Vector theta, double radius)
    {
        return MAP(theta.e, radius);
    }
    
    public Vector MAP(Vector theta)
    {
        return MAP(theta.e);
    }
    
    public float[] MAP(float[] theta, float radius)
    {
        float[] out = MAP(theta);
        float[] result = new float[out.length];
        for(int i = 0; i < out.length; i++)
            result[i] = out[i]*radius;
        return result;
    }
    
    @Override
    public Vector MAP(double[] thetas) // unit radius
    {
        double[] v = new double[DIMENSION];

        double ctheta = Math.cos(thetas[0]), 
              stheta = Math.sin(thetas[0]),  
              cphi = Math.cos(thetas[1]), 
              sphi = Math.sin(thetas[1]);
        
        if(thetas.length == 2)
            return new Vector(new double[]{ctheta*cphi, stheta*cphi, sphi});
        
        for(int k = 0; k < v.length; k++)
        {
            v[k] = 1.0;
            if(k > 0) v[k] = Math.sin(thetas[k-1]);
            
            for(int i = k; i < DIMENSION-1; i++)
                v[k] *= Math.cos(thetas[i]);
        }
        
        return new Vector(v, getColor(v));
    }
    
    public float[] MAP(float[] thetas) // unit radius
    {
        float[] image = new float[DIMENSION];

        float ctheta = (float)Math.cos(thetas[0]), 
              stheta = (float)Math.sin(thetas[0]),  
              cphi = (float)Math.cos(thetas[1]), 
              sphi = (float)Math.sin(thetas[1]);
        
        if(thetas.length == 2)
            return new float[]{ctheta*cphi, stheta*cphi, sphi};
        
        for(int k = 0; k < image.length; k++)
        {
            image[k] = 1;
            if(k > 0)  image[k] = (float)Math.sin(thetas[k-1]);
            for(int i = k; i < DIMENSION-1; i++)
                image[k] *= (float)Math.cos(thetas[i]);
        }
        
        return image;
    }
    
    public Vector INVERT(Vector w0) // unit radius
    {
        double[] x = w0.e;
        if(x.length != DIMENSION) return null;
        
        double radius = 0;
        for(int i = 0; i < x.length; i++)
            radius += x[i]*x[i];
        radius = Math.sqrt(radius);
        
        for(int i = 0; i < x.length; i++)
            if(x[i] == radius)
                switch(i)
                {
                    case 0: return new Vector(new double[]{0,0});                 // x+
                    case 1: return new Vector(new double[]{Math.PI/2.0, 0}); // y+
                    case 2: return new Vector(new double[]{0, Math.PI/2}); // z+
                    default: break;
                }
            else if(x[i] == -radius)
                switch(i)
                {
                    case 0: return new Vector(new double[]{Math.PI,0});                 // x-
                    case 1: return new Vector(new double[]{-Math.PI/2, 0}); // y-
                    case 2: return new Vector(new double[]{0, -Math.PI/2}); // z-
                    default: break;
                }
        
        double[] theta = new double[DIMENSION-1];
        theta[0] = arctan(x[1], x[0]); 
        for(int k = 2; k < DIMENSION; k++)
        {
            double psum = 0;
            for(int i = 0; i < k; i++) 
                psum += Math.pow(x[i],2);
            
            theta[k-1] = arctan(x[k], Math.sqrt(psum));
        }
        
        return new Vector(theta);
    }
    
    public float[] INVERT(float[] x) // unit radius
    {
        if(x.length != DIMENSION) return null;
        
        float radius = 0;
        for(int i = 0; i < x.length; i++)
            radius += x[i]*x[i];
        radius = (float)Math.sqrt(radius);
        for(int i = 0; i < x.length; i++)
            if(x[i] == radius)
                switch(i)
                {
                    case 0: return new float[]{0,0};                 // x+
                    case 1: return new float[]{(float)Math.PI/2, 0}; // y+
                    case 2: return new float[]{0, (float)Math.PI/2}; // z+
                    default: break;
                }
            else if(x[i] == -radius)
                switch(i)
                {
                    case 0: return new float[]{(float)Math.PI, 0};   // x-
                    case 1: return new float[]{-(float)Math.PI/2, 0}; // y-
                    case 2: return new float[]{0, -(float)Math.PI/2}; // z-
                    default: break;
                }
        
        float[] theta = new float[DIMENSION-1];
        theta[0] = arctan(x[1], x[0]); 
        
        for(int k = 2; k < DIMENSION; k++)
        {
            double psum = 0;
            for(int i = 0; i < k; i++) 
                psum += Math.pow(x[i],2);
            
            theta[k-1] = arctan(x[k],(float)Math.sqrt(psum));
        }
        
        return theta;
    }
    
    public Vector getVector(double[] theta) // with radius
    {
        double[] v = new double[DIMENSION];

        for(int k = 0; k < v.length; k++)
        {
            v[k] = theta[0];
            if(k > 0) v[k] *= Math.sin(theta[k]);
            
            for(int i = k+1; i < DIMENSION; i++)
                v[k] *= Math.cos(theta[i]);
        }
        
        return new Vector(v, getColor(v));
    }
    
    public Vector getVectorInverse(double[] x) // with radius
    {
        if(x.length != DIMENSION) return null;

        double[] t = new double[DIMENSION];
        
        double sum = 0;
        for(int i = 0; i < DIMENSION; i++)
            sum += Math.pow(x[i],2);
        t[0] = Math.sqrt(sum); // calculate radius
        t[1] = arctan(x[1], x[0]);
            
        for(int k = 2; k < DIMENSION; k++)
        {
            double psum = 0;
            for(int i = 0; i < k; i++) psum += Math.pow(x[i],2);
            t[k] = arctan(x[k], Math.sqrt(psum));
        }
        
        return new Vector(t);
    }
    
    @Override
    public Vector tangentAt(double[] param1, Vector param2) 
    {
        double[][] mat = new double[DIMENSION][DIMENSION-1];
        
        for(int k = 0; k < DIMENSION; k++)
        {
            for(int j = 0; j < DIMENSION-1; j++)
            {
                if(j <= k-2) 
                    mat[k][j] = 0.0;
                else 
                {
                    double product = 1.0;
                    for(int i = k; i < DIMENSION-1; i++) if(i != j) 
                        product *= Math.cos(param1[i]);
                    if(j == k-1)
                        product *= Math.cos(param1[k-1])*Math.cos(param1[j]);
                    else
                    {        
                        product *= -Math.sin(param1[j]);
                        if(k > 0) product *= Math.sin(param1[k-1]);
                    }
                    mat[k][j] = product;
                }
            }
        }
        Matrix M = new Matrix(mat);
        
        return M.map(param2);
    }
    
    private Color getColor(double[] theta)
    {
        //double value = slopeAt(theta, 0, 2);
        double value = theta[0];
        Color c1 = Color.BLUE;
        Color c2 = Color.GREEN;
        int red = (int)colorAt(value, c1.getRed(), c2.getRed());
        int green = (int)colorAt(value, c1.getGreen(), c2.getGreen());
        int blue = (int)colorAt(value, c1.getBlue(), c2.getBlue());
        return new Color(red, green, blue);
    }
}

/*@Override
    public void genVertices()  
    {
        int N = DIMENSION - 1;
        double Rdegree = 2*Math.PI/R;
        double Pdegree = 2*Math.PI/P;
        for(int t = 0; t < N; t++)
        {
            for(int a = 0; a < Math.pow(R, N-1); a++)
            {
                // Circle construction
                int[] alpha = bitString(a, R, N-1);
                double[] theta = new double[N];
                for(int j = 0; j < N; j++)
                {
                    if(j < t) theta[j] = alpha[j]*Rdegree;
                    if(j > t) theta[j] = alpha[j-1]*Rdegree;
                }
                
                Vector prev = null;
                Vector init = new Vector(N);
                for(double d = 0.0; d < 2*Math.PI; d += Pdegree)
                {
                    theta[t] = d;
                    Vector next = getVectorR1(theta);
                    if(d == 0.0) init = next;
                    vertices.put(next, count++);
                        //System.out.println(next);
                    ArrayList<Vector> cxns = new ArrayList<Vector>();
                    cxns.add(prev);
                    cxn.put(next, cxns);
                    if(prev != null) cxn.get(prev).add(next);
                    prev = next;
                }
                cxn.get(prev).add(init);
                cxn.get(init).add(prev);
            }
        }
    }*/
