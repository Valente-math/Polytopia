package manifolds.polytopes.polychora.uniform.hexadecachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class CantitruncatedHexadecachoron extends Polytope
{
    public CantitruncatedHexadecachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,1,2,3});
        connect(sqrt(2));
    }
}
