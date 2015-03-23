package solution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scotlandyard.Colour;
import scotlandyard.Edge;
import scotlandyard.Graph;
import scotlandyard.MoveDouble;
import scotlandyard.MovePass;
import scotlandyard.MoveTicket;
import scotlandyard.Player;
import scotlandyard.Route;
import scotlandyard.ScotlandYard;
import scotlandyard.ScotlandYardGraphReader;
import scotlandyard.Spectator;
import scotlandyard.Ticket;

public class ScotlandYardModel extends ScotlandYard {
	
	//Game Constants:
	final private int numberOfDetectives;
	final private Graph<Integer, Route> graph;
	final private List<Boolean> rounds;
	
	//Participants:
	private List<PlayerInfo> playerInfos;
	final private List<Spectator> spectators;
	
	//Game Variables:
	private int round;
	Colour currentPlayer;
	int MrXsLastKnownLocation;
	
	//For Game Helper
	private Set<Integer> mrXPossibleLocations;
	
    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException {
    	
    	super(numberOfDetectives, rounds, graphFileName);
		
		//Get the Graph from the Input File.
    	ScotlandYardGraphReader reader 	= new ScotlandYardGraphReader();
		graph = reader.readGraph(graphFileName);
		
		//Initialise Game Constants.
		this.rounds = rounds;
		this.numberOfDetectives = numberOfDetectives;
		
		//Initialise Observer Lists.		
		playerInfos = new ArrayList<PlayerInfo>();
		spectators = new ArrayList<Spectator>();

		//Initialise Game Variables.
		MrXsLastKnownLocation = 0;
		round = 0;
		currentPlayer = Colour.Black;
		
		mrXPossibleLocations = new HashSet<Integer>();
		
    }

    @Override
    //Returns the move the player wishes to make.
    protected scotlandyard.Move getPlayerMove(Colour colour) {
    	PlayerInfo p = getPlayer(colour);
    	return p.getPlayer().notify(p.getLocation(), validMoves(colour));
    }

    @Override
    //Changes currentPlayer to the next player in the ArrayList.
    protected void nextPlayer() {
    	int index = playerInfos.indexOf(getPlayer(currentPlayer));
    	currentPlayer = playerInfos.get((index+1)%(playerInfos.size())).getColour();
    }
    
    @Override
    //Changes the game-state to after a move has been played and notifies the spectators. 
    protected void play(MoveTicket move) {
    	makeMove(move);
    	if((move.colour != Colour.Black) || (getRounds().get(getRound()) == true)){
    		notifySpectators(move);
    	}else{
    		notifySpectators(getDummyTicket(move));
    	}
    }
    
    //Play a move and tell the presenter to update the GUI.
    public void playMove(scotlandyard.Move move, Presenter p){
    	if(Debug.debug){System.out.println("Move recieved, playing move");}
    	play(move);
    	nextPlayer();
    	if(Debug.debug){System.out.println("Notifiying presenter of state change");}
    	
    	List<scotlandyard.Move> moves = validMoves(currentPlayer);
    	while(moves.contains(new MovePass(currentPlayer))){
        	nextPlayer();
        	moves = validMoves(currentPlayer);
    	}
    	p.notifyModelChange(moves);
    }

    @Override
    //Changes the game-state to after a double-move has been played and notifies the spectators.
    protected void play(MoveDouble move) {
    	scotlandyard.Move dummy1 = move.moves.get(0);
    	scotlandyard.Move dummy2 = move.moves.get(1);
    	if(getRounds().get(getRound()+1) != true)
    		dummy1 = getDummyTicket((scotlandyard.MoveTicket)move.moves.get(0));
    	if(getRounds().get(getRound()+2) != true)
    		dummy2 = getDummyTicket((scotlandyard.MoveTicket)move.moves.get(1));
    	notifySpectators(new MoveDouble(move.colour, dummy1, dummy2));
    	
    	makeMove((MoveTicket)move.moves.get(0));
    	notifySpectators(dummy1);
    	makeMove((MoveTicket)move.moves.get(1));
    	notifySpectators(dummy2);
    	
    	getPlayer(Colour.Black).removeTickets(Ticket.DoubleMove); 	
    }
    
