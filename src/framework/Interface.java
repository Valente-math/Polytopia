package framework;

import manifolds.polytopes.polychora.uniform.hexacosichoron.CantellatedHexacosichoron;
import abstracts.Manifold;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.HashMap;
import javax.swing.*;
import manifolds.polytopes.*;
import manifolds.polytopes.polychora.regular.*;
import manifolds.polytopes.polychora.uniform.hecatonicosachoron.*;
import manifolds.polytopes.polychora.uniform.hexacosichoron.*;
import manifolds.polytopes.polychora.uniform.hexadecachoron.*;
import manifolds.polytopes.polychora.uniform.icositetrachoron.*;
import manifolds.polytopes.polychora.uniform.tesseract.*;
import manifolds.polytopes.polyhedra.*;
import manifolds.smoothmanifolds.Hypersphere;
import manifolds.smoothmanifolds.KleinBottle;
import manifolds.smoothmanifolds.Parametric;
import manifolds.smoothmanifolds.RP2;

/**
 *
 * @author Valente Productions
 */
public class Interface extends JPanel implements ActionListener, PropertyChangeListener
{
    private JFormattedTextField DField; // DIMENSION
    private JFormattedTextField RField; // ROTATION SPEED
    private JFormattedTextField MField; // MOVE SPEED
    
    private JLabel DLabel;
    private JLabel RLabel;
    private JLabel MLabel;
    
    private JComboBox manifoldList;
    
    private JPanel textPane;
    
    private JButton button;
    
    private JFrame frame;
    
    private int DIMENSION = 4;
    private int WEIGHT;
    private String TYPE;
    public int[] ROT;
    public int RSPEED;
    public int MSPEED;
    public int MSENS = 1;
    private HashMap<String,int[]> rotMap;
    
    public Manifold manifold;
    
    public Canvas canvas;
    
    public Interface()
    {
        super(new FlowLayout());
        rotMap = new HashMap<String,int[]>();
        canvas = new Canvas();
    }
    
    public void run()
    {
        createAndShowGUI();
    }
    
