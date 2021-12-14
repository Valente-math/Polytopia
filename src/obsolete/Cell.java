package obsolete;

import framework.primitives.Line;
import framework.primitives.Vector;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import manifolds.smoothmanifolds.Hypersphere;

/**
 *
 * @author Valente Productions
 */

public class Cell
{
    public int NORTH = 0, NEAST = 1, EAST = 2, SEAST = 3,
               SOUTH = 4, SWEST = 5, WEST = 6, NWEST = 7;
    
    public double[] nw, ne, sw, se, ce; 
    public Vector NW, NE, SW, SE, CE;
    
    public int r_, c_, R_, C_;
    public double STEP;
    
    public Vector center;
    public Vector vertex;
    public Node anchor;
    
    public int type;
    public Cell[][] matrix; 
    public int RES;
    public int SUBRES;
    
    public double RO;
    public double JRO;
    public Cell[] neighbors;
    public boolean[] dirs;
    public int dir;
    
    public Cell rowReflection;
    public Cell colReflection;
    public Cell rcReflection;
    
    //public HashSet<Line> lineReceiver;
    public HashSet<Cell> receiver;
    public Line[] localReceiver;
    public Cell[] neighborhood;
    
    public BufferedWriter writer;
    public double SIZE, SCALAR;
    
    public Cell(Vector _vertex, int r, int c, int _RES, int _SUBRES, double _JRO, double[] _sw, double[] _ne)
    {
        vertex = _vertex;
        RES = _RES;
        SUBRES = _SUBRES;
        JRO = _JRO;
        r_ = r;
        c_ = c;
        STEP = (_ne[1] - _sw[1])/(double)RES;
        double theta = c_*STEP + _sw[0];// theta0
        double phi = r_*STEP + _sw[1];// phi0
        
        sw = new double[]{theta, phi};
        se = new double[]{theta + STEP, phi};
        nw = new double[]{theta, phi + STEP};
        ne = new double[]{theta + STEP, phi + STEP};
        ce = new double[]{theta + STEP/2.0, phi + STEP/2.0};
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
        localReceiver = new Line[8];
        Arrays.fill(localReceiver, null);
    }
    
    private void setType(int _type, Node _anchor)
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
    
    public void setType2(Node _anchor, double _RO, Cell[] _neighbors)
    {
        setType(2, _anchor);
        RO = _RO;
        neighbors = _neighbors;
        initSubcellMatrix();
    }
    
    public void setType2(Node _anchor, Cell[] _neighbors)
    {
        setType(2, _anchor);
        neighbors = _neighbors;
    }
    
    public void setType3(Node _anchor)
    {
        setType(3, _anchor);
    }
    
    public void setType4(Node _anchor, int _dir, HashSet<Cell> _receiver)
    {
        setType(4, _anchor);
        dir = _dir;
        _receiver.add(this);
        if(_dir == NEAST || _dir == SWEST)
            localReceiver[_dir] = new Line(new Vector(nw), new Vector(se));
        else if(_dir == NWEST || _dir == SEAST)
            localReceiver[_dir] = new Line(new Vector(ne), new Vector(sw));
    }
    
    public void setReflections(Cell row, Cell col, Cell rc)
    {
        rowReflection = row;
        colReflection = col;
        rcReflection = rc;
    }
    
    public void refineReceiver()
    {
        for(int r = 0; r < SUBRES; r++)
        for(int c = 0; c < SUBRES; c++)
        if(matrix[r][c].type == 2)
            matrix[r][c].mapNeighborhood(matrix);
        
        for(int r = 0; r < SUBRES; r++)
        for(int c = 0; c < SUBRES; c++)
        if(matrix[r][c].type == 2)
        {
            Cell bcell = matrix[r][c];
            Cell[] nbhood = bcell.neighborhood;
            boolean[] has = new boolean[8];
            for(int i = 0; i < 8; i++)
                has[i] = ((nbhood[i] != null)&&(nbhood[i].type == 2));
            
            if(has[NWEST])
                if(has[NORTH] && nbhood[WEST] != null)
                    fill(nbhood[WEST], NEAST, bcell, nbhood[NWEST]);
                else if(has[WEST] && nbhood[NORTH] != null)
                    fill(nbhood[NORTH], SWEST, nbhood[NWEST], bcell);
            
            if(has[NEAST])
                if(has[NORTH] && nbhood[EAST] != null)
                    fill(nbhood[EAST], NWEST, bcell, nbhood[NEAST]);
                else if(has[EAST] && nbhood[NORTH] != null)
                    fill(nbhood[NORTH], SEAST, nbhood[NEAST], bcell);
            
            if(has[SEAST])
                if(has[SOUTH] && nbhood[EAST] != null)
                    fill(nbhood[EAST], SWEST, bcell, nbhood[SEAST]);
                else if(has[EAST] && nbhood[SOUTH] != null)
                    fill(nbhood[SOUTH], NEAST, nbhood[SEAST], bcell);
            
            if(has[SWEST])
                if(has[SOUTH] && nbhood[WEST] != null)
                    fill(nbhood[WEST], SEAST, bcell, nbhood[SWEST]);
                else if(has[WEST] && nbhood[SOUTH] != null)
                    fill(nbhood[SOUTH], NWEST, nbhood[SWEST], bcell);
            
        }
    }
    
