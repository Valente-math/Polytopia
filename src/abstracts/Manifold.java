package abstracts;

import framework.primitives.Line;
import framework.primitives.Matrix;
import framework.primitives.Projections;
import framework.primitives.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Valente Productions
 * 
 * Abstract superclass for manifolds. 
 * Contains common fields and helper methods used in the manifold algorithms.
 */
public abstract class Manifold 
{
    public final double PHI = (1.0 + Math.sqrt(5.0))/2.0;
    public final double PHI2 = PHI*PHI, PHI3 = PHI2*PHI, PHI4 = PHI3*PHI,
                        PHI5 = PHI4*PHI, PHI6 = PHI5*PHI;
    public final double PSI = 1.0/PHI;
    public int count = 0;
    
    public int DIMENSION;
    
    public HashMap<Vector, Integer> vertices;
    
    public HashMap<Double, ArrayList<Line>> colormap;
    
    public ArrayList<Line> edges;
    public ArrayList<Line> edgesR3;
    public HashMap<Line, Line> dlink;
    
    public Matrix projector;
    
    public Vector center;
    public double scalar = 0.25;
    
    public int ID; 
    
    
    /****************************** Abstracts ******************************/   
    
    public abstract void genVertices();
    
    public abstract ArrayList<Vector> connections(Vector v);
    
    /***********************************************************************/   
    
    public Manifold(int dim) 
    {
        generateID();
        vertices = new HashMap<Vector, Integer>();
        edgesR3 = new ArrayList<Line>();
        edges = new ArrayList<Line>();
        colormap = new HashMap<Double, ArrayList<Line>>();
        dlink = new HashMap<Line, Line>();
        DIMENSION = dim;
        center = new Vector(DIMENSION);
    }
    public void CONSTRUCT()
    {
        projector = Projections.optimal(DIMENSION, 3);
        genVertices();
        genEdges();
    }
    
    public void genEdges()
    {
        synchronized(edgesR3)
        {
            for(Vector v : vertices.keySet())
                for(Vector w : connections(v))
                    if(w != null) if(vertices.get(w) > vertices.get(v)) draw(v,w);
            
        }
    }
    
    public void draw(Vector v, Vector w)
    {
        Vector v0 = v.clone(); Vector v1 = w.clone();
        v0.translate(center); v1.translate(center);
        v0.scale(scalar); v1.scale(scalar);
        Line next = new Line(v0, v1);
        synchronized(edges)
        {
            edges.add(next);
        }
        Line next3D = new Line(embed3D(v0), embed3D(v1));
        measure(next3D);
        edgesR3.add(next3D);

        dlink.put(next3D, next);
    }
    
    public String getName() 
    {
        return getClass().getSimpleName() + " " + DIMENSION + "-" + getID();
    }
    
    public void measure(Line L)
    {
        double d = Math.floor((1000*L.v0.distanceTo(L.v1)));
        
        if(colormap.containsKey(d)) 
            colormap.get(d).add(L);
        else
        {
            ArrayList<Line> group = new ArrayList<Line>();
            group.add(L);
            colormap.put(d, group);
        }
    }
    
    public Vector embed(Vector v, int image) 
    {
        return Projections.optimal(v.DIM, image).map(v);
    }
    
    public Vector embed3D(Vector v)
    {
        return projector.map(v);
    }
    
    public Line embed3D(Line e)
    {
        return new Line(embed3D(e.v0), embed3D(e.v1));
    }
    
    public void rotate(double rot, int A[])
    {
        for(int i = 0; i < A.length-1; i++)
            for(int j = i+1; j < A.length; j++)
                rotatePlane(rot, A[i], A[j]);
    }
    
    public void rotatePlane(double rot, int A, int B)
    {
        edgesR3.clear();
        synchronized(edges)
        {
            for(Line e : edges)
            {
                e.rotate2D(rot, A, B);
                edgesR3.add(embed3D(e));
            }
        }
    }
    
    
    public void hyperrotate(double rot, int A[])
    {
        edgesR3.clear();
        double[] _rot = new double[A.length-1];
        Arrays.fill(_rot, rot);
        for(Line e : edges)
        {
            e.rotate(_rot, A);
            edgesR3.add(embed3D(e));
        }
        
    }
    
    private void generateID()
    {
        Date present = new Date();
        int key = (int)Math.abs(present.getTime())%10000;
        if(key < 0) key *= -1;
        ID = key;
    }
    
    public int getID()
    {
        return ID;
    }
    
    public void printID()
    {
        System.out.println("Manifold ID: " + ID);
    }
    
    /****************************** Utilities ******************************/
    
