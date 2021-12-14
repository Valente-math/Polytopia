package obsolete;

import obsolete.Node;
import obsolete.Cell;
import abstracts.Manifold;
import framework.primitives.Line;
import framework.primitives.Vector;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import manifolds.smoothmanifolds.Hypersphere;

/**
 *
 * @author Valente Productions
 */

public class CellComplex 
{
    private Manifold manifold;
    private HashSet<Vector> vertices;
    private HashSet<Line> edges;
    
    private HashMap<Vector, HashSet<Node>> vertexMap; 
    private HashMap<Line, ArrayList<Node>> edgeMap;
    private HashMap<Node, ArrayList<Line>> receiverLines;
    private HashMap<Node, HashSet<Cell>> receiverCells;
    private HashMap<Vector, Cell[][]> cellMap;
    private HashMap<Line, HashSet<Vector[]>> rodMap;
    
    private float RO;  // Rod radius
    private float rodScale = 0.5f;
    private float JRO = 2.4f; // Joint radius
    private int RES = 24; // Joint resolution
    private int SUBRES = 3;// Resolution of boundary cells
    private double STEP = Math.PI/(double)RES;
    private double SUBSTEP = STEP/(double)SUBRES;
 
    private BufferedWriter writer;
    public static Hypersphere sphere = new Hypersphere(3);
    
    public int NORTH = 0, NEAST = 1, EAST = 2, SEAST = 3,
               SOUTH = 4, SWEST = 5, WEST = 6, NWEST = 7;
    
    public CellComplex(Manifold M)
    {
        manifold = M;
        edges = new HashSet<Line>();
        vertices = new HashSet<Vector>();
        for(Line e : M.edgesR3)
        {
            Line ec = e.clone();
            if(ec.length() > 0 && !ec.v0.isEqualToR(ec.v1)) 
                edges.add(ec);
            vertices.add(ec.v0);
            vertices.add(ec.v1);
        }
    }
    
    public void create()
    {
        open();
        System.out.println("Finding Nodes...");
            findNodes();
        System.out.println("Calculating Radius...");
            calculateRadius();
        System.out.println("Refining Nodes...");
            refineNodes();
        System.out.println("Initializing Cell Map...");
            initializeCellMap();
        System.out.println("Marking Interior...");
            markInterior();
        System.out.println("Marking Receivers...");
            markBoundary();
        System.out.println("Refining...");
            refineBoundary();
        System.out.println("Connecting...");
            createRods();
        System.out.println("Writing...");
            write();
        System.out.println("DONE!");
        close();
        
        System.out.println("BOUNDING BOX: " + boundingBox());
        System.out.println("");
    }
    
    private double boundingBox()
    {
        return 0;
    }
    
    private void write()
    {
        HashMap<Vector, Boolean> check = new HashMap<Vector, Boolean>();
        for(Vector vertex : vertices) check.put(vertex, true);
        for(Line edge : edges)
        {
            for(Vector[] facet : rodMap.get(edge))
                writeFacet(facet[0], facet[1], facet[2]);
            
            if(check.get(edge.v0))
            {
                for(int r = 0; r < RES; r++)
                for(int c = 0; c < 2*RES; c++)
                    cellMap.get(edge.v0)[r][c].write(writer);
                
                check.put(edge.v0, false);
            }
            if(check.get(edge.v1))
            {
                for(int r = 0; r < RES; r++)
                for(int c = 0; c < 2*RES; c++)
                    cellMap.get(edge.v1)[r][c].write(writer);
                
                check.put(edge.v1, false);
            }
        }
    }   
    
