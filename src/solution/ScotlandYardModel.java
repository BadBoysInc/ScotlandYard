package solution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scotlandyard.Colour;
import scotlandyard.Edge;
import scotlandyard.Graph;
import scotlandyard.Move;
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
	final private List<PlayerInfo> playerInfos;
	final private List<Spectator> spectators;
	
	//Game Variables:
	private int round;
	private Colour currentPlayer;
	private int MrXsLastKnownLocation;
	

    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException {
		super(numberOfDetectives, rounds, graphFileName);
		
		//Get the graph from the input file.
    	ScotlandYardGraphReader reader 	= new ScotlandYardGraphReader();
		graph = reader.readGraph(graphFileName);
		
		//Initialise game constants.
		this.rounds = rounds;
		this.numberOfDetectives = numberOfDetectives;
		
		//Initialise Observer Lists.		
		playerInfos = new ArrayList<PlayerInfo>();
		spectators = new ArrayList<Spectator>();

		//Initialise Game Variables
		MrXsLastKnownLocation = 0;
		round = 0;
		currentPlayer = Colour.Black;
		
    }

    @Override
    protected Move getPlayerMove(Colour colour) {
    	return getPlayer(colour).getSPlayer().notify(getPlayer(colour).getLocation(), validMoves(colour));
    }

    @Override
    protected void nextPlayer() {
    	int i = playerInfos.indexOf(getPlayer(currentPlayer));
    	currentPlayer = playerInfos.get((i+1)%(playerInfos.size())).getColour();
    }
    
    @Override
    protected void play(MoveTicket move) {
    	PlayerInfo player = getPlayer(move.colour);
    	if(player.getColour() != Colour.Black){
    		notifySpectators(move);
    	}else{
    		if(getRounds().get(getRound()+1) == true){
    			notifySpectators(move);
    		}else{
    			MoveTicket dummy = new MoveTicket(Colour.Black, MrXsLastKnownLocation, move.ticket);
    			notifySpectators(dummy);
    		}
    	}
    	player.setLocation(move.target);
    	player.removeTickets(move.ticket);
    	if(player.getColour() != Colour.Black){
    		getPlayer(Colour.Black).addTickets(move.ticket);
    	}
    	if(move.colour == Colour.Black){
    		round = round + 1;
    		if(getRounds().get(getRound()) == true)
        		MrXsLastKnownLocation = getPlayer(Colour.Black).getLocation();
    	}

    }

    @Override
    protected void play(MoveDouble move) {
    	//Need to check can you have a double move inside a double move????
    	
    	//If the round visibility is false hide the location of the move when telling the spectators;
    	Move dummy1, dummy2;
    	
    	if(getRounds().get(getRound()+1) == true){
    		dummy1 = move.moves.get(0);
    	}else{
    		dummy1 = new MoveTicket(Colour.Black, MrXsLastKnownLocation, ((MoveTicket) move.moves.get(0)).ticket);
    	}
    	if(getRounds().get(getRound()+2) == true){
    		dummy2 = move.moves.get(1);
    	}else{
    		dummy2 = new MoveTicket(Colour.Black, MrXsLastKnownLocation, ((MoveTicket) move.moves.get(1)).ticket);
    	}
    	
    	MoveDouble dummy  = new MoveDouble(Colour.Black, dummy1, dummy2);
    	notifySpectators(dummy);
    	
    	//Do first move;
    	PlayerInfo player = getPlayer(move.colour);
    	player.setLocation(((MoveTicket) move.moves.get(0)).target);
    	player.removeTickets(((MoveTicket) move.moves.get(0)).ticket);
    	round = round + 1;
    	if(getRounds().get(getRound()) == true){
        	MrXsLastKnownLocation = getPlayer(Colour.Black).getLocation();
    	}
    	
    	//Do second move;
    	play(move.moves.get(1));
    }

    @Override
    protected void play(MovePass move) {
    	notifySpectators(move);
    	if(move.colour == Colour.Black){
    		round = round + 1;
    		if(getRounds().get(getRound()) == true){
        		MrXsLastKnownLocation = getPlayer(Colour.Black).getLocation();
    		}
    	}
    }

    @Override
    protected List<Move> validMoves(Colour player) {
    	
        List<Move> movesSingle = singleMoves(getPlayer(player).getLocation(), player);
        List<Move> moves = new ArrayList<Move>(movesSingle);
        
        if(hasTickets(Ticket.DoubleMove, player)){
        	for(Move m: movesSingle){
        		List<Move> doubleMoves = singleMoves(((MoveTicket)  m).target, player);
        		for(Move dm: doubleMoves){
        			if((((MoveTicket) dm).ticket == ((MoveTicket) m).ticket)){
        				if(hasDoubleTickets(((MoveTicket) m).ticket, player)){
        					moves.add(new MoveDouble(player, m, dm));
        				}
        			}else if(hasTickets(((MoveTicket) dm).ticket, player)){
        				moves.add(new MoveDouble(player, m, dm));
        			}
        		}
        	}
        }
        if(moves.isEmpty() && player != Colour.Black)
        	moves.add(new MovePass(player));
        return moves;
    }
    
    private List<Move> singleMoves(int location, Colour player){
    	List<Move> moves = new ArrayList<Move>();
    	for(Edge<Integer, Route> e: graph.getEdges()){	       	
    		if(e.source()==location||e.target()==location){   			
        		Move m = new MoveTicket(player, e.other(location), Ticket.fromRoute(e.data()));
        		if(!playerPresent(e.other(location), player) && hasTickets(((MoveTicket) m).ticket, player)) 
        			if(player == Colour.Black){
        				moves.add(m);
        				if(hasTickets(Ticket.SecretMove, player)){
        					Move secretm = new MoveTicket(Colour.Black, ((MoveTicket) m).target, Ticket.SecretMove);
        					moves.add(secretm);
        				}
        			}else if(((MoveTicket) m).ticket != Ticket.SecretMove){
        				moves.add(m);
        			}
        	}
        }
    	return moves;
    }
    
    private boolean playerPresent(int location, Colour player){
    	for(PlayerInfo p: playerInfos){
    		if( (p.getLocation() == location) && (p != getPlayer(player)) && (p.getColour() != Colour.Black))
    			return true;
    	}
    	return false;
    }
    
    private boolean hasTickets(Ticket t, Colour player){
    	return (getPlayer(player).getTickets(t) > 0);
    }
    
    private boolean hasDoubleTickets(Ticket t, Colour player){
    	return (getPlayer(player).getTickets(t) >= 2);
    }
    
    @Override
    public void spectate(Spectator spectator) {
    	spectators.add(spectator);
    }
    
    private void notifySpectators(Move move){
    	for(Spectator s: spectators){
    		s.notify(move);
    	}
    }

    @Override
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
    public List<Colour> getPlayers() {
    	List<Colour> c = new ArrayList<Colour>();
    	for(PlayerInfo p : playerInfos){
    		c.add(p.getColour());
    	}
        return c;
    }

    @Override
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
    public int getPlayerTickets(Colour colour, Ticket ticket) {
    	if(playerExists(colour)){
    		return getPlayer(colour).getTickets(ticket);
    	}else{
    		return 0;
    	}
    }

    @Override
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

    private boolean MrXHasNowhereToGo() {
    	return validMoves(Colour.Black).isEmpty();
	}

	private boolean isMrXCaught() {
		for(PlayerInfo p: playerInfos){
			if(p.getColour() != Colour.Black){
				if(p.getLocation() == getPlayer(Colour.Black).getLocation())
					return true;
			}
		}
		return false;
	}

	private boolean endOfFinalRound() {
		return round>=(rounds.size()-1) && currentPlayer == Colour.Black;
 	}

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
    public boolean isReady() {
        return ((playerInfos.size() == (numberOfDetectives + 1)));
    }

    @Override
    public Colour getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public int getRound() {
        return round;
    }

    @Override
    public List<Boolean> getRounds() {
        return rounds;
    }
    
    private boolean playerExists(Colour colour){
    	return getPlayer(colour) != null;
    }
    
    private PlayerInfo getPlayer(Colour colour){
    	for(PlayerInfo p : playerInfos){
    		if(p.getColour() == colour)
    			return p;
    	}
    	return null;
    }
}
