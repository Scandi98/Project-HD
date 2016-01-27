package server.model.content;

import java.util.Date;

public class StopWatch
{ 
    public static Date startTime;

    public static void startTiming()
    {
        startTime = new Date();
    }

    public static String stopTiming()
    {
    	if (startTime == null)
    		return "";
        Date stopTime = new Date();
        long timediff = (stopTime.getTime() - startTime.getTime())/1000L;
        int minutes = (int) (timediff / (60));
        int seconds = (int) ((timediff) % 60);
        String str = String.format("%d:%02d", minutes, seconds);
        return str;
    }

}
