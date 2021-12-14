package obsolete;

import abstracts.Manifold;
import framework.primitives.Vector;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Valente Productions
 */
public abstract class Polychoron extends Manifold
{
    private HashMap<Vector, ArrayList<Vector>> cxn; 
    
    public Polychoron(int dim) 
    {
        super(dim);
        cxn = new HashMap<Vector, ArrayList<Vector>>();
    }
    
    public void registerAll(double[] d)
    {
        int[] limits = new int[4];
        for(int i = 0; i < 4; i++)
            if(d[i] == 0 ) limits[i] = 0; else limits[i] = 1;
       
        for(int i1 = -limits[0]; i1 <= limits[0]; i1 += 2)
        for(int i2 = -limits[1]; i2 <= limits[1]; i2 += 2)
        for(int i3 = -limits[2]; i3 <= limits[2]; i3 += 2)
        for(int i4 = -limits[3]; i4 <= limits[3]; i4 += 2)
        {
            Vector w = new Vector(
                    new double[]{i1*d[0], i2*d[1], i3*d[2], i4*d[3]});
            HashMap<Vector, Integer> permutations = permutationsOf(w);
            for(Vector vertex : permutations.keySet()) 
                vertices.put(vertex, ++count); 
        }
    }
    
    
    
    public void registerEven(double[] d)
    {
        
        int[] limits = new int[4];
        for(int i = 0; i < 4; i++)
            if(d[i] == 0 ) 
                limits[i] = 0; 
            else 
                limits[i] = 1;
        
        for(int i1 = -limits[0]; i1 <= limits[0]; i1 += 2)
        for(int i2 = -limits[1]; i2 <= limits[1]; i2 += 2)
        for(int i3 = -limits[2]; i3 <= limits[2]; i3 += 2)
        for(int i4 = -limits[3]; i4 <= limits[3]; i4 += 2)
        {
            Vector w = new Vector(
                    new double[]{i1*d[0], i2*d[1], i3*d[2], i4*d[3]});
            HashMap<Vector, Integer> permutations = permutationsOf(w);
            for(Vector vertex : permutations.keySet()) 
                if(permutations.get(vertex)%2 == 0) vertices.put(vertex, ++count); 
        }
    }
    
    public void registerOdd(double[] d)
    {
        
        int[] limits = new int[4];
        for(int i = 0; i < 4; i++)
            if(d[i] == 0 ) 
                limits[i] = 0; 
            else 
                limits[i] = 1;
        
        for(int i1 = -limits[0]; i1 <= limits[0]; i1 += 2)
        for(int i2 = -limits[1]; i2 <= limits[1]; i2 += 2)
        for(int i3 = -limits[2]; i3 <= limits[2]; i3 += 2)
        for(int i4 = -limits[3]; i4 <= limits[3]; i4 += 2)
        {
            Vector w = new Vector(
                    new double[]{i1*d[0], i2*d[1], i3*d[2], i4*d[3]});
            HashMap<Vector, Integer> permutations = permutationsOf(w);
            for(Vector vertex : permutations.keySet()) 
                if(permutations.get(vertex)%2 == 1) vertices.put(vertex, ++count); 
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

    @Override
    public ArrayList<Vector> connections(Vector v) 
    {
        return cxn.get(v);
    }
}
