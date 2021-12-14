package manifolds.polytopes.polychora.uniform.hexacosichoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class RuncitruncatedHexacosichoron extends Polytope
{
    public RuncitruncatedHexacosichoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{1,1,PHI3,3+8*PHI});
        registerAll(new double[]{1,1,1+4*PHI,5+6*PHI});
        registerAll(new double[]{PHI2,3+PHI,PHI5,PHI5});
        registerAll(new double[]{2+PHI,2+PHI,2+5*PHI,4+5*PHI});
        registerAll(new double[]{3*PHI,PHI4,2+5*PHI,2+5*PHI});
        registerAll(new double[]{1+3*PHI,1+3*PHI,1+5*PHI,PHI5});
        
        registerEven(new double[]{0,1,2+5*PHI,5*PHI2});
        registerEven(new double[]{0,PHI2,3+8*PHI,2+PHI});
        registerEven(new double[]{0,PHI3,4+7*PHI,3+PHI});
        registerEven(new double[]{0,2*PHI2,2+6*PHI,4*PHI2});
        registerEven(new double[]{0,1+3*PHI,3*PHI3,4+3*PHI});
        
        registerEven(new double[]{1,PHI,2*PHI4,1+5*PHI});
        registerEven(new double[]{1,2,PHI5,4+5*PHI});
        registerEven(new double[]{1,2,1+3*PHI,4+7*PHI});
        registerEven(new double[]{1,2+PHI,2+6*PHI,PHI5});
        registerEven(new double[]{1,PHI3,5+6*PHI,3+2*PHI});
        registerEven(new double[]{1,3*PHI,2*PHI4,3*PHI2});
        registerEven(new double[]{1,3*PHI2,2+5*PHI,4*PHI2});
        registerEven(new double[]{1,2*PHI3,PHI5,4+3*PHI});
        
        registerEven(new double[]{PHI,PHI2,2*PHI,3+8*PHI});
        registerEven(new double[]{PHI,2+PHI,3*PHI,4+7*PHI});
        registerEven(new double[]{PHI,3+2*PHI,PHI5,4*PHI2});
        registerEven(new double[]{PHI,PHI4,4+5*PHI,4+3*PHI});
        
        registerEven(new double[]{2,PHI2,3*PHI3,2+5*PHI});
        registerEven(new double[]{2,PHI2,5+6*PHI,PHI4});
        registerEven(new double[]{2,2*PHI,2*PHI4,2*PHI3});
        
        registerEven(new double[]{PHI2,3*PHI,5+6*PHI,2*PHI2});
        registerEven(new double[]{PHI2,1+3*PHI,5*PHI2,3*PHI2});
        registerEven(new double[]{PHI2,3*PHI2,1+5*PHI,PHI5});
        
        registerEven(new double[]{2*PHI,2+PHI,1+3*PHI,5+6*PHI});
        registerEven(new double[]{2*PHI,3+2*PHI,2+5*PHI,PHI5});
        registerEven(new double[]{2*PHI,1+4*PHI,4+5*PHI,3*PHI2});
        registerEven(new double[]{2+PHI,PHI3,5*PHI2,2*PHI3});
        registerEven(new double[]{2+PHI,1+3*PHI,2*PHI4,3+2*PHI});
        
        registerEven(new double[]{PHI3,3+PHI,PHI4,2*PHI4});
        registerEven(new double[]{PHI3,2*PHI2,1+5*PHI,4+5*PHI});
        registerEven(new double[]{PHI3,3+2*PHI,1+4*PHI,3*PHI3});
        registerEven(new double[]{PHI3,2*PHI3,1+5*PHI,2+5*PHI});
        registerEven(new double[]{3*PHI,1+4*PHI,PHI5,2*PHI3});
        registerEven(new double[]{1+3*PHI,PHI4,1+4*PHI,2+6*PHI});
        
        connect(2);
    }
}
