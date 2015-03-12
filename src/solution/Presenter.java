package solution;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import scotlandyard.Colour;
import scotlandyard.Move;
import scotlandyard.Player;
import scotlandyard.Ticket;

public class Presenter implements Player{
	IntroScreen introGui;
	MainScreen	mainGui;
	ScotlandYardModel model;
	
	Presenter(){
		introGui = new IntroScreen();
		introGui.setupScreen(this);
	}
	
	public static void main(String[] args) {
		Presenter p = new Presenter();
	}


	public void beginGame(Set<Colour> colours) {
		introGui = null;
		try {
			model = new ScotlandYardModel(colours.size()-1, Arrays.asList(false, false, false, true,  false, 
																	 	  false, false, false, true,  false, 
																		  false, false, false, true,  false, 
																		  false, false, false, true,  false, 
																		  false, false, false, false, true ), "resources/graph.txt");
		} catch (IOException e) {
			System.err.println("File not Found, possibly");
		}
		for(Colour c: colours){
			model.join(this, c , getStartingLocation(c), getStartingTickets(c));
		}
		mainGui = new MainScreen(this);
		System.out.println(model.getPlayerLocation(Colour.Black));
		model.start();
	}
	
	private int getStartingLocation(Colour c) {
		Random r = new Random();
		int i;
		do{
			i = (r.nextInt()%199) + 1;
			i = i<1? -i: i;
		}while(model.playerPresent(i, c));
		return i;
	}


	Map<Ticket, Integer> getStartingTickets(Colour c){
		if(c == Colour.Black){
			Map<Ticket, Integer> tickets = new HashMap<Ticket, Integer>();
			tickets.put(Ticket.Underground, 0);
			tickets.put(Ticket.Bus, 0);
			tickets.put(Ticket.Taxi, 0);
			tickets.put(Ticket.DoubleMove, 2);
			tickets.put(Ticket.SecretMove, 5);
			return tickets;
		}else{
			Map<Ticket, Integer> tickets = new HashMap<Ticket, Integer>();
			tickets.put(Ticket.Underground, 4);
			tickets.put(Ticket.Bus, 8);
			tickets.put(Ticket.Taxi, 11);
			tickets.put(Ticket.DoubleMove, 0);
			tickets.put(Ticket.SecretMove, 0);
			return tickets;
		}
	}

	@Override
	public Move notify(int location, List<Move> list) {
		return mainGui.chooseMove(list, location, model.getCurrentPlayer(), model.getRound(), 4);
	}	
}
