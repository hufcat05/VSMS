package camino.solutions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import camino.solutions.service.PhoneService;

@RestController
public class PhoneController {
	
	@Autowired
	PhoneService phoneService;
    
    @RequestMapping("/api/v1/issongstartqueued")
    public boolean isSongStartQueued(){
        return phoneService.isSongStartQueued();
    }
    
    @RequestMapping("/api/v1/isnotificationqueued")
    public boolean isNotificationQueued(){
    	return phoneService.isNotificationQueued();
    }
    
    @RequestMapping("/api/v1/setsongstartqueued/{queued}")
    public boolean setSongStartQueued(@PathVariable(value="queued") boolean queued){
    	phoneService.setSongStartQueued(queued);
    	return true;
    }
    
    @RequestMapping("/api/v1/setnotificationqueued/{queued}")
    public boolean setNotificationQueued(@PathVariable(value="queued") boolean queued){
    	phoneService.setNotificationQueued(queued);
    	return true;
    }
    
    @RequestMapping("/api/v1/igotthesongqueue")
    public boolean songQueueAccepted(){
    	phoneService.setSongStartQueued(false);
    	return true;
    }
    
    @RequestMapping("/api/v1/igotthenotificationqueue")
    public boolean notificationQueueAccepted(){
    	phoneService.setNotificationQueued(false);
    	return true;
    }
    
    @RequestMapping("/api/v1/startsong/{millis}")
    public boolean startSong(@PathVariable(value="millis") long millis){
    	boolean success = phoneService.launchSongAt(millis);
    	return success;
    }
    
    @RequestMapping("/api/v1/stopsong")
    public void stopSong(){
    	phoneService.stopSong();
    }
    
    @RequestMapping("/api/v1/setdelay/{delay}")
    public void setDelay(@PathVariable(value="delay") long delay){
    	phoneService.setDelay(delay);
    }
    
    @RequestMapping("/api/v1/didyougetit")
    public boolean songReceived(){
    	return phoneService.isSongReceived();
    }
    
    @RequestMapping("/api/v1/executeosc/{osc}")
    public void executeOsc(@PathVariable(value="osc") String osc){
    	phoneService.executeOsc(osc);
    }
}
