package manifolds.polytopes.polychora.uniform.hecatonicosachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class RuncinatedHecatonicosachoron extends Polytope
{
    public RuncinatedHecatonicosachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{1,1,PHI3,3+4*PHI});
        registerAll(new double[]{PHI,PHI,PHI,2+5*PHI});
        registerAll(new double[]{PHI,2+PHI,PHI4,PHI4});
        registerAll(new double[]{PHI2,PHI2,1+3*PHI,3*PHI2});
        registerAll(new double[]{PHI2,1+3*PHI,1+3*PHI,1+3*PHI});
        registerAll(new double[]{PHI3,PHI3,PHI3,1+4*PHI});
        
        registerEven(new double[]{0,1,PHI4,3*PHI2});
        registerEven(new double[]{0,1,2+5*PHI,PHI2});
        registerEven(new double[]{0,2*PHI,2*PHI3,2*PHI2});
        registerEven(new double[]{0,PHI2,1+4*PHI,PHI4});
        registerEven(new double[]{0,PHI2,3+4*PHI,2+PHI});
        
        registerEven(new double[]{1,PHI,2*PHI3,1+3*PHI});
        registerEven(new double[]{1,2*PHI2,1+3*PHI,PHI4});
        registerEven(new double[]{PHI,PHI2,2*PHI,3+4*PHI});
        registerEven(new double[]{PHI,PHI3,3*PHI2,2*PHI2});
        registerEven(new double[]{PHI2,2*PHI,PHI3,2*PHI3});
        registerEven(new double[]{2*PHI,PHI3,PHI4,1+3*PHI});
        
        connect(2);
    }
}