    //Changes the game-state to after a move has been played.
    void makeMove(MoveTicket move) {
    	PlayerInfo player = getPlayer(move.colour);
    	player.setLocation(move.target);
    	player.removeTickets(move.ticket);
    	
    	if(player.getColour() != Colour.Black){
    		getPlayer(Colour.Black).addTickets(move.ticket);
    	}else{
    		if(Debug.debug)
    			System.out.println("Round Increment.");
    		round = round + 1;
    		if(getRounds().get(getRound()) == true){
    			MrXsLastKnownLocation = getPlayer(Colour.Black).getLocation();
        		mrXPossibleLocations = new HashSet<Integer>();
        		mrXPossibleLocations.add(MrXsLastKnownLocation);
    		}else{
    			mrXPossibleLocations = computePossibleLocations(mrXPossibleLocations, move.ticket);
    		}
    		
    		
    		
    	}
    }
    
    //Find Mr. X's possible moves.
    private Set<Integer> computePossibleLocations(Set<Integer> possibleLocations, Ticket ticket) {
		Set<Integer> newPos = new HashSet<Integer>();
		for(Integer location: possibleLocations){
			for(Edge<Integer, Route> e: graph.getEdges()){
				
				if((e.source().equals(location)||e.target().equals(location))){
		    		if((Ticket.fromRoute(e.data()) == ticket || ticket == Ticket.SecretMove) && !playerPresent(e.other(location), Colour.Black)){   			
		    			newPos.add(e.other(location));
		        	}
				}
	        }
		}
		return newPos;
	}

	//Creates a dummy ticket with Mr. X's last known location. 
    private scotlandyard.Move getDummyTicket(scotlandyard.MoveTicket move) {
    	return new scotlandyard.MoveTicket(Colour.Black, MrXsLastKnownLocation, move.ticket);
    }

    @Override
    //Notifies the spectators that nothing has changed.
    protected void play(MovePass move) {
    	notifySpectators(move);
    }

    @Override
    //Returns the possible moves a player can make.
    protected List<scotlandyard.Move> validMoves(Colour player) {
    	//Adds all the moves around a players current location.
        List<scotlandyard.Move> movesSingle = singleMoves(getPlayer(player).getLocation(), player);
        List<scotlandyard.Move> moves = new ArrayList<scotlandyard.Move>(movesSingle);
        //Adds double-moves to Mr.X's valid moves.
        if(hasTickets(Ticket.DoubleMove, player, 1)){
        	for(scotlandyard.Move m: movesSingle){
        		List<scotlandyard.Move> doubleMoves = singleMoves(((scotlandyard.MoveTicket)  m).target, player);
        		for(scotlandyard.Move dm: doubleMoves){
        			if((((scotlandyard.MoveTicket) dm).ticket == ((scotlandyard.MoveTicket) m).ticket)){
        				if(hasTickets(((scotlandyard.MoveTicket) m).ticket, player, 2))
        					moves.add(new MoveDouble(player, m, dm));
        			}else if(hasTickets(((scotlandyard.MoveTicket) dm).ticket, player, 1)){
        				moves.add(new MoveDouble(player, m, dm));
        			}
        		}
        	}
        }
        //Adds a pass move if there is no possible moves.
        if(moves.isEmpty() && player != Colour.Black)
        	moves.add(new MovePass(player));
     
        return moves;
    }
    
    //Returns the list of moves around the players current location.
    private List<scotlandyard.Move> singleMoves(int location, Colour player) {
    	List<scotlandyard.Move> moves = new ArrayList<scotlandyard.Move>();
    	for(Edge<Integer, Route> e: graph.getEdges()){	       	
    		if(e.source()==location||e.target()==location){   			
    			scotlandyard.Move m = new scotlandyard.MoveTicket(player, e.other(location), Ticket.fromRoute(e.data()));
        		if(!playerPresent(e.other(location), player) && hasTickets(((scotlandyard.MoveTicket) m).ticket, player, 1)){ 
        			moves.add(m);
        		}
        		//Add a secret ticket alternative for Mr. X.
        		if(hasTickets(Ticket.SecretMove, player, 1) && ((MoveTicket) m).ticket != Ticket.SecretMove && !playerPresent(e.other(location), player)){
        			scotlandyard.Move secretm = new scotlandyard.MoveTicket(player, e.other(location), Ticket.SecretMove);
        			moves.add(secretm);
        		}
        	}
        }
    	return moves;
    }

    //Checks whether a player is on the specified location.
    boolean playerPresent(int location, Colour player) {
    	for(PlayerInfo p: playerInfos){
    		if( (p.getLocation() == location) && (p != getPlayer(player)) && (p.getColour() != Colour.Black))
    			return true;
    	}
    	return false;
    }
    
    //Checks whether a player has n tickets of the specified type.
    private boolean hasTickets(Ticket t, Colour player, int n) {
    	return (getPlayer(player).getTickets(t) >= n);
    }
    
    @Override
    //Adds a spectator to the game.
    public void spectate(Spectator spectator) {
    	spectators.add(spectator);
    }
    
