package manifolds.polytopes;

import abstracts.Polytope;
import framework.primitives.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Nicholas
 */
public class Polyplex extends Polytope
{
    public int numSeeds = 3;
    public Vector[] seeds;
    
    public int p = 5;
    public int[] degrees = {2};
    public int[] bases = {5};
    
    private Random gen;

    public Polyplex(int dimension) 
    {
        super(dimension);
        gen = new Random();
    }
    
    public Polyplex(int dimension, long seed) 
    {
        super(dimension);
        gen = new Random(seed);
    }
    
    /*
    * Generate random elements of the field
    * Z_p(a1^p1,...,an^pn)
    * (a1,...,an) = bases
    * (1/p1,...,1/pn) = degrees
    */
    private void generateSeeds()
    {
        seeds = new Vector[numSeeds];
        Arrays.sort(degrees);
        int max = degrees[degrees.length-1];
        
        double[] generators = new double[bases.length];
        for(int i = 0; i < bases.length; i++)
            generators[i] = Math.pow(bases[i], 1.0/degrees[i]);
        
        for(int i = 0; i < numSeeds; i++)
        {
            seeds[i] = new Vector(DIMENSION);
            // Generate entry from field for each component of the seed
            for(int c = 0; c < DIMENSION; c++)
            {
                double entry = gen.nextInt(p);
                // Choose a coefficient for each possible combination of generators
                for(int j = 0; j < Math.pow(max, degrees.length); j++)
                {
                    int[] pows = bitString(j, max, degrees.length);
                    double term = 1.0;
                    for(int k = 0; k < generators.length; k++)
                        term *= Math.pow(generators[k], pows[k]);
                    
                    if(gen.nextBoolean()) // produces more zeroes
                        entry += gen.nextInt(p)*term;
                }
                seeds[i].e[c] = entry;
            }
            System.out.println(i + ": " + seeds[i]);
        }
    }

    @Override
    public void genVertices() 
    {
        //register and connect
        generateSeeds();
        for(int i = 0; i < numSeeds; i++)
            register(seeds[i].e, gen.nextInt(DIMENSION-2)+1, gen.nextInt(DIMENSION-2), true, true);
        
        double[] distances = new double[vertices.keySet().size()];
        int index = 0;
        for(Vector vertex : vertices.keySet())
            if(!vertex.equals(seeds[0]))
                distances[index++] = seeds[0].distanceTo(vertex);
        
        Arrays.sort(distances);
        System.out.println("connecting: " + distances.length);
        connect(Arrays.copyOfRange(distances, 1, 50));//gen.nextInt(distances.length)]);
    }
}
