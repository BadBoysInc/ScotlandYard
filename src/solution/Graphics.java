package solution;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import scotlandyard.Colour;

public class Graphics extends JFrame{
	
	
	
	public Graphics(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	    /*JPanel mainContainer = new JPanel();
	    
	    JPanel rightContainer = new JPanel();
	    JPanel leftContainer = new JPanel();
	    
	    URL u = this.getClass().getResource("map.jpg");
	    ImageIcon icon = new ImageIcon(u);
	    leftContainer.add(new JLabel(icon));
	   
	    JButton quit = new JButton("KILL ME!!!");
	    quit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
	    });
	    
	    rightContainer.add(quit);
	    
	    mainContainer.add(leftContainer);
	    mainContainer.add(rightContainer);
	    
	    add(mainContainer);
	    
	    pack();
	    setLocationByPlatform(true);
	    setVisible(true);*/
	}

	void setupScreen(final Presenter p){
		
		//Panel to hold all buttons
		JPanel main = new JPanel();
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
					p.beginGame(colours);
					setVisible(false);
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
		
	}
	
}
