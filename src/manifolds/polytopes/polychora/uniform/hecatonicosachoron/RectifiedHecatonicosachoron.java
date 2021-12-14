package manifolds.polytopes.polychora.uniform.hecatonicosachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class RectifiedHecatonicosachoron extends Polytope
{
    public RectifiedHecatonicosachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,0,2*PHI,2*PHI3});
        registerAll(new double[]{0,2*PHI2,2*PHI2,2*PHI2});
        registerAll(new double[]{PHI2,PHI2,PHI2,3*PHI2});
        registerAll(new double[]{PHI2,PHI2,1+3*PHI,1+3*PHI});
        
        registerEven(new double[]{0,1,PHI4,1+3*PHI});
        registerEven(new double[]{0,PHI,3*PHI2,PHI3});
        registerEven(new double[]{1,PHI,2*PHI3,PHI2});
        registerEven(new double[]{1,PHI2,PHI4,2*PHI2});
        registerEven(new double[]{PHI,PHI3,1+3*PHI,2*PHI2});
        registerEven(new double[]{PHI2,2*PHI,PHI4,PHI3});
        connect(2);
    }
}
