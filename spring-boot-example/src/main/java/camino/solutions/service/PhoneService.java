package camino.solutions.service;

import java.util.Date;
import java.util.Timer;

import org.springframework.stereotype.Component;

@Component
public class PhoneService {
	
	private long delay = 105;
	private boolean songStartQueued;
	private boolean notificationQueued;
	private boolean songReceived;
	private SongTask songTask;
	private Timer timer;
	
	public PhoneService(){
		songTask = new SongTask();
	}
	
	public boolean isSongReceived() {
		return songReceived;
	}

	public void setSongReceived(boolean songReceived) {
		this.songReceived = songReceived;
	}

	public boolean isSongStartQueued(){
		return songStartQueued;
	}
	
	public void setSongStartQueued(boolean queued){
		songStartQueued = queued;
	}
	
	public boolean isNotificationQueued(){
		return notificationQueued;
	}
	
	public void setNotificationQueued(boolean queued){
		notificationQueued = queued;
	}
	
	public boolean launchSongAt(long millis){
		System.out.println("request received");
		timer = new Timer(true);
		timer.schedule(songTask, new Date(millis + delay));
		return true;
	}
	
	public void setDelay(long millis){
		delay = millis;
		System.out.println(delay);
	}
	
	public void stopSong(){
		songTask.stopStong();
		songTask = new SongTask();
	}
	
	public void launchSong(){
		
	}
}
