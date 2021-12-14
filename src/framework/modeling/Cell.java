/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.modeling;

/**
 *
 * @author Nicholas
 */
public class Cell
{
    private boolean active = false;
    
    public Cell[] neighbors;
    
    public int L, R, C;
    
    public Cell(int _L, int _R, int _C)
    {
        L = _L;
        R = _R;
        C = _C;
    }
    
    public void activate()
    {
        active = true;
    }
    
    public void deactivate()
    {
        active = false;
    }
    
    public boolean isActive()
    {
        return active;
    }
    
    public void anchor()
    {
        
    }
}
