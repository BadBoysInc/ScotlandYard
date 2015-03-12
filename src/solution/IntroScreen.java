package solution;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import scotlandyard.Colour;
import scotlandyard.Move;

public class IntroScreen extends JFrame{
	
	public IntroScreen(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	void setupScreen(final Presenter p){
		
		//Panel to hold all buttons
		final JPanel main = new JPanel();
		main.setLayout(new GridLayout(7,1));
		//init list of buttons
		final Map<String, JToggleButton> buttons = new HashMap<String, JToggleButton>();
		for(Colour c: Colour.values()){
			JToggleButton b = new JToggleButton(c.toString() + " Player");
			b.setAlignmentX(Component.CENTER_ALIGNMENT);
			buttons.put(c.toString(), b);
			main.add(b);
			b.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					String id = e.getActionCommand();
					JToggleButton b = buttons.get(id);
				}
			});
			b.setActionCommand(c.toString());
		}
		
		JButton play = new JButton("Play!");
		play.setAlignmentX(Component.CENTER_ALIGNMENT);
		play.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Set<Colour> colours = new HashSet<Colour>(); 
				for(String id: buttons.keySet()){
					JToggleButton b = buttons.get(id);
					if(b.isSelected())
						colours.add(Colour.valueOf(id));
				}
				if(colours.contains(Colour.Black) && colours.size()>1){
					closeWindow();
					p.beginGame(colours);
				}
			}

			
		});
		main.add(play);
		
		this.add(main);
		setPreferredSize(new Dimension(300, 500));
	    pack();
	    setLocationByPlatform(true);
	    setVisible(true);
	    
	}

	void closeWindow(){
		this.dispose();
	}
	
}
