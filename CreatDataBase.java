import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JOptionPane;

public class CreatDataBase
{

	public CreatDataBase()
	{
		Connection connection = connectionBase();
		creatTableUsers(connection);
		creatTableCandidates(connection);
	}
	
	private Connection connectionBase(){
		Connection connection = null;
		try{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:calculator.db");
		}catch (Exception e){
			JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
			return null;
		}
		return connection;
		}
	
	private void creatTableUsers(Connection connection){
		Statement stat = null;
		try {
			stat = connection.createStatement();
			String tableUsers = "CREATE TABLE IF NOT EXISTS users (name TEXT, lastName TEXT, pesel int)";
			stat.executeUpdate(tableUsers);
			stat.close();
		} catch (SQLException e){
			JOptionPane.showInternalMessageDialog(null, "Can't creat table: "+e);
        }
	}
	
	private void creatTableCandidates(Connection connection){
		Statement stat = null;
		try {
			stat = connection.createStatement();
			String table = "CREATE TABLE IF NOT EXISTS candidates (name TEXT, party TEXT, value int)";
			stat.executeUpdate(table);
			stat.close();
		} catch (SQLException e){
			JOptionPane.showInternalMessageDialog(null, "Can't creat table: "+e);
        }
		
		int valueOfRecords = 0;		
		try {
			stat = connection.createStatement();
			String select = "SELECT * FROM candidates";
			ResultSet result = stat.executeQuery(select);
			while (result.next()) valueOfRecords++;
			result.close();
		} catch (Exception e){
			System.out.println("ERROR: "+e);
		}
		if (valueOfRecords == 0)
		{
		
			URL url = null;
			try
			{
				url = new URL("http://webtask.future-processing.com:8069/candidates");
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			 try (InputStream is = url.openStream();
			      JsonReader rdr = Json.createReader(is)) {
			
			     JsonObject obj = rdr.readObject();
			     JsonObject obj2 = obj.getJsonObject("candidates");
			     JsonArray results = obj2.getJsonArray("candidate");
				     try {
						stat = connection.createStatement();
				     
					     for (JsonObject res : results.getValuesAs(JsonObject.class)) {
					    	 String name = res.getString("name");
					         String party = res.getString("party");
					    	 String insert = "INSERT INTO candidates (name, party, value) VALUES ('"+name+"','"+party+"','"+0+"');";
					       	 stat.executeUpdate(insert);
					     }
				       	 String insert = "INSERT INTO candidates (name, party, value) VALUES ('invalid','','"+0+"');";
				       	 stat.executeUpdate(insert);
					     insert = "INSERT INTO candidates (name, party, value) VALUES ('blocked','','"+0+"');";
				       	 stat.executeUpdate(insert);
					    stat.close();
						connection.close();
					} catch (Exception e){
						System.out.println("ERROR: "+e);
					}       
			    
			 } catch (IOException e)
				{
					e.printStackTrace();
				}
		}
	}
	
}
