package solution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.ImageView;

import scotlandyard.Colour;
import scotlandyard.Ticket;

public class MainScreen extends JFrame {

	JToggleButton taxiButton;
	JToggleButton busButton;
	JToggleButton undergroundButton;
	JToggleButton secretButton;
	JToggleButton doublemoveButton;

	JLabel roundStat;
	JLabel mrXStat;
	JLabel currentStat;
	JLabel roundsLeftStat;
	JLabel timerStat;
	
	int currentTime = 0;

	Hashtable<Colour, ImageIcon[]> borderImages;

	JLabel map;
	JLabel top;
	JLabel bottom;
	JLabel side1;
	JLabel side2;
	JLabel ticketPanelLabel;

	BufferedImage imagemain;
	BufferedImage imagetaxi;
	BufferedImage imagebus;
	BufferedImage imageunder;
	BufferedImage imagesecret;

	BufferedImage taxiOverlay;
	BufferedImage busOverlay;
	BufferedImage underOverlay;
	BufferedImage secretOverlay;

	BufferedImage taxiSelected;
	BufferedImage busSelected;
	BufferedImage underSelected;
	BufferedImage secretSelected;

	BufferedImage blackToken;
	BufferedImage whiteToken;
	BufferedImage yellowToken;
	BufferedImage blueToken;
	BufferedImage greenToken;
	BufferedImage redToken;

	BufferedImage firstMoveText;
	BufferedImage secondMoveText;

	BufferedImage ticketPanel;
	BufferedImage taxiTicket;
	BufferedImage busTicket;
	BufferedImage undergroundTicket;
	BufferedImage secretTicket;
	
	BufferedImage undergroundZones;
	BufferedImage busZones;
	BufferedImage allZones;
	
	ImageIcon image;
	JPanel mapContainer;

	Presenter presenter;
	GraphDisplay position;
	Colour currentPlayer;
	Set<Integer> taxiMoves;
	Set<Integer> busMoves;
	Set<Integer> undergroundMoves;
	Set<Integer> secretMoves;
	Map<Colour, Integer> locations;
	Map<Ticket, Integer> tickets;
	int selected;
	int target;
	boolean firstMove;
	boolean rulesOpen;
	boolean waiting;

