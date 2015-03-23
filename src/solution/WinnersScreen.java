package solution;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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

	Presenter presenter;
	
	WinnersScreen(final Set<Colour> winners, Presenter p){
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		presenter =  p;
		String statement = "";
		
				
		final JPanel container = new JPanel();
		JLabel text = new JLabel(statement);
		text.setFont(new Font("Serif", Font.PLAIN, 25));
		
		text.setPreferredSize(new Dimension(300, 200));
		container.add(text);
		JButton quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}});

		this.setLayout(new BorderLayout());
		BufferedImage image = null;
		try {
			if(winners.contains(Colour.Black)){
				image = ImageIO.read(new File("resources/xWin.png"));
				SoundHelper.soundLost();
			}else{
				image = ImageIO.read(new File("resources/detectivesWin.png"));
				SoundHelper.soundSuccess();
			}
		} catch (IOException e1) {}
		JLabel background = new JLabel(new ImageIcon(image));

		
		JButton replay = new JButton("Save Replay");
		replay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    chooser.showSaveDialog(container);
			    
			    File file = chooser.getSelectedFile();
			    if(file != null)
			    	presenter.saveForReplay(file);
			}
		});
		container.add(quit);
		add(container);


		add(background, BorderLayout.CENTER);
		add(replay, BorderLayout.SOUTH);

		pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		setVisible(true);
	}
	
	
	
}
