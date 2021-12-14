package manifolds.polytopes.polychora.uniform.hecatonicosachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class CantellatedHecatonicosachoron extends Polytope
{
    public CantellatedHecatonicosachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,0,2*PHI3,4*PHI2});
        registerAll(new double[]{1,1,PHI3,3*PHI3});
        registerAll(new double[]{1,1,3+4*PHI,3+4*PHI});
        registerAll(new double[]{2*PHI,2*PHI2,2*PHI3,2*PHI3});
        registerAll(new double[]{PHI3,PHI3,1+4*PHI,3+4*PHI});
        
        registerEven(new double[]{0,1,4+5*PHI,1+3*PHI});
        registerEven(new double[]{0,PHI,PHI5,1+4*PHI});
        registerEven(new double[]{0,PHI2,3*PHI3,2+PHI});
        registerEven(new double[]{0,PHI3,2+5*PHI,3*PHI2});
        registerEven(new double[]{1,PHI2,2+5*PHI,2*PHI3});
        registerEven(new double[]{1,PHI2,4+5*PHI,2*PHI2});
        registerEven(new double[]{1,2*PHI,PHI5,PHI4});
        registerEven(new double[]{1,PHI4,2*PHI3,3*PHI2});
        registerEven(new double[]{PHI,PHI2,2*PHI,3*PHI3});
        
        registerEven(new double[]{PHI,2*PHI2,3+4*PHI,3*PHI2});
        registerEven(new double[]{PHI2,2*PHI,4+5*PHI,PHI3});
        registerEven(new double[]{PHI2,2+PHI,3+4*PHI,2*PHI3});
        registerEven(new double[]{PHI2,PHI3,4*PHI2,PHI4});
        registerEven(new double[]{PHI2,PHI4,1+4*PHI,2*PHI3});
        registerEven(new double[]{2*PHI,1+3*PHI,3+4*PHI,PHI4});
        registerEven(new double[]{2+PHI,PHI3,PHI5,2*PHI2});
        registerEven(new double[]{PHI3,2*PHI2,1+3*PHI,2+5*PHI});
        connect(2.0);
    }
}