    public void createRods()
    {
        rodMap = new HashMap<Line, HashSet<Vector[]>>();
        for(Line edge : edges)
        {
            if(edgeMap.get(edge) == null || edgeMap.get(edge).size() == 1)
                System.out.println("SPECIAL EDGE: " + edge);
            rodMap.put(edge, new HashSet<Vector[]>());
            
            Node n0 = edgeMap.get(edge).get(0), 
                 n1 = edgeMap.get(edge).get(1);
            Vector v0 = n0.vertex,
                   v1 = n1.vertex;
            Cell[][] matrix0 = cellMap.get(v0),
                     matrix1 = cellMap.get(v1);
            
            int n0C = (int)Math.floor((n0.preimage.e[0] + Math.PI)/(Math.PI/(double)RES)),
                n0R = (int)Math.floor((n0.preimage.e[1] + Math.PI/2.0)/(Math.PI/(double)RES)),
                n1C = (int)Math.floor((n1.preimage.e[0] + Math.PI)/(Math.PI/(double)RES)),
                n1R = (int)Math.floor((n1.preimage.e[1] + Math.PI/2.0)/(Math.PI/(double)RES));
            
            if(n0R >= RES) n0R = RES - 1;
            if(n0R < 0) n0R = 0;
            if(n1R >= RES) n1R = RES - 1;
            if(n1R < 0) n1R = 0;
            
            if(n0C >= 2*RES) n0C = 2*RES-1;
            if(n0C < 0) n0C = 0;
            if(n1C >= 2*RES) n1C = 2*RES-1;
            if(n1C < 0) n1C = 0;
            
            int Rshift = n1R - n0R,
                Cshift = n1C - n0C;    
            
            HashSet<Cell> receiver0 = receiverCells.get(n0),
                            receiver1 = receiverCells.get(n1);
                        
            // iterate through each receiver cell
            for(Cell bcell : receiver0)
            {
                int R = bcell.R_, 
                    C = bcell.C_,
                    r = bcell.r_,
                    c = bcell.c_;
                
                boolean POLE = (matrix0[R][C].rowReflection == null);
                
                if(POLE)
                {
                    //System.out.println("no reflection");
                    //System.out.printf("node: r = %d, c = %d\ncell: r = %d, c = %d\n", n0R, n0C, R, C);
                    
                    int Rdist = R - n0R;
                    R = R - 2*Rdist;
                    
                    //slide
                    R = R + Rshift;
                    r = SUBRES-1 - r;
                    c = c;
                }
                else
                {
                    //System.out.println("has reflection");
                    //System.out.printf("node: r = %d, c = %d\ncell: r = %d, c = %d\n", n0R, n0C, R, C);
                    //flip
                    R = matrix0[R][C].colReflection.r_;
                    C = matrix0[R][C].colReflection.c_;
                    //and slide
                    R = R + Rshift;
                    C = mod(C + Cshift, 2*RES); // wrap-around
                    r = r;
                    c = SUBRES-1 - c;
                }
                
                if(matrix1[R][C].matrix == null)
                {
                    System.out.println("NULL CELL");
                    System.out.printf("node0: r = %d, c = %d\ncell: r = %d, c = %d\n", n0R, n0C, bcell.R_, bcell.C_);
                    System.out.printf("node1: r = %d, c = %d\ncell: r = %d, c = %d\n", n1R, n1C, R, C);
                }
                
                Cell target = matrix1[R][C].matrix[r][c];
                for(int i = 0; i < 8; i++)
                {
                    Line edge0 = null, edge1 = null;
                    edge0 = bcell.localReceiver[i];
                    
                    // north and south stay the same if not on a pole
                    if(i == NORTH) 
                        if(POLE)
                            edge1 = target.localReceiver[SOUTH];
                        else
                            edge1 = target.localReceiver[NORTH];
                    
                    if(i == SOUTH) 
                        if(POLE)
                            edge1 = target.localReceiver[NORTH];
                        else
                            edge1 = target.localReceiver[SOUTH];
                    
                    // no east or west on poles, but still wraps wround
                    if(i == EAST) 
                        edge1 = target.localReceiver[WEST];
                    
                    if(i == WEST)
                        edge1 = target.localReceiver[EAST];
                    
                    // first diagonal
                    if(i == NEAST || i == SWEST)
                        if(target.localReceiver[NWEST] != null)
                            edge1 = target.localReceiver[NWEST];
                        else
                            edge1 = target.localReceiver[SEAST];
                    // second diagonal
                    if(i == NWEST || i == SEAST)
                        if(target.localReceiver[NEAST] != null)
                            edge1 = target.localReceiver[NEAST];
                        else
                            edge1 = target.localReceiver[SWEST];
                    
                    if(edge0 != null && edge1 != null)
                    {
                        Line south = edge0, north =  edge1;

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
        }
    }
    
    private void refineBoundary()
    {
        //receiverLines = new HashMap<Node, ArrayList<Line>>();
        receiverCells = new HashMap<Node, HashSet<Cell>>();
        
        for(Vector vertex : vertices)
        {
            Cell[][] matrix = cellMap.get(vertex);
            for(Node node : vertexMap.get(vertex))
            {
                //receiverLines.put(node, new ArrayList<Line>());
                receiverCells.put(node, new HashSet<Cell>());
            }
            
            for(int r = 0; r < RES; r++)
            for(int c = 0; c < 2*RES; c++)
            {
                Cell bcell = matrix[r][c];
                
                if(bcell.type == 2)
                {
                    bcell.drawReceiver(receiverCells.get(bcell.anchor));
                    //receiverLines.get(bcell.anchor).addAll(bcell.lineReceiver);
                    //receiverCells.get(bcell.anchor).addAll(bcell.receiver);
                }
            }
        }
    }
    
    private void markBoundary()
    {
        for(Vector vertex : vertices)
        {
            Cell[][] matrix = cellMap.get(vertex);
            
            // mark boundary
            for(int r = 0; r < RES; r++)
            for(int c = 0; c < 2*RES; c++)
            {
                Cell bcell = matrix[r][c];
                
                if(bcell.type == 0)
                {
                    if((r < RES-1) && (matrix[r+1][c].type == 2))  // NORTH = 0
                        bcell.setType1(matrix[r+1][c].anchor, 0, getNeighbors(matrix, r, c));
                    
                    if((c < 2*RES-1) && (matrix[r][c+1].type == 2))//  EAST = 1
                        bcell.setType1(matrix[r][c+1].anchor, 1, getNeighbors(matrix, r, c));
                    
                    if((r > 0) && (matrix[r-1][c].type == 2))      // SOUTH = 2
                        bcell.setType1(matrix[r-1][c].anchor, 2, getNeighbors(matrix, r, c));
                    
                    if((c > 0) && (matrix[r][c-1].type == 2))      //  WEST = 3
                        bcell.setType1(matrix[r][c-1].anchor, 3, getNeighbors(matrix, r, c));
                }
            }
        }
    }
    
    private void markInterior()
    {
        for(Vector vertex : vertices)
        {
            Cell[][] matrix = cellMap.get(vertex);
            
            for(int r = 0; r < RES; r++)
            for(int c = 0; c < 2*RES; c++)
            if(matrix[r][c].type == 0)
            {
                Cell cell = matrix[r][c];
                Vector NW = sphere.MAP(cell.nw, JRO).plus(vertex),
                       NE = sphere.MAP(cell.ne, JRO).plus(vertex),
                       SW = sphere.MAP(cell.sw, JRO).plus(vertex),
                       SE = sphere.MAP(cell.se, JRO).plus(vertex);
                
                for(Node node : vertexMap.get(vertex))
                {
                    double  theta = node.preimage.e[0],
                            phi = node.preimage.e[1];
                    int nodeRow = (int)Math.floor((phi + Math.PI/2.0)/STEP),
                        nodeCol= (int)Math.floor((theta + Math.PI)/STEP);
                    if(nodeCol >= 2*RES) nodeCol = 2*RES - 1;
                    if(nodeRow >= RES) nodeRow = RES - 1;
                    if(nodeCol < 0) nodeCol = 0;
                    if(nodeRow < 0) nodeRow = 0;
                    
                    int Rdist = r - nodeRow,
                        Cdist = c - nodeCol;
                    int _r = r - 2*Rdist,
                        _c = mod(c - 2*Cdist, 2*RES); // east wraps around to west
                    Cell rowRef = null,
                         colRef = null,
                         rcRef = null;
                    if(0 <= _r  && _r < RES)
                        rowRef = matrix[_r][c];
                    if(0 <= _c  && _c < 2*RES)
                        colRef = matrix[r][_c];
                    else
                        System.out.println("THIS SHOULDN'T HAPPEN");
                    
                    boolean rR = (rowRef == null);
                    
                    if(!rR) rcRef = matrix[_r][_c];
                    
                    double R = RO;   
                    boolean d1 = (node.actual.distanceTo(NW) <= R),
                            d2 = (node.actual.distanceTo(NE) <= R),
                            d3 = (node.actual.distanceTo(SW) <= R),
                            d4 = (node.actual.distanceTo(SE) <= R);
                    
                    if(d1 || d2 || d3 || d4)
                        if(d1 && d2 && d3 && d4)
                        {
                            cell.setType3(node);
                            colRef.setType3(node);
                            if(!rR)
                            {
                                rowRef.setType3(node);
                                rcRef.setType3(node);
                            }
                        }
                        else
                        {
                            cell.setType2(node, RO,  getNeighbors(matrix, r, c));
                            cell.setReflections(rowRef, colRef, rcRef);
                            
                            colRef.setType2(node, RO,  getNeighbors(matrix, r, _c));
                            colRef.setReflections(rcRef, cell, rowRef);
                            
                            if(!rR)
                            {
                                rowRef.setType2(node, RO,  getNeighbors(matrix, _r, c));
                                rowRef.setReflections(cell, rcRef, colRef);
                                
                                rcRef.setType2(node, RO,  getNeighbors(matrix, _r, _c));
                                rcRef.setReflections(colRef, rowRef, cell);
                            }
                            
                            cell.markInterior();
                            colRef.markInterior();
                            if(!rR)
                            {
                                rowRef.markInterior();
                                rcRef.markInterior();
                            }
                        }
                }
             }
        }
    }
    
    private int mod(int x, int n)
    {
        if(x >= 0) 
            return (x % n);
        else
            return ((n + x) % n);
    }
    
    private void initializeCellMap()
    {
        cellMap = new HashMap<Vector, Cell[][]>();
        
        for(Vector vertex : vertices)
        {
            Cell[][] matrix = new Cell[RES][2*RES];
            
            for(int r = 0; r < RES; r++)
                for(int c = 0; c < 2.0*RES; c++)
                    matrix[r][c] = (new Cell(vertex, r, c, RES, SUBRES, JRO,
                                        new double[]{-Math.PI, -Math.PI/2.0},
                                        new double[]{ Math.PI,  Math.PI/2.0}));
            
            cellMap.put(vertex, matrix);
        }
    }
    
    // Any node whose interior includes a pole will be moved directly on the pole
    private void refineNodes()
    {
        Vector nPole =  new Vector(new double[]{0, 0, JRO}),
               sPole =  new Vector(new double[]{0, 0, -JRO});
        
        for(Vector vertex : vertices)
            for(Node node : vertexMap.get(vertex))
            {
                Line edge = node.edge;
                
                double nDist = node.actual.distanceTo(nPole.plus(vertex)),
                       sDist = node.actual.distanceTo(sPole.plus(vertex));
                
                if(nDist <= 1.2*RO)
                    node.setPreimage(new Vector(new double[]{0, Math.PI/2.0}));
                else if(sDist <= 1.2*RO)
                    node.setPreimage(new Vector(new double[]{0, -Math.PI/2.0}));
                
            }
    }
    
    private void calculateRadius()
    {
        float min = Float.MAX_VALUE;
        for(Vector vertex : vertexMap.keySet())
            for(Node n1 : vertexMap.get(vertex))
                for(Node n2 : vertexMap.get(vertex))
                {
                    Vector v1 = n1.image;
                    Vector v2 = n2.image;
                    double ratio = v1.dot(v2)/(v1.norm()*v2.norm());
                    float theta = round(Math.abs(Math.acos(ratio)));
                    
                    if(theta > 0 && theta < min) min = theta;
                }
        
        // see sphere map notes for derivation of main equation
        // the 0.5 scales it back from the maximum radius
        RO = rodScale*(JRO*(float)Math.sin(min/2.0)); 
        System.out.println("RADIUS = " + RO);
        //System.out.println("RADIUS = " + RO);
    }
    
    private void findNodes()
    {
        vertexMap = new HashMap<Vector, HashSet<Node>>();
        edgeMap = new HashMap<Line, ArrayList<Node>>();
        
        for(Line edge : edges)
        {
            edgeMap.put(edge, new ArrayList<Node>());
            
            Vector v0 = edge.v0,
                   v1 = edge.v1;
            
            if(vertexMap.get(v0) == null)
                vertexMap.put(v0, new HashSet<Node>());
            if(vertexMap.get(v1) == null)
                vertexMap.put(v1, new HashSet<Node>());
            
            
            // For each edge connected to the vertex v0, find the intersection
            // of that line and the joint surrounding v0 of radius JRO. 
            // These points are the nodes
                Vector dv0 = edge.deltas(v0),
                       dv1 = edge.deltas(v1);
                
                Line cxn0 = new Line(new Vector(3), dv0),
                     cxn1 = new Line(new Vector(3), dv1);

                double t0 = JRO/dv0.norm(),
                       t1 = JRO/dv1.norm();
                Vector sphereNode0 = cxn0.f(t0),
                       sphereNode1 = cxn1.f(t1);

                Node n0 = new Node(sphere.INVERT(sphereNode0), v0, edge, JRO),
                     n1 = new Node(sphere.INVERT(sphereNode1), v1, edge, JRO);
                
                edgeMap.get(edge).add(n0);
                edgeMap.get(edge).add(n1);
                vertexMap.get(v0).add(n0);
                vertexMap.get(v1).add(n1);
        }
    }
    
    private void findNodes2()
    {
        vertexMap = new HashMap<Vector, HashSet<Node>>();
        edgeMap = new HashMap<Line, ArrayList<Node>>();
        
        for(Vector vertex : vertices)
        {
            HashSet<Node> nodes = new HashSet<Node>();
            HashSet<Line> localEdges = new HashSet<Line>();
            
            // Identify all edges that connect to this vertex
            for(Line edge: edges)
                if(edge.v0.isEqualTo(vertex)||edge.v1.isEqualTo(vertex))
                    localEdges.add(edge);
            
            // For each edge connected to the vertex v0, find the intersection
            // of that line and the joint surrounding v0 of radius JRO. 
            // These points are the nodes
            for(Line edge : localEdges)
            {
                Vector dv = edge.deltas(vertex);
                
                if(edgeMap.get(edge) == null) 
                    edgeMap.put(edge, new ArrayList<Node>());
                
                Line temp = new Line(new Vector(3), dv);

                double t = JRO/dv.norm();
                Vector sphereNode = temp.f(t);

                Node n = new Node(sphere.INVERT(sphereNode), vertex, edge, JRO);
                
                nodes.add(n);
                
                edgeMap.get(edge).add(n);
            }
            //nodes.add(new Node(new Vector(new double[]{3.10, 1.45}), vertex));
            vertexMap.put(vertex, nodes);
        }
    }
    
    public void writeFacet(Vector v1, Vector v2, Vector v3)
    {
        float v1x = round(v1.e[0]),
              v1y = round(v1.e[1]),
              v1z = round(v1.e[2]),
        
              v2x = round(v2.e[0]),
              v2y = round(v2.e[1]),
              v2z = round(v2.e[2]),
        
              v3x = round(v3.e[0]),
              v3y = round(v3.e[1]),
              v3z = round(v3.e[2]);
        
        String N1 = 0f  + " " + 0f  + " " + 0f;
        String V1 = v1x + " " + v1y + " " + v1z;
        String V2 = v2x + " " + v2y + " " + v2z;
        String V3 = v3x + " " + v3y + " " + v3z;
        
        try
        {
            writer.write("facet normal " + N1);
                writer.newLine();
            writer.write("outer loop");
                writer.newLine();
            writer.write("vertex " + V1);
                writer.newLine();
            writer.write("vertex " + V2);
                writer.newLine();
            writer.write("vertex " + V3);
                writer.newLine();
            writer.write("endloop");
                writer.newLine();
            writer.write("endfacet");
                writer.newLine();
        }catch(IOException ex){};
    }
    
    private Cell[] getNeighbors(Cell[][] matrix, int r, int c)
    {
        int rN = RES,
            cN = 2*RES;
        Cell[] neighbors = new Cell[8]; 
        Arrays.fill(neighbors, null);
        boolean hasNorth = (r < rN-1),
                hasSouth = (r > 0),
                hasEast  = (c < cN-1),
                hasWest  = (c > 0);
        if(hasNorth) 
        {
            neighbors[NORTH] = matrix[r+1][c]; // NORTH
            if(hasEast) neighbors[NEAST] = matrix[r+1][c+1];
            else neighbors[NEAST] = matrix[r+1][0];
            
            if(hasWest) neighbors[NWEST] = matrix[r+1][c-1];
            else neighbors[NWEST] = matrix[r+1][cN-1];
        }
        if(hasSouth) 
        {
            neighbors[SOUTH] = matrix[r-1][c]; // NORTH
            if(hasEast) neighbors[SEAST] = matrix[r-1][c+1];
            else neighbors[SEAST] = matrix[r-1][0];
                
            if(hasWest) neighbors[SWEST] = matrix[r-1][c-1];
            else neighbors[SWEST] = matrix[r-1][cN-1];
        }
        
        if(hasEast) neighbors[EAST] = matrix[r][c+1];
        else neighbors[EAST] = matrix[r][0];
        
        if(hasWest) neighbors[WEST] = matrix[r][c-1];
        else neighbors[WEST] = matrix[r][cN-1];
        
        return neighbors;
    }
    
    public void open()
    {
        try 
        {
            writer = new BufferedWriter(new FileWriter("C:\\Users\\Nicholas\\Documents\\Models\\" + manifold.getName() + ".StL"));
            writer.write("solid " + manifold.ID);
            writer.newLine();
        }catch(IOException ex){}
    }
    
    public void close()
    {
        try 
        {
            writer.write("endsolid " + manifold.ID);
            writer.close();
        }catch(IOException ex){}
    }
    
    public float round(double x)
    {
        return round(x, 6);
    }
    
    public float round(double x, int precision)
    {
        float p = (float)Math.pow(10, precision);
        return Math.round(x*p)/p;
    }
    
    public void printSample2()
    {
        Vector vertex = vertices.iterator().next();
        Cell[][] matrix = cellMap.get(vertex);
        
        for(Node n : vertexMap.get(vertex))
        {
            double theta = n.preimage.e[0] + Math.PI,
                   phi = n.preimage.e[1] + Math.PI/2.0;
            
            int nodeRow = (int)Math.floor(phi/STEP),
                nodeCol = (int)Math.floor(theta/STEP);
            if(nodeCol >= 2*RES) nodeCol = 2*RES - 1;
            if(nodeRow >= RES) nodeRow = RES - 1;
            if(nodeCol < 0) nodeCol = 0;
            if(nodeRow < 0) nodeRow = 0;
            System.out.println(matrix[nodeRow][nodeCol].center);
            //System.out.println(thetaN + "," + phiN);
        }
        
        for(int r = 0; r < RES; r++)
        for(int c = 0; c < 2*RES; c++)
        if(matrix[r][c].type == 2)
            //System.out.println(matrix[r][c].center);
            for(int rr = 0; rr < SUBRES; rr++)
            for(int cc = 0; cc < SUBRES; cc++)
            {
                Cell subcell = matrix[r][c].matrix[rr][cc];
                if(subcell.type == 2)
                    System.out.println(subcell.center);
            }
    }

    public void printSample()
    {
        //for(Line edge : edges)
        {
        Line edge = edges.iterator().next();
            Node n0 = null, n1 = null;
            for(Node node : edgeMap.get(edge))
            {
                if(node.vertex.isEqualTo(edge.v0))
                    n0 = node;
                if(node.vertex.isEqualTo(edge.v1))
                    n1 = node;
            }
            if(round(n0.preimage.e[1], 4) != 0.0)
            {
                /*n0 = new Node(new Vector(new double[]{1.57, 0.1}), n0.vertex, edge, JRO);
                n1 = new Node(new Vector(new double[]{1.57, -0.1}), n0.vertex, edge, JRO);*/
                ArrayList<Line> receiver0 = receiverLines.get(n0),
                                receiver1 = receiverLines.get(n1);

                if(receiverLines.get(n0).size() != receiverLines.get(n1).size())
                {
                    System.out.println("INCOMPATIBLE RECEIVERS");
                    System.out.println("RECEIVER 0: " + n0);
                    System.out.println("RECEIVER 1: " + n1);
                    System.out.println("");
                }

                System.out.println("--- V0 ---");
                for(Line seg : receiver0)
                    System.out.println(seg.midpoint());
                
                System.out.println("");
                
                System.out.println("--- V1 ---");
                for(Line seg : receiver1)
                    System.out.println(seg.midpoint());
            }
        }
    }
    
    public void printSampleReceiver()
    {
        Vector vertex = vertices.iterator().next();
        System.out.println("VERTEX: " + vertex);
        for(Node node : vertexMap.get(vertex))
        {
            HashSet<Vector> nodes = new HashSet<Vector>();
            for(Line line : receiverLines.get(node))
                {nodes.add(line.v0); nodes.add(line.v1);}

            System.out.println(node.preimage);
            for(Vector bound : nodes)
                System.out.println(bound.toStringR());
            System.out.println("");
        }
        System.out.println("");
    }
    
    public void printReceivers()
    {
        for(Vector vertex : vertices)
        {
            System.out.println("VERTEX: " + vertex);
            for(Node node : vertexMap.get(vertex))
            {
                for(Line line : receiverLines.get(node))
                    System.out.println(line.midpoint());
                System.out.println("");
            }
            for(int i = 0; i < 90; i++) System.out.print("_");
            System.out.println("");
        }
    }
    
    public void printVertices()
    {
        for(Vector vertex  : vertexMap.keySet())
        {
            System.out.println("VERTEX\n" + vertex);
            for(Node n : vertexMap.get(vertex))
                System.out.println("\t" + n);// + "\n\t  " + n.edge);
        }
    }
   
    public void printEdges()
    {
        System.out.println("EDGES");
        for(Line edge : edges)
        {
            System.out.println(edge.toString());
            if(edgeMap.get(edge) == null) 
            {
                System.out.println("NULL");
                System.out.println(vertexMap.get(edge.v0).size());
                System.out.println(vertexMap.get(edge.v1).size());
            }
            else
            for(Node node : edgeMap.get(edge))
            {
                //System.out.println("\t" + node);
             //   for(Cell cell : receiverCells.get(node))
             //       System.out.printf("\t%d, %d : %d, %d\n\n", cell.R_, cell.C_, cell.r_, cell.c_);
            }
        }
    }
    
    public void printEdges2()
    {
        System.out.println("EDGES");
        for(Line edge : edges)
        {
            for(Node n : edgeMap.get(edge))
            {
                if(Math.abs(n.preimage.e[1]) > 1.5)
                {
                    
                     System.out.println("NODE: " + n.preimage);
                    for(Cell bcell : receiverCells.get(n))
                        System.out.printf("R: %d, C: %d\n", bcell.r_, bcell.c_);
                        //System.out.printf("R: %d, C: %d\nr: %d, c: %d\n", bcell.R_, bcell.C_, bcell.r_, bcell.c_);
                        //System.out.println(bcell.center);
                }
            }
            for(int i = 0; i < 90; i++)
                System.out.print("_");
            System.out.println("");
        }
    }
    
}