    //Notifies all spectators that a move has been made.
    private void notifySpectators(scotlandyard.Move move) {
    	for(Spectator s: spectators){
    		s.notify(move);
    	}
    }

    @Override
    //Adds a player to the game.
    public boolean join(Player player, Colour colour, int location, Map<Ticket, Integer> tickets) {
    	if(playerExists(colour)){
    		return false;
    	}else{
    		if((colour == Colour.Black) && (getRounds().get(0) == true))
        		MrXsLastKnownLocation = location;
    		playerInfos.add(new PlayerInfo(colour, location, tickets, player));
    		return true;
    	}
    }

    @Override
    //Gets a list of all the players in the game.
    public List<Colour> getPlayers() {
    	List<Colour> c = new ArrayList<Colour>();
    	for(PlayerInfo p : playerInfos){
    		c.add(p.getColour());
    	}
        return c;
    }
    
    public List<PlayerInfo> getPlayerInfos(){
    	return playerInfos;
    }

    @Override
    //Gets a set of the winning players.
    public Set<Colour> getWinningPlayers() {
    	Set<Colour> winners = new HashSet<Colour>();
    	if(isGameOver()){
        	if(allDetectivesAreStuck() || endOfFinalRound()){
        		winners.add(Colour.Black);
        	}else{
        		winners =  new HashSet<Colour>(getPlayers());
        		winners.remove(Colour.Black);
        	}
    	}
     	return winners;
    }

    @Override
    //Gets a Detectives current location or MrX's last known location.
    public int getPlayerLocation(Colour colour) {
    	if(playerExists(colour)){
			if(colour == Colour.Black){
				return MrXsLastKnownLocation;
			}else{
    			return getPlayer(colour).getLocation();    				
			}
    	}else{
    		return 0;
    	}
    }

    @Override
    //Returns the number of tickets a player has of a specified type.
    public int getPlayerTickets(Colour colour, Ticket ticket) {
    	if(playerExists(colour)){
    		return getPlayer(colour).getTickets(ticket);
    	}else{
    		return 0;
    	}
    }

    @Override
    //Checks whether the conditions for the game's termination have been met.
    public boolean isGameOver() {
    	if(!isReady())
    		return false;
    	if(allDetectivesAreStuck())
    		return true;
    	if(endOfFinalRound())
    		return true;
    	if(isMrXCaught())
    		return true;
    	if(MrXHasNowhereToGo())
    		return true;
    	return false;
    }

    //Checks whether MrX has no valid moves.
    private boolean MrXHasNowhereToGo() {
    	return validMoves(Colour.Black).isEmpty();
	}
    
    //Checks whether a Detective has landed on Mr. X's location.
	private boolean isMrXCaught() {
		for(PlayerInfo p: playerInfos){
			if(p.getColour() != Colour.Black){
				if(p.getLocation() == getPlayer(Colour.Black).getLocation())
					return true;
			}
		}
		return false;
	}
	
	//Checks whether all rounds are completed.
	private boolean endOfFinalRound() {
		return round>=(rounds.size()-1) && currentPlayer == Colour.Black;
 	}

	//Checks whether all Detectives can no longer move.
	private boolean allDetectivesAreStuck() {
		for(PlayerInfo p: playerInfos){
			if(p.getColour() != Colour.Black){
				if(!validMoves(p.getColour()).contains(new MovePass(p.getColour()))){
					return false;
				}	
			}
		}
		return true;
	}

	@Override
	//Checks whether the game is ready to begin.
    public boolean isReady() {
        return ((playerInfos.size() == (numberOfDetectives + 1)));
    }

    @Override
    //Returns the player who is currently playing.
    public Colour getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    //Returns the round number.
    public int getRound() {
        return round;
    }

    @Override
    //Returns a list of booleans relating to whether Mr. X reveals his location.
    public List<Boolean> getRounds() {
        return rounds;
    }
    
    //Checks where a player has joined the game.
    private boolean playerExists(Colour colour) {
    	return getPlayer(colour) != null;
    }
    
    //Returns a the player's information from a colour.
    PlayerInfo getPlayer(Colour colour) {
    	for(PlayerInfo p : playerInfos){
    		if(p.getColour() == colour)
    			return p;
    	}
    	return null;
    }
    
    //Get Mr.X's real location.
    public int getMrXLocation(){
    	return getPlayer(Colour.Black).getLocation();
    }
    
    //Returns Mr.X's possible locations.
    public Set<Integer> getMrXPossibleLocations(){
    	return mrXPossibleLocations;
    }

}