package solution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
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
import scotlandyard.MoveDouble;
import scotlandyard.MovePass;
import scotlandyard.MoveTicket;
import scotlandyard.Player;
import scotlandyard.Ticket;



public class Presenter implements Player{
	//boolean response; 
	private IntroScreen introGui;
	private MainScreen	mainGui;
	private ScotlandYardModel model;
	final Presenter presenter = this;
	private Move firstMove;
	
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
			System.err.println("File not Found.");
		}
		
		//Add Players to model;
		for(Colour c: colours){
			model.join(this, c , getStartingLocation(c), getStartingTickets(c));
		}
		
		//Make gui
		mainGui = new MainScreen(presenter, colours);
		
		//game must be playable
		assert(!model.isReady() || model.isGameOver());
		
		Colour c = model.getCurrentPlayer();
		List<Move> validMoves = model.validMoves(c);
		mainGui.updateDisplay(c, Integer.toString(model.getRound()), "1", getTaxiMoves(validMoves), getBusMoves(validMoves), getUndergroundMoves(validMoves), getSecretMoves(validMoves), getLocations(), model.getPlayer(c).getCopyOfAllTickets());
	}
	
	Set<Integer> getTaxiMoves(List<Move> moves){
		if(moves.contains(new MovePass(model.getCurrentPlayer())))
			moves.remove(new MovePass(model.getCurrentPlayer()));
		List<Move> newMoves = new ArrayList<Move>();
		//REMOVE DOUBLE MOVES
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
	
	Set<Integer> getBusMoves(List<Move> moves){
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
	
	Set<Integer> getUndergroundMoves(List<Move> moves){
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
	
	Set<Integer> getSecretMoves(List<Move> moves){
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

	//Called by gui, tells model to play move
	public void sendMove(int target, Ticket t, Colour currentPlayer, boolean moveDouble) {
		Move m = null;
		if(moveDouble){
			Move secondMove = new MoveTicket(currentPlayer, target, t);
			m = new MoveDouble(currentPlayer, firstMove, secondMove);
			mainGui.updateTicketPanel(((MoveTicket)firstMove).ticket, model.getRound());
			mainGui.updateTicketPanel(((MoveTicket)secondMove).ticket, model.getRound()+1);
		}else{
			m = new MoveTicket(currentPlayer, target, t);
			if(currentPlayer == Colour.Black)
				mainGui.updateTicketPanel(((MoveTicket)m).ticket, model.getRound());
		}
		if(Debug.debug){System.out.println("Move received: "+ m + ", Sending move to model");}
		
		model.playMove(m, this);
	}

	//called by model, tells gui to update
	public void notifyModelChange(List<Move> validMoves) {
		if(Debug.debug){System.out.println("Presenter notified, updating gui");}
		if(model.isGameOver()){
			mainGui.displayWinner(model.getWinningPlayers());
		}else{
			Colour c = model.getCurrentPlayer();
			mainGui.updateDisplay(c, Integer.toString(model.getRound()), getRoundsUntilReveal(), getTaxiMoves(validMoves), getBusMoves(validMoves), getUndergroundMoves(validMoves), getSecretMoves(validMoves), getLocations(), model.getPlayer(c).getCopyOfAllTickets());
		}
	}

	private String getRoundsUntilReveal() {
		int r = model.getRound();
		int i = r;
		while(model.getRounds().get(i)==false){
			i++;
		}
		i = i - r;
		return Integer.toString(i);
	}

	public void sendFirstMove(int target, Ticket t, Colour currentPlayer) {
		firstMove = new MoveTicket(currentPlayer, target, t);
		List<Move> moves = model.validMoves(model.getCurrentPlayer());
		List<Move> newMoves = new ArrayList<Move>();
		
		for(Move m: moves){
			if(m.toString().contains("Move Double ") && ((MoveDouble) m).moves.get(0).equals(firstMove)){
				newMoves.add(((MoveDouble) m).moves.get(1));
			}
		}
		
		Hashtable<Colour, Integer> locations = getLocations();
		locations.put(Colour.Black, ((MoveTicket)firstMove).target);
		mainGui.updateDisplay(model.getCurrentPlayer(), Integer.toString(model.getRound()), "1", getTaxiMoves(newMoves), getBusMoves(newMoves), getUndergroundMoves(newMoves), getSecretMoves(newMoves), locations, model.getPlayer(model.getCurrentPlayer()).getCopyOfAllTickets());
		
	}
	
	public void doubleMoveFalse() {
		List<Move> validMoves = model.validMoves(model.getCurrentPlayer());
		mainGui.updateDisplay(model.getCurrentPlayer(), Integer.toString(model.getRound()), "1", getTaxiMoves(validMoves), getBusMoves(validMoves), getUndergroundMoves(validMoves), getSecretMoves(validMoves), getLocations(), model.getPlayer(model.getCurrentPlayer()).getCopyOfAllTickets());
	}

	public void saveCurrentState(File file) {
		
		String data = String.format("%s%n",Integer.toString(model.getPlayers().size()));
		for(Colour c : model.getPlayers()){
			PlayerInfo p = model.getPlayer(c);
			data = data + String.format("%s %d %d %d %d %d %d%n", c.toString(), p.getLocation(), p.getTickets(Ticket.Taxi), p.getTickets(Ticket.Bus), p.getTickets(Ticket.Underground), p.getTickets(Ticket.SecretMove), p.getTickets(Ticket.DoubleMove) );
		}
		data = data + Integer.toString(model.getRound());		
		
		try {
			Writer writer = Files.newBufferedWriter(file.toPath());
			writer.write(data);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
