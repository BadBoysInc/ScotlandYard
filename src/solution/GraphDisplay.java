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
	
	Map<String, List<Integer>> coordinateMap;

	GraphDisplay() {

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
        coordinateMap = new HashMap<String, List<Integer>>();
        
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
	}
	
	int getX(int i){
		return coordinateMap.get(Integer.toString(i)).get(0);
	}
	
	int getY(int i){
		return coordinateMap.get(Integer.toString(i)).get(1);
	}

}
