package camino.solutions.service;

import java.io.FileInputStream;
import java.util.TimerTask;

import javax.sound.sampled.Clip;

import javazoom.jl.player.Player;

public class SongTask extends TimerTask{
	Player player;
	
	
	public SongTask(){
		loadSong();
	}

	@Override
	public void run() {
		try {
			player.play();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void stopStong(){
		player.close();
	}
	
	public void loadSong(){
		try {
			 FileInputStream fis = new FileInputStream("C:\\Users\\hufcat05\\Documents\\sins.mp3");
			    player = new Player(fis);
			} catch (Exception ex){
				ex.printStackTrace();
			}
	}

}
