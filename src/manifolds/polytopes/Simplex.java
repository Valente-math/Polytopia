package manifolds.polytopes;

import framework.primitives.Vector;
import abstracts.Manifold;
import framework.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Valente Productions
 */
public class Simplex extends Manifold
{
    public Simplex(int dimension) 
    {
        super(dimension);
    }
    
    @Override
    public void genVertices() 
    {
        Vector[] vert = new Vector[DIMENSION+1];
        for(int n = 0; n < DIMENSION + 1; n++)
        {
            double[] c = new double[DIMENSION];
            Arrays.fill(c, 0.0);
            
            if(n == 0) c[0] = 0;
            if(n == 1) c[0] = 1;
            if(n >  1) 
            {
                c[0] = 0.5;

                for(int k = 1; k < n-1; k++)
                {
                    double sum = 0.0;
                    for(int i = 0; i < k-1; i++)
                        sum += 0.5*Math.pow(vert[k+1].e[i],2)-c[i]*vert[k+1].e[i];
                    c[k] = sum/vert[k+1].e[k] + 0.5*vert[k+1].e[k];
                }

                double sum = 0.0;
                for(int i = 0; i < n-1; i++)
                    sum += c[i]*c[i];
                c[n-1] = Math.sqrt(1 - sum);
            }
            vert[n] = new Vector(c);
        }
        for(int i = 0; i < DIMENSION; i++)
        {
            double sum = 0.0;
            for(int j = 0; j < DIMENSION+1; j++)
                sum += vert[j].e[i];
            center.e[i] = sum/((double)(DIMENSION+1));
        }
        for(int j = 0; j < DIMENSION+1; j++)
        {
            vertices.put(vert[j], j);
        }
    }

    @Override
    public ArrayList<Vector> connections(Vector v) 
    {
        ArrayList<Vector> temp = new ArrayList<Vector>();
        for(Vector w : vertices.keySet())
            if(!w.isEqualTo(v)) temp.add(w);
        return temp;
    }
    
}
