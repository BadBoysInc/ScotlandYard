package solution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import scotlandyard.Colour;
import scotlandyard.Move;
import scotlandyard.MoveDouble;
import scotlandyard.MovePass;
import scotlandyard.MoveTicket;
import scotlandyard.Player;
import scotlandyard.Ticket;

public class Presenter implements Player {

	private IntroScreen introGui;
	private MainScreen mainGui;
	private ScotlandYardModel model;
	final Presenter presenter = this;
	private Move firstMove;
	List<Ticket> mrXUsedTickets;
	GameData gameData;

	Presenter() {
		introGui = new IntroScreen();
		introGui.setupScreen(this);
		mrXUsedTickets = new ArrayList<Ticket>();
	}
	
	
	public void beginGame(Set<Colour> colours) {
		introGui = null;

		//Make Model.
		try {
			model = new ScotlandYardModel(colours.size() - 1,
					Arrays.asList(false, false, false, true, false, false,
							false, false, true, false, false, false, false,
							true, false, false, false, false, true, false,
							false, false, false, false, true), "resources/graph.txt");
		} catch (IOException e) {System.err.println("File not Found.");}

		//Add Players to model.
		gameData = new GameData();
		for (Colour c : colours) {
			int l = getStartingLocation(c);
			Map<Ticket, Integer> t = getStartingTickets(c);
			model.join(this, c, l, t);
			gameData.addPlayer(c, l, t);
		}

		//Make GUI.
		mainGui = new MainScreen(presenter, colours);

		//Game must be playable.
		assert (!model.isReady() || model.isGameOver());

		//Update GUI with initial data.
		Colour c = model.getCurrentPlayer();
		List<scotlandyard.Move> validMoves = model.validMoves(c);
		mainGui.updateDisplay(c, Integer.toString(model.getRound()),
				getRoundsUntilReveal(), getRoundsLeft(),
				getTaxiMoves(validMoves), getBusMoves(validMoves),
				getUndergroundMoves(validMoves), getSecretMoves(validMoves),
				getLocations(), model.getPlayer(c).getCopyOfAllTickets(), model.getMrXPossibleLocations() );
	}
	
	//Get the number of rounds left.
	private String getRoundsLeft() {
		int total = model.getRounds().size();
		int current = model.getRound();
		return Integer.toString((total - current) - 1);
	}
	
	//Get possible taxi moves.
	Set<Integer> getTaxiMoves(List<Move> moves) {
		if (moves.contains(new MovePass(model.getCurrentPlayer())))
			moves.remove(new MovePass(model.getCurrentPlayer()));
		List<Move> newMoves = new ArrayList<Move>();
		//Remove double moves.
		for (Move m : moves) {
			if (!m.toString().contains("Move Double ")) {
				newMoves.add(m);
			}
		}

		Set<Integer> taximoves = new HashSet<Integer>();
		for (Move m : newMoves) {
			if (((MoveTicket) m).ticket == Ticket.Taxi) {
				taximoves.add(((MoveTicket) m).target);
			}
		}
		return taximoves;
	}
	
	//Get possible bus moves.
	Set<Integer> getBusMoves(List<Move> moves) {
		if (moves.contains(new MovePass(model.getCurrentPlayer())))
			moves.remove(new MovePass(model.getCurrentPlayer()));
		List<Move> newMoves = new ArrayList<Move>();
		//Remove double moves.
		for (Move m : moves) {
			if (!m.toString().contains("Move Double ")) {
				newMoves.add(m);
			}
		}

		Set<Integer> busmoves = new HashSet<Integer>();
		for (Move m : newMoves) {
			if (((MoveTicket) m).ticket == Ticket.Bus) {
				busmoves.add(((MoveTicket) m).target);
			}
		}

		return busmoves;
	}
	
	//Get possible underground moves.
	Set<Integer> getUndergroundMoves(List<Move> moves) {
		if (moves.contains(new MovePass(model.getCurrentPlayer())))
			moves.remove(new MovePass(model.getCurrentPlayer()));
		List<Move> newMoves = new ArrayList<Move>();
		//Remove double moves.
		for (Move m : moves) {
			if (!m.toString().contains("Move Double ")) {
				newMoves.add(m);
			}
		}

		Set<Integer> undergroundmoves = new HashSet<Integer>();
		for (Move m : newMoves) {
			if (((MoveTicket) m).ticket == Ticket.Underground) {
				undergroundmoves.add(((MoveTicket) m).target);
			}
		}

		return undergroundmoves;
	}
	
	//Get possible secret moves.
	Set<Integer> getSecretMoves(List<Move> moves) {
		if (moves.contains(new MovePass(model.getCurrentPlayer())))
			moves.remove(new MovePass(model.getCurrentPlayer()));
		List<Move> newMoves = new ArrayList<Move>();
		//Remove double moves.
		for (Move m : moves) {
			if (!m.toString().contains("Move Double ")) {
				newMoves.add(m);
			}
		}

		Set<Integer> secretmoves = new HashSet<Integer>();
		for (Move m : newMoves) {
			if (((MoveTicket) m).ticket == Ticket.SecretMove) {
				secretmoves.add(((MoveTicket) m).target);
			}
		}

		return secretmoves;
	}
	
