package manifolds.polytopes.polychora.uniform.hecatonicosachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class CantitruncatedHecatonicosachoron extends Polytope
{
    public CantitruncatedHecatonicosachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{1,1,1+4*PHI,5+10*PHI});
        registerAll(new double[]{1+3*PHI,3*PHI2,3+7*PHI,3+7*PHI});
        registerAll(new double[]{PHI4,PHI4,2+7*PHI,4+7*PHI});
        
        registerEven(new double[]{0,1,6+7*PHI,3+7*PHI});
        registerEven(new double[]{0,1,6+9*PHI,1+5*PHI});
        registerEven(new double[]{0,PHI2,PHI6,2+7*PHI});
        registerEven(new double[]{0,PHI2,7+8*PHI,2+5*PHI});
        registerEven(new double[]{0,2*PHI,6+8*PHI,2+6*PHI});
        
        registerEven(new double[]{1,4+5*PHI,2*PHI4,5*PHI2});
        registerEven(new double[]{1,2,5+7*PHI,4+7*PHI});
        registerEven(new double[]{1,2+PHI,4*PHI3,3+7*PHI});
        registerEven(new double[]{1,PHI3,7+8*PHI,3+4*PHI});
        registerEven(new double[]{1,PHI3,5+10*PHI,3+2*PHI});
        registerEven(new double[]{1,3*PHI,6+8*PHI,PHI5});
        registerEven(new double[]{1,PHI4,4*PHI3,5*PHI2});
        
        registerEven(new double[]{2,PHI2,5+10*PHI,PHI4});
        registerEven(new double[]{2,PHI3,6+9*PHI,3*PHI2});
        registerEven(new double[]{2*2*PHI2,4*PHI3,2*PHI4});
        registerEven(new double[]{2,1+3*PHI,PHI6,4+5*PHI});
        
        registerEven(new double[]{PHI2,PHI5,3+7*PHI,5*PHI2});
        registerEven(new double[]{PHI2,3*PHI,5+10*PHI,2*PHI2});
        registerEven(new double[]{PHI2,3*PHI2,5+7*PHI,5*PHI2});
        
        registerEven(new double[]{2*PHI,2+PHI,1+3*PHI,5+10*PHI});
        registerEven(new double[]{2*PHI,3+4*PHI,4+7*PHI,5*PHI2});
        registerEven(new double[]{2+PHI,3*PHI,6+9*PHI,PHI4});
        registerEven(new double[]{2+PHI,3+2*PHI,5+7*PHI,2*PHI4});
        registerEven(new double[]{2+PHI,PHI4,6+7*PHI,4+5*PHI});
        
        registerEven(new double[]{PHI3,PHI5,2+7*PHI,2*PHI4});
        registerEven(new double[]{PHI3,2+6*PHI,3+7*PHI,4+5*PHI});
        
        registerEven(new double[]{3*PHI,2+5*PHI,4+7*PHI,4+5*PHI});
        registerEven(new double[]{3*PHI,3+4*PHI,3+7*PHI,2*PHI4});
        
        registerEven(new double[]{2*PHI2,1+3*PHI,7+8*PHI,PHI4});
        registerEven(new double[]{2*PHI2,3+2*PHI,4+7*PHI,3+7*PHI});
        registerEven(new double[]{2*PHI2,1+4*PHI,6+7*PHI,PHI5});
        
        registerEven(new double[]{1+3*PHI,1+5*PHI,5+7*PHI,PHI5});
        registerEven(new double[]{3+2*PHI,PHI4,6+8*PHI,3*PHI2});
        registerEven(new double[]{3+2*PHI,1+4*PHI,PHI6,3+4*PHI});
        registerEven(new double[]{PHI4,1+4*PHI,5+7*PHI,2+6*PHI});
        registerEven(new double[]{PHI4,1+5*PHI,4*PHI3,3+4*PHI});
        registerEven(new double[]{1+4*PHI,3*PHI2,2+5*PHI,4*PHI3});
        connect(2);
    }
}