    private void fill(Cell target, int _dir, Cell x, Cell y)
    {
        
        if(target.type == 0)
        {
            target.setType4(anchor, _dir, receiver);
            receiver.add(target);
        }
        else if(target.type == 1)
        {
            target.setType2(anchor, RO, target.neighbors);
            if(_dir == SWEST)
                target.matrix[0][0].setType4(anchor, _dir, receiver);
            if(_dir == SEAST)
                target.matrix[0][SUBRES-1].setType4(anchor, _dir, receiver);
            if(_dir == NWEST)
                target.matrix[SUBRES-1][0].setType4(anchor, _dir, receiver);
            if(_dir == NEAST)
                target.matrix[SUBRES-1][SUBRES-1].setType4(anchor, _dir, receiver);
        }
        
        if(_dir == NEAST)
        {
            y.localReceiver[SOUTH] = null;
            x.localReceiver[WEST] = null;
        }
        if(_dir == SEAST)
        {
            y.localReceiver[NORTH] = null;
            x.localReceiver[WEST] = null;
        }
        if(_dir == SWEST)
        {
            y.localReceiver[NORTH] = null;
            x.localReceiver[EAST] = null;
        }
        if(_dir == NWEST)
        {
            y.localReceiver[SOUTH] = null;
            x.localReceiver[EAST] = null;
        }
    }
    
    public void mapNeighborhood(Cell[][] matrix)
    {
        Cell[] nbhood = new Cell[8];
        Arrays.fill(nbhood, null);
        boolean hasNorth = (r_ < SUBRES-1),
                hasSouth = (r_ > 0),
                hasEast  = (c_ < SUBRES-1),
                hasWest  = (c_ > 0);
        
        if(hasNorth)
        {
            nbhood[NORTH] = matrix[r_+1][c_]; 
            
            if(hasEast)
                nbhood[NEAST] = matrix[r_+1][c_+1]; 
            else if(neighbors[EAST] != null && neighbors[EAST].type == 2)
                    nbhood[NEAST] = neighbors[EAST].matrix[r_+1][0];
            
            if(hasWest) 
                nbhood[NWEST] = matrix[r_+1][c_-1]; 
            else if(neighbors[WEST] != null && neighbors[WEST].type == 2)
                nbhood[NWEST] = neighbors[WEST].matrix[r_+1][SUBRES-1];
        }
        else if(neighbors[NORTH] != null && neighbors[NORTH].type == 2)
        {
            nbhood[NORTH] = neighbors[NORTH].matrix[0][c_]; 
            
            if(hasEast)
                nbhood[NEAST] = neighbors[NORTH].matrix[0][c_+1];
            else if(neighbors[NEAST] != null && neighbors[NEAST].type == 2) // NE corner
                nbhood[NEAST] = neighbors[NEAST].matrix[0][0];
            
            if(hasWest)
                nbhood[NWEST] = neighbors[NORTH].matrix[0][c_-1];
            else if(neighbors[NWEST] != null && neighbors[NWEST].type == 2) // NW corner
                nbhood[NWEST] = neighbors[NWEST].matrix[0][SUBRES-1];
        }
        else if(neighbors[NORTH] != null && neighbors[NORTH].type == 1)
            nbhood[NORTH] = neighbors[NORTH];
        
        if(hasSouth)
        {
            nbhood[SOUTH] = matrix[r_-1][c_];
            
            if(hasEast)
                nbhood[SEAST] = matrix[r_-1][c_+1]; 
            else if(neighbors[EAST] != null && neighbors[EAST].type == 2)
                nbhood[SEAST] = neighbors[EAST].matrix[r_-1][0]; 
            
            if(hasWest)
                nbhood[SWEST] = matrix[r_-1][c_-1];
            else if(neighbors[WEST] != null && neighbors[WEST].type == 2)
                nbhood[SWEST] = neighbors[WEST].matrix[r_-1][SUBRES-1];
        }
        else if(neighbors[SOUTH] != null && neighbors[SOUTH].type == 2)
        {
            nbhood[SOUTH] = neighbors[SOUTH].matrix[SUBRES-1][c_];
            
            if(hasEast)
                nbhood[SEAST] = neighbors[SOUTH].matrix[SUBRES-1][c_+1];
            else if(neighbors[SEAST] != null && neighbors[SEAST].type == 2) // SE corner
                nbhood[SEAST] = neighbors[SEAST].matrix[SUBRES-1][0];
            
            if(hasWest)
                nbhood[SWEST] = neighbors[SOUTH].matrix[SUBRES-1][c_-1];
            else if(neighbors[SWEST] != null && neighbors[SWEST].type == 2) // SW corner
                nbhood[SWEST] = neighbors[SWEST].matrix[SUBRES-1][SUBRES-1];;
        }
        else if(neighbors[SOUTH] != null && neighbors[SOUTH].type == 1)
            nbhood[SOUTH] = neighbors[SOUTH];
        
        if(hasEast)
            nbhood[EAST] = matrix[r_][c_+1];
        else if(neighbors[EAST] != null && neighbors[EAST].type == 2)
            nbhood[EAST] = neighbors[EAST].matrix[r_][0];
        else if(neighbors[EAST] != null && neighbors[EAST].type == 1)
            nbhood[EAST] = neighbors[EAST];
        
        if(hasWest)
            nbhood[WEST] = matrix[r_][c_-1]; 
        else if(neighbors[WEST] != null && neighbors[WEST].type == 2)
            nbhood[WEST] = neighbors[WEST].matrix[r_][SUBRES-1];
        else if(neighbors[WEST] != null && neighbors[WEST].type == 1)
            nbhood[WEST] = neighbors[WEST];
        
        
        neighborhood = nbhood;
    }
    
