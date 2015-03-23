package solution;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import scotlandyard.Colour;

public class IntroScreen extends JFrame{
	
	public IntroScreen(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		SoundHelper.init();
	}

	void setupScreen(final Presenter p){
		
		//Panels
		final JPanel right = new JPanel();
		right.setLayout(new GridLayout(7,1));
		final JPanel left = new JPanel();
		left.setLayout(new GridLayout(3,1));
		
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
					SoundHelper.soundClick();
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
					SoundHelper.soundBell();
					closeWindow();
					p.beginGame(colours);
				}else{
					SoundHelper.soundWrong();
				}
			}			
		});
		JPanel startPanel = new JPanel();
		startPanel.setLayout(new BorderLayout());
		startPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
		startPanel.add(start);
		
		//Right Panel
		right.add(startPanel);
		right.setBorder(BorderFactory.createTitledBorder("Choose Players"));
		right.setPreferredSize(new Dimension(250, 1000));
		
		//LoadGameButton
		final JButton loadGame = new JButton("Load Game");
		loadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		loadGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundHelper.soundSsh();
				JFileChooser chooser = new JFileChooser();
			    chooser.showOpenDialog(left);
			    
			    File file = chooser.getSelectedFile();
			    if(file !=null){
			    	p.loadGameState(file);
			    	closeWindow();
			    }
			}			
		});
		JPanel loadPanel = new JPanel();
		loadPanel.setLayout(new BorderLayout());
		loadPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		loadPanel.add(loadGame);	
		
		
		//ReplayButton
		final JButton replay = new JButton("Load Replay");
		replay.setAlignmentX(Component.CENTER_ALIGNMENT);
		replay.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundHelper.soundSsh();
				JFileChooser chooser = new JFileChooser();
			    chooser.showOpenDialog(left);
			    
			    File file = chooser.getSelectedFile();
			    if(file !=null){
			    	p.startReplay(file);
			    	closeWindow();
			    }
			}			
		});
		JPanel replayPanel = new JPanel();
		replayPanel.setLayout(new BorderLayout());
		replayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		replayPanel.add(replay);
		
		//NewGameButton
		final JToggleButton newGame = new JToggleButton("New Game");
		newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		newGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundHelper.soundSsh();
				if(newGame.isSelected()){
					for(String b: buttons.keySet()){
						buttons.get(b).setEnabled(true);
					}
					start.setEnabled(true);
					loadGame.setEnabled(false);
					replay.setEnabled(false);
					
				}else{
					for(String b: buttons.keySet()){
						buttons.get(b).setEnabled(false);
					}
					start.setEnabled(false);
					loadGame.setEnabled(true);
					replay.setEnabled(true);
				}
			}			
		});
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new BorderLayout());
		newPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		newPanel.add(newGame);
		
		//Left Panel
		left.add(newPanel);
		left.add(loadPanel);
		left.add(replayPanel);
		
		//Add Panels
		this.setLayout(new BorderLayout());
		this.add(right, BorderLayout.EAST);
		this.add(left, BorderLayout.CENTER);
		setPreferredSize(new Dimension(600, 400));
	    pack();
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
	    setVisible(true);
	    
	}

	void closeWindow(){
		this.dispose();
	}
	
}
