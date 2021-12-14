package obsolete;

/**
 *
 * @author Valente Productions
 */

public class Backup 
{
/*
 * private void refineBoundary()
    {
        receiverMap = new HashMap<Node, ArrayList<Line>>();
        
        for(Vector vertex : vertices)
        {
            Cell[][] matrix = cellMap.get(vertex);
            for(Node node : vertexMap.get(vertex))
                receiverMap.put(node, new ArrayList<Line>());
            
            for(int r = 0; r < RES; r++)
            for(int c = 0; c < 2*RES; c++)
            {
                Cell bcell = matrix[r][c];
                
                if(bcell.type == 2)
                {
                    bcell.drawReceiver();
                    receiverMap.get(bcell.anchor).addAll(bcell.receiver);
                }
            }
        }
    }
    
    private void markBoundary()
    {
        for(Vector vertex : vertices)
        {
            Cell[][] matrix = cellMap.get(vertex);
            
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
            {
                Cell cell = matrix[r][c];
                Vector NW = sphere.MAP(cell.nw, JRO).plus(vertex),
                       NE = sphere.MAP(cell.ne, JRO).plus(vertex),
                       SW = sphere.MAP(cell.sw, JRO).plus(vertex),
                       SE = sphere.MAP(cell.se, JRO).plus(vertex);
                
                for(Node node : vertexMap.get(vertex))
                {
                    float R = RO;   
                    boolean d1 = (node.actual.distanceTo(NW) <= R),
                            d2 = (node.actual.distanceTo(NE) <= R),
                            d3 = (node.actual.distanceTo(SW) <= R),
                            d4 = (node.actual.distanceTo(SE) <= R);
                    
                    if(d1 || d2 || d3 || d4)
                        if(d1 && d2 && d3 && d4)
                            cell.setType3(node);
                        else
                            cell.setType2(node, RO,  getNeighbors(matrix, r, c));
                }
             }
        }
    }
    
 */
}

