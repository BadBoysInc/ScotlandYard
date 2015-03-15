package solution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import scotlandyard.Colour;
import scotlandyard.Move;
import scotlandyard.MovePass;
import scotlandyard.MoveTicket;
import scotlandyard.Player;
import scotlandyard.Ticket;



public class Presenter implements Player{
	boolean response; 
	IntroScreen introGui;
	MainScreen	mainGui;
	ScotlandYardModel model;
	final Presenter presenter = this;
	
	Presenter(){
		introGui = new IntroScreen();
		introGui.setupScreen(this);
	}
	
	public static void main(String[] args) {
		Presenter p = new Presenter();
	}


	public void beginGame(Set<Colour> colours) {
		introGui = null;
		
		//Make Model
		try {
			model = new ScotlandYardModel(colours.size()-1, Arrays.asList(false, false, false, true,  false, 
																	 	  false, false, false, true,  false, 
																		  false, false, false, true,  false, 
																		  false, false, false, true,  false, 
																		  false, false, false, false, true ), "resources/graph.txt");
		} catch (IOException e) {
			System.err.println("File not Found, possibly");
		}
		
		//Add Players to model;
		for(Colour c: colours){
			model.join(this, c , getStartingLocation(c), getStartingTickets(c));
		}
		
		//Make gui
		mainGui = new MainScreen(presenter, colours);
		
		//model.start();
		if(!model.isReady() || model.isGameOver()){
			System.exit(1);
		}
		
		mainGui.updateDisplay(model.getCurrentPlayer(), Integer.toString(model.getRound()), "1", getTaxiMoves(), 
							  getBusMoves(), getUndergroundMoves(), getSecretMoves(), getLocations());

	}
	
	Set<Integer> getTaxiMoves(){
		List<Move> moves = model.validMoves(model.getCurrentPlayer());
		if(moves.contains(new MovePass(model.getCurrentPlayer())))
			moves.remove(new MovePass(model.getCurrentPlayer()));
		List<Move> newMoves = new ArrayList<Move>();
		//REMOVE DOUBLE MOVES FOR NOW
		for(Move m: moves){
			if(!m.toString().contains("Move Double ")){
				newMoves.add(m);
			}
		}
		
		Set<Integer> taximoves = new HashSet<Integer>();
		for(Move m: newMoves){
			if(((MoveTicket) m).ticket == Ticket.Taxi){
				taximoves.add(((MoveTicket) m).target);
			}
		}
		
		return taximoves;
	}
	
	Set<Integer> getBusMoves(){
		List<Move> moves = model.validMoves(model.getCurrentPlayer());
		if(moves.contains(new MovePass(model.getCurrentPlayer())))
			moves.remove(new MovePass(model.getCurrentPlayer()));
		List<Move> newMoves = new ArrayList<Move>();
		//REMOVE DOUBLE MOVES
		for(Move m: moves){
			if(!m.toString().contains("Move Double ")){
				newMoves.add(m);
			}
		}
		
		Set<Integer> busmoves = new HashSet<Integer>();
		for(Move m: newMoves){
			if(((MoveTicket) m).ticket == Ticket.Bus){
				busmoves.add(((MoveTicket) m).target);
			}
		}
		
		return busmoves;
	}
	
	Set<Integer> getUndergroundMoves(){
		List<Move> moves = model.validMoves(model.getCurrentPlayer());
		if(moves.contains(new MovePass(model.getCurrentPlayer())))
			moves.remove(new MovePass(model.getCurrentPlayer()));
		List<Move> newMoves = new ArrayList<Move>();
		//REMOVE DOUBLE MOVES
		for(Move m: moves){
			if(!m.toString().contains("Move Double ")){
				newMoves.add(m);
			}
		}
		
		Set<Integer> undergroundmoves = new HashSet<Integer>();
		for(Move m: newMoves){
			if(((MoveTicket) m).ticket == Ticket.Underground){
				undergroundmoves.add(((MoveTicket) m).target);
			}
		}
		
		return undergroundmoves;
	}
	
	Set<Integer> getSecretMoves(){
		List<Move> moves = model.validMoves(model.getCurrentPlayer());
		if(moves.contains(new MovePass(model.getCurrentPlayer())))
			moves.remove(new MovePass(model.getCurrentPlayer()));
		List<Move> newMoves = new ArrayList<Move>();
		//REMOVE DOUBLE MOVES
		for(Move m: moves){
			if(!m.toString().contains("Move Double ")){
				newMoves.add(m);
			}
		}
		
		Set<Integer> secretmoves = new HashSet<Integer>();
		for(Move m: newMoves){
			if(((MoveTicket) m).ticket == Ticket.SecretMove){
				secretmoves.add(((MoveTicket) m).target);
			}
		}
		
		return secretmoves;
	}
	
	Hashtable<Colour, Integer> getLocations(){
		Hashtable<Colour, Integer> locations = new Hashtable<Colour, Integer>();
		for(Colour p: model.getPlayers()){
			locations.put(p, model.getPlayerLocation(p));
		}
		if(model.getCurrentPlayer() == Colour.Black){
			locations.remove(Colour.Black);
			locations.put(Colour.Black, model.getMrXLocation());
		}
		if(locations.get(Colour.Black) == 0)
			locations.remove(Colour.Black);
		return locations;
	}
	
	int getStartingLocation(Colour c) {
		Random r = new Random();
		int i;
		do{
			i = (r.nextInt()%199);
			i = i<0? -i: i;
		}while(model.playerPresent(i + 1, c));
		return i + 1;
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
		return null;
	}

	public void sendMove() {
		model.playMove(model.validMoves(model.getCurrentPlayer()).get(0));
		if(model.isGameOver())
			System.exit(0);
		mainGui.updateDisplay(model.getCurrentPlayer(), Integer.toString(model.getRound()), "1", getTaxiMoves(), 
							  getBusMoves(), getUndergroundMoves(), getSecretMoves(), getLocations());
	}	
}
