import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class DataBase
{
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
	
	public void addUser(String name, String lastName, long pesel)
	{
		Connection connection = connectionBase();
		Statement stat = null;
			try {
				stat = connection.createStatement();
			    	 String insert = "INSERT INTO users (name, lastName, pesel) VALUES ('"+name+"','"+lastName+"','"+pesel*7+"');";	// pesel * 7 : simply coding pesel
			    	 stat.executeUpdate(insert);
			    	 stat.close();
			    	 connection.close();
				} catch (Exception e){
					System.out.println("ERROR: "+e);
				}  
	}
	
	public int numberOfCandidates()
	{
		Connection connection = connectionBase();
		Statement stat = null;
		int number=0;
			try {
				stat = connection.createStatement();
			    	String select = "SELECT * FROM candidates";
					ResultSet res = stat.executeQuery(select);
					while(res.next()){
						number++;
						}
					res.close();
			    	stat.close();
			    	connection.close();
				} catch (Exception e){
					System.out.println("ERROR: "+e);
				} 
			return (number-2);	// - blocked - invalid ; 2 last records
	}
	
	public String[][] getAllCandidates()
	{
		int number = numberOfCandidates();
		String[][] allCandidates = new String[number][3];
		Connection connection = connectionBase();
		Statement stat = null;
			try {
				stat = connection.createStatement();
			    	String select = "SELECT * FROM candidates";
					ResultSet res = stat.executeQuery(select);
					int i = 0;
					while(res.next()){
						allCandidates[i][0]=(res.getString("name"));
						allCandidates[i][1]=(res.getString("party"));
						allCandidates[i][2]=Long.toString(res.getLong("value"));
						i++;
						if (i == number) break;
						}
					res.close();
			    	stat.close();
			    	connection.close();
				} catch (Exception e){
					System.out.println("ERROR: "+e);
				} 
			return allCandidates;
	}
	
	public void addInvalid()
	{
		Connection connection = connectionBase();
		Statement stat = null;
			try {
				stat = connection.createStatement();
			    	 	String select = "SELECT value FROM candidates WHERE name = 'invalid'";
						ResultSet res = stat.executeQuery(select);
						int invalid = res.getInt("value");
						invalid++;
						select = "UPDATE candidates SET value="+invalid+" WHERE name = 'invalid'";
						stat.executeUpdate(select);
				    	stat.close();
				    	connection.close();
				} catch (Exception e){
					System.out.println("ERROR: "+e);
				}  
	}
	
	public void addVote(String chosen)
	{
		Connection connection = connectionBase();
		Statement stat = null;
			try {
				stat = connection.createStatement();
			    	String select = "SELECT * FROM candidates";
					ResultSet res = stat.executeQuery(select);
					while(res.next())
					{
						if ((res.getString("name")+", "+res.getString("party")).equals(chosen))
						{
							String canName = res.getString("name");
							String canParty = res.getString("party");
				    	 	String select1 = "SELECT value FROM candidates WHERE name = '"+canName+"' AND party = '"+canParty+"'";
				    	 	ResultSet res1 = stat.executeQuery(select1);
							int v = res1.getInt("value");
							v++;
							select1 = "UPDATE candidates SET value="+v+" WHERE name = '"+canName+"' AND party = '"+canParty+"'";
							stat.executeUpdate(select1);
							res1.close();
						}
					}
					res.close();
			    	stat.close();
			    	connection.close();
				} catch (Exception e){
					System.out.println("ERROR: "+e);
				} 
	}
	
	public void addBlockedLogin()		// disalowet and under age ligins
	{
		Connection connection = connectionBase();
		Statement stat = null;
			try {
				stat = connection.createStatement();
			    	String select =  "SELECT value FROM candidates WHERE name = 'blocked'";
					ResultSet res = stat.executeQuery(select);
					int v = res.getInt("value");
					res.close();
					v++;
					select = "UPDATE candidates SET value="+v+" WHERE name = 'blocked'";
					stat.executeUpdate(select);
			    	stat.close();
			    	connection.close();
				} catch (Exception e){
					System.out.println("ERROR: "+e);
				} 
	}
	
	public ArrayList<Long> getUsers(long pesel)
	{
		ArrayList<Long> users = new ArrayList<Long>();

		Connection connection = connectionBase();
		Statement stat = null;
			try {
				stat = connection.createStatement();
			    	String select = "SELECT pesel FROM users";
					ResultSet res = stat.executeQuery(select);
					while(res.next()){
						users.add(res.getLong("pesel")/7);		// pesel/7 : simply encoding pesel
					}
					res.close();
			    	stat.close();
			    	connection.close();
				} catch (Exception e){
					System.out.println("ERROR: "+e);
				} 
			return users;
	}
	
	public int getInvalid()
	{
		int invalid = 0;
		Connection connection = connectionBase();
		Statement stat = null;
			try {
				stat = connection.createStatement();
			    	String select = "SELECT value FROM candidates WHERE name ='invalid'";
					ResultSet res = stat.executeQuery(select);
					invalid = res.getInt("value");
					res.close();
			    	stat.close();
			    	connection.close();
				} catch (Exception e){
					System.out.println("ERROR: "+e);
				} 
			return invalid;
	}
	
	public int getBlockedLogin()
	{
		int blocked = 0;
		Connection connection = connectionBase();
		Statement stat = null;
			try {
				stat = connection.createStatement();
			    	String select = "SELECT value FROM candidates WHERE name ='blocked'";
					ResultSet res = stat.executeQuery(select);
					blocked = res.getInt("value");
					res.close();
			    	stat.close();
			    	connection.close();
				} catch (Exception e){
					System.out.println("ERROR: "+e);
				} 
			return blocked;
	}
	
	public String[][] getResultsParty()
	{
		
		String [][] candidates = getAllCandidates(); 
		String[][] partyResults = new String[4][2];		
		partyResults[0][0] = "Piastowie";
		partyResults[1][0] = "Dynastia Jagiellonów";
		partyResults[2][0] = "Elekcyjni dla Polski";
		partyResults[3][0] = "Wazowie";
		
		int p0 = 0;	// number of votes
		int p1 = 0;
		int p2 = 0;
		int p3 = 0;
		
		for (String[] x : candidates)
		{
			switch (x[1])
			{
			case "Piastowie":
				p0 += Integer.valueOf(x[2]);
				break;
			case "Dynastia Jagiellonów":
				p1 += Integer.valueOf(x[2]);
				break;
			case "Elekcyjni dla Polski":
				p2 += Integer.valueOf(x[2]);
				break;
			case "Wazowie":
				p3 += Integer.valueOf(x[2]);
				break;				
			}
		}
		
		partyResults[0][1] = String.valueOf(p0);
		partyResults[1][1] = String.valueOf(p1);
		partyResults[2][1] = String.valueOf(p2);
		partyResults[3][1] = String.valueOf(p3);
		
		return partyResults;
	}
}
