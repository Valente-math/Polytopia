package manifolds.smoothmanifolds;

import framework.primitives.Vector;
import abstracts.SmoothManifold;
import framework.*;

/**
 *
 * @author Valente Productions
 */
public class Parametric extends SmoothManifold
{
    private static double bound = Math.PI/4;
    
    public Parametric() 
    {
        super(4, 3, -bound, bound);
        R = 4;
    }

    @Override
    public Vector tangentAt(double[] param1, Vector param2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector MAP(double[] params) 
    {
        double x = params[0], y = params[1], z = params[2];
        double pow = 2.0;
        return (new Vector(new double[]{
           
            x, y, z, x*x - y*y - z*z
            //cos(x), sin(x), cos(y), sin(y)
            /*cos(x)*cos(y)*cos(z) - sin(x)*sin(y)*sin(z),
            sin(x)*cos(y)*cos(z) + cos(x)*sin(y)*sin(z),
            cos(x)*sin(y)*cos(z) - sin(x)*cos(y)*sin(z),
            cos(x)*cos(y)*sin(z) + sin(x)*sin(y)*cos(z)*/
            
            //x, y, Math.cos(x)*Math.sin(y), Math.cos(y*Math.sin(x))
            //x, y, z, x*x + y*y - Math.sin(3*z)
            //x, y, z, Math.pow(x, pow) - Math.pow(y, pow) - Math.pow(z, pow)
            //x*cos(x), y*cos(y), z*cos(z), x*y*z*cos(x*y*z)
            //x*x - z*z, y*y*x*z - y, z*z*z - x*x*x, x*y
            //x, y, z, Math.pow(x, pow) - Math.pow(y, pow) - Math.pow(z, pow)
            //x, y, z, x*x - y*y - z*z
            //x, x*y, x*y*z, x - y - z
        }));//.scaledBy(0.5);
    }

    private double cos(double x) {
        return Math.cos(x);
    }
    
    private double sin(double x) {
        return Math.sin(x);
    }
}