    public void setManifold()
    {
        int d = DIMENSION; 
        
        if(TYPE.equals("Hypersphere")) manifold = new Hypersphere(d);
        else if(TYPE.equals("Klein Bottle")) manifold = new KleinBottle();
        else if(TYPE.equals("Projective Plane")) manifold = new RP2();
        else if(TYPE.equals("Parametric")) manifold = new Parametric(); 
        else if(TYPE.equals("Hypercube")) manifold = new Hypercube(d);
        else if(TYPE.equals("Orthoplex")) manifold = new Orthoplex(d);
        else if(TYPE.equals("Simplex")) manifold = new Simplex(d);
        else if(TYPE.equals("Hecatonicosachoron")) manifold = new Hecatonicosachoron();
        else if(TYPE.equals("Hexacosichoron")) manifold = new Hexacosichoron();
        else if(TYPE.equals("Icositetrachoron")) manifold = new Icositetrachoron();
        else if(TYPE.equals("Dodecahedron")) manifold = new Dodecahedron();
        else if(TYPE.equals("Icosahedron")) manifold = new Icosahedron();
        
        else if(TYPE.equals("Rectified Tesseract")) manifold = new RectifiedTesseract();
        else if(TYPE.equals("Truncated Tesseract")) manifold = new TruncatedTesseract();
        else if(TYPE.equals("Cantellated Tesseract")) manifold = new CantellatedTesseract();
        else if(TYPE.equals("Runcinated Tesseract")) manifold = new RuncinatedTesseract();
        else if(TYPE.equals("Bitruncated Tesseract")) manifold = new BitruncatedTesseract();
        else if(TYPE.equals("Cantitruncated Tesseract")) manifold = new CantitruncatedTesseract();
        else if(TYPE.equals("Runcitruncated Tesseract")) manifold = new RuncitruncatedTesseract();
        else if(TYPE.equals("Omnitruncated Tesseract")) manifold = new OmnitruncatedTesseract();
        
        else if(TYPE.equals("Rectified Hexadecachoron")) manifold = new Icositetrachoron();
        else if(TYPE.equals("Truncated Hexadecachoron")) manifold = new TruncatedHexadecachoron();
        else if(TYPE.equals("Cantellated Hexadecachoron")) manifold = new CantellatedHexadecachoron();
        else if(TYPE.equals("Runcinated Hexadecachoron")) manifold = new RuncinatedTesseract();
        else if(TYPE.equals("Bitruncated Hexadecachoron")) manifold = new BitruncatedTesseract();
        else if(TYPE.equals("Cantitruncated Hexadecachoron")) manifold = new CantitruncatedHexadecachoron();
        else if(TYPE.equals("Runcitruncated Hexadecachoron")) manifold = new RuncitruncatedTesseract();
        else if(TYPE.equals("Omnitruncated Hexadecachoron")) manifold = new OmnitruncatedTesseract();
        
        else if(TYPE.equals("Rectified Icositetrachoron")) manifold = new CantellatedHexadecachoron();
        else if(TYPE.equals("Truncated Icositetrachoron")) manifold = new CantitruncatedHexadecachoron();
        else if(TYPE.equals("Cantellated Icositetrachoron")) manifold = new CantellatedIcositetrachoron();
        else if(TYPE.equals("Runcinated Icositetrachoron")) manifold = new RuncinatedIcositetrachoron();
        else if(TYPE.equals("Bitruncated Icositetrachoron")) manifold = new BitruncatedIcositetrachoron();
        else if(TYPE.equals("Cantitruncated Icositetrachoron")) manifold = new CantitruncatedIcositetrachoron();
        else if(TYPE.equals("Runcitruncated Icositetrachoron")) manifold = new RuncitruncatedIcositetrachoron();
        else if(TYPE.equals("Omnitruncated Icositetrachoron")) manifold = new OmnitruncatedIcositetrachoron();
        
        else if(TYPE.equals("Rectified Hecatonicosachoron")) manifold = new RectifiedHecatonicosachoron();
        else if(TYPE.equals("Truncated Hecatonicosachoron")) manifold = new TruncatedHecatonicosachoron();
        else if(TYPE.equals("Cantellated Hecatonicosachoron")) manifold = new CantellatedHecatonicosachoron();
        else if(TYPE.equals("Runcinated Hecatonicosachoron")) manifold = new RuncinatedHecatonicosachoron();
        else if(TYPE.equals("Bitruncated Hecatonicosachoron")) manifold = new BitruncatedHecatonicosachoron();
        else if(TYPE.equals("Cantitruncated Hecatonicosachoron")) manifold = new CantitruncatedHecatonicosachoron();
        else if(TYPE.equals("Runcitruncated Hecatonicosachoron")) manifold = new RuncitruncatedHecatonicosachoron();
        else if(TYPE.equals("Omnitruncated Hecatonicosachoron")) manifold = new OmnitruncatedHecatonicosachoron();
        
        else if(TYPE.equals("Rectified Hexacosichoron")) manifold = new RectifiedHexacosichoron();
        else if(TYPE.equals("Truncated Hexacosichoron")) manifold = new TruncatedHexacosichoron();
        else if(TYPE.equals("Cantellated Hexacosichoron")) manifold = new CantellatedHexacosichoron();
        else if(TYPE.equals("Runcinated Hexacosichoron")) manifold = new RuncinatedHecatonicosachoron();
        else if(TYPE.equals("Bitruncated Hexacosichoron")) manifold = new BitruncatedHecatonicosachoron();
        else if(TYPE.equals("Cantitruncated Hexacosichoron")) manifold = new CantitruncatedHexacosichoron();
        else if(TYPE.equals("Runcitruncated Hexacosichoron")) manifold = new RuncitruncatedHexacosichoron();
        else if(TYPE.equals("Omnitruncated Hexacosichoron")) manifold = new OmnitruncatedHecatonicosachoron();
        
        else if(TYPE.equals("Grand Antiprism")) manifold = new GrandAntiprism();
        
        
        
        manifold.CONSTRUCT();
    }
    
