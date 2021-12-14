                package manifolds.polytopes;

import framework.primitives.Vector;
import abstracts.Manifold;
import framework.*;
import java.util.ArrayList;

/**
 *
 * @author Valente Productions
 */
public class Orthoplex extends Manifold
{
    public Orthoplex(int dim) 
    {
        super(dim);
    }
    
    @Override
    public void genVertices()
    {
        for(int i = 0; i < DIMENSION; i++)
        {
            double[] coord1 = new double[DIMENSION];
            double[] coord2 = new double[DIMENSION];
            
            for(int j = 0; j < DIMENSION; j++)
                if(j != i) 
                    coord1[j] = coord1[j] = 0;
                else 
                {
                    coord1[j] = 1.0;
                    coord2[j] = -1.0;
                }
            vertices.put(new Vector(coord1), ++count);
            vertices.put(new Vector(coord2), ++count);
        }
    }

    @Override
    public ArrayList<Vector> connections(Vector v) 
    {
        ArrayList<Vector> temp = new ArrayList<Vector>();
        for(Vector w : vertices.keySet())
            if(!w.isEqualTo(v)&&!w.isEqualTo(v.scaledBy(-1.0))) temp.add(w);
        return temp;
    }
}