	public MainScreen(Presenter p, Set<Colour> players) {
		
		//Initialise Variables
		position = new GraphDisplay();
		currentPlayer = Colour.Black;
		presenter = p;
		firstMove = true;
		rulesOpen = false;
		waiting = true;
		final MainScreen m = this;
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//JPanels
		JPanel mainContainer 		= new JPanel();
		mapContainer 				= new JPanel();
		JPanel insideMapContainer 	= new JPanel();
		JPanel mouseContainer 		= new JPanel();
		JPanel infoContainer 		= new JPanel();
		JPanel northInfoContainer 	= new JPanel();
		JPanel statsContainer	 	= new JPanel();
		JPanel ticketContainer 		= new JPanel();
		JPanel southInfoContainer 	= new JPanel();
		
		//Borders and Backgrounds
		mainContainer.setBackground(Color.DARK_GRAY);
		infoContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		statsContainer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Statistics"), new EmptyBorder(2, 10, 2, 10)));
		ticketContainer.setBorder(BorderFactory.createTitledBorder("Tickets"));
		southInfoContainer.setBorder(BorderFactory.createBevelBorder(0));

		//Set Layouts
		mapContainer.setLayout(new BorderLayout());
		insideMapContainer.setLayout(new BorderLayout());
		mouseContainer.setLayout(new BorderLayout());
		infoContainer.setLayout(new BorderLayout());
		northInfoContainer.setLayout(new BorderLayout());
		statsContainer.setLayout(new GridLayout(5, 2));
		ticketContainer.setLayout(new GridLayout(5, 1));
		southInfoContainer.setLayout(new BorderLayout());
		
		//Set JLabels
		map 	= new JLabel();
		top 	= new JLabel();
		bottom 	= new JLabel();
		side1 	= new JLabel();
		side2 	= new JLabel();
		
		//Load All Images
		image = new ImageIcon();
		BufferedImage imageTop;
		BufferedImage imageBottom;
		BufferedImage imageSide;
		try {
			imagemain = ImageIO.read(new File("resources/map.png"));
			
			taxiOverlay = ImageIO.read(new File("resources/taxiMove.png"));
			busOverlay = ImageIO.read(new File("resources/busMove.png"));
			underOverlay = ImageIO.read(new File("resources/underMove.png"));
			secretOverlay = ImageIO.read(new File("resources/secretMove.png"));

			taxiSelected = ImageIO.read(new File("resources/taxiMoveSelected.png"));
			busSelected = ImageIO.read(new File("resources/busMoveSelected.png"));
			underSelected = ImageIO.read(new File("resources/underMoveSelected.png"));
			secretSelected = ImageIO.read(new File("resources/secretMoveSelected.png"));

			blackToken = ImageIO.read(new File("resources/BlackToken.png"));
			whiteToken = ImageIO.read(new File("resources/WhiteToken.png"));
			yellowToken = ImageIO.read(new File("resources/YellowToken.png"));
			blueToken = ImageIO.read(new File("resources/BlueToken.png"));
			greenToken = ImageIO.read(new File("resources/GreenToken.png"));
			redToken = ImageIO.read(new File("resources/RedToken.png"));

			firstMoveText = ImageIO.read(new File("resources/make-first-Move.png"));
			secondMoveText = ImageIO.read(new File("resources/make-second-Move.png"));

			ticketPanel = ImageIO.read(new File("resources/TicketPanel.png"));
			taxiTicket = ImageIO.read(new File("resources/taxiTicket.png"));
			busTicket = ImageIO.read(new File("resources/busTicket.png"));
			undergroundTicket = ImageIO.read(new File("resources/underTicket.png"));
			secretTicket = ImageIO.read(new File("resources/secretTicket.png"));
			
			undergroundZones = ImageIO.read(new File("resources/undergroundZones.png"));
			busZones = ImageIO.read(new File("resources/busZones.png"));
			allZones = ImageIO.read(new File("resources/allZones.png"));
	
			borderImages = new Hashtable<Colour, ImageIcon[]>();
			for (Colour c : players) {
				imageTop = ImageIO.read(new File("resources/" + c.toString() + "BorderTop.png"));
				imageBottom = ImageIO.read(new File("resources/" + c.toString() + "BorderBottom.png"));
				imageSide = ImageIO.read(new File("resources/" + c.toString() + "BorderSide.png"));
				ImageIcon[] labels = { new ImageIcon(imageTop),
						new ImageIcon(imageBottom), new ImageIcon(imageSide),
						new ImageIcon(imageSide) };
				borderImages.put(c, labels);
			}
		} catch (IOException e1) {e1.printStackTrace();}

		//Buttons
		JButton saveButton  = new JButton("Save");
		JButton rulesButton = new JButton("Rules");
		taxiButton 			= new JToggleButton("Taxi");		
		busButton 			= new JToggleButton("Bus");		
		undergroundButton 	= new JToggleButton("Underground");		
		secretButton 		= new JToggleButton("Secret Move");
		doublemoveButton 	= new JToggleButton("Double Move");
		
		//Set Button Icons
		taxiButton.setIcon(new ImageIcon(taxiTicket));
		taxiButton.setIconTextGap(50);		
		busButton.setIcon(new ImageIcon(busTicket));
		busButton.setIconTextGap(50);
		undergroundButton.setIcon(new ImageIcon(undergroundTicket));
		undergroundButton.setIconTextGap(50);
		secretButton.setIcon(new ImageIcon(secretTicket));
		secretButton.setIconTextGap(50);
		doublemoveButton.setIcon(new ImageIcon(taxiTicket));
		doublemoveButton.setIconTextGap(50);
		
		//Set Sizes
		infoContainer.setPreferredSize(new Dimension(270, 920));
		ticketContainer.setPreferredSize(new Dimension(300, 600));
		saveButton.setPreferredSize(new Dimension(100, 50));
		statsContainer.setPreferredSize(new Dimension(200, 180));

		//Add Mouse Events
		mouseContainer.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent a) {
				int x = a.getX();
				int y = a.getY();
				
				if (selected > 0) {
					if (position.getX(selected) - 15 < x && 
						x < position.getX(selected) + 15 && 
						position.getY(selected) - 15 < y && 
						y < position.getY(selected) + 15) {
						
						if (Debug.debug) {
							System.out.println("Move choosen, sending to presenter");
						}

						Ticket ticketUsed = null;
						if (taxiButton.isSelected()) {
							ticketUsed = Ticket.Taxi;
						} else if (busButton.isSelected()) {
							ticketUsed = Ticket.Bus;
						} else if (undergroundButton.isSelected()) {
							ticketUsed = Ticket.Underground;
						} else if (secretButton.isSelected()) {
							ticketUsed = Ticket.SecretMove;
						}

						taxiButton.setSelected(false);
						busButton.setSelected(false);
						undergroundButton.setSelected(false);
						secretButton.setSelected(false);

						selected = 0;
						
						if (doublemoveButton.isSelected() && firstMove == true) {
							System.out.println("DoubleMOVE1");
							firstMove = false;
							presenter.sendFirstMove(target, ticketUsed, currentPlayer);
						} else {
							firstMove = true;
							boolean doubleMove = doublemoveButton.isSelected();
							doublemoveButton.setSelected(false);
							presenter.sendMove(target, ticketUsed, currentPlayer, doubleMove);
						}

					}else{
						selected = 0;
						if (taxiButton.isSelected()) {
							taxiMap();
						} else if (busButton.isSelected()) {
							busMap();
						} else if (undergroundButton.isSelected()) {
							undergroundMap();
						} else if (secretButton.isSelected()) {
							secretMap();
						}	
					}
				}else if(selected == -2){
					if(x < 699 && x > 319 && y < 550 && y > 450){
						waiting = false;
						setButtonVisibility(tickets);
						mainMap();
						selected = 0;
					}else{
						selected = 0;
					}
				} else {
					selected = 0;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if(waiting == true){
					if(x < 699 && x > 319 && y < 550 && y > 450)
						selected = -2;
				} else if (taxiButton.isSelected()) {
					for (int i : taxiMoves) {
						if (position.getX(i) - 15 < x
								&& x < position.getX(i) + 15
								&& position.getY(i) - 15 < y
								&& y < position.getY(i) + 15) {
							selected = i;
							target = selected;
							taxiMap();
							return;
						}
					}
					selected = 0;
				} else if (busButton.isSelected()) {
					for (int i : busMoves) {
						if (position.getX(i) - 15 < x
								&& x < position.getX(i) + 15
								&& position.getY(i) - 15 < y
								&& y < position.getY(i) + 15) {
							selected = i;
							target = selected;
							busMap();
							return;
						}
					}
					selected = 0;
				} else if (undergroundButton.isSelected()) {
					for (int i : undergroundMoves) {
						if (position.getX(i) - 15 < x
								&& x < position.getX(i) + 15
								&& position.getY(i) - 15 < y
								&& y < position.getY(i) + 15) {
							selected = i;
							target = selected;
							undergroundMap();
							return;
						}
					}
					selected = 0;
				} else if (secretButton.isSelected()) {
					for (int i : secretMoves) {
						if (position.getX(i) - 15 < x
								&& x < position.getX(i) + 15
								&& position.getY(i) - 15 < y
								&& y < position.getY(i) + 15) {
							selected = i;
							target = selected;
							secretMap();
							return;
						}
					}
					selected = 0;
				} else {
					selected = 0;
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});

		//Statistics
		JLabel roundTitle = new JLabel("<html>Round Number</html>");
		JLabel mrXTitle = new JLabel("<html>Rounds Until Mr.X Reveals</html>");
		JLabel currentTitle = new JLabel("<html>Current Player</html>");
		JLabel roundsLeftTitle = new JLabel("<html>Rounds Left</html>");
		JLabel timerTitle = new JLabel("<html>Time</html>");
		roundStat = new JLabel("####", SwingConstants.RIGHT);
		mrXStat = new JLabel("####", SwingConstants.RIGHT);
		currentStat = new JLabel("####", SwingConstants.RIGHT);
		roundsLeftStat = new JLabel("####", SwingConstants.RIGHT);
		timerStat = new JLabel("####", SwingConstants.RIGHT);
		statsContainer.add(roundTitle, 0);
		statsContainer.add(roundStat, 1);
		statsContainer.add(mrXTitle, 2);
		statsContainer.add(mrXStat, 3);
		statsContainer.add(currentTitle, 4);
		statsContainer.add(currentStat, 5);
		statsContainer.add(roundsLeftTitle, 6);
		statsContainer.add(roundsLeftStat, 7);
		statsContainer.add(timerTitle, 8);
		statsContainer.add(timerStat, 9);
		
		//Implement Timer
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				
				int seconds = currentTime%60;
				int minutes = currentTime/60;
								
				timerStat.setText(String.format("%02d:%02ds",minutes, seconds));
				currentTime++;
			}
			
		}, 0, 1000);

		//Button Listeners
		taxiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				busButton.setSelected(false);
				undergroundButton.setSelected(false);
				secretButton.setSelected(false);
				if (taxiButton.isSelected()) {
					taxiMap();
				} else {
					mainMap();
				}
			}
		});
		
		busButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				taxiButton.setSelected(false);
				undergroundButton.setSelected(false);
				secretButton.setSelected(false);
				if (busButton.isSelected()) {
					busMap();
				} else {
					mainMap();
				}
			}
		});
		
		undergroundButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				taxiButton.setSelected(false);
				busButton.setSelected(false);
				secretButton.setSelected(false);
				if (undergroundButton.isSelected()) {
					undergroundMap();
				} else {
					mainMap();
				}
			}
		});
		
		secretButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				taxiButton.setSelected(false);
				busButton.setSelected(false);
				undergroundButton.setSelected(false);
				if (secretButton.isSelected()) {
					secretMap();
				} else {
					mainMap();
				}
			}
		});
		
		doublemoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (doublemoveButton.isSelected()) {
					taxiButton.setSelected(false);
					busButton.setSelected(false);
					undergroundButton.setSelected(false);
					secretButton.setSelected(false);
					firstMove = true;
					mainMap();
				} else {
					taxiButton.setSelected(false);
					busButton.setSelected(false);
					undergroundButton.setSelected(false);
					secretButton.setSelected(false);
					presenter.doubleMoveFalse();
				}
			}
		});
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    chooser.showSaveDialog(map);
			    
			    File file = chooser.getSelectedFile();
			    presenter.saveCurrentState(file);
			    
			}
		});
		
		rulesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!rulesOpen){
					rulesOpen = true;
					RulesScreen rulesScreen = new RulesScreen(m);
				}
			}
		});
		
		//MapPanel Adding
		insideMapContainer.add(mouseContainer, BorderLayout.CENTER);
		mouseContainer.add(map);
		insideMapContainer.add(top, BorderLayout.NORTH);
		insideMapContainer.add(bottom, BorderLayout.SOUTH);
		insideMapContainer.add(side1, BorderLayout.EAST);
		insideMapContainer.add(side2, BorderLayout.WEST);
		insideMapContainer.add(mouseContainer, BorderLayout.CENTER);
		ImageIcon ticketPanelIcon = new ImageIcon(ticketPanel);
		ticketPanelLabel = new JLabel(ticketPanelIcon);
		mapContainer.add(ticketPanelLabel, BorderLayout.SOUTH);
		mapContainer.add(insideMapContainer, BorderLayout.NORTH);
		
		//InfoPanel Adding
		ticketContainer.add(taxiButton);
		ticketContainer.add(busButton);
		ticketContainer.add(undergroundButton);
		ticketContainer.add(secretButton);
		ticketContainer.add(doublemoveButton);
		northInfoContainer.add(rulesButton, BorderLayout.NORTH);
		northInfoContainer.add(statsContainer, BorderLayout.SOUTH);
		southInfoContainer.add(saveButton, BorderLayout.CENTER);
		infoContainer.add(southInfoContainer, BorderLayout.SOUTH);
		infoContainer.add(northInfoContainer, BorderLayout.NORTH);
		infoContainer.add(ticketContainer, BorderLayout.CENTER);
		
		//MainPanel Adding
		mainContainer.add(mapContainer);
		mainContainer.add(infoContainer);
		
		//Finalisation
		add(mainContainer);
		pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		setVisible(true);
	}
	
	//Draws the standard map.
	protected void mainMap() {
		mapContainer.setVisible(false);

		try {
			imagemain = ImageIO.read(new File("resources/map.png"));
		} catch (IOException e) {}
		Graphics2D g = imagemain.createGraphics();
		g.drawImage(imagemain, 0, 0, null);
		addPlayerTokens(g);
		addDoubleMoveText(g);
		g.dispose();
		
		image.setImage(imagemain);
		map.setIcon(image);
		top.setIcon(borderImages.get(currentPlayer)[0]);
		bottom.setIcon(borderImages.get(currentPlayer)[1]);
		side1.setIcon(borderImages.get(currentPlayer)[2]);
		side2.setIcon(borderImages.get(currentPlayer)[3]);

		mapContainer.setVisible(true);
	}
	
	
	//Draws the map for secret routes.
	protected void secretMap() {
		mapContainer.setVisible(false);
		
		try {
			imagesecret = ImageIO.read(new File("resources/secretmap.png"));
		} catch (IOException e) {}
		Graphics2D g = imagesecret.createGraphics();
		g.drawImage(imagesecret, 0, 0, null);
		addPlayerTokens(g);		
		for (int i : secretMoves) {
			g.drawImage(secretOverlay, position.getX(i) - 17,
					position.getY(i) - 17, null);
		}
		if (selected != 0) {
			g.drawImage(secretSelected, position.getX(selected) - 17,
					position.getY(selected) - 17, null);
		}	
		g.drawImage(allZones, 0, 0, null);		
		addDoubleMoveText(g);		
		g.dispose();
		
		image.setImage(imagesecret);
		map.setIcon(image);
		top.setIcon(borderImages.get(currentPlayer)[0]);
		bottom.setIcon(borderImages.get(currentPlayer)[1]);
		side1.setIcon(borderImages.get(currentPlayer)[2]);
		side2.setIcon(borderImages.get(currentPlayer)[3]);

		mapContainer.setVisible(true);
	}

	//Draws the map for underground routes.
	protected void undergroundMap() {
		mapContainer.setVisible(false);
		
		try {
			imageunder = ImageIO.read(new File("resources/undergroundmap.png"));
		} catch (IOException e) {}
		Graphics2D g = imageunder.createGraphics();
		g.drawImage(imageunder, 0, 0, null);
		addPlayerTokens(g);	
		for (int i : undergroundMoves) {
			g.drawImage(underOverlay, position.getX(i) - 17,
					position.getY(i) - 17, null);
		}
		if (selected != 0) {
			g.drawImage(underSelected, position.getX(selected) - 17,
					position.getY(selected) - 17, null);
		}
		g.drawImage(undergroundZones, 0, 0, null);
		addPlayerTokens(g);	
		addDoubleMoveText(g);
		g.dispose();
		
		image.setImage(imageunder);
		map.setIcon(image);
		top.setIcon(borderImages.get(currentPlayer)[0]);
		bottom.setIcon(borderImages.get(currentPlayer)[1]);
		side1.setIcon(borderImages.get(currentPlayer)[2]);
		side2.setIcon(borderImages.get(currentPlayer)[3]);

		mapContainer.setVisible(true);
	}
	
	
	//Draws the map for bus routes.
	protected void busMap() {
		mapContainer.setVisible(false);
		
		try {
			imagebus = ImageIO.read(new File("resources/busmap.png"));
		} catch (IOException e) {}
		Graphics2D g = imagebus.createGraphics();
		g.drawImage(imagebus, 0, 0, null);
		addPlayerTokens(g);
		for (int i : busMoves) {
			g.drawImage(busOverlay, position.getX(i) - 17,
					position.getY(i) - 17, null);
		}
		if (selected != 0) {
			g.drawImage(busSelected, position.getX(selected) - 17,
					position.getY(selected) - 18, null);
		}
		g.drawImage(busZones, 0, 0, null);		
		addDoubleMoveText(g);
		g.dispose();
		
		image.setImage(imagebus);
		map.setIcon(image);
		top.setIcon(borderImages.get(currentPlayer)[0]);
		bottom.setIcon(borderImages.get(currentPlayer)[1]);
		side1.setIcon(borderImages.get(currentPlayer)[2]);
		side2.setIcon(borderImages.get(currentPlayer)[3]);

		mapContainer.setVisible(true);
	}
	
	
	//Draws the map for taxi routes.
	protected void taxiMap() {
		mapContainer.setVisible(false);
		
		try {
			imagetaxi = ImageIO.read(new File("resources/taximap.png"));
		} catch (IOException e) {}
		Graphics2D g = imagetaxi.createGraphics();
		g.drawImage(imagetaxi, 0, 0, null);		
		addPlayerTokens(g);
		for (int i : taxiMoves) {
			g.drawImage(taxiOverlay, position.getX(i) - 17, position.getY(i) - 17, null);
		}
		if (selected != 0) {
			g.drawImage(taxiSelected, position.getX(selected) - 17, position.getY(selected) - 17, null);
		}
		g.drawImage(allZones, 0, 0, null);	
		addDoubleMoveText(g);
		g.dispose();
		
		image.setImage(imagetaxi);
		map.setIcon(image);
		top.setIcon(borderImages.get(currentPlayer)[0]);
		bottom.setIcon(borderImages.get(currentPlayer)[1]);
		side1.setIcon(borderImages.get(currentPlayer)[2]);
		side2.setIcon(borderImages.get(currentPlayer)[3]);

		mapContainer.setVisible(true);
	}
	
	//Draws the player confirmation image.
	protected void nextTurnMap() {
		mapContainer.setVisible(false);
		
		BufferedImage nextTurn = null;
		try {
			nextTurn = ImageIO.read(new File("resources/nextTurnScreen.png"));
		} catch (IOException e) {}
		image.setImage(nextTurn);
		map.setIcon(image);
		top.setIcon(borderImages.get(currentPlayer)[0]);
		bottom.setIcon(borderImages.get(currentPlayer)[1]);
		side1.setIcon(borderImages.get(currentPlayer)[2]);
		side2.setIcon(borderImages.get(currentPlayer)[3]);

		mapContainer.setVisible(true);
	}
	
	//Draws the player tokens on their location.
	private void addPlayerTokens(Graphics2D g) {
		for (Colour c : Colour.values()) {
			if (locations.containsKey(c)) {
				switch (c.toString()) {
				case ("Black"):
					g.drawImage(blackToken,
							position.getX(locations.get(c)) - 18,
							position.getY(locations.get(c)) - 18, null);
					break;
				case ("White"):
					g.drawImage(whiteToken,
							position.getX(locations.get(c)) - 18,
							position.getY(locations.get(c)) - 18, null);
					break;
				case ("Green"):
					g.drawImage(greenToken,
							position.getX(locations.get(c)) - 18,
							position.getY(locations.get(c)) - 18, null);
					break;
				case ("Yellow"):
					g.drawImage(yellowToken,
							position.getX(locations.get(c)) - 18,
							position.getY(locations.get(c)) - 18, null);
					break;
				case ("Red"):
					g.drawImage(redToken, 
							position.getX(locations.get(c)) - 18,
							position.getY(locations.get(c)) - 18, null);
					break;
				case ("Blue"):
					g.drawImage(blueToken,
							position.getX(locations.get(c)) - 18,
							position.getY(locations.get(c)) - 18, null);
					break;
				}
			}
		}
	}
	
	//Adds text telling player to make first/second move in a double move.
	private void addDoubleMoveText(Graphics2D g) {
		if (doublemoveButton.isSelected() && firstMove) {
			g.drawImage(firstMoveText, 0, 761, null);
		} else if (doublemoveButton.isSelected() && !firstMove) {
			g.drawImage(secondMoveText, 0, 761, null);
		}
	}

	//Called by Presenter to Update the GUI.
	public void updateDisplay(Colour c, String round, String roundsUntilReveal, String roundLeft,
			Set<Integer> tm, Set<Integer> bm,
			Set<Integer> um, Set<Integer> sm,
			Hashtable<Colour, Integer> l, Map<Ticket, Integer> t) {
		
		if (Debug.debug) {
			System.out.println("update recieved, rendering data");
		}
		
		//Update GUI Variables.
		currentPlayer  	 = c;
		taxiMoves 		 = tm;
		busMoves 		 = bm;
		undergroundMoves = um;
		secretMoves 	 = sm;
		locations 	 	 = l;
		tickets 		 = t;
		
		//Update Ticket Numbers and Hide Buttons.
		displayTicketNumbers(tickets);
		hideButtons();
		
		//Update Statistics Text.
		roundStat.setText(round);
		mrXStat.setText(roundsUntilReveal);
		currentStat.setText(currentPlayer.toString());
		roundsLeftStat.setText(roundLeft);
		
		//Set the state to waiting for player to confirm he is ready.
		nextTurnMap();
		if(firstMove == true){
			waiting = true;
		}else{
			mainMap();
			setButtonVisibility(tickets);
		}
	}
	
	//Set Button Visibility for Players.
	private void setButtonVisibility(Map<Ticket, Integer> tickets) {
		taxiButton.setEnabled(tickets.get(Ticket.Taxi) != 0);
		busButton.setEnabled(tickets.get(Ticket.Bus) != 0);
		undergroundButton.setEnabled(tickets.get(Ticket.Underground) != 0);
		secretButton.setEnabled(tickets.get(Ticket.SecretMove) != 0);
		doublemoveButton.setEnabled(tickets.get(Ticket.DoubleMove) != 0);
		if (currentPlayer != Colour.Black) {
			secretButton.setVisible(false);
			doublemoveButton.setVisible(false);
		} else {
			secretButton.setVisible(true);
			doublemoveButton.setVisible(true);
		}
	}
	
	//Hide all Buttons.
	private void hideButtons() {
		taxiButton.setEnabled(false);
		busButton.setEnabled(false);
		undergroundButton.setEnabled(false);
		secretButton.setEnabled(false);
		doublemoveButton.setEnabled(false);
	}
	
	//Display the Ticket Number for the Player.
	private void displayTicketNumbers(Map<Ticket, Integer> tickets) {
		taxiButton.setText(String.format("Taxi (%d)", tickets.get(Ticket.Taxi)));
		busButton.setText(String.format("Bus (%d)", tickets.get(Ticket.Bus)));
		undergroundButton.setText(String.format("Underground (%d)",
				tickets.get(Ticket.Underground)));
		secretButton.setText(String.format("Secret Move (%d)",
				tickets.get(Ticket.SecretMove)));
		doublemoveButton.setText(String.format("Double Move (%d)",
				tickets.get(Ticket.DoubleMove)));
	}
	
	
	//Draw Mr.X's last used ticket.
	public void updateTicketPanel(Ticket ticket, int r) {
		Graphics2D g = ticketPanel.createGraphics();
		switch (ticket){
		case Taxi:
			g.drawImage(taxiTicket, 8 + (r * 44), 5, null);
			break;
		case Bus:
			g.drawImage(busTicket, 8 + (r * 44), 5, null);
			break;
		case Underground:
			g.drawImage(undergroundTicket, 8 + (r * 44), 5, null);
			break;
		case SecretMove:
			g.drawImage(secretTicket, 8 + (r * 44), 5, null);
			break;
		default:
			break;
		}
		g.dispose();
		ticketPanelLabel.setIcon(new ImageIcon(ticketPanel));
	}
	
	//Rules Screen notifies the Main Screen it's been closed.
	public void rulesClosed() {
		rulesOpen = false;	
	}

}
