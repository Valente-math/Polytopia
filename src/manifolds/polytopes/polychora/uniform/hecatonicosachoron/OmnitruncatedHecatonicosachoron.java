package manifolds.polytopes.polychora.uniform.hecatonicosachoron;

import abstracts.Polytope;

/**
 *
 * @author Nicholas
 */
public class OmnitruncatedHecatonicosachoron extends Polytope
{
    public OmnitruncatedHecatonicosachoron() 
    {
        super(4);
    }

    @Override
    public void genVertices() 
    {
        registerAll(new double[]{1,1,1+6*PHI,7+10*PHI});
        registerAll(new double[]{1,1,3+8*PHI,7+8*PHI});
        registerAll(new double[]{1,1,1+4*PHI,5+12*PHI});
        registerAll(new double[]{1,3,PHI6,PHI6});
        
        registerAll(new double[]{2,2,4*PHI3,6+8*PHI});
        registerAll(new double[]{2*PHI2,4+2*PHI,4*PHI3,4*PHI3});
        registerAll(new double[]{3+2*PHI,3+2*PHI,3+8*PHI,PHI6});
        registerAll(new double[]{1+4*PHI,3+4*PHI,3+8*PHI,3+8*PHI});
        registerAll(new double[]{2*PHI3,2*PHI3,2+8*PHI,4*PHI3});
        
        registerEven(new double[]{1,5*PHI2,4+7*PHI,6*PHI2});
        registerEven(new double[]{1,2*PHI4,5+7*PHI,6+5*PHI});
        registerEven(new double[]{1,2,1+5*PHI,6+11*PHI});
        registerEven(new double[]{1,PHI2,6+9*PHI,2+8*PHI});
        registerEven(new double[]{1,PHI2,8+9*PHI,2+6*PHI});
        registerEven(new double[]{1,2*PHI,7+9*PHI,2+7*PHI});
        registerEven(new double[]{1,PHI3,5+12*PHI,3+2*PHI});
        registerEven(new double[]{1,3+PHI,4+9*PHI,4*PHI3});
        registerEven(new double[]{1,1+3*PHI,8+9*PHI,4*PHI2});
        registerEven(new double[]{1,1+3*PHI,6+11*PHI,4+2*PHI});
        registerEven(new double[]{1,4*PHI,7+9*PHI,4+5*PHI});
        registerEven(new double[]{1,3*PHI2,4+9*PHI,6*PHI2});
        registerEven(new double[]{1,2*PHI3,5+9*PHI,6+5*PHI});
        
        registerEven(new double[]{2,PHI2,5+12*PHI,PHI4});
        registerEven(new double[]{2,2+PHI,5+9*PHI,3+8*PHI});
        registerEven(new double[]{2,PHI3,8+9*PHI,PHI5});
        registerEven(new double[]{2,3*PHI,7+9*PHI,3*PHI3});
        registerEven(new double[]{2,1+3*PHI,7+10*PHI,4+3*PHI});
        registerEven(new double[]{2,3+2*PHI,4+9*PHI,5+7*PHI});
        registerEven(new double[]{2,1+4*PHI,6+9*PHI,5*PHI2});
        
        registerEven(new double[]{PHI2,4+5*PHI,3+8*PHI,6*PHI2});
        registerEven(new double[]{PHI2,3*PHI3,4*PHI3,6+5*PHI});
        registerEven(new double[]{PHI2,3,2*PHI3,6+11*PHI});
        registerEven(new double[]{PHI2,3*PHI,5+12*PHI,2*PHI2});
        registerEven(new double[]{PHI2,3+2*PHI,4*PHI,6+11*PHI});
        registerEven(new double[]{PHI2,4+3*PHI,PHI6,6*PHI2});
        registerEven(new double[]{PHI2,3+4*PHI,6+8*PHI,6+5*PHI});
        
        registerEven(new double[]{3,PHI3,7+10*PHI,3+4*PHI});
        registerEven(new double[]{3,2*PHI2,5+9*PHI,4+7*PHI});
        registerEven(new double[]{3,1+3*PHI,6+9*PHI,2*PHI4});
        
        registerEven(new double[]{2*PHI,4*PHI2,4*PHI3,6*PHI2});
        registerEven(new double[]{2*PHI,PHI5,PHI6,6+5*PHI});
        registerEven(new double[]{2*PHI,2+PHI,1+3*PHI,5+12*PHI});
        registerEven(new double[]{2*PHI,3+PHI,1+4*PHI,6+11*PHI});
        
        registerEven(new double[]{2+PHI,4*PHI,7+10*PHI,3*PHI2});
        registerEven(new double[]{2+PHI,4+2*PHI,PHI6,5+7*PHI});
        registerEven(new double[]{2+PHI,2*PHI3,7+8*PHI,5*PHI2});
        
        registerEven(new double[]{PHI3,4+5*PHI,2+8*PHI,5+7*PHI});
        registerEven(new double[]{PHI3,5*PHI2,2+7*PHI,4*PHI3});
        
        registerEven(new double[]{3+PHI,3*PHI,7+10*PHI,2*PHI3});
        registerEven(new double[]{3+PHI,3+2*PHI,6+8*PHI,4+7*PHI});
        registerEven(new double[]{3+PHI,PHI4,7+8*PHI,2*PHI4});
        
        registerEven(new double[]{3*PHI,4*PHI2,3+8*PHI,5+7*PHI});
        registerEven(new double[]{3*PHI,2+6*PHI,PHI6,5*PHI2});
        
        registerEven(new double[]{2*PHI2,1+4*PHI,8+9*PHI,3*PHI2});
        registerEven(new double[]{2*PHI2,1+5*PHI,7+8*PHI,4+5*PHI});
        
        registerEven(new double[]{1+3*PHI,1+6*PHI,6+8*PHI,4+5*PHI});
        registerEven(new double[]{1+3*PHI,3*PHI3,2+8*PHI,4+7*PHI});
        registerEven(new double[]{1+3*PHI,2+7*PHI,3+8*PHI,2*PHI4});
        registerEven(new double[]{1+3*PHI,3+2*PHI,2*PHI3,8+9*PHI});
        registerEven(new double[]{1+3*PHI,4+3*PHI,3+8*PHI,4*PHI3});
        
        registerEven(new double[]{3+2*PHI,1+4*PHI,7+8*PHI,3*PHI3});
        registerEven(new double[]{3+2*PHI,2*PHI3,7+9*PHI,4+3*PHI});
        registerEven(new double[]{3+2*PHI,1+5*PHI,6+9*PHI,4*PHI2});
        
        registerEven(new double[]{4*PHI,PHI5,3+8*PHI,4+7*PHI});
        registerEven(new double[]{4*PHI,2+6*PHI,4*PHI3,2*PHI4});
        
        registerEven(new double[]{PHI4,4*PHI2,1+6*PHI,5+9*PHI});
        registerEven(new double[]{PHI4,4+2*PHI,3+4*PHI,7+9*PHI});
        registerEven(new double[]{PHI4,3*PHI2,2+8*PHI,PHI6});
        
        registerEven(new double[]{4+2*PHI,1+4*PHI,6+9*PHI,PHI5});
        
        registerEven(new double[]{1+4*PHI,1+6*PHI,PHI6,3*PHI3});
        registerEven(new double[]{1+4*PHI,3*PHI2,2+7*PHI,6+8*PHI});
        registerEven(new double[]{1+4*PHI,4+3*PHI,2+6*PHI,5+9*PHI});
        
        registerEven(new double[]{2*PHI3,1+6*PHI,4+9*PHI,PHI5});
        registerEven(new double[]{2*PHI3,1+5*PHI,PHI6,2+7*PHI});
        
        registerEven(new double[]{1+5*PHI,3+4*PHI,2+6*PHI,4+9*PHI});
        connect(2);
    }
}
