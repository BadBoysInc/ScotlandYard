package solution;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;

import javax.swing.BorderFactory;
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
		
		//Panels
		final JPanel right = new JPanel();
		right.setLayout(new GridLayout(7,1));
		final JPanel left = new JPanel();
		left.setLayout(new GridLayout(3,1));
		//Right Panel
		
		//Colour Buttons
		final Map<String, JToggleButton> buttons = new HashMap<String, JToggleButton>();
		for(Colour c: Colour.values()){
			JToggleButton b = new JToggleButton(c.toString() + " Player");
			b.setAlignmentX(Component.CENTER_ALIGNMENT);
			buttons.put(c.toString(), b);
			right.add(b);
			b.setEnabled(false);
			b.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					String id = e.getActionCommand();
					JToggleButton b = buttons.get(id);
				}
			});
			b.setActionCommand(c.toString());
		}
		//Start Button
		final JButton start = new JButton("Start");
		start.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.setEnabled(false);
		start.addActionListener(new ActionListener(){
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
		JPanel startPanel = new JPanel();
		startPanel.setLayout(new BorderLayout());
		startPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
		startPanel.add(start);
		//Finalize Panel
		right.add(startPanel);
		right.setBorder(BorderFactory.createTitledBorder("Choose Players"));
		right.setPreferredSize(new Dimension(250, 1000));
		
		//Left Panel
		//LoadGameButton
		final JButton loadGame = new JButton("Load Game");
		loadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		loadGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}			
		});
		JPanel loadPanel = new JPanel();
		loadPanel.setLayout(new BorderLayout());
		loadPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		loadPanel.add(loadGame);		
		//NewGameButton
		final JToggleButton newGame = new JToggleButton("New Game");
		newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		newGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(newGame.isSelected()){
					for(String b: buttons.keySet()){
						buttons.get(b).setEnabled(true);
					}
					start.setEnabled(true);
					loadGame.setEnabled(false);
				}else{
					for(String b: buttons.keySet()){
						buttons.get(b).setEnabled(false);
					}
					start.setEnabled(false);
					loadGame.setEnabled(true);
				}
			}			
		});
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new BorderLayout());
		newPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		newPanel.add(newGame);
		
		//QuitButton
		JButton quit = new JButton("Quit");
		quit.setAlignmentX(Component.CENTER_ALIGNMENT);
		quit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}			
		});
		
		JPanel quitPanel = new JPanel();
		quitPanel.setLayout(new BorderLayout());
		quitPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		quitPanel.add(quit);
		
		//Add Buttons
		left.add(newPanel);
		left.add(loadPanel);
		left.add(quitPanel);
		
		//Add Panels
		this.setLayout(new BorderLayout());
		this.add(right, BorderLayout.EAST);
		this.add(left, BorderLayout.CENTER);
		setPreferredSize(new Dimension(600, 500));
	    pack();
	    setLocationByPlatform(true);
	    setVisible(true);
	    
	}

	void closeWindow(){
		this.dispose();
	}
	
}
