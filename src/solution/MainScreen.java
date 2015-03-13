package solution;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.apache.tools.ant.taskdefs.WaitFor;

import scotlandyard.Colour;
import scotlandyard.Move;

public class MainScreen extends JFrame{
	
	JToggleButton taxi;
    JToggleButton bus;
    JToggleButton underground;
    JToggleButton secret;
    JToggleButton doublemove; 
    
    JLabel roundStat;
    JLabel mrXStat;
    JLabel currentStat;
    
    private boolean waitingForUser;
    
    Presenter presenter;
	
	public MainScreen(Presenter p){
		presenter = p;
		waitingForUser = true;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel mainContainer = new JPanel();	    
		JPanel infoContainer = new JPanel();
		JPanel mapContainer = new JPanel();
	    infoContainer.setLayout(new BorderLayout());
	    
	    //Map
	    URL u = this.getClass().getResource("map.jpg");
	    ImageIcon icon = new ImageIcon(u);
	    mapContainer.add(new JLabel(icon));
	    
	    
	    //Quit
	    JButton quit = new JButton("Quit");
	    quit.setPreferredSize(new Dimension(100, 100));
	    quit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
	    });
	    
	    //Stats
	    JPanel stats = new JPanel();
	    stats.setLayout(new GridLayout(3,2));
	    stats.setPreferredSize(new Dimension(100, 100));
	    
	    JLabel roundTitle = new JLabel("<html>Round Number:</html>");
	    JLabel mrXTitle = new JLabel("<html>Rounds Until Mr.X's Location is Revealed:</html>");
	    JLabel currentTitle = new JLabel("<html>Current Player:</html>");
	    
	    roundStat = new JLabel("####", SwingConstants.RIGHT);
	    mrXStat = new JLabel("####", SwingConstants.RIGHT);
	    currentStat = new JLabel("####", SwingConstants.RIGHT);
	    
	    stats.add(roundTitle, 0);
	    stats.add(roundStat, 1);
	    stats.add(mrXTitle, 2);
	    stats.add(mrXStat, 3);
	    stats.add(currentTitle, 4);
	    stats.add(currentStat, 5);
	    
	    //Tickets
		JPanel ticketContainer = new JPanel();
	    ticketContainer.setLayout(new GridLayout(5,1));
	    ticketContainer.setPreferredSize(new Dimension(300, 600));
	    
	    taxi = new JToggleButton("Taxi");
	    bus = new JToggleButton("Bus");
	    underground = new JToggleButton("Underground");
	    secret = new JToggleButton("Secret Move");
	    doublemove = new JToggleButton("Double Move");
	    
	    taxi.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
	    });
	    
	    ticketContainer.add(taxi);
	    ticketContainer.add(bus);
	    ticketContainer.add(underground);
	    ticketContainer.add(secret);
	    ticketContainer.add(doublemove);
	    
	    //Adding
	    infoContainer.add(quit, BorderLayout.SOUTH);
	    infoContainer.add(stats, BorderLayout.NORTH);
	    infoContainer.add(ticketContainer ,BorderLayout.CENTER);
	    
	    mainContainer.add(mapContainer);
	    mainContainer.add(infoContainer);
	    
	    add(mainContainer);
	    
	    pack();
	    setLocationByPlatform(true);
	    setVisible(true);
	}

	public Move chooseMove(List<Move> list, int location, Colour currentPlayer, int round, int roundsUntilReveal) {
		currentStat.setText(currentPlayer.toString());
		roundStat.setText(Integer.toString(round));
		
		while(waitingForUser){
			
		}
		
		return list.get(0);
	}
	
}
