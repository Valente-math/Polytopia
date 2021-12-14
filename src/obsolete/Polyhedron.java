package obsolete;

import abstracts.Manifold;
import framework.primitives.Vector;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Valente Productions
 */
public abstract class Polyhedron extends Manifold
{
    private HashMap<Vector, ArrayList<Vector>> cxn; 
    
    public Polyhedron(int dim) 
    {
        super(dim);
        cxn = new HashMap<Vector, ArrayList<Vector>>();
    }
    
    public void register(double[] d)
    {
        int[] limits = new int[3];
        for(int i = 0; i < 3; i++)
            if(d[i] == 0 ) limits[i] = 0; else limits[i] = 1;
       
        for(int i1 = -limits[0]; i1 <= limits[0]; i1 += 2)
        for(int i2 = -limits[1]; i2 <= limits[1]; i2 += 2)
        for(int i3 = -limits[2]; i3 <= limits[2]; i3 += 2)
        {
            Vector w = new Vector(
                    new double[]{i1*d[0], i2*d[1], i3*d[2]});
            vertices.put(w, ++count); 
        }
    }
    
    public void connect(double d0, double d1)
    {
        for(Vector v1 : vertices.keySet())
        {
            cxn.put(v1, new ArrayList<Vector>());
            for(Vector v2 : vertices.keySet())
            {
                double delta = v1.distanceTo(v2);
                if(d0 < delta && delta < d1 )
                {
                        cxn.get(v1).add(v2);
                }
            }
        }
    }
    
    public void connect(double d0, double d1, double d3, double d4)
    {
        for(Vector v1 : vertices.keySet())
        {
            cxn.put(v1, new ArrayList<Vector>());
            for(Vector v2 : vertices.keySet())
            {
                double delta = v1.distanceTo(v2);
                if((d0 < delta && delta < d1 )||(d3 < delta && delta < d4 ))
                {
                        cxn.get(v1).add(v2);
                }
            }
        }
    }

    @Override
    public ArrayList<Vector> connections(Vector v) 
    {
        return cxn.get(v);
    }
}
