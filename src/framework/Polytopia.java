package framework;

import framework.primitives.Vector;
import framework.primitives.Line;
import framework.modeling.Model;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import manifolds.smoothmanifolds.Hypersphere;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Polytopia 
{
    private boolean done = false;
    private Interface control;
    private int WIDTH;
    private int HEIGHT;
    private float FOVY = 45;
    private DisplayMode dspmode;
    
    private float dx, dy, dz, dtheta, dphi, dtheta3, dphi3;
    private float X = 0, Y = 0, Z = 0;    //^(TRY TO) IMPLEMENT THIS!
                                             
    private float mspeed = 0.001f;
    private float rspeed = 0.01f;
    
    private boolean rotating;
    private boolean lock;
    
    private boolean fullscreen = false;
    
    private boolean menu = false;
    
    private boolean[] rswitch1, rswitch2;
    private int[] rotation1, rotation2;
    private int rcount1, rcount2;
    
    private Date clock = new Date();
    private long timer = clock.getTime();
    
    private BufferedWriter writer;
    private boolean PRINT = false;
    private ArrayList<Vector[]> vmesh; 
    private double RADIUS = 0.5; 
    
    public static void main(String[] args)
    {   
        Polytopia world = new Polytopia();
        world.run();
    }

    private void run()
    {
        control = new Interface();
        control.run();
        
        initialize();
        
        while(!done)
        {
            update();
            render();
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

    private void initialize() 
    {
        try 
        {
            WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().width);
            HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().height);
            dspmode = new DisplayMode((int)(0.62*WIDTH), (int)(0.62*HEIGHT));
            Display.setDisplayMode(dspmode);
            Display.setResizable(true);
            Display.setTitle("Polytopia");
            Display.create();
        } catch (LWJGLException ex){}
        
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); 
        glClearDepth(1.0f); 
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glMatrixMode(GL_PROJECTION); 
        glLoadIdentity(); 
        gluPerspective(FOVY, (float)WIDTH/(float)HEIGHT, 0.0f, 100.0f);
        glMatrixMode(GL_MODELVIEW);
        
        translate(0,0,-5*(float)control.manifold.scalar);
        
        // set default rotation
        resetRotation();
        
        buildMesh();
    }
    
    private void update() 
    {
        if(Display.wasResized()) 
            glViewport(0, 0, Display.getWidth(), Display.getHeight());
        
        if(Mouse.isButtonDown(0)&&menu)
            updateRotationSelection();
        else if(Mouse.isButtonDown(0))
        {
            dtheta = Mouse.getDX()*rspeed/rcount1; 
            dphi = Mouse.getDY()*rspeed/rcount2;
        }
        else
            dtheta = dphi = 0;
        
        /*if(Mouse.isButtonDown(1))
        {
            dtheta3 = Mouse.getDX()*rspeed; dphi3 = Mouse.getDY()*rspeed;
        }*/
        
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
                ||Display.isCloseRequested()) done = true;
        
        
        boolean M = Mouse.isButtonDown(0)&&!menu;
                
        boolean W = Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean A = Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean S = Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean D = Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean Q = Keyboard.isKeyDown(Keyboard.KEY_Q);
        boolean E = Keyboard.isKeyDown(Keyboard.KEY_E);
        boolean R = Keyboard.isKeyDown(Keyboard.KEY_R);
        boolean T = Keyboard.isKeyDown(Keyboard.KEY_T);
        boolean N = Keyboard.isKeyDown(Keyboard.KEY_N);
        boolean P = Keyboard.isKeyDown(Keyboard.KEY_P);
        boolean tab = Keyboard.isKeyDown(Keyboard.KEY_TAB);
        boolean F1 = Keyboard.isKeyDown(Keyboard.KEY_F1);
        boolean F2 = Keyboard.isKeyDown(Keyboard.KEY_F2);
        boolean F4 = Keyboard.isKeyDown(Keyboard.KEY_F4);
        boolean F5 = Keyboard.isKeyDown(Keyboard.KEY_F5);
        boolean F6 = Keyboard.isKeyDown(Keyboard.KEY_F6);
        boolean F11 = Keyboard.isKeyDown(Keyboard.KEY_F11);
        boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        
        int wheel = 100*Mouse.getDWheel();
        if(F4) control.RSPEED += Integer.signum(wheel);
        if(F5) control.MSPEED += Integer.signum(wheel);
        if(F6) control.MSENS += Integer.signum(wheel);
            
        
        if((space||Mouse.isButtonDown(1)) && !asleep()) menu = !menu;
        
        if(F1) resetView();
        if(F2) control.setManifold();
        
        if(F11) switchMode();
        
        float movespeed = control.MSPEED*mspeed;
        
        if(W) dz = movespeed;
        if(S) dz = -movespeed;
        if(!(W||S)) dz = 0;
        
        if(A) dx = movespeed;
        if(D) dx = -movespeed;
        if(!(A||D)) dx = 0;
        
        if(Q) dy = movespeed;
        if(E) dy = -movespeed;
        if(!(Q||E)) dy = 0;
        
        if(lock&&tab) lock = rotating = false;
        if(rotating&&tab) lock = true;
        if(M) rotating = true;
        if(R) 
        { 
            rotating = true;
            lock = true; 
        }
        if(T)
        {
            rotating = false;
            lock = false;
        }
        if(!M&&!lock) rotating = false;
        
        if(P&&!asleep()) PRINT = true;
    }
    
    private void render() 
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        if(!menu) translate(dx, dy, dz);
        
        if(rotating) 
        {
            if(lock) dtheta = control.RSPEED*rspeed/100;
            control.manifold.rotate((double)dtheta, rotation1);
            control.manifold.rotate((double)dphi, rotation2);
        }
        //control.manifold.rotate3D(dtheta3, dphi3);
        
        if(menu) drawMenu();
        drawManifold();
    }
    
    private void drawManifold()
    {
        if(PRINT)
        {
            /*try 
            {
                writer = new BufferedWriter(new FileWriter(control.manifold.getName() + ".StL"));
                writer.write("solid " + control.manifold.ID);
                writer.newLine();
            }catch(IOException ex){}*/
            
            Model model = new Model(control.manifold);
            model.create();
           
            //model.testInversion();
        }
        //drawVertices();
        glColor3f(1,1,1);
        glLineWidth(0.1f);
        glBegin(GL_LINES);
            if(control.manifold != null && control.manifold.edgesR3 != null)
                synchronized(control.manifold.edgesR3)
                {
                    for(Line edge : control.manifold.edgesR3)
                    {
                        /*glColor3f((float)e.color.v[0],
                                (float)e.color.v[1],
                                (float)e.color.v[2]);*/
                                
                        glVertex3f((float)edge.v0.e[0], 
                                (float)edge.v0.e[1], 
                                (float)edge.v0.e[2]);
                        
                        glVertex3f((float)edge.v1.e[0], 
                                (float)edge.v1.e[1], 
                                (float)edge.v1.e[2]);
                    }
                }
        glEnd();
            
        if(PRINT) 
        {
            /*try 
            {
                writer.write("endsolid " + control.manifold.ID);
                writer.close();
            }catch(IOException ex){}
            System.out.println("DONE");*/
            PRINT = false;
        }
    }
    
    private void drawVertices()
    {
        HashSet<Vector> vertices = new HashSet<Vector>();
        synchronized(control.manifold.edgesR3)
        {
            for(Line e : control.manifold.edgesR3)
                {vertices.add(e.v0); vertices.add(e.v1);}
        }
        
        
        glBegin(GL_TRIANGLES);
        for(Vector[] facet : vmesh)
            for(Vector v0 : vertices)
            {
                
                if(PRINT) writeFacet(facet[0].plus(v0), 
                                     facet[1].plus(v0), 
                                     facet[2].plus(v0));
                    
                glVertex3f((float)(facet[0].plus(v0)).e[0], 
                        (float)(facet[0].plus(v0)).e[1], 
                        (float)(facet[0].plus(v0)).e[2]);
                
                
                glVertex3f((float)(facet[1].plus(v0)).e[0], 
                        (float)(facet[1].plus(v0)).e[1], 
                        (float)(facet[1].plus(v0)).e[2]);
                
                
                glVertex3f((float)(facet[2].plus(v0)).e[0], 
                        (float)(facet[2].plus(v0)).e[1], 
                        (float)(facet[2].plus(v0)).e[2]);
            }
        glEnd();
    }
    
    private void drawCylinder(Vector v1, Vector v2)
    {
        // Copy coordinates to local variable
        double x1 = v1.e[0], y1 = v1.e[1], z1 = v1.e[2],
               x2 = v2.e[0], y2 = v2.e[1], z2 = v2.e[2];
        
        // Slopes
        float dx = (float)(x2 - x1), 
              dy = (float)(y2 - y1), 
              dz = (float)(z2 - z1);
        
        float max = dx;
        if(Math.abs(dy) > Math.abs(dx)) max = dy;
        if(Math.abs(dz) > Math.abs(max)) max = dz;
        
        double tres = 2;
        double ures = 7;
        double r = RADIUS/2.0; 
        for(int i = 0; i < tres; i++)
        {
            double t = (1.0/tres)*i;
            Vector[] c1 = new Vector[(int)ures];
            Vector[] c2 = new Vector[(int)ures];
            
            for(int j = 0; j < ures; j++)
            {
                double u = (2*Math.PI/ures)*j;
                if(max == dx)
                {
                    double ro = r/(dx/Math.sqrt(dx*dx + dy*dy + dz*dz));
                    c1[j] = new Vector(new double[]{dx*t + x1, dy*t + ro*Math.cos(u) + y1, dz*t + ro*Math.sin(u) + z1});
                }
                else if(max == dy)
                {
                    double ro = r/(dy/Math.sqrt(dx*dx + dy*dy + dz*dz));
                    c1[j] = new Vector(new double[]{dx*t + ro*Math.cos(u) + x1, dy*t + y1, dz*t + ro*Math.sin(u) + z1});
                }
                else if(max == dz)
                {
                    double ro = r/(dz/Math.sqrt(dx*dx + dy*dy + dz*dz));
                    c1[j] = new Vector(new double[]{dx*t + ro*Math.cos(u) + x1, dy*t + ro*Math.sin(u) + y1, dz*t + z1});
                }
            }
            
            t = (1.0/tres)*(i+1);
            Vector point2 = new Vector(new double[]{dx*t + x1,
                                               dy*t + y1,
                                               dz*t + z1});
            for(int j = 0; j < ures; j++)
            {
                double u = (2*Math.PI/ures)*j;
                if(max == dx)
                {
                    double ro = r/(dx/Math.sqrt(dx*dx + dy*dy + dz*dz));
                    c2[j] = new Vector(new double[]{dx*t + x1, dy*t + ro*Math.cos(u) + y1, dz*t + ro*Math.sin(u) + z1});
                }
                else if(max == dy)
                {
                    double ro = r/(dy/Math.sqrt(dx*dx + dy*dy + dz*dz));
                    c2[j] = new Vector(new double[]{dx*t + ro*Math.cos(u) + x1, dy*t + y1, dz*t + ro*Math.sin(u) + z1});
                }
                else if(max == dz)
                {
                    double ro = r/(dz/Math.sqrt(dx*dx + dy*dy + dz*dz));
                    c2[j] = new Vector(new double[]{dx*t + ro*Math.cos(u) + x1, dy*t + ro*Math.sin(u) + y1, dz*t + z1});
                }
            }
            
            glBegin(GL_TRIANGLE_STRIP);
            for(int k = 0; k < ures; k++)
            {
                glVertex3f((float)c1[k].e[0], (float)c1[k].e[1], (float)c1[k].e[2]);
                glVertex3f((float)c2[k].e[0], (float)c2[k].e[1], (float)c2[k].e[2]);
            }
            glVertex3f((float)c1[0].e[0], (float)c1[0].e[1], (float)c1[0].e[2]);
            glVertex3f((float)c2[0].e[0], (float)c2[0].e[1], (float)c2[0].e[2]);
            glEnd();
            
            if(PRINT)
            {
                for(int m = 0; m < ures; m++)
                {
                    int n = m+1;
                    if(m == ures-1) n = 0;
                        
                    writeFacet(c1[m], c1[n], c2[m]);
                    writeFacet(c2[n], c2[m], c1[n]);
                }
            }
                    
        }
    }
    
    private void writeFacet(Vector v1, Vector v2, Vector v3)
    {
        if(true)return;
        float v1x = (float)v1.e[0],
              v1y = (float)v1.e[1],
              v1z = (float)v1.e[2];
        float v2x = (float)v2.e[0],
              v2y = (float)v2.e[1],
              v2z = (float)v2.e[2];
        float v3x = (float)v3.e[0],
              v3y = (float)v3.e[1],
              v3z = (float)v3.e[2];
        
        String N1 = 0f + " " + 0f + " " + 0f;
        String V1 = v1x + " " + v1y + " " + v1z;
        String V2 = v2x + " " + v2y + " " + v2z;
        String V3 = v3x + " " + v3y + " " + v3z;
        
        try{
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
    
    private void buildMesh()
    {
        Hypersphere sphere = new Hypersphere(3);
        vmesh = new ArrayList<Vector[]>();
        float step = (float)Math.PI/6;
        for(float theta = -(float)Math.PI/2; theta < Math.PI/2; theta += step)
            for(float phi = 0; phi < 2*Math.PI; phi += step)
            {
                Vector[] F1 = new Vector[3], F2 = new Vector[3];
                F1[0] = sphere.MAP(new double[]{theta, phi}).scaledBy(RADIUS);
                F1[1] = sphere.MAP(new double[]{theta+step, phi}).scaledBy(RADIUS);
                F1[2] = sphere.MAP(new double[]{theta, phi+step}).scaledBy(RADIUS);
                
                F2[0] = sphere.MAP(new double[]{theta, phi+step}).scaledBy(RADIUS);
                F2[1] = sphere.MAP(new double[]{theta+step, phi}).scaledBy(RADIUS);
                F2[2] = sphere.MAP(new double[]{theta+step, phi+step}).scaledBy(RADIUS);
                
                vmesh.add(F1);
                vmesh.add(F2);
            }
    }
    
    private void drawMenu()
    {
        int N = control.manifold.DIMENSION;
        
        if(N != rswitch1.length || N != rswitch2.length) resetRotation();
        
        float focus = (float)(metricWidth()/2.0);
        
        float A1 = -focus/2, B1 = 0, 
               A2 = focus/2,  B2 = 0;
        
        double theta0 = 2*Math.PI/N;
        double r = focus/2; // radius
        
        for(int k = 0; k < N; k++)
        {
            float x1 = (float)(r*Math.cos(k*theta0));
            float y1 = (float)(r*Math.sin(k*theta0));
            float x2 = (float)(r*Math.cos((k+1)*theta0));
            float y2 = (float)(r*Math.sin((k+1)*theta0));
            
            glBegin(GL_LINES);
                glVertex2f(A1, B1);
                glVertex2f(x1 + A1, y1 + B1);

                glVertex2f(x1 + A1, y1 + B1);
                glVertex2f(x2 + A1, y2 + B1);

                glVertex2f(x2 + A1, y2 + B1);
                glVertex2f(A1, B1);
                
                glVertex2f(A2, B2);
                glVertex2f(x1 + A2, y1 + B2);

                glVertex2f(x1 + A2, y1 + B2);
                glVertex2f(x2 + A2, y2 + B2);

                glVertex2f(x2 + A2, y2 + B2);
                glVertex2f(A2, B2);
            glEnd();
            
            if(rswitch1[k])
            {
                glBegin(GL_TRIANGLES);
                    glVertex2f(A1, B1);
                    glVertex2f(x1 + A1, y1 + B1);
                    glVertex2f(x2 + A1, y2 + B1);
                glEnd();
            }
            
            if(rswitch2[k])
            {
                glBegin(GL_TRIANGLES);
                    glVertex2f(A2, B2);
                    glVertex2f(x1 + A2, y1 + B2);
                    glVertex2f(x2 + A2, y2 + B2);
                glEnd();
            }
            
        }
    }
    
    
    
    private void resetView()
    {
        glLoadIdentity();
        glTranslatef(0,0,-5*(float)control.manifold.scalar);
    }
    
    private void switchMode()
    {
        fullscreen = !fullscreen;
        System.out.println(fullscreen);
        try 
        {
            Display.setFullscreen(fullscreen);
            Thread.sleep(1000);
        }catch(Exception e){}
    }
    
    private void resetRotation()
    {
        int N = control.manifold.DIMENSION;
        
        rcount1 = rcount2 = 2;
        
        rotation1 = new int[2]; rotation2 = new int[2];
        rotation1[0] = 0; rotation1[1] = 1;
        rotation2[0] = N-1; rotation2[1] = N-2;
        
        rswitch1 = new boolean[N]; Arrays.fill(rswitch1, false);
        rswitch2 = new boolean[N]; Arrays.fill(rswitch2, false);
        rswitch1[0] = rswitch1[1] = rswitch2[N-1] = rswitch2[N-2] = true;
    }

    private void updateRotationSelection() 
    {
        if(asleep()) return;
        
        double width = Display.getWidth();
        double height = Display.getHeight();
        
        double x = Mouse.getX() - width/2.0;
        double y = Mouse.getY() - height/2.0;
        double shift = (double)width/4.0;
        int side = (int)Math.floor(Mouse.getX()/(width/2));
        x += shift*Math.pow(-1, side);
        
        double theta = Math.atan(y/x);
        if(x < 0) theta += Math.PI;
        else if(y < 0) theta += 2*Math.PI;
         
        double thetahat = 2*Math.PI/control.manifold.DIMENSION;
                
        int sector = (int)Math.floor(theta/thetahat);
        if(side == 0)
        {
            if(rswitch1[sector]) rcount1--;
                else rcount1++;
            rswitch1[sector] = !rswitch1[sector];
            rotation1 = new int[rcount1];
            for(int j = 0, i = 0; j < rswitch1.length; j++)
                if(rswitch1[j]) rotation1[i++] = j;
        }
        else
        {
            if(rswitch2[sector]) rcount2--;
                else rcount2++;
            rswitch2[sector] = !rswitch2[sector];
            rotation2 = new int[rcount2];
            for(int j = 0, i = 0; j < rswitch2.length; j++)
                if(rswitch2[j]) rotation2[i++] = j;
        }
    }
    
    private void translate(float _x, float _y, float _z)
    {
        glTranslatef(_x, _y, _z);
        X += _x; Y += _y; Z += _z;
    }
    
    private boolean asleep()
    {
        clock = new Date();
        if(clock.getTime() - timer < 256) return true;
        else timer = clock.getTime();
        return false;
    }
    
    private double metricWidth()
    {
        return 2.0*Math.abs(Z)*Math.tan(FOVY*Math.PI/360.0)*
                ((double)WIDTH/(double)HEIGHT);
    }
}