    public float arctan(float y, float x)
    {
        // INJECTIVE ARCTANGENT
        // returns a value between positive and negative pi radians
        if(x > 0 && y == 0) return 0;
        else if(x < 0 && y == 0) return (float)Math.PI;
        else if(x == 0 && y > 0) return (float)Math.PI/2;
        else if(x == 0 && y < 0) return -(float)Math.PI/2;
        
        float theta = (float)Math.atan(y/x);
        if(x < 0) theta += (float)Math.PI; // Q2 and Q3
        
        if(theta > (float)Math.PI) theta -= 2*(float)Math.PI;
        return theta;
    }
    
    public double arctan(double y, double x)
    {
        // INJECTIVE ARCTANGENT
        // returns a value between positive and negative pi radians
        if(x > 0 && y == 0) return 0;
        else if(x < 0 && y == 0) return Math.PI;
        else if(x == 0 && y > 0) return Math.PI/2;
        else if(x == 0 && y < 0) return -Math.PI/2;
        
        
        double theta = Math.atan(y/x);
        if(x < 0) theta += Math.PI; // Q2 and Q3
        
        if(theta > Math.PI) theta -= 2*Math.PI;
        return theta;
    }
    
    public static int choose(long n, long r)
    {
        // Efficient choose algorithm
        // Avoids large products by using preemptive factorial reduction
        int p1 = 1;
        for(long i = n; i > (n-r); i--) p1 *= i;
        int p2 = 1;
        for(long i = r; i > 0; i--) p2 *= i;
        
        return (int)(p1/p2);
    }
    
    public static int[] bitString(int N, int B, int length)
    {
        int[] str = changeBase(N, B);
            
        int[] result = new int[length];
        int buffer = length - str.length; // Addresses leading zeroes
        for(int j = 0; j < buffer; j++)
            result[j] = 0;
        for(int j = buffer; j < length; j++)
            result[j] = str[j-buffer];
        
        return result;
    }
    
    public static int[] changeBase(int N, int B)
    {
        // Converts base 10 integer N to a base B string
        if(N == 0) return new int[]{0};
        double K = N;
        double L0 = (Math.log(K)/Math.log(B)); 
        double L = Math.floor(L0);
        int[] bits = new int[(int)L+1];
        for(int i = 0; i < L; i++) bits[i] = 0;
        do
        {
            int M = (int)Math.floor(K/Math.pow(B, L));
            bits[bits.length-1-(int)L] = M;
            K -= M*Math.pow(B, L);
            L0 = (Math.log(K)/Math.log(B));
            L = Math.floor(L0);
        }while((L >= 0));
        
        return bits;
    }
    
    public double valueOf(double[] str, int B)
    {
        // Converts base B string to base 10
        double sum = 0; 
        for(int i = 0; i < str.length; i++)
            sum += str[i]*Math.pow(B, i);
        
        return sum;
    }
    
    public double colorAt(double input, double c1, double c2)
    {
        return ((c2-c1)/Math.PI)*Math.atan(input) + (c2+c1)/2.0;
    }
    
    public int factorial(int n)
    {
        int product = 1;
        for(int i = 2; i <= n; i++) product *= i; 
        return product;
    }
    
    /*
     * Permutation algorithm based on permutation group 2-cycle generators
     */
    public HashMap<Vector, Integer> permutationsOf(Vector A)
    {
        int n = A.DIM;
        int order = factorial(n);
        
        // Matrix of 2-cycles
        Vector[][] swaps = new Vector[order][n-1];
        for(int a = 0; a < order; a++)
            for(int b = 0; b < n-1; b++)
                swaps[a][b] = new Vector(2);
        
        for(int c = 1; c <= n-1; c++)
        {
            int comp = (n-1)-c;
            
            for(int j = 0; j < factorial(n)/factorial(c+1); j++)
                for(int k = comp; k <= n-1; k++)
                {
                    Vector v = new Vector(new double[]{comp, k});
                    for(int m = 0; m < factorial(c); m++)
                    {
                        int r = j*factorial(c+1)+factorial(c)*(k-comp) + m; 
                        swaps[r][c-1] = v;
                    }
                }
        }
        
        // Perform swaps by reading each row as a single cycle product 
        // and acting from right to left. 
        HashMap<Vector,Integer> permutations = new HashMap<Vector,Integer>();
        
        for(int r = 0; r < order; r++)
        {
            Vector perm = A.clone();
            int count = 0;
                
            for(int c = n-2; c >= 0; c--)
            {
                int p0 = (int)swaps[r][c].e[0]; 
                int p1 = (int)swaps[r][c].e[1];
                perm.swap(p0, p1);
                if(p0 != p1) count++; // if not a trivial swap, increase count
            }
            permutations.put(perm, new Integer(count));
        }
        
        return permutations;
    }
    
    public double sqrt(double x)
    {
        return Math.sqrt(x);
    }
}
