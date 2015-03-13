package solution;

import scotlandyard.Edge;
import scotlandyard.Graph;
import scotlandyard.GraphReader;
import scotlandyard.Route;
import scotlandyard.ScotlandYardGraphReader;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GraphDisplay {

	public static void main(String[] args) {
		
		// load up the image and the graph
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(new File("resources/map.jpg"));
		}
		catch( IOException e )
		{
			System.out.println(e);
		}
		
		
		ScotlandYardGraphReader reader = new ScotlandYardGraphReader();
		
	
		
		Graph<Integer, Route> graph = null;
		try {
			graph = reader.readGraph("resources/graph.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.exit(0);
		}
		
		System.out.println(graph);
		
		
		// read the input positions
		File file = new File("resources/pos.txt");	
		Scanner in = null;
        try 
        {
			in = new Scanner(file);
		} 
        catch (FileNotFoundException e) 
        {
			System.out.println(e);
		}
        
        Map<String, List<Integer>> coordinateMap = new HashMap<String, List<Integer>>();
        
        // get the number of nodes
        String topLine = in.nextLine();
        int numberOfNodes = Integer.parseInt(topLine);
        
        
        for(int i = 0; i < numberOfNodes; i++)
        {
        	String line = in.nextLine();
       
        	String[] parts = line.split(" ");
        	List<Integer> pos = new ArrayList<Integer>();
        	pos.add(Integer.parseInt(parts[1]));
        	pos.add(Integer.parseInt(parts[2]));
        	
        	String key = parts[0];
        	coordinateMap.put(key, pos);
        }
        
        System.out.println(coordinateMap);
        
        Graphics2D g2d = img.createGraphics();
        BasicStroke bs = new BasicStroke(4);
		g2d.setStroke(bs);
		
		// get the list of colors
		int opacity = 255;
		List<Color> colours = new ArrayList<Color>();
		colours.add(new Color(255, 0, 0, opacity));
		colours.add(new Color(0, 255, 0, opacity));
		colours.add(new Color(0, 0, 255, opacity));
		
		
		// sort the edges
		Set<Edge<Integer, Route>> edges = new HashSet<Edge<Integer, Route>>(graph.getEdges());
		List<Edge<Integer, Route>> sortedEdges = new ArrayList<Edge<Integer, Route>>();
		Route[] types = new Route[3];
		types[0] = Route.Taxi;
		types[1] = Route.Bus;
		types[2] = Route.Underground;
		
		for(int i = 0; i < 3; i++)
		{
			Route t = types[i];
			for(Edge<Integer, Route> e : edges)
			{
				if(e.data() == t)
				{
					sortedEdges.add(e);
				}
			}
		}
		
        // draw the edges on the map
        for(Edge<Integer, Route> e : sortedEdges)
        {
        	Color c = colours.get(0);
        	switch(e.data())
        	{
        	case Taxi :
        		c = colours.get(1);
        		break;
        	case Bus :
        		c = colours.get(2);
        		break;
        	case Underground :
        		c = colours.get(0);
        		break;	
        	}
        	
        	List<Integer> p1 = coordinateMap.get(Integer.toString(e.source()));
        	List<Integer> p2 = coordinateMap.get(Integer.toString(e.target()));
        	
        	System.out.println(String.format("%d, %d, %d, %d", p1.get(0), p1.get(1), p2.get(0), p2.get(1)));
        	
        	g2d.setColor(c);
        	
        	g2d.drawLine(p1.get(0), p1.get(1), p2.get(0), p2.get(1));
        }
        
        
        
        
        
		
		
		
		
		
	
		
		
		
		
		

		ImageIcon icon = new ImageIcon(img);
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(1018, 809);
		JLabel lbl = new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		// display the image
		

	}

}
