package manifolds.polytopes.polychora.regular;

import abstracts.Polytope;

/**
 * The 600-cell
 * @author Valente Productions
 */
public class Hexacosichoron extends Polytope
{
    public Hexacosichoron() 
    {
        super(4);
    }

    @Override
    public void genVertices()  
    {
        registerAll(new double[]{0.5, 0.5, 0.5, 0.5});
        registerAll(new double[]{0.0, 0.0, 0.0, 1.0});
        registerEven(new double[]{0.5, PHI/2, 1/(2*PHI), 0});
        connect(0.618); // 1/PHI
    }

    
    
}
