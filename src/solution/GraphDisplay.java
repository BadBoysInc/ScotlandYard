package solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
