package solution;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundHelper {

	private static Clip popClip;
	private static Clip bellClip;
	private static Clip wrongClip;
	private static Clip sshClip;
	private static Clip successClip;
	private static Clip lostClip;

	
	static void init(){
		try {
			File file = new File("./resources/pop.wav");
		    AudioInputStream sound = AudioSystem.getAudioInputStream(file);
		    popClip = (Clip) AudioSystem.getClip();
		    popClip.open(sound);
			
			File file2 = new File("./resources/bell.wav");
		    AudioInputStream sound2 = AudioSystem.getAudioInputStream(file2);
		    bellClip = (Clip) AudioSystem.getClip();
		    bellClip.open(sound2);
		    
		    File file3 = new File("./resources/wrong.wav");
		    AudioInputStream sound3 = AudioSystem.getAudioInputStream(file3);
		    wrongClip = (Clip) AudioSystem.getClip();
		    wrongClip.open(sound3);
		    
		    File file4 = new File("./resources/shush.wav");
		    AudioInputStream sound4 = AudioSystem.getAudioInputStream(file4);
		    sshClip = (Clip) AudioSystem.getClip();
		    sshClip.open(sound4);
		    
		    File file5 = new File("./resources/success.wav");
		    AudioInputStream sound5 = AudioSystem.getAudioInputStream(file5);
		    successClip = (Clip) AudioSystem.getClip();
		    successClip.open(sound5);
		    
		    File file6 = new File("./resources/lost.wav");
		    AudioInputStream sound6 = AudioSystem.getAudioInputStream(file6);
		    lostClip = (Clip) AudioSystem.getClip();
		    lostClip.open(sound6);
			
		} catch (LineUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void soundClick(){
		if(popClip.isRunning())
			popClip.stop();
		popClip.setFramePosition(0);
		popClip.start();
	}
	
	static void soundBell(){
		if(bellClip.isRunning())
			bellClip.stop();
		bellClip.setFramePosition(0);
		bellClip.start();
	}
	
	static void soundWrong(){
		if(wrongClip.isRunning())
			wrongClip.stop();
		wrongClip.setFramePosition(0);
		wrongClip.start();
	}
	
	static void soundSsh(){
		if(sshClip.isRunning())
			sshClip.stop();
		sshClip.setFramePosition(0);
		sshClip.start();
	}
	
	static void soundSuccess(){
		if(successClip.isRunning())
			successClip.stop();
		successClip.setFramePosition(0);
		successClip.start();
	}
	
	static void soundLost(){
		if(lostClip.isRunning())
			lostClip.stop();
		lostClip.setFramePosition(0);
		lostClip.start();
	}
	
}
