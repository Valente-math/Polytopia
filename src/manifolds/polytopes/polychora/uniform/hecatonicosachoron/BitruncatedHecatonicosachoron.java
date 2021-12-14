package manifolds.polytopes.polychora.uniform.hecatonicosachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class BitruncatedHecatonicosachoron extends Polytope
{
    public BitruncatedHecatonicosachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,0,4*PHI,2*PHI4});
        registerAll(new double[]{2+PHI,2+PHI,2+5*PHI,2+5*PHI});
        
        registerEven(new double[]{0,1,4+5*PHI,1+5*PHI});
        registerEven(new double[]{0,1,3*PHI,3+7*PHI});
        registerEven(new double[]{0,PHI,5*PHI2,1+4*PHI});
        registerEven(new double[]{0,3*PHI2,3+4*PHI,4+3*PHI});
        registerEven(new double[]{1,2*PHI,3+7*PHI,2+PHI});
        registerEven(new double[]{1,2*PHI,5*PHI2,PHI4});
        registerEven(new double[]{1,2,PHI5,2+5*PHI});
        registerEven(new double[]{1,2*PHI2,PHI5,4+3*PHI});
        registerEven(new double[]{PHI,2,PHI3,3+7*PHI});
        registerEven(new double[]{PHI,PHI4,2+5*PHI,4+3*PHI});
        
        registerEven(new double[]{2*PHI,1+4*PHI,2+5*PHI,3*PHI2});
        registerEven(new double[]{2*PHI,PHI4,1+5*PHI,3+4*PHI});
        registerEven(new double[]{2,2*PHI,2*PHI4,2*PHI2});
        registerEven(new double[]{2,PHI3,4+5*PHI,3*PHI2});
        registerEven(new double[]{2,2+PHI,PHI5,3+4*PHI});
        registerEven(new double[]{PHI3,4*PHI,PHI5,PHI4});
        registerEven(new double[]{PHI3,2*PHI2,1+5*PHI,2+5*PHI});
        registerEven(new double[]{3*PHI,2*PHI2,1+4*PHI,PHI5});
        registerEven(new double[]{2+PHI,PHI3,5*PHI2,2*PHI2});
        registerEven(new double[]{2+PHI,3*PHI,4+5*PHI,PHI4});
        
        connect(2);
    }
}
