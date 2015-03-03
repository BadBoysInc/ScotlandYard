package solution;

import scotlandyard.*;
import scotlandyard.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScotlandYardModel extends ScotlandYard {
	
	private List<PlayerInfo> playerinfo;
	private int numberOfDetectives; 

    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException {
		super(numberOfDetectives, rounds, graphFileName);
		
		//Get the graph from the input file.
    	ScotlandYardGraphReader reader 	= new ScotlandYardGraphReader();
		Graph<Integer, Route> graph 	= reader.readGraph(graphFileName);
		
		//Initialize detectives.
		this.numberOfDetectives = numberOfDetectives;
		playerinfo = new ArrayList<PlayerInfo>();
    }

    @Override
    protected Move getPlayerMove(Colour colour) {
    	
        return null;
    }

    @Override
    protected void nextPlayer() {

    }

    @Override
    protected void play(MoveTicket move) {

    }

    @Override
    protected void play(MoveDouble move) {

    }

    @Override
    protected void play(MovePass move) {

    }

    @Override
    protected List<Move> validMoves(Colour player) {
        return null;
    }

    @Override
    public void spectate(Spectator spectator) {

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
        return null;
    }

    @Override
    public int getRound() {
        return 0;
    }

    @Override
    public List<Boolean> getRounds() {
        return null;
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
