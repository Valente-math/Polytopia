package obsolete;

import abstracts.Manifold;
import framework.primitives.Line;
import framework.primitives.Vector;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * RESPOSITORY OF (POTENTIALLY) OBSOLETE CODE
 * 
 */

public class Obsolete 
{/*
    private void markBoundary2()
    {
        receiverMap = new HashMap<Node, LinkedHashSet<Node>>();
        
        for(Node node : boundaryMap.keySet())
        {
            Vector vertex = node.vertex;
            Cell[][] matrix = cellMap.get(vertex);
            
            int c0 = (int)Math.floor((node.preimage.e[0] + Math.PI)/(Math.PI/(double)RES)),
                r0 = (int)Math.floor((node.preimage.e[1] + Math.PI/2.0)/(Math.PI/(double)RES));
            Cell origin = matrix[r0][c0]; // The cell containing node
            
            LinkedHashSet<Cell> boundary = new LinkedHashSet<Cell>();
            int n = boundaryMap.get(node).size();
            for(int i = 0; i < n; i++)
            {
                Node bpoint = boundaryMap.get(node).get(i);
                Node prev = boundaryMap.get(node).get(n-1); 
                if(i != 0) prev = boundaryMap.get(node).get(i-1);
                
                // If the line connecting two boundary points
                // spans multiple cells, go through each cell and add to receiver
                Line cxn = new Line(bpoint.image, prev.image);
                float res = (float)Math.ceil(cxn.length()/(Math.PI/(double)RES)); 
                for(double t = 0.0; t < 1; t += 1.0/res)
                {
                    Vector temp = sphere.INVERT(cxn.f(t));
                    double _theta = temp.e[0] + Math.PI,
                           _phi   = temp.e[1] + Math.PI/2.0;
                    
                    int c = (int)Math.floor(_theta/(Math.PI/(double)RES)),
                        r = (int)Math.floor(_phi/(Math.PI/(double)RES));
                    
                    Cell bcell = matrix[r][c]; // boundary cell
                    bcell.active = false;
                    bcell.anchor = node;
                    boundary.add(bcell);
                    
                    // Move to central axis and mark interior
                    // If near the poles (tolerance of pi/4), move horizontally
                    // Otherwise, move vertically
                    if(r > r0)
                            for(int ri = r; ri >= r0; ri--)
                            matrix[ri][c].active = false;
                        else
                            for(int ri = r; ri <= r0; ri++)
                            matrix[ri][c].active = false;
                    
                    if(Math.abs(node.preimage.e[1]) > Math.PI/4.0)
                        if(c > c0)
                            for(int ci = c; ci >= c0; ci--)
                            matrix[r][ci].active = false;
                        else
                            for(int ci = c; ci <= c0; ci++)
                            matrix[r][ci].active = false;
                    /*else
                        if(r > r0)
                            for(int ri = r; ri >= r0; ri--)
                            matrix[ri][c].active = false;
                        else
                            for(int ri = r; ri <= r0; ri++)
                            matrix[ri][c].active = false;
                    
                }
            }
            
            
            LinkedHashSet<Node> receiver = new LinkedHashSet<Node>();
            for(Cell bcell : boundary)
            {
                int r = bcell.r,
                    c = bcell.c;
                
                Node NE = new Node(new Vector(bcell.ne), vertex),
                     NW = new Node(new Vector(bcell.nw), vertex),
                     SW = new Node(new Vector(bcell.sw), vertex),
                     SE = new Node(new Vector(bcell.se), vertex);
                            
                if((c < 2*RES-1) && matrix[r][c+1].active) // EAST
                {
                    receiver.add(NE);
                    receiver.add(SE);
                }
                if((c > 0) && matrix[r][c-1].active) // WEST
                {
                    receiver.add(NW);
                    receiver.add(SW);
                }
                if((r < RES-1) && matrix[r+1][c].active) //NORTH
                {
                    
                    receiver.add(NE);
                    receiver.add(NW);
                }
                if((r > 0) && matrix[r-1][c].active) // SOUTH
                {
                    receiver.add(SE);
                    receiver.add(SW);
                }
            }
            receiverMap.put(node, receiver);
        }
    }
    
    // Fills boundaryMap
    private void calculateBoundary()
    { 
        boundaryMap = new HashMap<Node, ArrayList<Node>>();
        
        double  r = JRO,
                R = r*r - (RO*RO)/2.0;
        
        for(Vector vertex : vertexMap.keySet())
        {
            for(Node node : vertexMap.get(vertex))
            {
                ArrayList<Node> boundary1 = new ArrayList<Node>(),
                                boundary2 = new ArrayList<Node>();
                
                Vector coords = node.image.clone();
                
                double x0 = coords.e[0],
                       y0 = coords.e[1],
                       z0 = coords.e[2];
                
                boolean swapZ = (round(Math.abs(z0)) < 0.1),
                        swapY = (round(Math.abs(y0)) < 0.1);
                
                if(swapZ)
                    if(swapY)
                    {
                        x0 = coords.e[2];
                        z0 = coords.e[0];
                    }
                    else
                    {
                        y0 = coords.e[2];
                        z0 = coords.e[1];
                    }
                
                double ax = 4.0*(x0*x0*y0*y0 - (x0*x0 - r*r)*(y0*y0 - r*r)),
                       bx = 8.0*R*x0*(r*r - (x0*x0 + y0*y0)),
                       cx = 4.0*(R*R*y0*y0 - (x0*x0 - r*r)*(r*r*(r*r - (x0*x0 + y0*y0)) - R*R)),
                       discrimX = Math.abs(bx*bx - 4*ax*cx),
                       d1 = Math.min((-bx + Math.sqrt(discrimX))/(2.0*ax),
                                     (-bx - Math.sqrt(discrimX))/(2.0*ax)),
                       d2 = Math.max((-bx + Math.sqrt(discrimX))/(2.0*ax),
                                     (-bx - Math.sqrt(discrimX))/(2.0*ax)),
                       res = 8.0;
                
                double STEP = (d2 - d1)/res;
                for(double x = d1 + 0.001; x < d2 - 0.001; x += STEP*Math.sin(Math.PI*(x - d1)/(d2 - d1)))
                {
                    
                    double ay = x0*x0 - r*r,
                           by = 2.0*y0*(R - x0*x),
                           cy = (y0*y0 - r*r)*x*x + 2.0*R*x0*x + (r*r*(r*r - (x0*x0 + y0*y0)) - R*R),
                           discrimY = Math.abs(by*by - 4.0*ay*cy),
                           y1 = (-by - Math.sqrt(discrimY))/(2.0*ay),
                           y2 = (-by + Math.sqrt(discrimY))/(2.0*ay),
                           z1 = Math.sqrt(r*r - x*x - y1*y1),
                           z2 = Math.sqrt(r*r - x*x - y2*y2);
                            
                    if(z0 < 0){z1 *= -1.0; z2 *= -1.0;} 
                           
                    Vector p1 = new Vector(new double[]{x, y1, z1}),
                           p2 = new Vector(new double[]{x, y2, z2});
                    
                    
                    if(swapZ)
                        if(swapY)
                        {
                            p1 = new Vector(new double[]{z1, y1, x});
                            p2 = new Vector(new double[]{z2, y2, x});
                        }
                        else
                        {
                            p1 = new Vector(new double[]{x, z1, y1});
                            p2 = new Vector(new double[]{x, z2, y2});
                        }
                    
                    Node n1 = new Node(sphere.INVERT(p1), vertex, node.edge),
                         n2 = new Node(sphere.INVERT(p2), vertex, node.edge);
                    
                    boundary1.add(n1);
                    if(discrimY > 0) boundary2.add(n2);
                }     
                
                ArrayList<Node> boundary = new ArrayList<Node>(boundary1);
                for(int i = boundary2.size() - 1; i >= 0; i--)
                    boundary.add(boundary2.get(i));
                
                boundaryMap.put(node, boundary);
            }
        }
    }
    
    public void printBoundaries()
    {
        for(Vector vertex : vertices)
        {
            System.out.println("VERTEX: " + vertex);
            for(Node node : vertexMap.get(vertex))
            {
                System.out.println(node.preimage);
                for(Node bound : boundaryMap.get(node))
                    System.out.println(bound.preimage.toStringR());
                System.out.println("");
            }
            for(int i = 0; i < 90; i++) System.out.print("_");
            System.out.println("");
        }
    } 
    
    public void testInversion()
    {
        System.out.println("TESTING SPHERE INVERSION");
        float step = (float)Math.PI/12;
        for(float theta = -(float)Math.PI; theta <= Math.PI; theta += step)
            for(float phi = -(float)Math.PI/2; phi <= Math.PI/2; phi += step)
            {
                float[] input    = new float[]{theta, phi},
                        image1   = sphere.MAP(input),
                        preimage = sphere.INVERT(image1),
                        image2   = sphere.MAP(preimage);
                
                if(!areEqual(image1, image2))
                {
                    print(input); System.out.print(" --> "); print(image1);
                    System.out.println("");
                    print(preimage); System.out.print(" --> "); print(image2);
                    System.out.println("");
                    System.out.println("");
                }
            }
    }
    
    public void print(float[] f)
    {
        System.out.print("");// "(");
        for(int i = 0; i < f.length-1; i++)
            System.out.print(f[i] + ", " );
        System.out.print(round(f[f.length-1]) + "\n");
    }
    
    public void printExact(float[] f)
    {
        System.out.print("(");
        for(int i = 0; i < f.length-1; i++)
            System.out.print(f[i] + ", " );
        System.out.print(f[f.length-1] + ")");
    }
    
    public boolean areEqual(float[] a, float[] b)
    {
        if(a.length != b.length) return false;
        for(int i = 0; i < a.length; i++)
            if(round(a[i]) != round(b[i])) return false;
        return true;
    }
     
    
    public void createRods2()
    {
        rodMap = new HashMap<Line, HashSet<Vector[]>>();
        for(Line edge : edges)
        {
            rodMap.put(edge, new HashSet<Vector[]>());
            
            Node n0 = null, n1 = null;
            for(Node node : edgeMap.get(edge))
            {
                if(node.vertex.isEqualTo(edge.v0))
                    n0 = node;
                if(node.vertex.isEqualTo(edge.v1))
                    n1 = node;
            }
            ArrayList<Line> receiver0 = getSortedReceiver(n0),
                            receiver1 = getSortedReceiver(n1);
            
            HashMap<Line, Boolean> check = new HashMap<Line, Boolean>();
            for(Line seg : receiver1) check.put(seg, true);
            
            for(Line south : receiver0)
            {
                Line _south = new Line(sphere.MAP(south.v0.e, JRO).plus(edge.v0),
                                       sphere.MAP(south.v1.e, JRO).plus(edge.v0));
                
                long dist = Long.MAX_VALUE;
                Line north = null;
                for(Line candidate : receiver1)
                    if(check.get(candidate))
                    {
                        Line _north = new Line(sphere.MAP(candidate.v0.e, JRO).plus(edge.v1),
                                               sphere.MAP(candidate.v1.e, JRO).plus(edge.v1));
                        
                        if(_south.midpoint().distanceTo(_north.midpoint()) < dist)
                        {
                            dist = (long)_south.midpoint().distanceTo(_north.midpoint());
                            north = candidate;
                        }
                    }
                
                check.put(north, false);
                Vector SW = sphere.MAP(south.v0.e, JRO).plus(edge.v0),
                       SE = sphere.MAP(south.v1.e, JRO).plus(edge.v0),
                       NW = sphere.MAP(north.v0.e, JRO).plus(edge.v1),
                       NE = sphere.MAP(north.v1.e, JRO).plus(edge.v1);
                
                rodMap.get(edge).add(new Vector[]{SW, NW, NE});
                rodMap.get(edge).add(new Vector[]{NE, SE, SW});
            }
            
            
        }
    }
    * 
    * 
    * 
    
    public ArrayList<Line> getSortedReceiver(Node node)
    {   
        ArrayList<Line> sorted = new ArrayList<Line>();
        
        HashSet<Line> receiver = new HashSet<Line>();
        for(Line e : receiverMap.get(node)) receiver.add(e);
        
        double nx = node.image.e[0],
               ny = node.image.e[1],
               nz = node.image.e[2];
        
        while(!receiver.isEmpty())
        {
            double min = Double.MAX_VALUE;
            Line next = null;
            for(Line edge : receiver)
            {
                Vector image = sphere.MAP(edge.midpoint().e);
                double x0 = image.e[0] - nx,
                       y0 = image.e[1] - ny,
                       z0 = image.e[2] - nz;
                
                double theta = 
                        sphere.INVERT(new Vector(new double[]{x0, y0, z0})).e[0];
                
                if(theta < min)
                {
                    min = theta;
                    next = edge;
                }
            }
            sorted.add(next);
            receiver.remove(next);
        }
        
        return sorted;
    }
    
    
    public ArrayList<Line> deinterlaceCCW2(ArrayList<Line> xsorted)
    {
        ArrayList<Line> sorted = new ArrayList<Line>();
        int n = xsorted.size();
        sorted.add(xsorted.get(0));
        
        int i = 1;
        for(; i < n; i += 2)
            sorted.add(xsorted.get(i));
        
        if(i == n) i--;
        else i -= 3;
        
        i = 2;
        for(; i >= 2; i -= 2)
            sorted.add(xsorted.get(i));
        
        //System.out.println("CCW: " + (sorted.size() - xsorted.size()));
        return sorted;
    }
    
    public ArrayList<Line> deinterlaceCC2(ArrayList<Line> xsorted)
    {
        ArrayList<Line> sorted = new ArrayList<Line>();
        int n = xsorted.size();
        sorted.add(xsorted.get(0));
        
        //System.out.println("ADDING: ");
        int i = 2;
        for(; i < n; i += 2)
        {
            //System.out.print(i + ", ");
            sorted.add(xsorted.get(i)); 
        }
        
        if(i == n) i --;
        else i -= 3;
        
        for(; i >= 1; i -= 2)
        {
            //System.out.print(i + ", ");
            sorted.add(xsorted.get(i));
        }
        //System.out.println("");
        
        //System.out.println("CC: " + (sorted.size() - xsorted.size()));
        int pos = 0, neg = 0;
        for(Line edge0 : xsorted)
        {
            if(edge0.v0.e[0] > 0) pos++;
            if(edge0.v0.e[0] < 0) neg++;
        }
        if(pos > 0  && neg > 0 && (pos > neg + 4 || neg > pos + 4) )
        {
            System.out.println("\n\nCROSS-SORTED");
            for(Line edge : xsorted)
                System.out.println(edge.midpoint());
            System.out.println("SORTED");
            for(Line edge : sorted)
                System.out.println(edge.midpoint());
        }
        
        return sorted;
    }
    
    public ArrayList<Line> sequence(ArrayList<Line> xsorted)
    {
        ArrayList<Line> sorted = new ArrayList<Line>();
        
        HashMap<Line, Boolean> check = new HashMap<Line, Boolean>();
        for(Line edge : xsorted) check.put(edge, true);
        
        
        sorted.add(xsorted.get(0));
        check.put(xsorted.get(0), false);
        Line prev = xsorted.get(0);
            
        while(sorted.size() < xsorted.size() - 1)
        {
            double mindist = Double.MAX_VALUE;
            Line min = null;
            for(int n = 1; n < xsorted.size(); n++)
                if(check.get(xsorted.get(n)))
                {
                    Vector next = sphere.MAP(xsorted.get(n).midpoint().e);
                    double dist = next.distanceTo(sphere.MAP(prev.midpoint().e));
                    if(dist > 0 && dist < mindist) 
                    {
                        mindist = dist;
                        min = xsorted.get(n);
                    }
                }
            sorted.add(min);
            check.put(min, false);
            prev = min;
        }
        for(Line edge : xsorted) 
            if(check.get(edge) == true)
                sorted.add(edge);
        
        System.out.println("\n\nCROSS-SORTED");
        for(Line edge : xsorted)
            System.out.println(edge.midpoint());
        System.out.println("SORTED");
        for(Line edge : sorted)
            System.out.println(edge.midpoint());
        
        return sorted;
    }
    * 
    
    public void createRods2()
    {
        rodMap = new HashMap<Line, HashSet<Vector[]>>();
        for(Line edge : edges)
        {
            rodMap.put(edge, new HashSet<Vector[]>());
            
            Vector v0 = edge.v0,
                   v1 = edge.v1;
            
            Node n0 = null, n1 = null;
            for(Node node : edgeMap.get(edge))
            {
                if(node.vertex.isEqualTo(v0))
                    n0 = node;
                if(node.vertex.isEqualTo(v1))
                    n1 = node;
            }
            
            //System.out.println("--------------------------------RECEIVER 0-------------------------------------");
            ArrayList<Line> receiver0 = getSortedReceiver(n0);
            //System.out.println("--------------------------------RECEIVER 1-------------------------------------");
            ArrayList<Line> receiver1 = getSortedReceiver(n1);
            
            if(receiver0.size() != receiver1.size())
                System.out.println("INCOMPATIBLE RECEIVERS");
            
            for(int i = 0; i < receiver0.size(); i++)
            {
                Line edge0 = receiver0.get(i),
                     edge1 = receiver1.get(i);
                
                
                Line south = edge0, north = edge1;
                
                Vector SW = sphere.MAP(south.v0, JRO).plus(v0),
                       SE = sphere.MAP(south.v1, JRO).plus(v0),
                       NW = sphere.MAP(north.v0, JRO).plus(v1),
                       NE = sphere.MAP(north.v1, JRO).plus(v1);
                
                // Make sure lines are oriented correctly
                if(SW.preciseDistanceTo(NW) > SW.preciseDistanceTo(NE))
                {
                    NE = sphere.MAP(north.v0, JRO).plus(v1);
                    NW = sphere.MAP(north.v1, JRO).plus(v1);
                }

                rodMap.get(edge).add(new Vector[]{SW, NW, NE});
                rodMap.get(edge).add(new Vector[]{NE, SE, SW});
            }
            int panels = rodMap.get(edge).size();
            System.out.println("PANELS: " + panels/2 + " = " + receiverMap.get(edgeMap.get(edge).iterator().next()).size());
        }
    }
    * 
    * public void createRods()
    {
        rodMap = new HashMap<Line, HashSet<Vector[]>>();
        for(Line edge : edges)
        {
            rodMap.put(edge, new HashSet<Vector[]>());
            
            Node n0 = null, n1 = null;
            for(Node node : edgeMap.get(edge))
            {
                if(node.vertex.isEqualTo(edge.v0))
                    n0 = node;
                if(node.vertex.isEqualTo(edge.v1))
                    n1 = node;
            }
            //System.out.println("--------------------------------RECEIVER 0-------------------------------------");
            ArrayList<Line> receiver0 = sequence(receiverMap.get(n0));
            //System.out.println("--------------------------------RECEIVER 1-------------------------------------");
            ArrayList<Line> receiver1 = sequence(receiverMap.get(n1));
            
            if(receiver0.size() != receiver1.size())
                System.out.println("INCOMPATIBLE RECEIVERS");
            
            
            int shift = alignment(n0, receiver0, n1, receiver1);
            int N = receiver1.size();
            
             Line  edgeA = receiver0.get(1),
                   edgeB = receiver1.get(mod((shift + 1), N)),
                   edgeC = receiver1.get(mod((shift - 1), N));
            Vector pA = sphere.MAP(edgeA.midpoint()).plus(n0.vertex),
                   pB = sphere.MAP(edgeB.midpoint()).plus(n1.vertex),
                   pC = sphere.MAP(edgeC.midpoint()).plus(n1.vertex);
        
            long dplus  = pA.preciseDistanceTo(pB),
                 dminus = pA.preciseDistanceTo(pC);
            
            // If receiver orientation is the same, dplus should be smaller than dminus
            boolean flipped = (dplus > dminus);
            
            for(int i = 0; i < receiver0.size(); i++)
            {
                int index = mod((i + shift), N);
                Line south = receiver0.get(i),
                     north = receiver1.get(index);
                if(flipped) 
                     north = receiver1.get(mod((N - index), N));
                
                Vector SW = sphere.MAP(south.v0.e, JRO).plus(edge.v0),
                       SE = sphere.MAP(south.v1.e, JRO).plus(edge.v0),
                       NW = sphere.MAP(north.v0.e, JRO).plus(edge.v1),
                       NE = sphere.MAP(north.v1.e, JRO).plus(edge.v1);
                
                // Make sure lines are oriented correctly
                if(SW.preciseDistanceTo(NW) > SW.preciseDistanceTo(NE))
                {
                    Vector hold = NW;
                    NE = NW;
                    NW = hold;
                }
                
                rodMap.get(edge).add(new Vector[]{SW, NW, NE});
                rodMap.get(edge).add(new Vector[]{NE, SE, SW});
            }
        }
    }
    * 
    * public void createRods()
    {
        rodMap = new HashMap<Line, HashSet<Vector[]>>();
        for(Line edge : edges)
        {
            rodMap.put(edge, new HashSet<Vector[]>());
            
            Node n0 = null, n1 = null;
            for(Node node : edgeMap.get(edge))
            {
                if(node.vertex.isEqualTo(edge.v0))
                    n0 = node;
                if(node.vertex.isEqualTo(edge.v1))
                    n1 = node;
            }
            //System.out.println("--------------------------------RECEIVER 0-------------------------------------");
            ArrayList<Line> receiver0 = sequence(receiverMap.get(n0));
            //System.out.println("--------------------------------RECEIVER 1-------------------------------------");
            ArrayList<Line> receiver1 = sequence(receiverMap.get(n1));
            
            if(receiver0.size() != receiver1.size())
                System.out.println("INCOMPATIBLE RECEIVERS");
            
            
            int shift = alignment(n0, receiver0, n1, receiver1);
            int N = receiver1.size();
            
             Line  edgeA = receiver0.get(1),
                   edgeB = receiver1.get(mod((shift + 1), N)),
                   edgeC = receiver1.get(mod((shift - 1), N));
            Vector pA = sphere.MAP(edgeA.midpoint()).plus(n0.vertex),
                   pB = sphere.MAP(edgeB.midpoint()).plus(n1.vertex),
                   pC = sphere.MAP(edgeC.midpoint()).plus(n1.vertex);
        
            long dplus  = pA.preciseDistanceTo(pB),
                 dminus = pA.preciseDistanceTo(pC);
            
            // If receiver orientation is the same, dplus should be smaller than dminus
            boolean flipped = (dplus > dminus);
            
            for(int i = 0; i < receiver0.size(); i++)
            {
                int index = mod((i + shift), N);
                Line south = receiver0.get(i),
                     north = receiver1.get(index);
                if(flipped) 
                     north = receiver1.get(mod((N - index), N));
                
                Vector SW = sphere.MAP(south.v0.e, JRO).plus(edge.v0),
                       SE = sphere.MAP(south.v1.e, JRO).plus(edge.v0),
                       NW = sphere.MAP(north.v0.e, JRO).plus(edge.v1),
                       NE = sphere.MAP(north.v1.e, JRO).plus(edge.v1);
                
                // Make sure lines are oriented correctly
                if(SW.preciseDistanceTo(NW) > SW.preciseDistanceTo(NE))
                {
                    Vector hold = NW;
                    NE = NW;
                    NW = hold;
                }
                
                rodMap.get(edge).add(new Vector[]{SW, NW, NE});
                rodMap.get(edge).add(new Vector[]{NE, SE, SW});
            }
        }
    }
    * 
    * 
    
    private ArrayList<Line> sequence(ArrayList<Line> xsorted)
    {
        ArrayList<Line> sorted = new ArrayList<Line>();
        
        HashMap<Line, Boolean> check = new HashMap<Line, Boolean>();
        for(Line edge : xsorted) check.put(edge, true);
        
        
        sorted.add(xsorted.get(0));
        check.put(xsorted.get(0), false);
        Line prev = xsorted.get(0);
            
        while(sorted.size() < xsorted.size() - 1)
        {
            double mindist = Double.MAX_VALUE;
            Line min = null;
            for(int n = 1; n < xsorted.size(); n++)
                if(check.get(xsorted.get(n)))
                {
                    Vector next = sphere.MAP(xsorted.get(n).midpoint().e);
                    double dist = next.distanceTo(sphere.MAP(prev.midpoint().e));
                    if(dist > 0 && dist < mindist) 
                    {
                        mindist = dist;
                        min = xsorted.get(n);
                    }
                }
            sorted.add(min);
            check.put(min, false);
            prev = min;
        }
        for(Line edge : xsorted) 
            if(check.get(edge) == true)
                sorted.add(edge);
        
        System.out.println("\n\nCROSS-SORTED");
        for(Line edge : xsorted)
            System.out.println(edge.midpoint());
        System.out.println("SORTED");
        for(Line edge : sorted)
            System.out.println(edge.midpoint());
        
        return sorted;
    }
    
    private int alignment(ArrayList<Line> receiver0, ArrayList<Line> receiver1)
    {
        Line edge0 = receiver0.get(0);
        Vector p0 = sphere.MAP(edge0.midpoint());
        
        double min = Double.MAX_VALUE;
        int mindex = 0;
        for(int i = 0; i < receiver1.size(); i++)
        {
            Line edge1 = receiver1.get(i);
            Vector p1 = sphere.MAP(edge1.midpoint());
            
            double dist = p0.distanceTo(p1);
            if(dist < min)
            {
                min = dist;
                mindex = i;
            }
        }
        return mindex;
    }
    
    private int mod(int x, int n)
    {
        if(x >= 0) 
            return (x % n);
        else
            return ((n + x) % n);
    }
    * 
    * 
    
    public void createRods3()
    {
        rodMap = new HashMap<Line, HashSet<Vector[]>>();
        for(Line edge : edges)
        {
            rodMap.put(edge, new HashSet<Vector[]>());
            
            Vector v0 = edge.v0,
                   v1 = edge.v1;
            
            Node n0 = null, n1 = null;
            for(Node node : edgeMap.get(edge))
            {
                if(node.vertex.isEqualTo(v0))
                    n0 = node;
                if(node.vertex.isEqualTo(v1))
                    n1 = node;
            }
            
            //System.out.println("--------------------------------RECEIVER 0-------------------------------------");
            ArrayList<Line> receiver0 = receiverMap.get(n0);
            //System.out.println("--------------------------------RECEIVER 1-------------------------------------");
            ArrayList<Line> receiver1 = receiverMap.get(n1);
            
            if(receiver0.size() != receiver1.size())
                System.out.println("INCOMPATIBLE RECEIVERS");
            
            for(int i = 0; i < receiver0.size(); i++)
            {
                Line edge0 = receiver0.get(i),
                     edge1 = receiver1.get(i);
                
                
                Line south = edge0, north = edge1;
                
                Vector SW = sphere.MAP(south.v0, JRO).plus(v0),
                       SE = sphere.MAP(south.v1, JRO).plus(v0),
                       NW = sphere.MAP(north.v0, JRO).plus(v1),
                       NE = sphere.MAP(north.v1, JRO).plus(v1);
                
                // Make sure lines are oriented correctly
                if(SW.preciseDistanceTo(NW) > SW.preciseDistanceTo(NE))
                {
                    NE = sphere.MAP(north.v0, JRO).plus(v1);
                    NW = sphere.MAP(north.v1, JRO).plus(v1);
                }

                rodMap.get(edge).add(new Vector[]{SW, NW, NE});
                rodMap.get(edge).add(new Vector[]{NE, SE, SW});
            }
            int panels = rodMap.get(edge).size();
            System.out.println("PANELS: " + panels/2 + " = " + receiverMap.get(edgeMap.get(edge).iterator().next()).size());
        }
    }
    public void createRods2()
    {
        rodMap = new HashMap<Line, HashSet<Vector[]>>();
        for(Line edge : edges)
        {
            rodMap.put(edge, new HashSet<Vector[]>());
            
            Node n0 = null, n1 = null;
            for(Node node : edgeMap.get(edge))
            {
                if(node.vertex.isEqualTo(edge.v0))
                    n0 = node;
                if(node.vertex.isEqualTo(edge.v1))
                    n1 = node;
            }
            //System.out.println("--------------------------------RECEIVER 0-------------------------------------");
            ArrayList<Line> receiver0 = sequence(receiverMap.get(n0));
            //System.out.println("--------------------------------RECEIVER 1-------------------------------------");
            ArrayList<Line> receiver1 = sequence(receiverMap.get(n1));
            
            if(receiver0.size() != receiver1.size())
                System.out.println("INCOMPATIBLE RECEIVERS");
            
            
            int shift = alignment(receiver0, receiver1);
            int N = receiver1.size();
            
             Line  edgeA = receiver0.get(1),
                   edgeB = receiver1.get(mod((shift + 1), N)),
                   edgeC = receiver1.get(mod((shift - 1), N));
            Vector pA = sphere.MAP(edgeA.midpoint()).plus(n0.vertex),
                   pB = sphere.MAP(edgeB.midpoint()).plus(n1.vertex),
                   pC = sphere.MAP(edgeC.midpoint()).plus(n1.vertex);
        
            long dplus  = pA.preciseDistanceTo(pB),
                 dminus = pA.preciseDistanceTo(pC);
            
            // If receiver orientation is the same, dplus should be smaller than dminus
            boolean flipped = (dplus > dminus);
            
            for(int i = 0; i < receiver0.size(); i++)
            {
                int index = mod((i + shift), N);
                Line south = receiver0.get(i),
                     north = receiver1.get(index);
                if(flipped) 
                     north = receiver1.get(mod((N - index), N));
                
                Vector SW = sphere.MAP(south.v0.e, JRO).plus(edge.v0),
                       SE = sphere.MAP(south.v1.e, JRO).plus(edge.v0),
                       NW = sphere.MAP(north.v0.e, JRO).plus(edge.v1),
                       NE = sphere.MAP(north.v1.e, JRO).plus(edge.v1);
                
                // Make sure lines are oriented correctly
                if(SW.preciseDistanceTo(NW) > SW.preciseDistanceTo(NE))
                {
                    Vector hold = NW;
                    NE = NW;
                    NW = hold;
                }
                
                rodMap.get(edge).add(new Vector[]{SW, NW, NE});
                rodMap.get(edge).add(new Vector[]{NE, SE, SW});
            }
        }
    }
    * 
    * 
    private void clearInterior()
    {
        for(Vector vertex : vertices)
        {
            Cell[][] matrix = cellMap.get(vertex);
            
            for(int r = 0; r < RES; r++)
            for(int c = 0; c < 2*RES; c++)
            {
                Cell bcell = matrix[r][c];
                // If a type2 does not have at least one type1 neighbor, 
                // then deactivate it 
                if(bcell.type == 2)
                {
                    Cell[] neighbors = bcell.neighbors;
                    boolean pass = false;
                    for(int i = 0; i < 3; i++)
                        if(neighbors[i] != null && neighbors[i].type == 1) 
                            pass = true;
                    
                    if(pass)
                    {
                        // clear subcell interior
                        for(int _r = 0; _r < bcell.SUBRES; _r++)
                        for(int _c = 0; _c < bcell.SUBRES; _c++)
                        {
                            Cell subcell = bcell.matrix[_r][_c];
                            Cell[] _neighbors = getSubNeighbors(bcell.matrix, _r, _c);
                                    
                            boolean _pass = false;
                            for(int i = 0; i < 3; i++)
                                if(_neighbors[i] != null && _neighbors[i].type == 0) 
                                    _pass = true;
                            
                            if(!_pass) 
                                subcell.deactivate();
                        }
                    }
                    else
                        bcell.deactivate();
                    
                    
                }
            }
        }
    }
    * 
    * 
    
    public void createRods2()
    {
        rodMap = new HashMap<Line, HashSet<Vector[]>>();
        for(Line edge : edges)
        {
            rodMap.put(edge, new HashSet<Vector[]>());
            
            Vector v0 = edge.v0,
                   v1 = edge.v1;
            
            Node n0 = null, n1 = null;
            for(Node node : edgeMap.get(edge))
            {
                if(node.vertex.isEqualTo(v0))
                    n0 = node;
                if(node.vertex.isEqualTo(v1))
                    n1 = node;
            }
            
            ArrayList<Line> receiver0 = receiverLines.get(n0);
            ArrayList<Line> receiver1 = receiverLines.get(n1);
            
            if(receiver0.size() != receiver1.size())
            {
                System.out.println("INCOMPATIBLE RECEIVERS");
                System.out.println(receiver0.size() + " : " + receiver1.size());
            }
            
            HashMap<Line, Boolean> check = new HashMap<Line, Boolean>();
            for(Line edge1 : receiver1) check.put(edge1, true);
            
            for(Line edge0 : receiver0)
            {
                Vector p0 = edge0.midpoint();
                double min = Double.MAX_VALUE;
                Line closest = null;
                
                for(Line edge1 : receiver1)
                if(check.get(edge1))
                {
                    Vector p1 = edge1.midpoint();
                    
                    double thetaN = n1.preimage.e[0],
                           phiN   = n1.preimage.e[1];
                    
                    double width = Math.PI/((double)RES*(double)SUBRES);
                            
                    double thetaC = (width/2.0)*(Math.floor(thetaN/width) + Math.ceil(thetaN/width)),
                           phiC = (width/2.0)*(Math.floor(phiN/width) + Math.ceil(phiN/width));
                        
                    double theta  = p1.e[0],
                           phi    = p1.e[1];
                    
                    theta -= thetaC;
                    theta *= -1.0;
                    theta -= Math.PI - thetaC;
                    phi   -= 2.0*phiC;
                    
                    if(theta <= -Math.PI) theta += 2*Math.PI;
                    if(theta > Math.PI)  theta -= 2*Math.PI;
                    
                    p1.e[0] = theta;
                    p1.e[1] = phi;
                    
                    double dist = p0.distanceTo(p1);
                    if(dist < min)
                    {
                        min = dist;
                        closest = edge1;
                    }
                }
                //System.out.println("MINIMAL DISTANCE: " + min);
                check.put(closest, false);
                Line south = edge0, north =  closest;
                System.out.println(north);
                
                Vector SW = sphere.MAP(south.v0, JRO).plus(v0),
                       SE = sphere.MAP(south.v1, JRO).plus(v0),
                       NW = sphere.MAP(north.v0, JRO).plus(v1),
                       NE = sphere.MAP(north.v1, JRO).plus(v1);
                
                // Make sure lines are oriented correctly
                if(SW.preciseDistanceTo(NW) > SW.preciseDistanceTo(NE))
                {
                    NE = sphere.MAP(north.v0, JRO).plus(v1);
                    NW = sphere.MAP(north.v1, JRO).plus(v1);
                }

                rodMap.get(edge).add(new Vector[]{SW, NW, NE});
                rodMap.get(edge).add(new Vector[]{NE, SE, SW});
            }
        }
    }
    
    public void createRods3()
    {
        rodMap = new HashMap<Line, HashSet<Vector[]>>();
        for(Line edge : edges)
        {
            rodMap.put(edge, new HashSet<Vector[]>());
            
            Vector v0 = edge.v0,
                   v1 = edge.v1;
            
            Node n0 = null, n1 = null;
            for(Node node : edgeMap.get(edge))
            {
                if(node.vertex.isEqualTo(v0))
                    n0 = node;
                if(node.vertex.isEqualTo(v1))
                    n1 = node;
            }
            
            ArrayList<Line> receiver0 = sortForward(n0);
            ArrayList<Line> receiver1 = sortBackward(n1);
            if(round(Math.abs(n1.preimage.e[1])) == round(Math.PI/2.0))
                receiver1 = sortForward(n1);
            
            if(receiver0.size() != receiver1.size())
            {
                System.out.println("INCOMPATIBLE RECEIVERS");
                System.out.println(receiver0.size() + " : " + receiver1.size());
            }
            
            HashMap<Line, Boolean> check = new HashMap<Line, Boolean>();
            for(Line edge1 : receiver1) check.put(edge1, true);
            
            for(int i = 0; i < receiver0.size(); i++)
            {
                Line south = receiver0.get(i), 
                     north = receiver1.get(i);
                
                Vector SW = sphere.MAP(south.v0, JRO).plus(v0),
                       SE = sphere.MAP(south.v1, JRO).plus(v0),
                       NW = sphere.MAP(north.v0, JRO).plus(v1),
                       NE = sphere.MAP(north.v1, JRO).plus(v1);
                
                // Make sure lines are oriented correctly
                // SW-NE is the diagonal and should be longer than any side
                if(SW.preciseDistanceTo(NW) > SW.preciseDistanceTo(NE))
                {
                    NE = sphere.MAP(north.v0, JRO).plus(v1);
                    NW = sphere.MAP(north.v1, JRO).plus(v1);
                }

                rodMap.get(edge).add(new Vector[]{SW, NW, NE});
                rodMap.get(edge).add(new Vector[]{NE, SE, SW});
            }
        }
    }
    
    private ArrayList<Line> sortForward(Node node)
    {
        return sort(node, true);
    }
    private ArrayList<Line> sortBackward(Node node)
    {
        return sort(node, false);
        
    }
    private ArrayList<Line> sort(Node node, boolean forward)
    {
        ArrayList<Line> receiver = receiverLines.get(node);
        double thetaNode = node.preimage.e[0];
        
        HashMap<Float, ArrayList<Line>> phiSort = new HashMap<Float, ArrayList<Line>>();
        for(Line edge : receiver)
        {
            float phi = round(edge.midpoint().e[1] + Math.PI/2.0); // shift to avoid problems with ordering positives and negatives
            if(!phiSort.containsKey(phi))
                phiSort.put(phi, new ArrayList<Line>());
            phiSort.get(phi).add(edge);
        }
        
        for(Float phi : phiSort.keySet())
        {
            HashMap<Float, Line> thetaSort = new HashMap<Float, Line>();
            ArrayList<Line> row = phiSort.get(phi);
            float[] thetas = new float[row.size()];
            int index = 0;
            for(Line edge : row) 
            {
                float theta = round(edge.midpoint().e[0] + Math.PI);
                if(Math.abs(thetaNode - theta) > Math.PI) 
                    theta -= Math.signum(theta)*2*Math.PI;
                thetaSort.put(theta, edge);
                thetas[index++] = theta;
            }
            Arrays.sort(thetas);
            
            ArrayList<Line> sortedTheta = new ArrayList<Line>();
            if(forward)
                for(int i = 0; i < thetas.length; i++)
                    sortedTheta.add(thetaSort.get(thetas[i]));
            else
                for(int i = thetas.length-1; i >= 0; i--)
                    sortedTheta.add(thetaSort.get(thetas[i]));
            
            phiSort.put(phi, sortedTheta);
        }
        
        ArrayList<Line> sorted = new ArrayList<Line>();
        float[] phis = new float[phiSort.keySet().size()];
        int index = 0;
        for(Float phi : phiSort.keySet()) phis[index++] = phi;
        Arrays.sort(phis);
        for(int i = 0; i < phis.length; i++)
            sorted.addAll(phiSort.get(phis[i]));
        
        /*System.out.println("\n\nUNSORTED");
            for(Line edge : receiver)
                System.out.println(edge.midpoint());
        System.out.println("SORTED : " + forward);
        for(Line edge : sorted)
            System.out.println(edge.midpoint());
        
        return sorted;
    }


    private void EXTRUDE() throws IOException
    {
        Cell[][] previous = null, // L-2 
                 current = null,  // L-1
                 next = null;     // L
        
        final long start = (new Date()).getTime();
        for(int L = 0; L < RES; L++)
        {
            // Time check
            if(L % 10 == 2)
            {
                long unit = ((new Date()).getTime() - start)/(long)L;
                long remainder = unit*(RES-L);
                int hours = (int)Math.floor(remainder/3600000);
                int minutes = (int)Math.floor(remainder/60000);
                int seconds = (int)Math.floor((remainder/1000)%60);
                System.out.printf("%dh %dm %ds\n", hours, minutes, seconds);
            }
            
            // Pass through 'next' layer and activate cells 
            next = new Cell[RES][RES];
            for(int R = 0; R < RES; R++)
            for(int C = 0; C < RES; C++)
            {
                next[R][C] = new Cell(L, R, C);
                Vector point = getPoint(L, R, C);                                      
                for(Line edge : edges)
                {
                    double d0 = point.distanceTo(edge.v0),
                           d1 = point.distanceTo(edge.v1);
                    
                    if(d0 <= JRO || d1 <= JRO) 
                    {
                        next[R][C].activate();
                        break;
                    }
                    
                    Vector v0P = (new Line(edge.v0, point)).deltas(),
                           v01 = (new Line(edge.v0, edge.v1)).deltas(),
                           v1P = (new Line(edge.v1, point)).deltas(),
                           v10 = (new Line(edge.v1, edge.v0)).deltas();
                    
                    double cTheta0 = v0P.dot(v01)/(v0P.norm()*v01.norm()),
                           cTheta1 = v1P.dot(v10)/(v1P.norm()*v10.norm());
                    if(cTheta0 > 0 && cTheta1 > 0)
                    {
                        double r0 = v0P.norm()*v0P.norm()*(1.0 - cTheta0*cTheta0),
                               r1 = v1P.norm()*v1P.norm()*(1.0 - cTheta1*cTheta1);
                        if(r0 <= RRO*RRO && r1 <= RRO*RRO)
                        {
                            next[R][C].activate();
                            break;
                        }
                    }
                }
            }
            
            // Write panels of the 'current' layer                              ~~~~~~~~~~
            if(current != null)
            for(int R = 0; R < RES; R++)
            for(int C = 0; C < RES; C++)
            if(!current[R][C].isActive())
            {
                Vector point = getPoint(L-1, R, C);
                
                boolean[] status = new boolean[6];
                Arrays.fill(status, false);
                
                if(next != null)       status[UP] = next[R][C].isActive();
                if(previous != null) status[DOWN] = previous[R][C].isActive();
                if(C > 0)            status[LEFT] = current[R][C-1].isActive();
                if(C < RES-1)       status[RIGHT] = current[R][C+1].isActive();
                if(R > 0)           status[FRONT] = current[R-1][C].isActive();
                if(R < RES-1)        status[BACK] = current[R+1][C].isActive();
                
                Vector[] bp = new Vector[8]; // boundary points for the cell
                for(int i = 0; i < 8; i++)
                {
                    int[] digits = Manifold.bitString(i, 2, 3);
                    bp[i] = point.clone();
                    for(int j = 0; j < 3; j++)
                        bp[i].e[j] += digits[j]*STEP;
                }
                
                if(status[UP])   writePanel(bp,1,5,7,3);
                if(status[DOWN]) writePanel(bp,0,4,6,2);
                if(status[LEFT]) writePanel(bp,0,2,3,1);
                if(status[RIGHT])writePanel(bp,4,6,7,5);
                if(status[FRONT])writePanel(bp,0,4,5,1);
                if(status[BACK]) writePanel(bp,2,6,7,3);
            }
            
            previous = current;
            current = next;
        }
    }
    * 
    * private void EXTRUDE() throws IOException
    {
        framework.modeling.Cell[][] previous = null, // L-2 
                 current = null,  // L-1
                 next = null;     // L
        
        final long start = (new Date()).getTime();
        for(int L = 0; L < RES; L++)
        {
            // Time check
            if(L % 10 == 2)
            {
                long unit = ((new Date()).getTime() - start)/(long)L;
                long remainder = unit*(RES-L);
                int hours = (int)Math.floor(remainder/3600000);
                int minutes = (int)Math.floor(remainder/60000);
                int seconds = (int)Math.floor((remainder/1000)%60);
                System.out.printf("%dh %dm %ds\n", hours, minutes, seconds);
            }
            
            // Pass through 'next' layer and activate cells 
            next = new framework.modeling.Cell[RES][RES];
            for(int R = 0; R < RES; R++)
            for(int C = 0; C < RES; C++)
            {
                next[R][C] = new framework.modeling.Cell(L, R, C);
                Vector point = getPoint(L, R, C);                                      
                for(Line edge : edges)
                {
                    double d0 = point.distanceTo(edge.v0),
                           d1 = point.distanceTo(edge.v1);
                    
                    if(d0 <= JRO || d1 <= JRO) 
                    {
                        next[R][C].activate();
                        break;
                    }
                    
                    Vector v0P = (new Line(edge.v0, point)).deltas(),
                           v01 = (new Line(edge.v0, edge.v1)).deltas(),
                           v1P = (new Line(edge.v1, point)).deltas(),
                           v10 = (new Line(edge.v1, edge.v0)).deltas();
                    
                    double cTheta0 = v0P.dot(v01)/(v0P.norm()*v01.norm()),
                           cTheta1 = v1P.dot(v10)/(v1P.norm()*v10.norm());
                    if(cTheta0 > 0 && cTheta1 > 0)
                    {
                        double r0 = v0P.norm()*v0P.norm()*(1.0 - cTheta0*cTheta0),
                               r1 = v1P.norm()*v1P.norm()*(1.0 - cTheta1*cTheta1);
                        if(r0 <= RRO*RRO && r1 <= RRO*RRO)
                        {
                            next[R][C].activate();
                            break;
                        }
                    }
                }
            }
            
            // Write panels of the 'current' layer                              ~~~~~~~~~~
            if(current != null)
            for(int R = 0; R < RES; R++)
            for(int C = 0; C < RES; C++)
            if(!current[R][C].isActive())
            {
                Vector point = getPoint(L-1, R, C);
                
                boolean[] status = new boolean[6];
                Arrays.fill(status, false);
                
                if(next != null)       status[UP] = next[R][C].isActive();
                if(previous != null) status[DOWN] = previous[R][C].isActive();
                if(C > 0)            status[LEFT] = current[R][C-1].isActive();
                if(C < RES-1)       status[RIGHT] = current[R][C+1].isActive();
                if(R > 0)           status[FRONT] = current[R-1][C].isActive();
                if(R < RES-1)        status[BACK] = current[R+1][C].isActive();
                
                Vector[] bp = new Vector[8]; // boundary points for the cell
                for(int i = 0; i < 8; i++)
                {
                    int[] digits = Manifold.bitString(i, 2, 3);
                    bp[i] = point.clone();
                    for(int j = 0; j < 3; j++)
                        bp[i].e[j] += digits[j]*STEP;
                }
                
                if(status[UP])   writePanel(bp,1,5,7,3);
                if(status[DOWN]) writePanel(bp,0,4,6,2);
                if(status[LEFT]) writePanel(bp,0,2,3,1);
                if(status[RIGHT])writePanel(bp,4,6,7,5);
                if(status[FRONT])writePanel(bp,0,4,5,1);
                if(status[BACK]) writePanel(bp,2,6,7,3);
            }
            
            previous = current;
            current = next;
        }
    }
*/}
