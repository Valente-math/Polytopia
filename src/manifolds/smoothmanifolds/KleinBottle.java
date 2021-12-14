package manifolds.smoothmanifolds;

import abstracts.SmoothManifold;
import framework.primitives.Vector;

/**
 *
 * @author Valente Productions
 */

public class KleinBottle extends SmoothManifold
{
    public KleinBottle() 
    {
        super(4, 3, 0, 2*Math.PI);
        R = 12;
        P = 42;
    }
    
    public Vector MAP(double[] params)
    {
        double u = params[0], v = params[1];
        double r = 1.0;
        double a = 1.0;
        
        return new Vector(new double[]{
            Math.cos(u)*(r*Math.cos(v)+a),
            Math.sin(u)*(r*Math.cos(v)+a),
            r*Math.sin(v)*Math.cos(u/2.0),
            r*Math.sin(v)*Math.sin(u/2.0)
        });
    }

    @Override
    public Vector tangentAt(double[] param1, Vector param2) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
