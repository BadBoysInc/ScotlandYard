package solution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import scotlandyard.Colour;
import scotlandyard.Ticket;

public class MainScreen extends JFrame {

	JToggleButton taxi;
	JToggleButton bus;
	JToggleButton underground;
	JToggleButton secret;
	JToggleButton doublemove;

	JLabel roundStat;
	JLabel mrXStat;
	JLabel currentStat;

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
	int selected;
	int target;
	boolean firstMove;

	public MainScreen(Presenter p, Set<Colour> players) {
		// Initialise Variables
		position = new GraphDisplay();
		currentPlayer = Colour.Black;
		presenter = p;
		firstMove = true;
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//JPanels
		JPanel mainContainer 		= new JPanel();
		mapContainer 				= new JPanel();
		JPanel insideMapContainer 	= new JPanel();
		JPanel mouseContainer 		= new JPanel();
		JPanel infoContainer 		= new JPanel();
		JPanel northInfo 			= new JPanel();
		JPanel stats	 			= new JPanel();
		JPanel ticketContainer 		= new JPanel();
		JPanel southInfo 			= new JPanel();

		//Borders and Backgrounds
		mainContainer.setBackground(Color.DARK_GRAY);
		infoContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		stats.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Statistics"), new EmptyBorder(10, 10, 10, 10)));
		ticketContainer.setBorder(BorderFactory.createTitledBorder("Tickets"));
		southInfo.setBorder(BorderFactory.createBevelBorder(0));

		//Set Layouts
		mapContainer.setLayout(new BorderLayout());
		insideMapContainer.setLayout(new BorderLayout());
		mouseContainer.setLayout(new BorderLayout());
		infoContainer.setLayout(new BorderLayout());
		northInfo.setLayout(new BorderLayout());
		stats.setLayout(new GridLayout(3, 2));
		ticketContainer.setLayout(new GridLayout(5, 1));
		southInfo.setLayout(new BorderLayout());
		
		//Buttons
		JButton save  = new JButton("Save");
		JButton rules = new JButton("Rules");
		taxi 		= new JToggleButton("Taxi");
		bus 		= new JToggleButton("Bus");
		underground = new JToggleButton("Underground");
		secret 		= new JToggleButton("Secret Move");
		doublemove 	= new JToggleButton("Double Move");
		
		//Set Sizes
		infoContainer.setPreferredSize(new Dimension(270, 920));
		ticketContainer.setPreferredSize(new Dimension(300, 600));
		save.setPreferredSize(new Dimension(100, 50));
		stats.setPreferredSize(new Dimension(200, 180));
		
		insideMapContainer.add(mouseContainer, BorderLayout.CENTER);

		// Load All Images
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

			map = new JLabel();
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
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//Create Initial Map
		top = new JLabel();
		bottom = new JLabel();
		side1 = new JLabel();
		side2 = new JLabel();
		ImageIcon mainimage = new ImageIcon(imagemain);
		map.setIcon(mainimage);
		mouseContainer.add(map);
		top.setIcon(borderImages.get(Colour.Black)[0]);
		bottom.setIcon(borderImages.get(Colour.Black)[1]);
		side1.setIcon(borderImages.get(Colour.Black)[2]);
		side2.setIcon(borderImages.get(Colour.Black)[3]);
		insideMapContainer.add(top, BorderLayout.NORTH);
		insideMapContainer.add(bottom, BorderLayout.SOUTH);
		insideMapContainer.add(side1, BorderLayout.EAST);
		insideMapContainer.add(side2, BorderLayout.WEST);
		insideMapContainer.add(mouseContainer, BorderLayout.CENTER);
		ImageIcon ticketPanelIcon = new ImageIcon(ticketPanel);
		ticketPanelLabel = new JLabel(ticketPanelIcon);
		mapContainer.add(ticketPanelLabel, BorderLayout.SOUTH);

		// Add Mouse Events
		mouseContainer.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent a) {
				System.out.println("release");
				int x = a.getX();
				int y = a.getY();
				System.out.println("has coords");
				if (selected != -1) {
					if (position.getX(selected) - 15 < x
							&& x < position.getX(selected) + 15
							&& position.getY(selected) - 15 < y
							&& y < position.getY(selected) + 15) {
						if (Debug.debug) {
							System.out.println("Move choosen, sending to presenter");
						}

						Ticket ticketUsed = null;
						if (taxi.isSelected()) {
							ticketUsed = Ticket.Taxi;
						} else if (bus.isSelected()) {
							ticketUsed = Ticket.Bus;
						} else if (underground.isSelected()) {
							ticketUsed = Ticket.Underground;
						} else if (secret.isSelected()) {
							ticketUsed = Ticket.SecretMove;
						}

						taxi.setSelected(false);
						bus.setSelected(false);
						underground.setSelected(false);
						secret.setSelected(false);

						selected = 0;
						if (doublemove.isSelected() && firstMove == true) {
							System.out.println("DoubleMOVE1");
							firstMove = false;
							presenter.sendFirstMove(target, ticketUsed,
									currentPlayer);
						} else {
							firstMove = true;
							boolean doubleMove = doublemove.isSelected();
							doublemove.setSelected(false);
							presenter.sendMove(target, ticketUsed,
									currentPlayer, doubleMove);
						}

					} else {
						selected = 0;
						if (taxi.isSelected()) {
							taxiMap();
						} else if (bus.isSelected()) {
							busMap();
						} else if (underground.isSelected()) {
							undergroundMap();
						} else if (secret.isSelected()) {
							secretMap();
						}
					}
				} else {
					selected = 0;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (taxi.isSelected()) {
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
					selected = -1;
				} else if (bus.isSelected()) {
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
					selected = -1;
				} else if (underground.isSelected()) {
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
					selected = -1;
				} else if (secret.isSelected()) {
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
					selected = -1;
				} else {
					selected = -1;
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});
		mapContainer.add(insideMapContainer, BorderLayout.NORTH);

		// Quit and Rules
		
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    chooser.showSaveDialog(map);
			    
			    File file = chooser.getSelectedFile();
			    presenter.saveCurrentState(file);
			    
			}
		});

		rules.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				taxi.setSelected(false);
				bus.setSelected(false);
				underground.setSelected(false);
				secret.setSelected(false);
			}
		});


		// Statistics
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

		// Button Listeners
		taxi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bus.setSelected(false);
				underground.setSelected(false);
				secret.setSelected(false);
				if (taxi.isSelected()) {
					taxiMap();
				} else {
					mainMap();
				}
			}
		});
		bus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				taxi.setSelected(false);
				underground.setSelected(false);
				secret.setSelected(false);
				if (bus.isSelected()) {
					busMap();
				} else {
					mainMap();
				}
			}
		});
		underground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				taxi.setSelected(false);
				bus.setSelected(false);
				secret.setSelected(false);
				if (underground.isSelected()) {
					undergroundMap();
				} else {
					mainMap();
				}
			}
		});
		secret.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				taxi.setSelected(false);
				bus.setSelected(false);
				underground.setSelected(false);
				if (secret.isSelected()) {
					secretMap();
				} else {
					mainMap();
				}
			}
		});
		doublemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (doublemove.isSelected()) {
					taxi.setSelected(false);
					bus.setSelected(false);
					underground.setSelected(false);
					secret.setSelected(false);
					firstMove = true;
					mainMap();
				} else {
					taxi.setSelected(false);
					bus.setSelected(false);
					underground.setSelected(false);
					secret.setSelected(false);
					presenter.doubleMoveFalse();
				}
			}
		});

		//InfoPanel Adding
		ticketContainer.add(taxi);
		ticketContainer.add(bus);
		ticketContainer.add(underground);
		ticketContainer.add(secret);
		ticketContainer.add(doublemove);

		northInfo.add(rules, BorderLayout.NORTH);
		northInfo.add(stats, BorderLayout.SOUTH);

		southInfo.add(save, BorderLayout.CENTER);

		infoContainer.add(southInfo, BorderLayout.SOUTH);
		infoContainer.add(northInfo, BorderLayout.NORTH);
		infoContainer.add(ticketContainer, BorderLayout.CENTER);
		
		//MainPanel Adding
		mainContainer.add(mapContainer);
		mainContainer.add(infoContainer);
		
		//Finalize
		add(mainContainer);
		pack();
		setLocationByPlatform(true);
		setVisible(true);
	}

	protected void mainMap() {
		mapContainer.setVisible(false);

		try {
			imagemain = ImageIO.read(new File("resources/map.png"));
		} catch (IOException e) {
		}
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

	protected void secretMap() {
		mapContainer.setVisible(false);
		try {
			imagesecret = ImageIO.read(new File("resources/secretmap.png"));
		} catch (IOException e) {
		}
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

	protected void undergroundMap() {
		mapContainer.setVisible(false);
		try {
			imageunder = ImageIO.read(new File("resources/undergroundmap.png"));
		} catch (IOException e) {
		}
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

	protected void busMap() {
		mapContainer.setVisible(false);
		try {
			imagebus = ImageIO.read(new File("resources/busmap.png"));
		} catch (IOException e) {
		}
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

	protected void taxiMap() {
		mapContainer.setVisible(false);
		try {
			imagetaxi = ImageIO.read(new File("resources/taximap.png"));
		} catch (IOException e) {
		}
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

	private void addDoubleMoveText(Graphics2D g) {
		if (doublemove.isSelected() && firstMove) {
			g.drawImage(firstMoveText, 0, 761, null);
		} else if (doublemove.isSelected() && !firstMove) {
			g.drawImage(secondMoveText, 0, 761, null);
		}
	}

	// Called by presenter to render updates
	public void updateDisplay(Colour c, String round, String roundsUntilReveal,
			Set<Integer> taximoves, Set<Integer> busmoves,
			Set<Integer> undergroundmoves, Set<Integer> secretmoves,
			Hashtable<Colour, Integer> l, Map<Ticket, Integer> tickets) {
		if (Debug.debug) {
			System.out.println("update recieved, rendering data");
		}
		displayTicketNumbers(tickets);

		currentPlayer = c;
		taxiMoves = taximoves;
		busMoves = busmoves;
		undergroundMoves = undergroundmoves;
		secretMoves = secretmoves;
		locations = l;
		
		setButtonVisibility(tickets);
		
		System.out.println(doublemove.isSelected());
		roundStat.setText(round);
		mrXStat.setText(roundsUntilReveal);
		currentStat.setText(currentPlayer.toString());
		mainMap();
	}

	private void setButtonVisibility(Map<Ticket, Integer> tickets) {
		taxi.setEnabled(tickets.get(Ticket.Taxi) != 0);
		bus.setEnabled(tickets.get(Ticket.Bus) != 0);
		underground.setEnabled(tickets.get(Ticket.Underground) != 0);
		secret.setEnabled(tickets.get(Ticket.SecretMove) != 0);
		doublemove.setEnabled(tickets.get(Ticket.DoubleMove) != 0);
		
		if (currentPlayer != Colour.Black) {
			secret.setVisible(false);
			doublemove.setVisible(false);
		} else {
			secret.setVisible(true);
			doublemove.setVisible(true);
		}
	}

	private void displayTicketNumbers(Map<Ticket, Integer> tickets) {
		taxi.setText(String.format("Taxi (%d)", tickets.get(Ticket.Taxi)));
		bus.setText(String.format("Bus (%d)", tickets.get(Ticket.Bus)));
		underground.setText(String.format("Underground (%d)",
				tickets.get(Ticket.Underground)));
		secret.setText(String.format("Secret Move (%d)",
				tickets.get(Ticket.SecretMove)));
		doublemove.setText(String.format("Double Move (%d)",
				tickets.get(Ticket.DoubleMove)));
	}

	// NEED TO DO
	public void displayWinner(Set<Colour> winningPlayers) {
		System.out.println(winningPlayers + "  won");
		System.exit(0);
	}

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
	
}
