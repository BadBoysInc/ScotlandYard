package solution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javafx.scene.layout.Border;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

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
    
    Hashtable<Colour, JLabel[]> borderImages;
    
    JLabel map;
    
    ImageIcon imagemain;
    ImageIcon imagetaxi;
    ImageIcon imagebus;
    ImageIcon imageunder;
    
    JPanel mapContainer;
    
    Presenter presenter;
    Colour currentPlayer;
    
	public MainScreen(Presenter p, Set<Colour> players){
		presenter = p;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel mainContainer = new JPanel();	    
		JPanel infoContainer = new JPanel();
		mapContainer = new JPanel();
	    infoContainer.setLayout(new BorderLayout());
	    infoContainer.setPreferredSize(new Dimension(200, 900));
	    
	    //Map
	    JPanel insideMapContainer = new JPanel();
	    insideMapContainer.setLayout(new BorderLayout());
	    
	    BufferedImage mainimage;
	    BufferedImage taxiimage;
	    BufferedImage busimage;
	    BufferedImage underimage;
	    BufferedImage imageTop;
	    BufferedImage imageBottom;
	    BufferedImage imageSide;

		try {
			
			mainimage = ImageIO.read(new File("resources/map.png"));
			imagemain = new ImageIcon(mainimage);
			taxiimage = ImageIO.read(new File("resources/taximap.png"));
			imagetaxi = new ImageIcon(taxiimage);
			busimage = ImageIO.read(new File("resources/busmap.png"));
			imagebus = new ImageIcon(busimage);
			underimage = ImageIO.read(new File("resources/undergroundmap.png"));
			imageunder = new ImageIcon(underimage);
			map = new JLabel(imagetaxi);
			

			borderImages = new Hashtable<Colour, JLabel[]>();
			for(Colour c: players){
				imageTop = ImageIO.read(new File("resources/" + c.toString() + "BorderTop.png"));
				imageBottom = ImageIO.read(new File("resources/" + c.toString() + "BorderBottom.png"));
				imageSide = ImageIO.read(new File("resources/" + c.toString() + "BorderSide.png"));
				
				JLabel[] labels = {new JLabel(new ImageIcon(imageTop)), new JLabel(new ImageIcon(imageBottom)),new JLabel(new ImageIcon(imageSide)),new JLabel(new ImageIcon(imageSide))};
				borderImages.put(c, labels);
			}
			
			//starting image
			insideMapContainer.add(borderImages.get(Colour.Black)[0], BorderLayout.NORTH);
			insideMapContainer.add(borderImages.get(Colour.Black)[1], BorderLayout.SOUTH);
			insideMapContainer.add(borderImages.get(Colour.Black)[2], BorderLayout.EAST);
			insideMapContainer.add(borderImages.get(Colour.Black)[3], BorderLayout.WEST);
			insideMapContainer.add(map, BorderLayout.CENTER);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		mapContainer.add(insideMapContainer);
	    
	    
	    //Quit and Rules
	    JButton quit = new JButton("Quit");
	    quit.setSize(new Dimension(400, 50));
	    quit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
	    });
	    
	    JButton rules = new JButton("Rules");
	    rules.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				taxi.setSelected(false);
				bus.setSelected(false);
				underground.setSelected(false);
				secret.setSelected(false);
				presenter.sendMove();
			}
	    });
	    quit.setPreferredSize(new Dimension(100, 50));

	    
	    //Statistics
	    JPanel stats = new JPanel();
	    stats.setBorder(BorderFactory.createTitledBorder("Statistics"));
	    stats.setLayout(new GridLayout(3,2));
	    stats.setPreferredSize(new Dimension(100, 150));
	    
	    JLabel roundTitle = new JLabel("<html>Round Number</html>");
	    JLabel mrXTitle = new JLabel("<html>Rounds Until Mr.X's Location is Revealed</html>");
	    JLabel currentTitle = new JLabel("<html>Current Player</html>");
	    
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
				bus.setSelected(false);
				underground.setSelected(false);
				secret.setSelected(false);
				if(taxi.isSelected()){
					taxiMap();
				}else{
					mainMap();
				}
			}
	    });
	    bus.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				taxi.setSelected(false);
				underground.setSelected(false);
				secret.setSelected(false);
				if(bus.isSelected()){
					busMap();
				}else{
					mainMap();
				}
			}
	    });
	    underground.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				taxi.setSelected(false);
				bus.setSelected(false);
				secret.setSelected(false);
				if(underground.isSelected()){
					undergroundMap();
				}else{
					mainMap();
				}
			}
	    });
	    
	    ticketContainer.setBorder(BorderFactory.createTitledBorder("Tickets"));
	    ticketContainer.add(taxi);
	    ticketContainer.add(bus);
	    ticketContainer.add(underground);
	    ticketContainer.add(secret);
	    ticketContainer.add(doublemove);
	    
	    //Adding
	    JPanel north = new JPanel();
	    north.setLayout(new BorderLayout());
	    north.add(rules, BorderLayout.NORTH);
	    north.add(stats, BorderLayout.SOUTH);

	    JPanel south = new JPanel();
	    south.setLayout(new BorderLayout());
	    south.setBorder(BorderFactory.createBevelBorder(0));
	    south.add(quit, BorderLayout.CENTER);
	    
	    infoContainer.add(south, BorderLayout.SOUTH);
	    infoContainer.add(north, BorderLayout.NORTH);
	    infoContainer.add(ticketContainer ,BorderLayout.CENTER);
	    
	    mainContainer.add(mapContainer);
	    mainContainer.add(infoContainer);
	    
	    add(mainContainer);
	    
	    pack();
	    setLocationByPlatform(true);
	    setVisible(true);

	}

	protected void mainMap() {
		mapContainer.setVisible(false);
		map.setIcon(imagemain);
		JPanel insideMapContainer = new JPanel();
		insideMapContainer.setLayout(new BorderLayout());
		insideMapContainer.add(borderImages.get(currentPlayer)[0], BorderLayout.NORTH);
		insideMapContainer.add(borderImages.get(currentPlayer)[1], BorderLayout.SOUTH);
		insideMapContainer.add(borderImages.get(currentPlayer)[2], BorderLayout.EAST);
		insideMapContainer.add(borderImages.get(currentPlayer)[3], BorderLayout.WEST);
		insideMapContainer.add(map, BorderLayout.CENTER);
		mapContainer.removeAll();
		mapContainer.add(insideMapContainer);
		mapContainer.setVisible(true);
	}

	protected void undergroundMap() {
		mapContainer.setVisible(false);
		map.setIcon(imageunder);
		JPanel insideMapContainer = new JPanel();
		insideMapContainer.setLayout(new BorderLayout());
		insideMapContainer.add(borderImages.get(currentPlayer)[0], BorderLayout.NORTH);
		insideMapContainer.add(borderImages.get(currentPlayer)[1], BorderLayout.SOUTH);
		insideMapContainer.add(borderImages.get(currentPlayer)[2], BorderLayout.EAST);
		insideMapContainer.add(borderImages.get(currentPlayer)[3], BorderLayout.WEST);
		insideMapContainer.add(map, BorderLayout.CENTER);
		mapContainer.removeAll();
		mapContainer.add(insideMapContainer);
		mapContainer.setVisible(true);
	}

	protected void busMap() {
		mapContainer.setVisible(false);
		map.setIcon(imagebus);
		JPanel insideMapContainer = new JPanel();
		insideMapContainer.setLayout(new BorderLayout());
		insideMapContainer.add(borderImages.get(currentPlayer)[0], BorderLayout.NORTH);
		insideMapContainer.add(borderImages.get(currentPlayer)[1], BorderLayout.SOUTH);
		insideMapContainer.add(borderImages.get(currentPlayer)[2], BorderLayout.EAST);
		insideMapContainer.add(borderImages.get(currentPlayer)[3], BorderLayout.WEST);
		insideMapContainer.add(map, BorderLayout.CENTER);
		mapContainer.removeAll();
		mapContainer.add(insideMapContainer);
		mapContainer.setVisible(true);
	}

	protected void taxiMap() {
		mapContainer.setVisible(false);
		map.setIcon(imagetaxi);
		JPanel insideMapContainer = new JPanel();
		insideMapContainer.setLayout(new BorderLayout());
		insideMapContainer.add(borderImages.get(currentPlayer)[0], BorderLayout.NORTH);
		insideMapContainer.add(borderImages.get(currentPlayer)[1], BorderLayout.SOUTH);
		insideMapContainer.add(borderImages.get(currentPlayer)[2], BorderLayout.EAST);
		insideMapContainer.add(borderImages.get(currentPlayer)[3], BorderLayout.WEST);
		insideMapContainer.add(map, BorderLayout.CENTER);
		mapContainer.removeAll();
		mapContainer.add(insideMapContainer);
		mapContainer.setVisible(true);
	}

	public void updateDisplay(Colour c, String round, String roundsUntilReveal) {
		currentPlayer = c;
		roundStat.setText(round);
	    mrXStat.setText(roundsUntilReveal);
	    currentStat.setText(currentPlayer.toString());	    
	    mainMap();
	}
	
}
