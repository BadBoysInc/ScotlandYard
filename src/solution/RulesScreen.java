package solution;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RulesScreen extends JFrame{
	RulesScreen(final MainScreen m){
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowClosed(WindowEvent e) {	
				m.rulesClosed();
			}
			
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
        });
		JLabel background = null;
		try {
			background = new JLabel(new ImageIcon(ImageIO.read(new File("resources/rules.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.add(background);
	    pack();
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
	    setVisible(true);
	}
}
