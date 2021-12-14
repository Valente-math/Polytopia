package manifolds.polytopes.polychora.uniform.hecatonicosachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class TruncatedHecatonicosachoron extends Polytope
{
    public TruncatedHecatonicosachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{1,3+4*PHI,3+4*PHI,3+4*PHI});
        registerAll(new double[]{PHI3,PHI3,PHI3,5+6*PHI});
        registerAll(new double[]{2*PHI2,2*PHI2,2*PHI2,2*PHI4});
        
        registerEven(new double[]{0,1,4+5*PHI,PHI5});
        registerEven(new double[]{0,1,4+7*PHI,1+3*PHI});
        registerEven(new double[]{0,PHI2,3*PHI3,2+5*PHI});
        registerEven(new double[]{0,PHI2,5+6*PHI,PHI4});
        registerEven(new double[]{0,2*PHI,2*PHI4,2*PHI3});
        registerEven(new double[]{1,PHI2,4+7*PHI,2*PHI2});
        registerEven(new double[]{1,PHI3,3*PHI3,3+4*PHI});
        
        registerEven(new double[]{PHI2,2*PHI,4+7*PHI,PHI3});
        registerEven(new double[]{PHI2,2*PHI2,4+5*PHI,3+4*PHI});
        registerEven(new double[]{PHI2,2*PHI3,2+5*PHI,3+4*PHI});
        registerEven(new double[]{2*PHI,PHI4,PHI5,3+4*PHI});
        registerEven(new double[]{PHI3,2*PHI2,PHI5,2+5*PHI});
        registerEven(new double[]{PHI3,1+3*PHI,4+5*PHI,2*PHI3});
        registerEven(new double[]{2*PHI2,1+3*PHI,3*PHI3,PHI4});
        
        connect(2);
    }
}
