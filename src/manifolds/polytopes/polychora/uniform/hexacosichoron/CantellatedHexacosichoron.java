package manifolds.polytopes.polychora.uniform.hexacosichoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class CantellatedHexacosichoron extends Polytope
{
    public CantellatedHexacosichoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,0,2*PHI,2+6*PHI});
        registerAll(new double[]{0,2,2*PHI3,2*PHI3});
        registerAll(new double[]{1,1,1+4*PHI,3+4*PHI});
        registerAll(new double[]{PHI3,PHI3,1+4*PHI,1+4*PHI});
        
        registerEven(new double[]{0,1,3*PHI,PHI5});
        registerEven(new double[]{0,2*PHI,4*PHI2,2*PHI2});
        registerEven(new double[]{0,3+2*PHI,PHI4,3*PHI2});
        registerEven(new double[]{1,PHI,2+6*PHI,PHI2});
        registerEven(new double[]{1,PHI,4*PHI2,1+3*PHI});
        registerEven(new double[]{1,2*PHI,PHI5,2+PHI});
        registerEven(new double[]{1,2+PHI,2*PHI3,3*PHI2});
        registerEven(new double[]{1,PHI3,3+4*PHI,3+2*PHI});
        registerEven(new double[]{PHI,2,PHI3,PHI5});
        
        registerEven(new double[]{PHI,2*PHI2,1+4*PHI,3*PHI2});
        registerEven(new double[]{PHI,1+3*PHI,2*PHI3,3+2*PHI});
        registerEven(new double[]{2,PHI2,3+4*PHI,PHI4});
        registerEven(new double[]{PHI2,2+PHI,PHI3,4*PHI2});
        registerEven(new double[]{PHI2,2+PHI,1+4*PHI,2*PHI3});
        registerEven(new double[]{PHI2,3*PHI,3+4*PHI,2*PHI2});
        registerEven(new double[]{2*PHI,2+PHI,1+3*PHI,3+4*PHI});
        registerEven(new double[]{2*PHI,1+3*PHI,1+4*PHI,PHI4});
        registerEven(new double[]{PHI3,3*PHI,2*PHI3,1+3*PHI});
        connect(2.0);
    }
}
