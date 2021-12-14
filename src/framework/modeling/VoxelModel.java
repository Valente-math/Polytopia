package framework.modeling;

import abstracts.Manifold;
import framework.primitives.Line;
import framework.primitives.Vector;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import javax.sound.sampled.*;

/**
 *
 * @author Valente Productions
 */
public class VoxelModel 
{
    private BufferedWriter writer;
    
    private Manifold manifold;
    
    private ArrayList<Vector> vertices;
    private ArrayList<Line> edges;
 
    private HashSet<Vector> sphereTemplate;
    private HashSet<Vector> register;
    
    private double MRO = 1.0; // Model radius
    private double RRO  = 0.01; // Rod radius
    private double JRO  = RRO; // Joint radius
    private int    RES = 512;
    private double STEP;
    private Vector origin;
    
    public final int UP    = 0, DOWN  = 1, LEFT = 2, 
                     RIGHT = 3, FRONT = 4, BACK = 5;
    
    public VoxelModel(Manifold M)
    {
        manifold = M;
        edges = new ArrayList<Line>();
        vertices = new ArrayList<Vector>();
        for(Line e : M.edgesR3)
        {
            Line ec = e.clone();
            if(ec.length() > 0 && !ec.v0.isEqualToR(ec.v1)) 
                edges.add(ec);
            if(!vertices.contains(ec.v0))vertices.add(ec.v0);
            if(!vertices.contains(ec.v1))vertices.add(ec.v1);
        }
        register = new HashSet<Vector>();
        sphereTemplate = new HashSet<Vector>();
    }
    
    public void create()
    {
        try 
        {
            open();
                MEASURE();
                INJECT();
            close();
        } catch (IOException ex) {}
        //polish();
        playSound();
        System.out.println("DONE!");
    }
    
    private void MEASURE()
    {
        double minX, minY, minZ, maxX, maxY, maxZ;
        minX = minY = minZ = Double.MAX_VALUE;
        maxX = maxY = maxZ = Double.MIN_VALUE;
        
        for(Vector vertex : vertices)
        {
           if(vertex.e[0] < minX) minX = vertex.e[0];
           if(vertex.e[0] > maxX) maxX = vertex.e[0];
           
           if(vertex.e[1] < minY) minY = vertex.e[1];
           if(vertex.e[1] > maxY) maxY = vertex.e[1];
           
           if(vertex.e[2] < minZ) minZ = vertex.e[2];
           if(vertex.e[2] > maxZ) maxZ = vertex.e[2];
        }
        
        double size = Math.max(maxX-minX, Math.max(maxY-minY, maxZ-minZ));
        System.out.println("SIZE = " + round(size));
        
        double buffer = 1.5;
        minX *= buffer; minY *= buffer; minZ *= buffer;
        maxX *= buffer; maxY *= buffer; maxZ *= buffer;
        size = Math.max(maxX-minX, Math.max(maxY-minY, maxZ-minZ));
        origin = new Vector(new double[]{minX, minY, minZ});
        STEP = size/(double)RES;
        
    }
    
    
    private void INJECT() throws IOException
    {
        long start = (new Date()).getTime();
        
        double spacing = 0.125*Math.sqrt(2.0*RRO*STEP - STEP*STEP);   
        
        System.out.println("Casting edges(" + edges.size() + ")");
        setTemplate(RRO);
        
        int count = 0;
        for(Line edge : edges)
        {
            System.out.printf("%d / %d\n", count++, edges.size());
            double jump = spacing/edge.length();
            
            for(double t = 0; t <= 1.0; t += jump)
            {
                Vector center = edge.f(t);
                for(Vector point : sphereTemplate)
                    registerPoint(center.plus(point));
            }
        }
        
        
        Cell[][] previous = null, // L-2 
                 current = null,  // L-1
                 next = null;     // L
        
        System.out.println("Injecting material(" + RES + ")");
        for(int L = 0; L < RES; L++)
        {
            System.out.printf("%d / %d\n", L, RES);
            next = new Cell[RES][RES];
            for(int R = 0; R < RES; R++)
            for(int C = 0; C < RES; C++)
                next[R][C] = new Cell(L, R, C);
            
            // Scan register for local cells
            for(Vector key : register)
                if((int)key.e[2] == L)
                    next[(int)key.e[1]][(int)key.e[0]].activate();
            
            
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
    
    private void registerPoint(Vector point)
    {
        int x = (int)Math.floor((point.e[0] - origin.e[0])/STEP),
            y = (int)Math.floor((point.e[1] - origin.e[1])/STEP),
            z = (int)Math.floor((point.e[2] - origin.e[2])/STEP);
        
        register.add(new Vector(new double[]{x, y, z}));
    }
    
    private void setTemplate(double ro)
    {
        sphereTemplate.clear();
        
        double step = 2*Math.PI/(7.0*ro/STEP);
        for(double theta = 0; theta <= 2*Math.PI; theta += step)
            for(double phi = 0; phi <= 2*Math.PI; phi += step)
            {
                double x = ro*Math.cos(theta)*Math.cos(phi),
                       y = ro*Math.sin(theta)*Math.cos(phi),
                       z = ro*Math.sin(phi);
                
                sphereTemplate.add(new Vector(new double[]{x, y, z}));
            }
    }
    
    
    public void timeCheck(int L, long start, int total)
    {
        if(L % 10 == 2)
        {
            long unit = ((new Date()).getTime() - start)/(long)L;
            long remainder = unit*(total - L);
            int hours = (int)Math.floor(remainder/3600000);
            int minutes = (int)Math.floor(remainder/60000);
            int seconds = (int)Math.floor((remainder/1000)%60);
            System.out.printf("%dh %dm %ds\n", hours, minutes, seconds);
        }
    }
    
    public Vector getPoint(int L, int R, int C)
    {
        return (new Vector(new double[]{C*STEP, R*STEP, L*STEP})).plus(origin);
        //                              X       Y       Z  
    }
    
    public void writePanel(Vector[] points, int a, int b, int c, int d)
    {
        writeQuadrangle(points[a], points[b], points[c], points[d]);
    }
    
    public void writeQuadrangle(Vector v1, Vector v2, Vector v3, Vector v4)
    {
        writeTriangle(v1, v2, v3);
        writeTriangle(v3, v4, v1);
    }
    
    public void writeTriangle(Vector v1, Vector v2, Vector v3)
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
            writeln("facet normal " + N1);
            writeln("outer loop");                
                writeln("vertex " + V1);                
                writeln("vertex " + V2);                
                writeln("vertex " + V3);                
            writeln("endloop");                
            writeln("endfacet");                
        }catch(IOException ex){};
    }
    
    private void playSound()
    {
        try 
        {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    new File("C:\\Windows\\Media\\tada.wav"));
            clip.open(inputStream);
            clip.start(); 
        }catch(Exception e){System.out.println(e.getMessage());}
    }
    
    private void writeln(String output) throws IOException
    {
        writer.write(output);
        writer.newLine();
    }
    
    public void open()
    {
        try 
        {
            writer = new BufferedWriter(new FileWriter("C:\\Users\\Nicholas\\Documents\\Models\\" + manifold.getName() + ".StL"));
            writeln("solid " + manifold.ID);
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
}
