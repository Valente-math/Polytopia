package manifolds.smoothmanifolds;

import abstracts.SmoothManifold;
import framework.primitives.Vector;

/**
 *
 * @author Valente Productions
 */
public class RP2 extends SmoothManifold
{
    private Hypersphere sphere;
    
    public RP2() 
    {
        super(4, 3, 0, Math.PI);
        sphere = new Hypersphere(3);
        R = 16;
        P = 48;
    }

    public Vector MAP(double[] params)
    {
       
        Vector w = sphere.MAP(new double[]{params[0], params[1]});//params);
        double x = w.e[0];
        double y = w.e[1];
        double z = w.e[2];
        return new Vector(new double[]{x*x-y*y, x*y, x*z, y*z});
    }
    
    private double slope_TPRP2(double theta, double phi, int u1, int u2) // Slope along the u1xu2 plane
    {
        Vector x0 = TPRP2(theta, phi, 0, 0);
        Vector x1 = TPRP2(theta, phi, 1, 1);
        
        return (x0.e[u1] - x1.e[u1])/(x0.e[u2] - x1.e[u2]);        
    }
    
    private Vector TPRP2(double theta, double phi, double x, double y)
    {
        double W = Math.cos(2.0*theta)*Math.sin(phi)*Math.sin(phi)*x + 
                0.5*Math.sin(2*theta)*Math.sin(2*phi)*y;
        double X = -0.5*Math.sin(theta)*Math.sin(2.0*phi)*x +
                Math.cos(theta)*Math.cos(2*phi)*y;
        double Y = Math.sin(2*theta)*Math.sin(phi)*Math.sin(phi)*x - 
                Math.cos(theta)*Math.cos(theta)*Math.sin(2*phi)*y;
        double Z = Math.cos(theta)*Math.sin(2*phi)*x + 2*Math.sin(theta)*Math.cos(2*phi)*y;
        
        return new Vector(new double[]{W,X,Y,Z});
    }

    public Vector tangentAt(double[] param1, Vector param2) 
    {
        return TPRP2(param1[0], param1[1], param2.e[0], param2.e[1]);
    }
}
