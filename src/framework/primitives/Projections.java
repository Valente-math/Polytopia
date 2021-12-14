package framework.primitives;

import abstracts.Manifold;
import manifolds.smoothmanifolds.Hypersphere;
import manifolds.polytopes.Simplex;

/**
 * @author Valente Productions
 */
public class Projections
{
    private static final double phi = (1 + Math.sqrt(5))/2;
    private static final double psi = 1/phi;
    private static final double chi = Math.sqrt(1 + phi*phi);
    
    public static Matrix optimal(int n, int m)
    {
        Matrix p = regular(n, m);
        if(p != null) return p;
        else return binary(n, m);
    }
    
    public static double nextFactor(int A, double c)
    {
        for(double i = Math.ceil(c); ; i++) if(A%i == 0) return i;
    }
    
    public static Matrix regular(int n, int m)
    {
        if(m == 2)
        {
            double[][] poly = new double[n][2];
            double theta = 2*Math.PI/((double)n);
            for(int i = 0; i < n; i++)
            {
                poly[i][0] = Math.cos(i*theta);
                poly[i][1] = Math.sin(i*theta);
            }
        }
        
        // Octahedron
        if(n == 3) return (new Matrix(new double[][]{
                {1,0,0},{0,1,0},{0,0,1}})).transpose();
        
        // Cube
        if(n == 4) return (new Matrix(new double[][]{
                {1,1,1},{1,-1,1},{1,1,-1},{1,-1,-1}})).transpose();
        
        if(n == 5) return (new Matrix(new double[][]{
                {Math.sqrt(3),0,0},{1,1,1},{1,-1,1},{1,1,-1},{1,-1,-1}})).transpose();
        
        // Icosahedron
        if(n == 6) return (new Matrix(new double[][]{
            {0,1,phi},{0,-1,phi},{1,phi,0},
            {-1,phi,0},{phi,0,1},{phi,0,-1}})).transpose();
        
       if(n == 7) return (new Matrix(new double[][]{
            {0,1,phi},{0,-1,phi},{1,phi,0},
            {-1,phi,0},{1,0,phi},{-1,0,phi},
            {chi,0,0}})).transpose();
       
       if(n == 8) return (new Matrix(new double[][]{
            {0,1,phi},{0,-1,phi},{1,phi,0},
            {-1,phi,0},{1,0,phi},{-1,0,phi},
            {chi,0,0},{0,chi,0}})).transpose();
       
       if(n == 9) return (new Matrix(new double[][]{
            {0,1,phi},{0,-1,phi},{1,phi,0},
            {-1,phi,0},{1,0,phi},{-1,0,phi},
            {chi,0,0},{0,chi,0},{0,0,chi}})).transpose();
        
        // Dodecahedron
        if(n == 10) return (new Matrix(new double[][]{
            {1,1,1},{1,-1,1},{1,1,-1},{1,-1,-1},{0,psi,phi},{0,psi,-phi},
            {psi, phi, 0},{-psi,phi,0},{phi,0,psi},{-phi,0,psi}})).transpose();
        
        return null;
    }
    
    
    
    public static Matrix binary(int n, int m)
    {
        double[][] matrix = new double[n][m];
        
        // Orthonormal basis for R^m
        int count = 0; 
        for(int i = 0; i < m; i++)
        {
            Vector t = new Vector(m);
            t.e[i] = 1.0;
            matrix[count++] = t.e;
        }
        
        int N = n - 3;
        int params = m - 1;
        int layers = (int)Math.floor(N/Math.pow(2, params));
        Hypersphere sphere = new Hypersphere(m);
        
        // Quasiorthogonal embedded basis
        for(int i = 0; i <= layers; i++)
        {
            double alpha = Math.PI/Math.pow(2, i + 2);
            double beta = alpha + (Math.PI/2.0);
            
            for(int j = 0; j < N%Math.pow(2, params); j++)
            {
                Vector next = new Vector(Manifold.bitString(j, 2, params));
                for(int k = 0; k < next.e.length; k++)
                    if(next.e[k] == 0.0) next.e[k] = alpha;
                    else next.e[k] = beta;
                matrix[count++] = sphere.MAP(next.e).e;
            }
        }
        
        return (new Matrix(matrix)).transpose();
    }
    
    public static Matrix regular2D(int DIM)
    {
        double[][] matrix = new double[DIM][2];
        for(int r = 0; r < DIM; r++)
        {
            matrix[r][0] = 1.0; // ro
            matrix[r][1] = r*(2*Math.PI/(2*DIM)); // theta
        }
        
        return new Matrix(matrix);
    }
    
    public static double[][] regular3D(int DIM)
    {
        double[][] matrix = new double[DIM][2];
        for(int r = 0; r < DIM; r++)
        {
            matrix[r][0] = 1.0; // ro
            matrix[r][1] = r*(2*Math.PI/(2*DIM)); // theta
        }
        
        return matrix;
    }
    
    public static Matrix symplectic(int n, int m)
    {
        Simplex[] chain = new Simplex[n-m];
        for(int i = 1; i <= n-m; i++)
        {
            chain[i-1] = new Simplex(n-i);
            chain[i-1].genVertices();
            //System.out.println(n-i + "-simplex:\n" + (new Matrix(chain[i-1].vertices.keySet())));
        }
        
        Matrix matrix = (new Matrix(chain[0].vertices.keySet())).transpose();
        for(int k = 1; k < chain.length; k++)
        {
            
            matrix.multiplyBy((new Matrix(chain[k].vertices.keySet())).transpose());
        }
        
        
        return matrix;
    }
    
    private static Matrix convert(double[][] matrix)
    {
        int n = matrix.length;
        int m = matrix[0].length;
        double[][] map = new double[m][n];
        Hypersphere sphere = new Hypersphere(m);
        
        for(int r = 0; r < m; r++)
            map[r] = sphere.getVector(matrix[r]).e;
        
        return new Matrix(map);
    }
}
