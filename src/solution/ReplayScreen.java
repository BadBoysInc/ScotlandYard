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
	
	ImageIcon mapImage;
	ImageIcon timeImage;
	JLabel maplabel;
	JLabel timelabel;
	
	BufferedImage map;
	BufferedImage timeline;
	BufferedImage timePointer;
	
	BufferedImage blackToken;
	BufferedImage whiteToken;
	BufferedImage yellowToken;
	BufferedImage blueToken;
	BufferedImage greenToken;
	BufferedImage redToken;
	
	JPanel mapPanel;
	JPanel timePanel;
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
			timePointer = ImageIO.read(new File("resources/timePointer.png"));
			blackToken = ImageIO.read(new File("resources/BlackToken.png"));
			whiteToken = ImageIO.read(new File("resources/WhiteToken.png"));
			yellowToken = ImageIO.read(new File("resources/YellowToken.png"));
			blueToken = ImageIO.read(new File("resources/BlueToken.png"));
			greenToken = ImageIO.read(new File("resources/GreenToken.png"));
			redToken = ImageIO.read(new File("resources/RedToken.png"));
		} catch (IOException e) {e.printStackTrace();}
		mapImage = new ImageIcon();
		timeImage = new ImageIcon();
		
		maplabel = new JLabel();
		timelabel = new JLabel();
		
		timePanel = new JPanel();
		timePanel.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if(x < (954+60) && x > 954 && y < (57+7) && y > 7){
					selected = 1;
					drawMap();
				}else if(x < 60 && x > 10 && y < (57+7) && y > 7){
					selected = 2;
					drawMap();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if(selected == 1 && x < (954+60) && x > 954 && y < (57+7) && y > 7){
					if(currentTurn != locations.size()-1)
						currentTurn = currentTurn + 1;
					selected = 0;
					drawMap();
				}else if(selected == 2 && x < 60 && x > 10 && y < (57+7) && y > 7){
					if(currentTurn != 0)
						currentTurn = currentTurn - 1;
					selected = 0;
					drawMap();
				}else{
					selected = 0;
					drawMap();
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
		mapPanel.add(maplabel);
		timePanel.add(timelabel);
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
		
		mapImage.setImage(map);
		maplabel.setIcon(mapImage);

		mapPanel.setVisible(true);
		drawPointer();
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
	
	private void drawPointer(){
		timePanel.setVisible(false);

		try {
			if(selected == 0){
				timeline = ImageIO.read(new File("resources/timeBar.png"));
			}else if(selected == 1){
				timeline = ImageIO.read(new File("resources/timeBarForward.png"));
			}else if(selected == 2){
				timeline = ImageIO.read(new File("resources/timeBarRewind.png"));
			}
		} catch (IOException e) {}
		Graphics2D g = timeline.createGraphics();
		g.drawImage(timeline, 0, 0, null);	
		g.drawImage(timePointer, (int)(75+((currentTurn/(float)(locations.size()-1))*860)), 10, null);
		g.dispose();
		timeImage.setImage(timeline);
		timelabel.setIcon(timeImage);
		timePanel.setVisible(true);
		
	}
	
}
