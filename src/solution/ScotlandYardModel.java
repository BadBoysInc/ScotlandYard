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

    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException {
		super(numberOfDetectives, rounds, graphFileName);
		
		//Get the graph from the input file.
    	ScotlandYardGraphReader reader 	= new ScotlandYardGraphReader();
		graph = reader.readGraph(graphFileName);
		
		//Initialise detectives.
		this.numberOfDetectives = numberOfDetectives;
		playerinfo = new ArrayList<PlayerInfo>();
		spectators = new ArrayList<Spectator>();

		round = 0;
		currentPlayer = Colour.Black;
		
    }

    @Override
    protected Move getPlayerMove(Colour colour) {
    	return getPlayer(colour).getSPlayer().notify(getPlayerLocation(colour), validMoves(colour));
    }

    @Override
    protected void nextPlayer() {
    	int i = playerinfo.indexOf(getPlayer(currentPlayer));
    	currentPlayer = playerinfo.get((i+1)%(playerinfo.size())).getColour();
    }
    
    @Override
    protected void play(MoveTicket move) {
    	notifySpectators(move);
    	PlayerInfo player = getPlayer(move.colour);
    	player.setLocation(move.target);
    	player.removeTickets(move.ticket);
    	if(move.colour == Colour.Black){
    		round = round + 1;
    	}
    }

    @Override
    protected void play(MoveDouble move) {
    	notifySpectators(move);
    	play(move.moves.get(0));
    	play(move.moves.get(1));
    }

    @Override
    protected void play(MovePass move) {
    	notifySpectators(move);
    	if(move.colour == Colour.Black){
    		round = round + 1;
    	}
    }

    @Override
    protected List<Move> validMoves(Colour player) {
        List<Move> movesSingle = singleMoves(getPlayerLocation(player), player);
        List<Move> moves = new ArrayList<Move>(movesSingle);
        if(player == Colour.Black){
        	for(Move m: movesSingle){
        		List<Move> doubleMoves = singleMoves(((MoveTicket)  m).target, player);
        		for(Move dm: doubleMoves){
        			moves.add(new MoveDouble(player, m, dm));
        		}
        	}
        }
        moves.add(new MovePass(player));
        return moves;
    }
    
    private List<Move> singleMoves(int location, Colour player){
    	List<Move> moves = new ArrayList<Move>();
    	for(Edge<Integer, Route> e: graph.getEdges()){
        	if(e.source()==location||e.target()==location){
        		Move m = new MoveTicket(player, e.other(location), Ticket.fromRoute(e.data()));
        		moves.add(m);
        	}
        }
    	return moves;
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
    		return getPlayer(colour).getLocation();
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
        return Arrays.asList(true, false, true, false, true);
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
