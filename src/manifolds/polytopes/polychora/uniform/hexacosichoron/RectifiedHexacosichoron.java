package manifolds.polytopes.polychora.uniform.hexacosichoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class RectifiedHexacosichoron extends Polytope
{
    public RectifiedHexacosichoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,0,2*PHI,2*PHI2});
        registerAll(new double[]{1,1,PHI3,PHI3});
        registerEven(new double[]{0,1,PHI,1+3*PHI});
        registerEven(new double[]{0,PHI2,PHI3,2+PHI});
        registerEven(new double[]{1,PHI,2*PHI2,PHI2});
        registerEven(new double[]{PHI,PHI2,2*PHI,PHI3});
        connect(2);
    }
}
