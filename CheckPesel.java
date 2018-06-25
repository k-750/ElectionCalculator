import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JOptionPane;

public class CheckPesel
{
	private boolean status = false; // false - can vote
	
	public CheckPesel(String name, String lastName, long pesel)
	{
		checkAge(pesel);	// check user age
		checkBlocked(pesel);	// check blocked pesels
		checkBase(pesel);		// chceck pesels in DataBase in table users
	}
	
	private void checkAge(long pesel)
	{
		long peselYear = 1900+pesel/1000000000;
		long peselMounth;
		long peselDay;
		long nowYear;
		long nowMounth;
		long nowDay;
		
		peselMounth = (pesel/10000000)%100;
		if (peselMounth > 12) 
			{
				peselMounth -= 20;
				peselYear += 100;		
			}
		
		peselDay = (pesel/100000)%100;
		
		LocalDate date = LocalDate.now();
			nowYear = date.getYear();
			nowMounth = date.getMonthValue();
			nowDay = date.getDayOfMonth();
		
		if (nowYear < peselYear+18) status = true;	// check user age
		if (peselYear+18 == nowYear)
		{
			if (nowMounth < peselMounth) status = true;
			if (peselMounth == nowMounth)
			{
				if (nowDay < peselDay) status = true;
			}
		}
			if (status == true) 
			{
        	 	DataBase base = new DataBase();
        	 	base.addBlockedLogin();
				JOptionPane.showMessageDialog(null, "You are under age.");
			}
	}
	
	private void checkBlocked(long pesel)
	{

		 URL url = null;
		try
		{
			url = new URL("http://webtask.future-processing.com:8069/blocked");
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		 try (InputStream is = url.openStream();
		      JsonReader rdr = Json.createReader(is)) {
		
		     JsonObject obj = rdr.readObject();
		     JsonObject obj2 = obj.getJsonObject("disallowed");
		     JsonArray results = obj2.getJsonArray("person");
		     for (JsonObject result : results.getValuesAs(JsonObject.class)) {
		         if (Long.parseLong(result.getString("pesel")) == pesel) 
		        	 {
		        	 	JOptionPane.showMessageDialog(null, "You don't have right to vote.");
		        	 	status = true;
		        	 	DataBase base = new DataBase();
		        	 	base.addBlockedLogin();
		        	 	break;
		        	 }
		     }
		 } catch (IOException e)

			{
				e.printStackTrace();
			}
	}
	
	public void checkBase(long pesel)
	{
		DataBase base = new DataBase();
		ArrayList<Long> users = base.getUsers(pesel);
		for (long x : users)
		{
			if (x == pesel)
				{
					status = true;
					JOptionPane.showMessageDialog(null, "you have already voted.");
				}
		}
	}

	public boolean getStatus()
	{
		return status;
	}
}
