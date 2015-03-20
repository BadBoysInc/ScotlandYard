package solution;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scotlandyard.Colour;

public class ReplayScreen extends JFrame {
	
	ImageIcon image;
	JLabel label;
	
	BufferedImage map;
	BufferedImage timeline;
	
	BufferedImage blackToken;
	BufferedImage whiteToken;
	BufferedImage yellowToken;
	BufferedImage blueToken;
	BufferedImage greenToken;
	BufferedImage redToken;
	
	JPanel mapPanel;
	GraphDisplay position;
	
	ArrayList<Map<Colour, Integer>> locations;
	int currentTurn;
	int selected;
	
	ReplayScreen(ArrayList<Map<Colour, Integer>> l){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		position = new GraphDisplay();
		locations = l;
		currentTurn = 0;
		selected = 0;
		
		this.setLayout(new BorderLayout());
		try {
			map = ImageIO.read(new File("resources/map.png"));
			timeline = ImageIO.read(new File("resources/timeBar.png"));
			blackToken = ImageIO.read(new File("resources/BlackToken.png"));
			whiteToken = ImageIO.read(new File("resources/WhiteToken.png"));
			yellowToken = ImageIO.read(new File("resources/YellowToken.png"));
			blueToken = ImageIO.read(new File("resources/BlueToken.png"));
			greenToken = ImageIO.read(new File("resources/GreenToken.png"));
			redToken = ImageIO.read(new File("resources/RedToken.png"));
		} catch (IOException e) {e.printStackTrace();}
		image = new ImageIcon();
		label = new JLabel();
		JPanel timePanel = new JPanel();
		timePanel.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if(x < 814 && x > 755 && y < 65 && y > 6){
					selected = 1;
				}else if(x < 67 && x > 5 && y < 65 && y > 6){
					selected = 2;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if(selected == 1 && x < 814 && x > 755 && y < 65 && y > 6){
					currentTurn = currentTurn + 1;
					drawMap();
					selected = 0;
				}else if(selected == 2 && x < 67 && x > 5 && y < 65 && y > 6){
					currentTurn = currentTurn - 1;
					drawMap();
					selected = 0;
				}else{
					selected = 0;
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
			
		});
		mapPanel = new JPanel();
		mapPanel.add(label);
		timePanel.add(new JLabel(new ImageIcon(timeline)));
		this.add(mapPanel, BorderLayout.CENTER);
		this.add(timePanel, BorderLayout.SOUTH);
		drawMap();
		
		pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		setVisible(true);
	}
	
	//Draws the standard map.
	protected void drawMap() {
		mapPanel.setVisible(false);

		try {
			map = ImageIO.read(new File("resources/map.png"));
		} catch (IOException e) {}
		Graphics2D g = map.createGraphics();
		g.drawImage(map, 0, 0, null);
		addPlayerTokens(g);

		g.dispose();
		
		image.setImage(map);
		label.setIcon(image);

		mapPanel.setVisible(true);
	}
	
	private void addPlayerTokens(Graphics2D g) {
		for (Colour c : Colour.values()) {
			if (locations.get(currentTurn).containsKey(c)) {
				switch (c.toString()) {
				case ("Black"):
					g.drawImage(blackToken,
							position.getX(locations.get(currentTurn).get(c)) - 18,
							position.getY(locations.get(currentTurn).get(c)) - 18, null);
					break;
				case ("White"):
					g.drawImage(whiteToken,
							position.getX(locations.get(currentTurn).get(c)) - 18,
							position.getY(locations.get(currentTurn).get(c)) - 18, null);
					break;
				case ("Green"):
					g.drawImage(greenToken,
							position.getX(locations.get(currentTurn).get(c)) - 18,
							position.getY(locations.get(currentTurn).get(c)) - 18, null);
					break;
				case ("Yellow"):
					g.drawImage(yellowToken,
							position.getX(locations.get(currentTurn).get(c)) - 18,
							position.getY(locations.get(currentTurn).get(c)) - 18, null);
					break;
				case ("Red"):
					g.drawImage(redToken, 
							position.getX(locations.get(currentTurn).get(c)) - 18,
							position.getY(locations.get(currentTurn).get(c)) - 18, null);
					break;
				case ("Blue"):
					g.drawImage(blueToken,
							position.getX(locations.get(currentTurn).get(c)) - 18,
							position.getY(locations.get(currentTurn).get(c)) - 18, null);
					break;
				}
			}
		}
	}
	
}
