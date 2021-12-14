package framework.primitives;

import java.util.Set;

/**
 *
 * @author Valente Productions
 */
public class Matrix 
{
    public double[][] M;
    
    public Matrix(double[][] m)
    {
        M = m;
    }
    
    public Matrix(Set<Vector> set)
    {
        double[][] m = new double[set.size()][set.iterator().next().DIM];
        int count = 0;
        for(Vector w : set) m[count++] = w.e; // possible source of error, tbd
            
        M = m;
    }
    
    public Vector map(Vector v)
    {
        Vector V = v.clone();
        double[] result = new double[M.length];
        for(int i = 0; i < M.length; i++)
        {
            Vector W = new Vector(M[i]);
            result[i] = V.dot(W);
        }
        return new Vector(result);
    }
    
    public Matrix transpose()
    {
        int r = M.length;
        int c = M[0].length;
        double[][] n = new double[c][r];
        for(int _r = 0; _r < r; _r++)
            for(int _c = 0; _c < c; _c++)
                n[_c][_r] = M[_r][_c];
        return new Matrix(n);
    }
    
    @Override
    public String toString()
    {
        String s = "";
        for(int r = 0; r < M.length; r++)
        {
            for(int c = 0; c < M[r].length; c++)
                s += M[r][c] + "\t";
            s += "\n"; 
        }
        return s;
    }
    
    public void multiplyBy(Matrix b)
    {
        double[][] N = b.transpose().M;
        double[][] product = new double[M.length][N.length];
        for(int i = 0; i < M.length; i++)
            for(int j = 0; j < N.length; j++)
                product[i][j] = (new Vector(M[i])).dot(new Vector(N[j]));
        
        M = product;
    }
    
    public void add(Vector v)
    {
        // to be implemented
    }
}
