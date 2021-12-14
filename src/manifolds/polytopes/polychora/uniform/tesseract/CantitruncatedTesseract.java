package manifolds.polytopes.polychora.uniform.tesseract;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class CantitruncatedTesseract extends Polytope
{

    public CantitruncatedTesseract() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{1,1+sqrt(2),1+2*sqrt(2),1+2*sqrt(2)});
        connect(2);
    }

}
