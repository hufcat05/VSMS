package camino.solutions.service;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Timer;

import org.springframework.stereotype.Component;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortOut;

@Component
public class PhoneService {
	
	private long delay = 105;
	private boolean songStartQueued;
	private boolean notificationQueued;
	private boolean songReceived;
	private SongTask songTask;
	public InetAddress targetIP;
	 public OSCPortOut sender = null;
	private Timer timer;
	
	public PhoneService(){
		songTask = new SongTask();
		setConnection();
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
		songTask = new SongTask();
	}
	
	public void launchSong(){
		
	}
	
	public void setConnection(){
        try {
            targetIP = InetAddress.getLocalHost();
            //targetIP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {          
            e.printStackTrace();        }        

        try {
            sender = new OSCPortOut(targetIP, 53535); //------set up outgoing ------
        } catch (SocketException e) {
            e.printStackTrace();
        }

        /*try {                                     //------set up incoming-------
            receiver = new OSCPortIn(4444);
        } catch (SocketException e) {
            e.printStackTrace();
        } */ 

    }
	
	public void executeOsc(String osc){
		OSCMessage msg = new OSCMessage("/cue/selected/start");

        try {
            sender.send(msg);
            System.out.println("OSC message sent!");
        } catch (Exception e) {
            System.out.println("can not send");
            //e.printStackTrace();
        }
	}
}
