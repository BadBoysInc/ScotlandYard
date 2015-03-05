package solution;

import java.util.List;
import java.util.Map;

import scotlandyard.Colour;
import scotlandyard.Move;
import scotlandyard.Player;
import scotlandyard.Ticket;

public class PlayerInfo {
	
	final private Colour colour;
	private int location; 
	private Map<Ticket, Integer> tickets;
	private Player player;
		
	PlayerInfo(Colour c, int l, Map<Ticket, Integer> t, Player p){
		colour 	 = c;
		location = l;
		tickets  = t;
		player = p;
	}
	
	public void setLocation(int l){
		location = l;
	}
	
	public void removeTickets(Ticket t){
		tickets.put(t, tickets.get(t)-1);
	}
	
	public Colour getColour(){
		return colour;
	}
	
	public int getLocation(){
		return location;
	}
	
	public int getTickets(Ticket t){
		return tickets.get(t);
	}
	
	public Player getSPlayer(){
		return player;
	}
		
}
