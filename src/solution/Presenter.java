package solution;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import scotlandyard.Colour;

public class Presenter {
	Graphics gui;
	ScotlandYardModel model; 
	
	Presenter(){
		gui = new Graphics();
		gui.setupScreen(this);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Presenter p = new Presenter();
	}


	public void beginGame(Set<Colour> colours) {
		try {
			model = new ScotlandYardModel(colours.size()-1, Arrays.asList(false, false, false, true, false, 
																	 	  false, false, false, true, false, 
																		  false, false, false, true, false, 
																		  false, false, false, true, false, 
																		  false, false, false, false, true), "resources/graph.txt");
		} catch (IOException e) {
			System.err.println("File not Found, possibly");
		}
		//TODO
		//Implement players
		//add them to game
	}

	
	
}