	//Get the location of each player.
	Hashtable<Colour, Integer> getLocations() {
		Hashtable<Colour, Integer> locations = new Hashtable<Colour, Integer>();
		for (Colour p : model.getPlayers()) {
			locations.put(p, model.getPlayerLocation(p));
		}
		if (model.getCurrentPlayer() == Colour.Black) {
			locations.remove(Colour.Black);
			locations.put(Colour.Black, model.getMrXLocation());
		}
		if (locations.get(Colour.Black) == 0)
			locations.remove(Colour.Black);
		return locations;
	}
	
	//Give players starting locations.
	int getStartingLocation(Colour c) {
		Random r = new Random();
		int i;
		do {
			i = (r.nextInt() % 199);
			i = i < 0 ? -i : i;
		} while (model.playerPresent(i + 1, c));
		return i + 1;
	}
	
	//Give players their starting tickets.
	Map<Ticket, Integer> getStartingTickets(Colour c) {
		if (c == Colour.Black) {
			Map<Ticket, Integer> tickets = new HashMap<Ticket, Integer>();
			tickets.put(Ticket.Underground, 0);
			tickets.put(Ticket.Bus, 0);
			tickets.put(Ticket.Taxi, 0);
			tickets.put(Ticket.DoubleMove, 2);
			tickets.put(Ticket.SecretMove, 5);
			return tickets;
		} else {
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

	//GUI tells model to play a move.
	public void sendMove(int target, Ticket t, Colour currentPlayer,
			boolean moveDouble) {
		
		Move m = null;
		if (moveDouble) {
			Move secondMove = new MoveTicket(currentPlayer, target, t);
			m = new MoveDouble(currentPlayer, firstMove, secondMove);
			mainGui.updateTicketPanel(((MoveTicket) firstMove).ticket, model.getRound());
			mrXUsedTickets.add(((MoveTicket) firstMove).ticket);
			mainGui.updateTicketPanel(((MoveTicket) secondMove).ticket, model.getRound() + 1);
			mrXUsedTickets.add(((MoveTicket) secondMove).ticket);
			gameData.addMove(new solution.MoveTicket(currentPlayer, ((MoveTicket) firstMove).target, ((MoveTicket) firstMove).ticket));		
		} else {
			m = new MoveTicket(currentPlayer, target, t);
			if (currentPlayer == Colour.Black) {
				mrXUsedTickets.add(((MoveTicket) m).ticket);
				mainGui.updateTicketPanel(((MoveTicket) m).ticket, model.getRound());
			}
		}
		
		gameData.addMove(new solution.MoveTicket(currentPlayer, target, t));		
		
		if (Debug.debug) {
			System.out.println("Move received: " + m
					+ ", Sending move to model");
		}

		model.playMove(m, this);
	}

	//Model tells presenter to update the GUI.
	public void notifyModelChange(List<Move> validMoves) {
		if (Debug.debug) {
			System.out.println("Presenter notified, updating gui");
		}
		if (model.isGameOver()) {
			mainGui.dispose();
			WinnersScreen ws = new WinnersScreen(model.getWinningPlayers(),
					this);
		} else {
			Colour c = model.getCurrentPlayer();
			mainGui.updateDisplay(c, Integer.toString(model.getRound()),
					getRoundsUntilReveal(), getRoundsLeft(),
					getTaxiMoves(validMoves), getBusMoves(validMoves),
					getUndergroundMoves(validMoves),
					getSecretMoves(validMoves), getLocations(), model
							.getPlayer(c).getCopyOfAllTickets(), model.getMrXPossibleLocations() );
		}
	}
	
	//Get the number of rounds until Mr. X is revealed.
	private String getRoundsUntilReveal() {
		int r = model.getRound();
		int i = r;
		while (model.getRounds().get(i) == false) {
			i++;
		}
		i = i - r;
		return Integer.toString(i);
	}
	 
	public int getLastRevealRound() {
		int r = model.getRound();
		int i = r;
		while (model.getRounds().get(i) == false) {
			if(i<0){
				i=-1;
				break;
			}
			i--;
		}
		return i;
	}

	//Store the first move of a double move.
	public void sendFirstMove(int target, Ticket t, Colour currentPlayer) {
		firstMove = new MoveTicket(currentPlayer, target, t);
		List<Move> moves = model.validMoves(model.getCurrentPlayer());
		List<Move> newMoves = new ArrayList<Move>();

		for (Move m : moves) {
			if (m.toString().contains("Move Double ")
					&& ((MoveDouble) m).moves.get(0).equals(firstMove)) {
				newMoves.add(((MoveDouble) m).moves.get(1));
			}
		}

		Hashtable<Colour, Integer> locations = getLocations();
		locations.put(Colour.Black, ((MoveTicket) firstMove).target);
		Map<Ticket, Integer> tickets = model
				.getPlayer(model.getCurrentPlayer()).getCopyOfAllTickets();
		tickets.put(t, tickets.get(t) - 1);
		mainGui.updateDisplay(model.getCurrentPlayer(),
				Integer.toString(model.getRound()), getRoundsUntilReveal(),
				getRoundsLeft(), getTaxiMoves(newMoves), getBusMoves(newMoves),
				getUndergroundMoves(newMoves), getSecretMoves(newMoves),
				locations, tickets, model.getMrXPossibleLocations() );

	}
	
	//Re-update the GUI after double move is unselected.
	public void doubleMoveFalse() {
		List<Move> validMoves = model.validMoves(model.getCurrentPlayer());
		mainGui.updateDisplay(model.getCurrentPlayer(),
				Integer.toString(model.getRound()), getRoundsUntilReveal(),
				getRoundsLeft(), getTaxiMoves(validMoves),
				getBusMoves(validMoves), getUndergroundMoves(validMoves),
				getSecretMoves(validMoves), getLocations(),
				model.getPlayer(model.getCurrentPlayer()).getCopyOfAllTickets(), model.getMrXPossibleLocations() );
	}

	//Save the game.
	public void saveCurrentState(File file) {

		gameData.setTime(mainGui.currentTime);
		
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(gameData);
			out.close();
			fileOut.close();
		} catch (IOException e) {e.printStackTrace();}
		if(Debug.debug)System.out.println("Saved");
		
	}

	//Load the game.
	public void loadGameState(File file) {
		
		try {
			FileInputStream fileIn = new FileInputStream(file);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        gameData = (GameData) in.readObject();
	        in.close();
	        fileIn.close();
	        
	        //Make GUI.
			introGui = null;
			mainGui = new MainScreen(presenter, gameData.getColours());
	        
	        model = new ScotlandYardModel(gameData.getPlayers().size()-1, Arrays.asList(false, false,
					false, true, false, false, false, false, true, false,
					false, false, false, true, false, false, false, false,
					true, false, false, false, false, false, true),
					"resources/graph.txt");
	        
	        for(PlayerInfo p:gameData.getPlayers()){
	        	model.join(this, p.getColour(), p.getLocation(), p.getCopyOfAllTickets());
	        }
	        Colour lastPlayer = null;
	        for(solution.MoveTicket m: gameData.getMoves()){
	        	if(m.colour == Colour.Black){
	        		mainGui.updateTicketPanel(m.ticket, model.getRound());
	        	}
	        	System.out.println(m);
	        	model.play(new MoveTicket(m.colour, m.target, m.ticket));
	        	lastPlayer = m.colour;
	        }
	        model.currentPlayer = lastPlayer;
	        model.nextPlayer();
	        
	        mainGui.currentTime = gameData.getTime();
	        
	        Colour c = model.getCurrentPlayer();
			List<Move> validMoves = model.validMoves(c);
			mainGui.updateDisplay(c, Integer.toString(model.getRound()),
					getRoundsUntilReveal(), getRoundsLeft(),
					getTaxiMoves(validMoves), getBusMoves(validMoves),
					getUndergroundMoves(validMoves),
					getSecretMoves(validMoves), getLocations(), model.getPlayer(c).getCopyOfAllTickets(), model.getMrXPossibleLocations() );
	     
	       
		}catch(IOException e){
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	//Save the replay.
	public void saveForReplay(File file) {

	try {
		FileOutputStream fileOut = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(gameData);
		out.close();
		fileOut.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	if(Debug.debug)System.out.println("Saved");

	}

	//Play a replay.
	public void startReplay(File file) {
		try {
			FileInputStream fileIn = new FileInputStream(file);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        gameData = (GameData) in.readObject();
	        in.close();
	        fileIn.close();
		
			
			Map<Colour, Integer> locations = new HashMap<Colour, Integer>();
			for(PlayerInfo p: gameData.getPlayers()){
				Colour c = p.getColour();
				int l = p.getLocation();
				locations.put(c, l);
			}
		
			ArrayList<Map<Colour, Integer>> positions = new ArrayList<Map<Colour, Integer>>();
			positions.add(locations);
			
			Map<Colour, Integer> oldLocations = locations;
			for(solution.MoveTicket m: gameData.getMoves()){
				Map<Colour, Integer> newLocations = new HashMap<Colour, Integer>(oldLocations);
				newLocations.put(m.colour, m.target);
				positions.add(newLocations);
				oldLocations = newLocations;
			}
			
			introGui = null;
			
			ReplayScreen replayScreen = new ReplayScreen(positions);

			Presenter p = this;

		}catch(IOException e){
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
