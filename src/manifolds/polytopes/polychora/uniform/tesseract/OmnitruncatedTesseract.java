package manifolds.polytopes.polychora.uniform.tesseract;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class OmnitruncatedTesseract extends Polytope
{

    public OmnitruncatedTesseract() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{1.0,1+sqrt(2), 1+2*sqrt(2), 1+3*sqrt(2)});
        connect(2.0);
    }

}
