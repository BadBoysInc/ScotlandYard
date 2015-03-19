package solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scotlandyard.Colour;
import scotlandyard.Move;
import scotlandyard.MoveTicket;
import scotlandyard.Player;
import scotlandyard.Ticket;

public class GameData {

	private List<MoveTicket> moves;
	private List<PlayerInfo> players;
	
	GameData(){
		moves = new ArrayList<MoveTicket>();
		players = new ArrayList<PlayerInfo>();
	}
	
	void addPlayer(Colour c , int location, Map<Ticket, Integer> tickets){
		players.add(new PlayerInfo(c, location, new HashMap<Ticket, Integer>(tickets), null));
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
	
}
