package server.model.content.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import server.model.players.Client;
import server.util.Misc;

public class AuctionLogger {
	
	public static void main(Client c, int type, int itemId, int itemAmount, int price, String otPlr) {
		if(c == null){
			//todo: log eventual cheater transaction
			return;
		}
		
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();
        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);
	 
        
		try {
			String nameToWrite = Misc.capitalize(c.playerName);
			String[] logData = {
				(hour < 10 ? 0 + "" + hour : hour) + ":" + (minute < 10 ? 0 + "" + minute : minute) + ":" + (second < 10 ? 0 + "" + second : second) + " - "+nameToWrite+": "+fetchType(type)+" "+itemAmount+" "+c.getItems().getItemName(itemId) + " for "+price+"gp from/to "+Misc.capitalize(otPlr)
			};

            boolean success = (new File("Data/AuctionHouse/logs/"+fetchDate())).mkdir();
            if (success) {
              System.out.println("Directory: "+ fetchDate() + "/ created");
            }
            
			File file = new File("Data/AuctionHouse/logs/"+fetchDate()+"/"+Misc.capitalize(nameToWrite)+".txt");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 

			
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0; i < logData.length; i++){
				bw.write(logData[i]);
				bw.newLine();
			}
			bw.close();
 
			//System.out.println(nameToWrite + ":");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String fetchType(int type){
		return type == 0 ? "selling"  : (type == 1 ? "buying" : (type == 2 ? "instant sell" : (type == 3 ? "instant buy" : "remove offer")));
	}
	
	 public static String fetchDate(){
     {
        int day, month, year;
        GregorianCalendar date = new GregorianCalendar();
        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);
        return year+"-"+(month + 1)+"-"+day;
     }
  }   
}