/*
 * package framework.modeling;

import framework.Line;
import framework.Vector;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import manifolds.Hypersphere;

public class Cell
{
    public double[] nw, ne, sw, se, ce; 
    public Vector NW, NE, SW, SE, CE;
    
    public int r_, c_;
    
    public Vector center;
    public Vector vertex;
    public Node anchor;
    
    public int type;
    public Cell[][] matrix; 
    public int RES_;
    public int SUBRES = 4;
    
    public double RO;
    public double JRO;
    public Cell[] neighbors;
    public boolean[] dirs;
    public HashSet<Line> receiver;
    
    public BufferedWriter writer;
    public double SIZE, SCALAR;
    
    public Cell(int r, int c, int RES, Vector _vertex, double _JRO)
    {
        this(r, c, RES, new double[]{-Math.PI, -Math.PI/2},
                        new double[]{ Math.PI,  Math.PI/2}, _vertex, _JRO);
    }
    
    public Cell(int r, int c, int RES, double[] _sw, double[] _ne, Vector _vertex, double _JRO)
    {
        vertex = _vertex;
        RES_ = RES;
        JRO = _JRO;
        r_ = r;
        c_ = c;
        double STEP = (_ne[1] - _sw[1])/(double)RES_;
        double theta = c_*STEP + _sw[0];//theta0;
        double phi = r_*STEP + _sw[1];// phi0;
        
        sw = new double[]{theta, phi};
        se = new double[]{theta + STEP, phi};
        nw = new double[]{theta, phi + STEP};
        ne = new double[]{theta + STEP, phi + STEP};
        ce = new double[]{theta + STEP/2, phi + STEP/2};
        center = new Vector(ce);
        
        Hypersphere sphere = new Hypersphere(3);
        NW = sphere.MAP(nw, JRO).plus(vertex);
        SW = sphere.MAP(sw, JRO).plus(vertex);
        NE = sphere.MAP(ne, JRO).plus(vertex);
        SE = sphere.MAP(se, JRO).plus(vertex);
        CE = sphere.MAP(ce, JRO).plus(vertex);
        
        
        type = 0;
        dirs = new boolean[4];
        Arrays.fill(dirs, false);
        matrix = null;
    }
    
    public void setType(int _type, Node _anchor)
    {
        type = _type;
        anchor = _anchor;
    }
    
    public void setType1(Node _anchor, int dir, Cell[] _neighbors)
    {
        setType(1, _anchor);
        dirs[dir] = true;
        neighbors = _neighbors;
    }
    
    public void setType2(Node _anchor, float ro, Cell[] _neighbors)
    {
        setType(2, _anchor);
        
        RO = ro;
        neighbors = _neighbors;
        initSubcellMatrix();
        markInterior();
    }
    
    public void setType2(Node _anchor)
    {
        setType(2, _anchor);
    }
    
    public void setType3(Node _anchor)
    {
        setType(3, _anchor);
    }
    
    private void initSubcellMatrix()
    {
        matrix = new Cell[SUBRES][SUBRES];//matrix = new Cell[RES][RES];
        
        for(int r = 0; r < SUBRES; r++)
            for(int c = 0; c < SUBRES; c++)
                matrix[r][c] = (new Cell(r, c, SUBRES, sw, ne, vertex, JRO));
    }
    
    private void markInterior()
    {
        for(int r = 0; r < SUBRES; r++)
        for(int c = 0; c < SUBRES; c++)
        {
            Cell subcell = matrix[r][c];
            
            boolean d1 = (anchor.actual.distanceTo(subcell.NW) <= RO),
                    d2 = (anchor.actual.distanceTo(subcell.NE) <= RO),
                    d3 = (anchor.actual.distanceTo(subcell.SW) <= RO),
                    d4 = (anchor.actual.distanceTo(subcell.SE) <= RO);
            
            if(d1 || d2 || d3 || d4)
                if(d1 && d2 && d3 && d4)
                    subcell.setType3(anchor);
                else
                    subcell.setType2(anchor);
         }
    }
    
    public void drawReceiver()
    {
        receiver = new HashSet<Line>();
        int R = SUBRES - 1;
        for(int r = 0; r < SUBRES; r++)
        for(int c = 0; c < SUBRES; c++)
        {
            Cell bcell = matrix[r][c];

            if(bcell.type == 2)
            {
                Line north = new Line(new Vector(bcell.ne), new Vector(bcell.nw)),
                     east  = new Line(new Vector(bcell.ne), new Vector(bcell.se)),
                     south = new Line(new Vector(bcell.se), new Vector(bcell.sw)),
                     west  = new Line(new Vector(bcell.nw), new Vector(bcell.sw));
                
                Cell NORTH = neighbors[0];
                if(((r  < R)&&(matrix[r+1][c].type == 0))  // non-border
                  ||((r == R)&&(NORTH != null)
                    &&((NORTH.type  == 1)||((NORTH.type == 2) 
                      &&(NORTH.matrix[0][c].type == 0)))))
                    
                    receiver.add(north);

                Cell EAST = neighbors[1];
                if(((c  < R)&&(matrix[r][c+1].type == 0))  // non-border
                  ||((c == R)&&(EAST != null)
                    &&((EAST.type  == 1)||((EAST.type == 2) 
                      &&(EAST.matrix[r][0].type == 0))))) //subneighbor
                
                    receiver.add(east);

                Cell SOUTH = neighbors[2];
                if(((r > 0)&&(matrix[r-1][c].type == 0))  // non-border
                  ||((r == 0)&&(SOUTH != null) 
                    &&((SOUTH.type  == 1)||((SOUTH.type == 2) 
                      &&(SOUTH.matrix[R][c].type == 0)))))
                                                            //subneighbor
                    receiver.add(south);

                Cell WEST = neighbors[3];
                if(((c  > 0)&&(matrix[r][c-1].type == 0))  // non-border
                  ||((c == 0)&&(WEST != null)
                    &&((WEST.type  == 1)||((WEST.type == 2) 
                      &&(WEST.matrix[r][R].type == 0)))))
                                                            //subneighbor
                    receiver.add(west);
            }
        }
    }
    
    
    public void write(BufferedWriter _writer)
    {
        writer = _writer;
        switch(type)
        {
            case 0: write0(); break;
            case 1: write1(); break;
            case 2: write2(); break;
            default: break;
        }
    }
    
    public void write0()
    {
        if(!NW.isEqualTo(NE))
            writeFacet(SW, NW, NE);
        if(!SE.isEqualTo(SW))
            writeFacet(NE, SE, SW);
    }
    
    public void write1()
    {
        // NORTH
        if(dirs[0]) 
            for(int c = 0; c < SUBRES; c++)
            {
                writeFacet(CE, 
                           neighbors[0].matrix[0][c].SW,
                           neighbors[0].matrix[0][c].SE);
            }
        else if(r_ < RES_ - 1)
            writeFacet(NW, NE, CE);
        
        // EAST
        if(dirs[1]) 
            for(int r = 0; r < SUBRES; r++)
                writeFacet(CE, 
                           neighbors[1].matrix[r][0].NW,
                           neighbors[1].matrix[r][0].SW);
        else
            writeFacet(NE, SE, CE);
        
        // SOUTH
        if(dirs[2]) 
            for(int c = 0; c < SUBRES; c++)
                writeFacet(CE, 
                           neighbors[2].matrix[SUBRES-1][c].NE,
                           neighbors[2].matrix[SUBRES-1][c].NW);
        else if(r_ > 0)
            writeFacet(SE, SW, CE);
               
        // WEST
        if(dirs[3]) 
            for(int r = 0; r < SUBRES; r++)
                writeFacet(CE, 
                           neighbors[3].matrix[r][SUBRES-1].SE,
                           neighbors[3].matrix[r][SUBRES-1].NE);
        else
            writeFacet(SW, NW, CE);            
    }
    
    public void write2()
    {
        if(matrix != null)
            for(int r = 0; r < SUBRES; r++)
            for(int c = 0; c < SUBRES; c++)
                matrix[r][c].write(writer);
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
        float v1x = (float)v1.e[0],
              v1y = (float)v1.e[1],
              v1z = (float)v1.e[2];
        float v2x = (float)v2.e[0],
              v2y = (float)v2.e[1],
              v2z = (float)v2.e[2];
        float v3x = (float)v3.e[0],
              v3y = (float)v3.e[1],
              v3z = (float)v3.e[2];
        
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
    
    
    @Override
    public String toString()
    {
        Vector _NW = new Vector(nw),
               _NE = new Vector(ne),
               _SW = new Vector(sw),
               _SE = new Vector(se);
        return "nw: " + _NW + "\tne: " + _NE + "\nsw: " + _SW + "\tse: " + _SE;
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
}

 */
