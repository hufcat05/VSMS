package camino.solutions.songcontroller;

import java.io.IOException;
import java.util.TimerTask;

import android.media.MediaPlayer;
import android.os.Environment;

public class SongTask extends TimerTask {
	MediaPlayer mediaPlayer;
	
	
	public SongTask (){
		try {
			
			String audioFile= Environment.getExternalStorageDirectory().getAbsolutePath();
			audioFile+="/bluetooth/Sun.m4a";
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(audioFile);
			mediaPlayer.prepare();
		} catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
	}

	@Override
	public void run() {
		mediaPlayer.start();
	}
	
	public void stopSong(){
		mediaPlayer.stop();
	}
}
