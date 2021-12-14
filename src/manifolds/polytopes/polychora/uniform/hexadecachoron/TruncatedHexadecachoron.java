package manifolds.polytopes.polychora.uniform.hexadecachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class TruncatedHexadecachoron extends Polytope
{
    public TruncatedHexadecachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,0,1,2});
        connect(2*sqrt(2));
    }
}