    public void createAndShowGUI() 
    {
        manifoldList = new JComboBox(new String[]
            {"Simplex", "Hypercube", "Orthoplex",
            "Icositetrachoron", "Hecatonicosachoron", "Hexacosichoron",
            "Dodecahedron", "Icosahedron",
            "Hypersphere", "Projective Plane", "Klein Bottle", "Parametric",
            
            "Rectified Tesseract",
            "Truncated Tesseract",
            "Cantellated Tesseract",
            "Runcinated Tesseract",
            "Bitruncated Tesseract",
            "Cantitruncated Tesseract",
            "Runcitruncated Tesseract",
            "Omnitruncated Tesseract",
            
            "Rectified Hexadecachoron",
            "Truncated Hexadecachoron",
            "Cantellated Hexadecachoron",
            "Runcinated Hexadecachoron",
            "Bitruncated Hexadecachoron",
            "Cantitruncated Hexadecachoron",
            "Runcitruncated Hexadecachoron",
            "Omnitruncated Hexadecachoron",
        
            "Rectified Icositetrachoron",
            "Truncated Icositetrachoron",
            "Cantellated Icositetrachoron",
            "Runcinated Icositetrachoron",
            "Bitruncated Icositetrachoron",
            "Cantitruncated Icositetrachoron",
            "Runcitruncated Icositetrachoron",
            "Omnitruncated Icositetrachoron",
        
            "Rectified Hecatonicosachoron",
            "Truncated Hecatonicosachoron",
            "Cantellated Hecatonicosachoron",
            "Runcinated Hecatonicosachoron",
            "Bitruncated Hecatonicosachoron",
            "Cantitruncated Hecatonicosachoron",
            "Runcitruncated Hecatonicosachoron",
            "Omnitruncated Hecatonicosachoron",
            
            "Rectified Hexacosichoron",
            "Truncated Hexacosichoron",
            "Cantellated Hexacosichoron",
            "Runcinated Hexacosichoron",
            "Bitruncated Hexacosichoron",
            "Cantitruncated Hexacosichoron",
            "Runcitruncated Hexacosichoron",
            "Omnitruncated Hexacosichoron",
            "Grand Antiprism"});
        
        manifoldList.setSelectedIndex(1); // DEFAULT SELECTION
        manifoldList.addActionListener(this);
        
        TYPE = (String)manifoldList.getSelectedItem();//manifold_list[0];
        setManifold();
        
        DField = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
            DIMENSION = 4;
            DField.setValue(new Integer(DIMENSION));
            DField.setColumns(5);
            DField.addPropertyChangeListener("value", this);
            DLabel = new JLabel("Dimension:");
            DLabel.setLabelFor(DField);
        RField = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
            RSPEED = 100;
            RField.setValue(RSPEED);
            RField.addPropertyChangeListener("value", this);
            RLabel = new JLabel("Rot. Speed:");
            RLabel.setLabelFor(RField);
        MField = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
            MSPEED = 200;
            MField.setValue(MSPEED);
            MField.addPropertyChangeListener("value", this);
            MLabel = new JLabel("Move Speed:");
            MLabel.setLabelFor(MField);
            
        button = new JButton("Compute!");
        button.addActionListener(this);
        
        JPanel boxPane = new JPanel(new BorderLayout());
        boxPane.add(manifoldList, BorderLayout.NORTH);
        
        JPanel labelPane = new JPanel(new GridLayout(10,1));
        labelPane.add(DLabel);
        labelPane.add(RLabel);
        labelPane.add(MLabel);
        
        JPanel fieldPane = new JPanel(new GridLayout(10,1));
        fieldPane.add(DField);
        fieldPane.add(RField);
        fieldPane.add(MField);
        
        JPanel buttonPane = new JPanel();
        buttonPane.add(button);
        
        textPane = new JPanel(new BorderLayout());
        textPane.add(boxPane, BorderLayout.NORTH);
        textPane.add(labelPane, BorderLayout.WEST);
        textPane.add(fieldPane, BorderLayout.EAST);
        textPane.add(buttonPane, BorderLayout.SOUTH);
        
        this.add(textPane);
        
        frame = new JFrame("Polytopia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.pack();
        
        frame.setVisible(true);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) 
    {
        Object source = evt.getSource();
        if(source == DField)
            DIMENSION = ((Number)DField.getValue()).intValue();
        else if(source == RField)
            RSPEED = ((Number)RField.getValue()).intValue();
        else if(source == MField)
            MSPEED = ((Number)MField.getValue()).intValue();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
       if(e != null && e.getSource() == manifoldList)
           TYPE = (String)((JComboBox)e.getSource()).getSelectedItem();
       else
            setManifold();
    }
}

