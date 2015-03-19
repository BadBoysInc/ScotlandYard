package solution;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import scotlandyard.Colour;

public class WinnersScreen extends JFrame {

	WinnersScreen(Set<Colour> winners){
		String statement = "";
		if(winners.contains(Colour.Black)){
			statement = "<html>Mr X has Won, He's just such a good criminal. You are going to have to try harder than that if you want to bring him to justice. Detectives you should be ashamed of your selves. Youv'e let yourself down, each other down, me down, and worst of all youv'e let London down. Dismissed!</html>";
		}else{
			statement = "<html>Well done Detectives, youv'e successfully captured the hardened criminal, MR X. Maybe now we can learn his real name...?</html>";
		}
		
		
		
		JPanel container = new JPanel();
		JLabel text = new JLabel(statement);
		container.add(text);
		JButton quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0); 
			}
		});
		container.add(quit);
		add(container);
		pack();
		setLocationByPlatform(true);
		setVisible(true);
	}
	
	
	
}
