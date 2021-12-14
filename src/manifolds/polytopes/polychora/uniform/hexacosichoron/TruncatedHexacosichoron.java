package manifolds.polytopes.polychora.uniform.hexacosichoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class TruncatedHexacosichoron extends Polytope
{
    public TruncatedHexacosichoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerEven(new double[]{0,1,PHI,1+5*PHI});
        registerEven(new double[]{0,1,3*PHI,3*PHI2});
        registerEven(new double[]{0,2,2*PHI,2*PHI3});
        registerEven(new double[]{0,2+PHI,1+3*PHI,3+2*PHI});
        registerEven(new double[]{0,PHI3,PHI4,3+PHI});
        
        registerEven(new double[]{1,2,1+3*PHI,PHI4});
        registerEven(new double[]{1,2*PHI,3*PHI2,2+PHI});
        registerEven(new double[]{PHI,2,PHI3,3*PHI2});
        registerEven(new double[]{PHI,2+PHI,3*PHI,PHI4});
        registerEven(new double[]{2*PHI,PHI3,3*PHI,1+3*PHI});
        connect(2.0);
    }
}
