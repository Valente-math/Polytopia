package manifolds.polytopes.polyhedra;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class Icosahedron extends Polytope
{
    public Icosahedron()
    {
        super(3);
    }
    
    @Override
    public void genVertices() 
    {
        registerSigns(new double[]{0,1.0,PHI});
        registerSigns(new double[]{1.0,PHI,0});
        registerSigns(new double[]{PHI,0,1.0});
        
        connect(2.0);
    }
}
