package manifolds.polytopes.polychora.regular;

import abstracts.Polytope;

/**
 * The 120-cell
 * @author Valente Productions
 */
public class Hecatonicosachoron extends Polytope
{
    public Hecatonicosachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0.0, 0.0, 2.0, 2.0});
        registerAll(new double[]{1.0, 1.0, 1.0, Math.sqrt(5.0)});
        registerAll(new double[]{1.0/(PHI*PHI), PHI, PHI, PHI});
        registerAll(new double[]{1.0/PHI, 1.0/PHI, 1.0/PHI, PHI*PHI});
        registerEven(new double[]{0.0, 1.0/(PHI*PHI), 1.0, PHI*PHI});
        registerEven(new double[]{0.0, 1.0/PHI, PHI, Math.sqrt(5.0)});
        registerEven(new double[]{1.0/PHI, 1.0, PHI, 2.0});
        
        connect(0.76);
    }
}
