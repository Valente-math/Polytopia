package manifolds.polytopes.polychora.uniform.hexadecachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class CantellatedHexadecachoron extends Polytope
{
    public CantellatedHexadecachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,1,1,2});
        connect(sqrt(2));
    }
}
