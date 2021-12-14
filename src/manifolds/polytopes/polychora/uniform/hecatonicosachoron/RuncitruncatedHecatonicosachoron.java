package manifolds.polytopes.polychora.uniform.hecatonicosachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class RuncitruncatedHecatonicosachoron extends Polytope
{
    public RuncitruncatedHecatonicosachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,2,2*PHI4,2*PHI4});
        registerAll(new double[]{1,1,3*PHI3,5+6*PHI});
        registerAll(new double[]{1,1,1+4*PHI,PHI6});
        registerAll(new double[]{PHI3,3+2*PHI,3*PHI3,3*PHI3});
        registerAll(new double[]{2*PHI2,2*PHI2,2+6*PHI,2*PHI4});
        
        registerEven(new double[]{0,1,4+9*PHI,1+3*PHI});
        registerEven(new double[]{0,2+PHI,3+7*PHI,3*PHI3});
        registerEven(new double[]{0,PHI3,6+7*PHI,3*PHI2});
        registerEven(new double[]{0,3*PHI,5+7*PHI,3+4*PHI});
        
        registerEven(new double[]{1,4*PHI2,PHI5,4+5*PHI});
        registerEven(new double[]{1,PHI2,4+7*PHI,2+6*PHI});
        registerEven(new double[]{1,PHI2,6+7*PHI,2*PHI3});
        registerEven(new double[]{1,PHI2,4+9*PHI,2*PHI2});
        registerEven(new double[]{1,2*PHI,5+7*PHI,2+5*PHI});
        registerEven(new double[]{1,PHI3,PHI6,3+2*PHI});
        registerEven(new double[]{1,2*PHI2,3+7*PHI,4+5*PHI});
        registerEven(new double[]{1,1+3*PHI,4+7*PHI,4*PHI2});
        
        registerEven(new double[]{2,PHI2,PHI6,PHI4});
        registerEven(new double[]{2,PHI3,4+7*PHI,PHI5});
        
        registerEven(new double[]{PHI2,2+5*PHI,3*PHI3,4*PHI2});
        registerEven(new double[]{PHI2,2*PHI,4+9*PHI,PHI3});
        registerEven(new double[]{PHI2,3*PHI,PHI6,2*PHI2});
        registerEven(new double[]{PHI2,3+2*PHI,2*PHI4,4+5*PHI});
        registerEven(new double[]{PHI2,PHI4,5+6*PHI,4*PHI2});
        registerEven(new double[]{PHI2,3+4*PHI,2+6*PHI,4+5*PHI});
        
        registerEven(new double[]{2*PHI,2+PHI,1+3*PHI,PHI6});
        registerEven(new double[]{2*PHI,3*PHI2,3*PHI3,4+5*PHI});
        registerEven(new double[]{2*PHI,2*PHI3,2*PHI4,4*PHI2});
        registerEven(new double[]{2+PHI,2*PHI2,5+6*PHI,PHI5});
        
        registerEven(new double[]{PHI3,2+5*PHI,2+6*PHI,PHI5});
        registerEven(new double[]{PHI3,2*PHI2,1+3*PHI,6+7*PHI});
        registerEven(new double[]{PHI3,1+4*PHI,5+6*PHI,3+4*PHI});
        registerEven(new double[]{3*PHI,2*PHI3,3*PHI3,PHI5});
        
        registerEven(new double[]{2*PHI2,1+3*PHI,5+6*PHI,2+5*PHI});
        registerEven(new double[]{2*PHI2,3+2*PHI,PHI4,5+7*PHI});
        registerEven(new double[]{2*PHI2,1+4*PHI,4+7*PHI,3*PHI2});
        
        registerEven(new double[]{1+3*PHI,3+2*PHI,2*PHI3,4+7*PHI});
        registerEven(new double[]{1+3*PHI,PHI4,3*PHI3,2+6*PHI});
        registerEven(new double[]{1+3*PHI,1+4*PHI,2*PHI4,2+5*PHI});
        registerEven(new double[]{PHI4,1+4*PHI,3+7*PHI,2*PHI3});
        connect(2);
    }
}
