package manifolds.polytopes.polychora.regular;

import abstracts.Polytope;

/**
 * The 24-cell
 * @author Valente Productions
 */
public class Icositetrachoron extends Polytope
{
    public Icositetrachoron()
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0.5, 0.5, 0.5, 0.5});
        registerAll(new double[]{0.0, 0.0, 0.0, 1.0});
        connect(1.0); 
    }
    
    

}
