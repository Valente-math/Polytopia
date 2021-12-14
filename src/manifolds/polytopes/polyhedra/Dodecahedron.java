package manifolds.polytopes.polyhedra;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class Dodecahedron extends Polytope
{
    public Dodecahedron()
    {
        super(3);
    }
    
    @Override
    public void genVertices() 
    {
        registerSigns(new double[]{1,1,1,1});
        registerSigns(new double[]{0,1.0/PHI,PHI});
        registerSigns(new double[]{1.0/PHI,PHI,0});
        registerSigns(new double[]{PHI,0,1.0/PHI});
        
        connect(2.0/PHI);
    }
    
}
