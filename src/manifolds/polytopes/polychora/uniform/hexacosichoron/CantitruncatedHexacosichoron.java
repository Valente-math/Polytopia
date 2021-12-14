package manifolds.polytopes.polychora.uniform.hexacosichoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class CantitruncatedHexacosichoron extends Polytope
{
    public CantitruncatedHexacosichoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{1,1,1+6*PHI});
        registerAll(new double[]{1,3,3+6*PHI,3+6*PHI});
        registerAll(new double[]{2,2,2+6*PHI,2*PHI4});
        
        registerEven(new double[]{0,1,3*PHI,3+9*PHI});
        registerEven(new double[]{0,1,5*PHI,5+7*PHI});
        registerEven(new double[]{0,2,4*PHI,4*PHI3});
        registerEven(new double[]{0,4+3*PHI,PHI5,5+4*PHI});
        registerEven(new double[]{0,3+4*PHI,4+5*PHI,5+3*PHI});
        
        registerEven(new double[]{1,PHI,6*PHI2,1+5*PHI});
        registerEven(new double[]{1,2*PHI,3+9*PHI,2+PHI});
        registerEven(new double[]{1,3+PHI,3*PHI,4*PHI3});
        registerEven(new double[]{1,3*PHI,6*PHI2,3*PHI2});
        registerEven(new double[]{1,3+2*PHI,3*PHI3,5+4*PHI});
        registerEven(new double[]{1,PHI4,2*PHI4,5+3*PHI});
        
        registerEven(new double[]{PHI,2,PHI3,3+9*PHI});
        registerEven(new double[]{PHI,3,1+3*PHI,4*PHI3});
        registerEven(new double[]{PHI,3*PHI2,2+6*PHI,5+4*PHI});
        registerEven(new double[]{PHI,2*PHI3,3*PHI3,5+3*PHI});
        
        registerEven(new double[]{2,2*PHI,6*PHI2,2*PHI3});
        registerEven(new double[]{2,3+PHI,3*PHI3,4+5*PHI});
        registerEven(new double[]{2,3*PHI,5+7*PHI,3+2*PHI});
        registerEven(new double[]{2,1+3*PHI,5+6*PHI,4+3*PHI});
        
        registerEven(new double[]{3,2*PHI,5+7*PHI,PHI4});
        registerEven(new double[]{3,2+PHI,2*PHI4,PHI5});
        registerEven(new double[]{3,PHI3,5+6*PHI,3+4*PHI});
        
        registerEven(new double[]{2*PHI,3*PHI2,1+6*PHI,4+5*PHI});
        registerEven(new double[]{2*PHI,4+3*PHI,1+5*PHI,3*PHI3});
        registerEven(new double[]{2+PHI,3+PHI,2+6*PHI,3*PHI3});
        registerEven(new double[]{2+PHI,1+3*PHI,6*PHI2,3+2*PHI});
        registerEven(new double[]{2+PHI,4*PHI,5+6*PHI,3*PHI2});
        
        registerEven(new double[]{PHI3,3+PHI,PHI4,6*PHI2});
        registerEven(new double[]{PHI3,3+2*PHI,1+6*PHI,3*PHI3});
        registerEven(new double[]{PHI3,3*PHI2,5*PHI,2*PHI4});
        
        registerEven(new double[]{3+PHI,3*PHI,5+6*PHI,2*PHI3});
        registerEven(new double[]{3*PHI,3+2*PHI,1+5*PHI,2*PHI4});
        registerEven(new double[]{2*PHI,2*PHI3,1+6*PHI,PHI5});
        registerEven(new double[]{3*PHI,1+5*PHI,2+6*PHI,3+4*PHI});
        
        registerEven(new double[]{1+3*PHI,PHI4,1+6*PHI,2+6*PHI});
        registerEven(new double[]{1+3*PHI,5*PHI,3*PHI3,2*PHI3});
        registerEven(new double[]{4*PHI,PHI4,1+5*PHI,3*PHI3});
        
        connect(2.0);
    }
}
