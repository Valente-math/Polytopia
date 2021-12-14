package manifolds.polytopes.polychora.uniform.icositetrachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class OmnitruncatedIcositetrachoron extends Polytope
{
    public OmnitruncatedIcositetrachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{1,1+sqrt(2),1+2*sqrt(2),5+3*sqrt(2)});
        registerAll(new double[]{1,3+sqrt(2),3+2*sqrt(2),3+3*sqrt(2)});
        registerAll(new double[]{2,2+sqrt(2),2+2*sqrt(2),4+3*sqrt(2)});
        connect(2);
    }
}
