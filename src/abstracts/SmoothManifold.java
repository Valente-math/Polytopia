package abstracts;

import framework.primitives.Vector;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Valente Productions
 */
public abstract class SmoothManifold extends Manifold
{
    public int R = 5; // Mesh Resolution
    public int P = 32; // Smoothness/Precision
    
    public HashMap<Vector, ArrayList<Vector>> cxn; 
    
    public double D1;
    public double D2;
    public double DOMAIN;
    public int RANK; // number of variables
    
    public abstract Vector tangentAt(double[] param1, Vector param2);
    
    public abstract Vector MAP(double[] params);
    
    public SmoothManifold(int dimension, int rank, double d1, double d2)//, boolean closed)
    {
        super(dimension);
        RANK = rank;
        D1 = d1;
        D2 = d2;
        DOMAIN = d2 - d1;
        cxn = new HashMap<Vector, ArrayList<Vector>>();
    }
    
    @Override
    public void genVertices()  
    {
        double Rdegree = DOMAIN/R;
        double Pdegree = DOMAIN/P;
        
        for(int p = 0; p < RANK; p++)
        {
            for(int a = 0; a < Math.pow(R+1, RANK-1); a++)
            {
                int[] alpha = bitString(a, R+1, RANK-1);
                double[] params = new double[RANK]; 
                
                for(int j = 0; j < RANK; j++)
                {
                    if(j < p) params[j] = alpha[j]*Rdegree + D1;
                    if(j > p) params[j] = alpha[j-1]*Rdegree + D1;
                }
                
                Vector prev = null;
                for(double d = D1; d <= 1.001*D2; d += Pdegree)
                {
                    params[p] = d;
                    Vector next = MAP(params);
                    vertices.put(next, count++);
                    
                    ArrayList<Vector> cxns = new ArrayList<Vector>();
                    cxns.add(prev);
                    cxn.put(next, cxns);
                    if(prev != null) cxn.get(prev).add(next);
                    prev = next;
                }
            }
        }
    }
    
    @Override
    public ArrayList<Vector> connections(Vector v) 
    {
        return cxn.get(v);
    }
    
    public double slopeAt(double[] param1, int u1, int u2)
    {
        Vector x0 = tangentAt(param1, new Vector(param1.length));
        Vector x1 = tangentAt(param1, new Vector(param1.length, 1));
        
        return (x0.e[u1] - x1.e[u1])/(x0.e[u2] - x1.e[u2]); 
    }
}
