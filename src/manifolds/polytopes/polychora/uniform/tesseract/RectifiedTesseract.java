package manifolds.polytopes.polychora.uniform.tesseract;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class RectifiedTesseract extends Polytope
{
    public RectifiedTesseract() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{0,sqrt(2),sqrt(2),sqrt(2)});
        connect(2.0);
    }

}