    public void drawReceiver(HashSet<Cell> _receiver)
    {
        receiver = _receiver;
        int R = SUBRES-1;
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
                
                Cell nNORTH = neighbors[NORTH];
                if(((r  < R)&&(matrix[r+1][c].type == 0))  // non-border
                  ||((r == R)&&(nNORTH != null)
                    &&((nNORTH.type  == 1)||((nNORTH.type == 2) 
                      &&(nNORTH.matrix[0][c].type == 0)))))
                {
                    //lineReceiver.add(north);
                    receiver.add(bcell);
                    bcell.localReceiver[NORTH] = north;//.add(north);
                }

                Cell nEAST = neighbors[EAST];
                if(((c  < R)&&(matrix[r][c+1].type == 0))  // non-border
                  ||((c == R)&&(nEAST != null)
                    &&((nEAST.type  == 1)||((nEAST.type == 2) 
                      &&(nEAST.matrix[r][0].type == 0))))) //subneighbor
                {
                    //lineReceiver.add(east);
                    receiver.add(bcell);
                    bcell.localReceiver[EAST] = east;//.add(east);
                }

                Cell nSOUTH = neighbors[SOUTH];
                if(((r > 0)&&(matrix[r-1][c].type == 0))  // non-border
                  ||((r == 0)&&(nSOUTH != null) 
                    &&((nSOUTH.type  == 1)||((nSOUTH.type == 2) 
                      &&(nSOUTH.matrix[R][c].type == 0))))) //subneighbor
                {          
                    //lineReceiver.add(south);
                    receiver.add(bcell);
                    bcell.localReceiver[SOUTH] = south;//.add(south);
                }

                Cell nWEST = neighbors[WEST];
                if(((c  > 0)&&(matrix[r][c-1].type == 0))  // non-border
                  ||((c == 0)&&(nWEST != null)
                    &&((nWEST.type  == 1)||((nWEST.type == 2) 
                      &&(nWEST.matrix[r][R].type == 0)))))//subneighbor
                {                      
                    //lineReceiver.add(west);
                    receiver.add(bcell);
                    bcell.localReceiver[WEST] = west;//add(west);
                }
            }
        }
        //refineReceiver();
    }
    
    public void markInterior()
    {
        for(int r = 0; r < SUBRES; r++)
        for(int c = 0; c < SUBRES; c++)
        {
            Cell subcell = matrix[r][c];
            
            double R = RO;
            boolean d1 = (anchor.actual.distanceTo(subcell.NW) <= R),
                    d2 = (anchor.actual.distanceTo(subcell.NE) <= R),
                    d3 = (anchor.actual.distanceTo(subcell.SW) <= R),
                    d4 = (anchor.actual.distanceTo(subcell.SE) <= R);
            
            
            if(d1 || d2 || d3 || d4)
                if(d1 && d2 && d3 && d4)
                {
                    subcell.setType3(anchor);
                    if(rowReflection != null)
                        rowReflection.matrix[SUBRES-1 - r][c].setType3(anchor);
                    if(colReflection != null)
                        colReflection.matrix[r][SUBRES-1 - c].setType3(anchor);
                    if(rcReflection != null)
                        rcReflection.matrix[SUBRES-1 - r][SUBRES-1 - c].setType3(anchor);
                }
                else
                {
                    subcell.setType2(anchor, neighbors);
                    if(rowReflection != null)
                        rowReflection.matrix[SUBRES-1 - r][c].setType2(anchor, rowReflection.neighbors);
                    if(colReflection != null)
                        colReflection.matrix[r][SUBRES-1 - c].setType2(anchor, colReflection.neighbors);
                    if(rcReflection != null)
                        rcReflection.matrix[SUBRES-1 - r][SUBRES-1 - c].setType2(anchor, rcReflection.neighbors);
                }
         }
    }
    
    private void initSubcellMatrix()
    {
        matrix = new Cell[SUBRES][SUBRES];
        
        for(int r = 0; r < SUBRES; r++)
            for(int c = 0; c < SUBRES; c++)
            {
                matrix[r][c] = (new Cell(vertex, r, c, SUBRES, SUBRES, JRO, sw, ne));
                matrix[r][c].setSuperCell(r_, c_);
            }
    }
    
    private void setSuperCell(int r, int c)
    {
        R_ = r;
        C_ = c;
    }
    
    public void write(BufferedWriter _writer)
    {
        writer = _writer;
        switch(type)
        {
            case 0: writeType0(); break;
            case 1: writeType1(); break;
            case 2: writeType2(); break;
            case 4: writeType4(); break;
            default: break;
        }
    }
    
    public void writeType0()
    {
        if(!NW.isEqualTo(NE))
            writeFacet(SW, NW, NE);
        if(!SE.isEqualTo(SW))
            writeFacet(NE, SE, SW);
    }
    
    public void writeType1()
    {
        // NORTH
        if(dirs[0]) 
            for(int c = 0; c < SUBRES; c++)
                writeFacet(CE, 
                           neighbors[NORTH].matrix[0][c].SW,
                           neighbors[NORTH].matrix[0][c].SE);
        else if(r_ < RES - 1) // north pole
            writeFacet(NW, NE, CE);
        
        // EAST
        if(dirs[1]) 
            for(int r = 0; r < SUBRES; r++)
                writeFacet(CE, 
                           neighbors[EAST].matrix[r][0].NW,
                           neighbors[EAST].matrix[r][0].SW);
        else
            writeFacet(NE, SE, CE);
        
        // SOUTH
        if(dirs[2]) 
            for(int c = 0; c < SUBRES; c++)
                writeFacet(CE, 
                           neighbors[SOUTH].matrix[SUBRES-1][c].NE,
                           neighbors[SOUTH].matrix[SUBRES-1][c].NW);
        else if(r_ > 0) // south pole
            writeFacet(SE, SW, CE);
               
        // WEST
        if(dirs[3]) 
            for(int r = 0; r < SUBRES; r++)
                writeFacet(CE, 
                           neighbors[WEST].matrix[r][SUBRES-1].SE,
                           neighbors[WEST].matrix[r][SUBRES-1].NE);
        else
            writeFacet(SW, NW, CE);            
    }
    
    public void writeType2()
    {
        if(matrix != null)
            for(int r = 0; r < SUBRES; r++)
            for(int c = 0; c < SUBRES; c++)
                matrix[r][c].write(writer);
    }
    
    public void writeType4()
    {
        if(dir == NWEST) writeFacet(SE, NE, SW);
        if(dir == SEAST) writeFacet(NW, NE, SW);
        if(dir == SWEST) writeFacet(NE, NW, SE);
        if(dir == NEAST) writeFacet(SW, NW, SE);
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
        /*float v1x = (float)v1.e[0],
              v1y = (float)v1.e[1],
              v1z = (float)v1.e[2];
        float v2x = (float)v2.e[0],
              v2y = (float)v2.e[1],
              v2z = (float)v2.e[2];
        float v3x = (float)v3.e[0],
              v3y = (float)v3.e[1],
              v3z = (float)v3.e[2];*/
        
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
