package framework.modeling;

import abstracts.Manifold;
import framework.primitives.Line;
import framework.primitives.Vector;
import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;

/**
 *
 * @author Valente Productions
 */
public class Model 
{
    private BufferedWriter writer;
    
    private Manifold manifold;
    
    private ArrayList<Vector> vertices;
    private ArrayList<Line> edges;
    
    private ArrayList<ArrayList<Line>> colorGroups; 
    private String[] scheme;
    private String[] colors2 = {"Red","Blue"};
    private String[] colors3 = {"Red","Blue","Yellow"};
    private String[] colors4 = {"Red","Green","Blue","Yellow"};
    private boolean defaultColors = true;
    
    // -1 = length, n > -1 => average nth coordinate
    private int TAG = -1; 
    
    // 1 in ~ 25 mm
    // 1 mm ~ 0.04 in
    private double MRO = 25.0; // Model radius
    private double RRO = 1.0; // Rod radius >= 0.04
    private double JRO  = 1.2; // Joint radius
    
    public Model(Manifold M)
    {
        manifold = M;
        edges = new ArrayList<Line>();
        vertices = new ArrayList<Vector>();
        for(Line e : M.edgesR3)
        {
            Line ec = e;//.clone();
            if(ec.length() > 0 && !ec.v0.isEqualToR(ec.v1) && !edges.contains(ec)) 
                edges.add(ec);
            if(!vertices.contains(ec.v0))vertices.add(ec.v0);
            if(!vertices.contains(ec.v1))vertices.add(ec.v1);
        }
        partitionEdges();
    }
    
    public void partitionEdges()
    {
        ArrayList<Float> tags = new ArrayList<Float>();
        for(Line e : edges)
        {
            
            if(!tags.contains(tag(e)))
                tags.add(tag(e));
        }
        scheme = randomColorScheme(tags.size());
        colorGroups = new ArrayList<ArrayList<Line>>();
        for(int i = 0; i < scheme.length; i++)
        {
            colorGroups.add(new ArrayList<Line>());
            for(Line e : edges)
                if(tag(e) == tags.get(i))
                    colorGroups.get(i).add(e);
        }
    }
    
    public float tag(Line edge)
    {
        if(TAG == -1)
            return round(edge.length());
        else
        {
            Line cover = manifold.dlink.get(edge);
            return round((cover.v0.e[TAG%manifold.DIMENSION] 
                        + cover.v1.e[TAG%manifold.DIMENSION])/2.0);
        }
    }
    
    public void create()
    {
        manifold.printID();
        try 
        {
            writer = new BufferedWriter(new FileWriter("C:\\Users\\Nicholas\\Documents\\Programming\\Models\\" + manifold.getName() + ".m"));
            writer.write("coords0 = {");
            writer.write(vertices.get(0).toFormattedString());
            for(int i = 1; i < vertices.size(); i++) 
                writer.write("," + vertices.get(i).toFormattedString());
            writer.write("}");
            writer.newLine();
            writer.write("coords = Rescale[coords0, {0, Max[coords0]/"+MRO+"}]");
            writer.newLine();
            writer.write("model = Graphics3D[GraphicsComplex[coords,{");
            for(int c = 0; c < scheme.length; c++)
            {
                writer.write(scheme[c] + ",Cylinder[{");
                for(int i = 0; i < colorGroups.get(c).size(); i++) 
                {
                    Line edge = colorGroups.get(c).get(i);
                    if(i!=0) writer.write(",");
                    writer.write("{" + (vertices.indexOf(edge.v0)+1) + "," + (vertices.indexOf(edge.v1)+1) + "}");
                }
                writer.write("},"+RRO+"],");
            }  
            writer.write("White,Sphere[#,"+JRO+"]&/@Range[Length[coords]]}]]");
            writer.newLine();
            writer.write("Export[\"C:\\Users\\Nicholas\\Documents\\Programming\\Models\\"
                    + manifold.getName() + "-" + (int)MRO+"-"+round(RRO,1)+".3ds\", Show[model]]" );
            writer.close();
        } catch(IOException ex){}
        System.out.println("DONE!");
        playSound();
    }
    
    public void createNoColor()
    {
        try 
        {
            writer = new BufferedWriter(new FileWriter("C:\\Users\\Nicholas\\Documents\\Models\\" + manifold.getName() + ".m"));
            
            writer.write("coords0 = {");
            writer.write(vertices.get(0).toFormattedString());
            for(int i = 1; i < vertices.size(); i++) 
                writer.write("," + vertices.get(i).toFormattedString());
            writer.write("}");
            writer.newLine();
            writer.write("coords = Rescale[coords0, {0, Max[coords0]/"+MRO+"}]");
            writer.newLine();
            writer.write("model = Graphics3D[GraphicsComplex[coords,{Cylinder[{");
            
            for(int i = 0; i < edges.size(); i++) 
            {
                if(i!=0) writer.write(",");
                writer.write("{" + (vertices.indexOf(edges.get(i).v0)+1) + "," +(vertices.indexOf(edges.get(i).v1)+1) + "}");
            }
                        
            writer.write("}," + RRO + "],Sphere[#, " + JRO + "]&/@Range[Length[coords]]}]]");
            writer.newLine();
            writer.write("Export[\"C:\\Users\\Nicholas\\Documents\\Models\\" + manifold.getName() + ".x3d\", Show[model]]" );
            writer.close();
        } catch(IOException ex){}
        
        playSound();
        manifold.printID();
        System.out.println("DONE!");
    }
    
    public String[] randomColorScheme(int size)
    {
        if(defaultColors)
            if     (size == 2) return colors2;
            else if(size == 3) return colors3;
            else if(size == 4) return colors4;
        
        String[] rScheme = new String[size];
        int theta0 = manifold.getID()%360;
        int step = (int)Math.round((float)(360.0/(float)size));
        for(int i = 0; i < size; i++)
        {
            int theta = (theta0 + i*step)%360;
            float frac = round((float)(theta%60)/60.0);
            float ifrac = 1-frac;
            if(theta <= 60)
                rScheme[i] = "RGBColor[1," + frac + ",0]";
            else if(theta <= 120)
                rScheme[i] = "RGBColor[" + ifrac + ",1,0]";
            else if(theta <= 180)
                rScheme[i] = "RGBColor[0,1," + frac + "]";
            else if(theta <= 240)
                rScheme[i] = "RGBColor[0," + ifrac + ",1]";
            else if(theta <= 300)
                rScheme[i] = "RGBColor[" + frac + ",0,1]";
            else
                rScheme[i] = "RGBColor[1,0," + ifrac + "]";
        }
        return rScheme;
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
    
    public float round(double x)
    {
        return round(x, 2);
    }
    
    public float round(double x, int precision)
    {
        float p = (float)Math.pow(10, precision);
        return Math.round(x*p)/p;
    }
}
