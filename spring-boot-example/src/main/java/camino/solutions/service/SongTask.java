package camino.solutions.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.TimerTask;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class SongTask extends TimerTask{
	private static final String URL = "http://localhost:53000/cue/selected";
	
	public InetAddress targetIP;
	 public OSCPortOut sender = null;
	
	public SongTask(){
		setConnection();
	}

	@Override
	public void run() {
		try {
			executeOsc();
		} catch (Exception ex){
			ex.printStackTrace();
		}
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
	
	public void executeOsc(){
		OSCMessage msg = new OSCMessage("/cue/SONG/start");
        try {
            sender.send(msg);
            System.out.println("OSC message sent!");
        } catch (Exception e) {
            System.out.println("can not send");
            //e.printStackTrace();
        }
	}

}
