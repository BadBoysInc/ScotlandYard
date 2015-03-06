package solution;

import scotlandyard.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScotlandYardModel extends ScotlandYard {
	
	private List<PlayerInfo> playerinfo;
	private int numberOfDetectives;
	private Graph<Integer, Route> graph;
	private int round;
	private Colour currentPlayer;
	private List<Spectator> spectators;
	private int MrXsLastKnownLocation;
	private List<Boolean> rounds;

    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException {
		super(numberOfDetectives, rounds, graphFileName);
		
		//Get the graph from the input file.
    	ScotlandYardGraphReader reader 	= new ScotlandYardGraphReader();
		graph = reader.readGraph(graphFileName);
		this.rounds = rounds;
		
		//Initialise detectives.
		this.numberOfDetectives = numberOfDetectives;
		playerinfo = new ArrayList<PlayerInfo>();
		spectators = new ArrayList<Spectator>();
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
    	int i = playerinfo.indexOf(getPlayer(currentPlayer));
    	currentPlayer = playerinfo.get((i+1)%(playerinfo.size())).getColour();
    }
    
    @Override
    protected void play(MoveTicket move) {
    	PlayerInfo player = getPlayer(move.colour);
    	if(player.getColour() != Colour.Black){
    		notifySpectators(move);
    	}else{
    		MoveTicket dummy = new MoveTicket(Colour.Black, MrXsLastKnownLocation, move.ticket);
    		notifySpectators(dummy);
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
    	MoveTicket dummy1 = new MoveTicket(Colour.Black, MrXsLastKnownLocation, ((MoveTicket) move.moves.get(0)).ticket);
    	MoveTicket dummy2 = new MoveTicket(Colour.Black, MrXsLastKnownLocation, ((MoveTicket) move.moves.get(1)).ticket);
    	MoveDouble dummy  = new MoveDouble(Colour.Black, dummy1, dummy2);
    	notifySpectators(dummy);
    	PlayerInfo player = getPlayer(move.colour);
    	player.setLocation(((MoveTicket) move.moves.get(0)).target);
    	player.removeTickets(((MoveTicket) move.moves.get(0)).ticket);
    	round = round + 1;
    	if(getRounds().get(getRound()) == true){
        	MrXsLastKnownLocation = getPlayer(Colour.Black).getLocation();
    	}
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
    	for(PlayerInfo p: playerinfo){
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
    		PlayerInfo p = new PlayerInfo(colour, location, tickets, player);
    		playerinfo.add(p);
    		return true;
    	}
    }

    @Override
    public List<Colour> getPlayers() {
    	List<Colour> c = new ArrayList<Colour>();
    	for(PlayerInfo p : playerinfo){
    		c.add(p.getColour());
    	}
        return c;
    }

    @Override
    public Set<Colour> getWinningPlayers() {
        return null;
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
        return false;
    }

    @Override
    public boolean isReady() {
        return ((playerinfo.size() == (numberOfDetectives + 1)));
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
    	for(PlayerInfo p : playerinfo){
    		if(p.getColour() == colour)
    			return p;
    	}
    	return null;
    }
}
