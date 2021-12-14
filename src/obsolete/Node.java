package obsolete;

import framework.primitives.Line;
import framework.primitives.Vector;
import manifolds.smoothmanifolds.Hypersphere;

/**
 *
 * @author Valente Productions
 */

public class Node 
{
    public Vector preimage;
    public Vector image;
    public Vector actual;
    
    public Vector vertex;
    public Line edge;
    
    public double JRO;
    
    public Node(Vector _preimage, Vector _vertex, Line _edge, double _JRO)
    {
        JRO = _JRO;
        vertex = _vertex;
        edge = _edge;
        
        setPreimage(_preimage);
    }
    
    public void setPreimage(Vector _preimage)
    {
        preimage = _preimage;
        image = (new Hypersphere(3)).MAP(preimage);
        actual = image.scaledBy(JRO).plus(vertex);
    }
    
    @Override
    public boolean equals(Object obj) 
    {
        return this.toString().equals(obj.toString());
    }
    
    @Override
    public int hashCode() 
    {
        return this.toString().hashCode();
    }
    
    @Override
    public String toString()
    {
        return preimage.toStringR() + " : " + vertex.toStringR();
    }
    
}
