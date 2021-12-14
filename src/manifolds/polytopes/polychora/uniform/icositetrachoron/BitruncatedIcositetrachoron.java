package manifolds.polytopes.polychora.uniform.icositetrachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class BitruncatedIcositetrachoron extends Polytope
{
    public BitruncatedIcositetrachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,2+sqrt(2),2+sqrt(2),2+2*sqrt(2)});
        registerAll(new double[]{1,1+sqrt(2),1+sqrt(2),3+2*sqrt(2)});
        connect(2.0);
    }
}
