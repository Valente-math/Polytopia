package manifolds.polytopes.polychora.uniform.hexacosichoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class GrandAntiprism extends Polytope
{
    public GrandAntiprism() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerOne(new double[]{PHI,0,1,-PSI});
        registerOne(new double[]{-PHI,0,1,-PSI});
        registerOne(new double[]{-PHI,0,-1,PSI});
        registerOne(new double[]{PHI,0,-1,PSI});
        
        registerOne(new double[]{PSI,0,PHI,-1});
        registerOne(new double[]{-PSI,0,PHI,-1});
        registerOne(new double[]{-PSI,0,-PHI,1});
        registerOne(new double[]{PSI,0,-PHI,1});
        
        registerOne(new double[]{0,PHI,PSI,1});
        registerOne(new double[]{0,-PHI,PSI,1});
        registerOne(new double[]{0,-PHI,-PSI,-1});
        registerOne(new double[]{0,PHI,-PSI,-1});
        
        registerOne(new double[]{0,PSI,1,PHI});
        registerOne(new double[]{0,-PSI,1,PHI});
        registerOne(new double[]{0,-PSI,-1,-PHI});
        registerOne(new double[]{0,PSI,-1,-PHI});
        
        registerSigns(new double[]{0,0,2,0});
        registerSigns(new double[]{0,0,0,2});
        registerSigns(new double[]{1,1,1,1});
        registerSigns(new double[]{1,0,PSI,PHI});
        registerSigns(new double[]{0,PSI,1,PHI});
        registerSigns(new double[]{1,PSI,PHI,0});
        registerSigns(new double[]{PHI,1,PSI,0});
        registerSigns(new double[]{PSI,PHI,1,0});
        registerSigns(new double[]{PHI,PSI,0,1});
        registerSigns(new double[]{PSI,1,0,PHI});
        registerSigns(new double[]{1,PHI,0,PSI});
        connect(2.0/PHI);
    }
}
