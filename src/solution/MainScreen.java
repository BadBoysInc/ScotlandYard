package solution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
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
import javax.swing.filechooser.FileNameExtensionFilter;

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
	BufferedImage undergrondTicket;
	BufferedImage secretTicket;

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

		// MainContainer
		JPanel mainContainer = new JPanel();
		//mainContainer.setBackground(Color.DARK_GRAY);

		// RightContainer
		JPanel infoContainer = new JPanel();
		infoContainer.setLayout(new BorderLayout());
		infoContainer.setPreferredSize(new Dimension(270, 918));
		infoContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Left Container
		mapContainer = new JPanel();
		mapContainer.setLayout(new BorderLayout());
		JPanel mouseContainer = new JPanel();
		JPanel insideMapContainer = new JPanel();
		insideMapContainer.setLayout(new BorderLayout());
		mouseContainer.setLayout(new BorderLayout());

		insideMapContainer.add(mouseContainer, BorderLayout.CENTER);

		// Load All Images
		image = new ImageIcon();

		top = new JLabel();
		bottom = new JLabel();
		side1 = new JLabel();
		side2 = new JLabel();

		BufferedImage imageTop;
		BufferedImage imageBottom;
		BufferedImage imageSide;
		try {

			taxiOverlay = ImageIO.read(new File("resources/taxiMove.png"));
			busOverlay = ImageIO.read(new File("resources/busMove.png"));
			underOverlay = ImageIO.read(new File("resources/underMove.png"));
			secretOverlay = ImageIO.read(new File("resources/secretMove.png"));

			taxiSelected = ImageIO.read(new File(
					"resources/taxiMoveSelected.png"));
			busSelected = ImageIO
					.read(new File("resources/busMoveSelected.png"));
			underSelected = ImageIO.read(new File(
					"resources/underMoveSelected.png"));
			secretSelected = ImageIO.read(new File(
					"resources/secretMoveSelected.png"));

			blackToken = ImageIO.read(new File("resources/BlackToken.png"));
			whiteToken = ImageIO.read(new File("resources/WhiteToken.png"));
			yellowToken = ImageIO.read(new File("resources/YellowToken.png"));
			blueToken = ImageIO.read(new File("resources/BlueToken.png"));
			greenToken = ImageIO.read(new File("resources/GreenToken.png"));
			redToken = ImageIO.read(new File("resources/RedToken.png"));

			firstMoveText = ImageIO.read(new File(
					"resources/make-first-Move.png"));
			secondMoveText = ImageIO.read(new File(
					"resources/make-second-Move.png"));

			ticketPanel = ImageIO.read(new File("resources/TicketPanel.png"));
			taxiTicket = ImageIO.read(new File("resources/taxiTicket.png"));

			map = new JLabel();
			borderImages = new Hashtable<Colour, ImageIcon[]>();
			for (Colour c : players) {
				imageTop = ImageIO.read(new File("resources/" + c.toString()
						+ "BorderTop.png"));
				imageBottom = ImageIO.read(new File("resources/" + c.toString()
						+ "BorderBottom.png"));
				imageSide = ImageIO.read(new File("resources/" + c.toString()
						+ "BorderSide.png"));

				ImageIcon[] labels = { new ImageIcon(imageTop),
						new ImageIcon(imageBottom), new ImageIcon(imageSide),
						new ImageIcon(imageSide) };
				borderImages.put(c, labels);
			}

			// Initial Map
			imagemain = ImageIO.read(new File("resources/map.png"));
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

		} catch (IOException e1) {
			e1.printStackTrace();
		}

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
							System.out
									.println("Move choosen, sending to presenter");
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
		JButton quit = new JButton("Save");
		quit.setSize(new Dimension(400, 50));
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    chooser.showSaveDialog(map);
			}
		});

		JButton rules = new JButton("Rules");
		rules.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				taxi.setSelected(false);
				bus.setSelected(false);
				underground.setSelected(false);
				secret.setSelected(false);
			}
		});
		quit.setPreferredSize(new Dimension(100, 50));

		// Statistics
		JPanel stats = new JPanel();
		stats.setBorder(BorderFactory.createTitledBorder("Statistics"));
		stats.setLayout(new GridLayout(3, 2));
		stats.setPreferredSize(new Dimension(200, 180));

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

		// Tickets
		JPanel ticketContainer = new JPanel();
		ticketContainer.setLayout(new GridLayout(5, 1));
		ticketContainer.setPreferredSize(new Dimension(300, 600));

		taxi = new JToggleButton("Taxi");
		bus = new JToggleButton("Bus");
		underground = new JToggleButton("Underground");
		secret = new JToggleButton("Secret Move");
		doublemove = new JToggleButton("Double Move");

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

		ticketContainer.setBorder(BorderFactory.createTitledBorder("Tickets"));
		ticketContainer.add(taxi);
		ticketContainer.add(bus);
		ticketContainer.add(underground);
		ticketContainer.add(secret);
		ticketContainer.add(doublemove);

		// Adding
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
		infoContainer.add(ticketContainer, BorderLayout.CENTER);

		mainContainer.add(mapContainer);
		mainContainer.add(infoContainer);

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
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

		g.drawImage(imagesecret, 0, 0, null);

		for (int i : secretMoves) {
			g.drawImage(secretOverlay, position.getX(i) - 17,
					position.getY(i) - 17, null);
		}
		if (selected != 0) {
			g.drawImage(secretSelected, position.getX(selected) - 17,
					position.getY(selected) - 17, null);
		}

		addPlayerTokens(g);

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

		for (int i : undergroundMoves) {
			g.drawImage(underOverlay, position.getX(i) - 17,
					position.getY(i) - 17, null);
		}
		if (selected != 0) {
			g.drawImage(underSelected, position.getX(selected) - 17,
					position.getY(selected) - 17, null);
		}

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

		for (int i : busMoves) {
			g.drawImage(busOverlay, position.getX(i) - 17,
					position.getY(i) - 17, null);
		}
		if (selected != 0) {
			g.drawImage(busSelected, position.getX(selected) - 17,
					position.getY(selected) - 18, null);
		}

		addPlayerTokens(g);

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

		for (int i : taxiMoves) {
			g.drawImage(taxiOverlay, position.getX(i) - 17, position.getY(i) - 17, null);
		}
		if (selected != 0) {
			g.drawImage(taxiSelected, position.getX(selected) - 17, position.getY(selected) - 17, null);
		}

		addPlayerTokens(g);

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
					g.drawImage(redToken, position.getX(locations.get(c)) - 18,
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
		if (currentPlayer != Colour.Black) {
			secret.setVisible(false);
			doublemove.setVisible(false);
		} else {
			secret.setVisible(true);
			doublemove.setVisible(true);
		}
		System.out.println(doublemove.isSelected());
		roundStat.setText(round);
		mrXStat.setText(roundsUntilReveal);
		currentStat.setText(currentPlayer.toString());
		mainMap();
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
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
		g.drawImage(taxiTicket, 6 + (r * 45), 5, null);
		g.dispose();
		ticketPanelLabel.setIcon(new ImageIcon(ticketPanel));
		
	}

}
