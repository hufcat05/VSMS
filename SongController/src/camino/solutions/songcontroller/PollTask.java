package camino.solutions.songcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

public class PollTask extends TimerTask {
	
	private boolean processInitiated = false;
	
	private String ip;
	private String songStart;
	private String launchSong;
	private String acknowledge;
	private SongTask songTask;
	private Context context;
	
	public PollTask(String ip, String songStart, String launchSong, String acknowledge, SongTask songTask){
		this.ip = ip;
		this.songStart = songStart;
		this.launchSong = launchSong;
		this.songTask = songTask;
		this.acknowledge = acknowledge;
	}

	@Override
	public void run() {
		if (!processInitiated){
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(ip + songStart);
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
	
	
				HttpEntity entity = response.getEntity();
	
	
				text = getASCIIContentFromEntity(entity);
				
				Log.d("yolo", text);
				if (text.equals("true")){
					Log.d("win", "start this ish");
					long startTime = System.currentTimeMillis() + 5000;
					if (!processInitiated){
						SntpClient client = new SntpClient();
						for (int x = 0; x < 10; x++){
							if (client.requestTime("192.168.1.10", 1000)) {
							    long now = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
							    SystemClock.setCurrentTimeMillis(now);
							    Date current = new Date(now);
							    Log.d("NTP tag", current.toString());
							}
						}
						launchSong(startTime);
						Timer timer = new Timer(true);
						timer.schedule(songTask, new Date(startTime));
						sendAcknowledge();
					}
				     processInitiated = true;
				}


			} catch (Exception e) {
				Log.e("oops", e.getLocalizedMessage());
			}
		}
	}
	
	public void sendAcknowledge(){
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(ip + acknowledge);
		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);


			HttpEntity entity = response.getEntity();


			text = getASCIIContentFromEntity(entity);
			Log.d("response", text);
		} catch (Exception ex){
			Log.e("oops", ex.getMessage());
		}
	}
	
	public void launchSong(long startTime){
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(ip + launchSong + '/' + startTime);
		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);


			HttpEntity entity = response.getEntity();


			text = getASCIIContentFromEntity(entity);
			Log.d("response", text);
		} catch (Exception ex){
			Log.e("oops", ex.getMessage());
		}
	}
	
	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
		InputStream in = entity.getContent();


		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n>0) {
			byte[] b = new byte[4096];
			n =  in.read(b);


			if (n>0) out.append(new String(b, 0, n));
		}


		return out.toString();
	}

}
