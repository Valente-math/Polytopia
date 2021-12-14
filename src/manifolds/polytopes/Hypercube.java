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
public class Hypercube extends Manifold
{
    private int BASE;
    
    public Hypercube(int dimension, int base) 
    {
        super(dimension);
        BASE = base;
    }
    
    public Hypercube(int dimension) 
    {
        this(dimension, 2);
    }
    
    @Override
    public void genVertices() 
    {
        BASE = 2;
        Arrays.fill(center.e, 0.5*(BASE-1));
        
        for(int i = 0; i < (int)Math.pow(BASE, DIMENSION); i++)
            vertices.put((new Vector(bitString(i, BASE, DIMENSION))), new Integer(i));
    }
    
    public ArrayList<Vector> connections2(Vector w) 
    {
        return new ArrayList((this.vertices.keySet()));
    }
    
    public ArrayList<Vector> connections(Vector w) 
    {
        ArrayList<Vector> cxn = new ArrayList<Vector>();
        
        for(int j = 0; j < DIMENSION; j++)
        {
            double[] temp = new double[DIMENSION];
            for(int k = 0; k < DIMENSION; k++)
                if((k == j))
                    if(w.e[k] == (BASE - 1)) 
                        temp[k] = 0; //w.v[k];
                    else 
                        temp[k] = w.e[k]+1;
                else
                    temp[k] = w.e[k];
            
            cxn.add(new Vector(temp));
        }
        
        return cxn;
    }
}
