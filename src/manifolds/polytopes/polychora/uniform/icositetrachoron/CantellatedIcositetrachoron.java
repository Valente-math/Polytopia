package manifolds.polytopes.polychora.uniform.icositetrachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class CantellatedIcositetrachoron extends Polytope
{
    public CantellatedIcositetrachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,sqrt(2),sqrt(2),2+sqrt(2)});
        registerAll(new double[]{1,1+sqrt(2),1+sqrt(2),1+2*sqrt(2)});
        connect(2.0);
    }
}
