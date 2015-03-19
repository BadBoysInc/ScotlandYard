package solution;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scotlandyard.Colour;



public class WinnersScreen extends JFrame {

	WinnersScreen(Set<Colour> winners){
		this.setLayout(new BorderLayout());
		BufferedImage image = null;
		try {
			if(winners.contains(Colour.Black)){
				image = ImageIO.read(new File("resources/xWin.png"));
			}else{
				image = ImageIO.read(new File("resources/detectivesWin.png"));
			}
		} catch (IOException e1) {}
		JLabel background = new JLabel(new ImageIcon(image));

		JButton saveReplay = new JButton("Save Replay?");
		saveReplay.setPreferredSize(new Dimension (100, 50));
		saveReplay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0); 
			}
		});

		add(background, BorderLayout.CENTER);
		add(saveReplay, BorderLayout.SOUTH);
		pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		setVisible(true);
	}
	
	
	
}
