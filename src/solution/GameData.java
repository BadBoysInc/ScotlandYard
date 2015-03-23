package solution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scotlandyard.Colour;



import scotlandyard.Ticket;

public class GameData implements Serializable{

	private ArrayList<MoveTicket> moves;
	private ArrayList<PlayerInfo> players;
	private HashSet<Colour> colours;
	private int currentTime = 0;
	
	GameData(){
		moves = new ArrayList<MoveTicket>();
		players = new ArrayList<PlayerInfo>();
		colours = new HashSet<Colour>();
	}
	
	void addPlayer(Colour c , int location, Map<Ticket, Integer> tickets){
		players.add(new PlayerInfo(c, location, new HashMap<Ticket, Integer>(tickets), null));
		colours.add(c);
	}
	
	void addMove(MoveTicket m){
		moves.add(m);
	}
	
	int getNumOfMoves(){
		return moves.size();
	}
	
	List<MoveTicket> getMoves(){
		return moves;
	}
	
	List<PlayerInfo> getPlayers(){
		return players;
	}
	
	void setTime(int t){
		currentTime = t;
	}
	
	int getTime(){
		return currentTime;
	}
	
	Set<Colour> getColours(){
		return colours;
	}
	
}
