package abstracts;

import framework.primitives.Vector;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Valente Productions
 */
public abstract class Polytope extends Manifold
{
    private HashMap<Vector, ArrayList<Vector>> cxn; 
    
    private double tol = 0.01; // tolerance for edge length
    
    public Polytope(int dim) 
    {
        super(dim);
        cxn = new HashMap<Vector, ArrayList<Vector>>();
    }
    
    public void register(double[] d, int base, int residue, boolean signChange, boolean permute)
    {
        for(int i = 0; i < Math.pow(2, DIMENSION); i++)
        {
            int[] signs = bitString(i, 2, DIMENSION);
            Vector w = new Vector(DIMENSION);
            for(int j = 0; j < DIMENSION; j++)
                w.e[j] = Math.pow(-1, signs[j])*d[j];
            if(i == 0 || signChange)
                if(permute)
                {
                    HashMap<Vector, Integer> permutations = permutationsOf(w);
                    for(Vector vertex : permutations.keySet()) 
                        if(!vertices.keySet().contains(vertex)
                        &&(permutations.get(vertex)%base == residue))
                                vertices.put(vertex, ++count);
                }
                else if(!vertices.keySet().contains(w))
                    vertices.put(w, ++count);
            
        }
    }
    
    public void registerAll(double[] d)
    {
        register(d, 1, 0, true, true);
    }
    
    public void registerPerms(double[] d)
    {
        register(d, 1, 0, false, true);
    }
    
    public void registerSigns(double[] d)
    {
        register(d, 1, 0, true, false);
    }
    
    public void registerOne(double[] d)
    {
        register(d, 1, 0, false, false);
    }
    
    public void registerEven(double[] d)
    {
        register(d, 2, 0, true, true);
    }
    
    public void registerOdd(double[] d)
    {
        register(d, 2, 1, true, true);
    }
    
    public void connect(double[] lengths)
    {
        for(Vector v0 : vertices.keySet())
        {
            if(!cxn.containsKey(v0))
                cxn.put(v0, new ArrayList<Vector>());
            for(Vector v1 : vertices.keySet())
            {
                double delta = v0.distanceTo(v1);
                for(int i = 0; i < lengths.length; i++)
                    if(lengths[i]-tol < delta && delta < lengths[i]+tol)
                        cxn.get(v0).add(v1);
            }
        }
    }
    
    public void connect(double length)
    {
        connect(new double[]{length});
    }

    @Override
    public ArrayList<Vector> connections(Vector v) 
    {
        return cxn.get(v);
    }
}
