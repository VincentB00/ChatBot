package Model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.google.gson.Gson;

public class Manager 
{
	public SQL sql;
	public Gson gson;
	public Random random;
	public SimpleHttpClient client;
	
	public Manager()
	{
		gson = new Gson();
		sql = null;
		random = new Random();
		client = new SimpleHttpClient("192.168.1.32:1111/ChatBot");
	}
	
	public Manager(String ip, int port, String username, String password, String schema)
	{
		sql = new SQL(ip, port, username, password, schema);
		gson = new Gson();
		random = new Random();
	}
	
	public void setSQL(String jsonBody) throws StatusException
	{
		SQLData sqlData = gson.fromJson(jsonBody, SQLData.class);
		
		if(sqlData != null && sqlData.schema != null && sqlData.schema.compareTo("") != 0)
		{
			sql = new SQL(sqlData.ip, sqlData.port, sqlData.username, sqlData.password, sqlData.schema);
		}
		else
			throw new StatusException(400, "Body package", gson);
	}
	
	public String checkSQLServerStatus() throws StatusException
	{
		try(ResultSet rs = sql.executeQuery(String.format("%s.SERVER STATUS ONLINE CHECK();", sql.schema)))
		{
			rs.close();
			return Manager.toJson("server_check", "pass");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return Manager.toJson("server_check", "fail");
		}
	}
	
	public String checkServerStatus() throws StatusException
	{
		String status = "normal";
		String result = "{";
		if(sql == null)
		{
			result += "\"SQL_connection\":\"fail\"";
			status = "bad";
			result += String.format(",\"status\":\"%s\"}", status);
			return result;
		}
		
		try(ResultSet rs = sql.executeQuery("select now();"))
		{
			String temp = "";
			while(rs.next())
				temp = rs.getString(1);
			
			rs.close();
			
			if(temp == null || temp.compareTo("") == 0)
			{
				status = "bad";
				result += "\"SQL_connection\":\"fail\"";
			}
			else
			{
				result += "\"SQL_connection\":\"pass\"";
				result += String.format(",\"SQL_datetime_test\":\"%s\"", temp);
			}
				
		}
		catch(Exception ex)
		{
			status = "bad";
			result += "\"SQL_connection\":\"fail\"";
		}
		
		sql.closeConnection();	
		
		result += String.format(",\"status\":\"%s\"}", status);
		return result;
	}
	
	public static String toJson(String variable, String obj)
	{
		return String.format("{\"%s\":\"%s\"}", variable, obj);
	}
	public static String toJson(String variable, int obj)
	{
		return String.format("{\"%s\":%s}", variable, obj);
	}
	public static String toJson(String variable, double obj)
	{
		return String.format("{\"%s\":%s}", variable, obj);
	}
	
	//-------------------------------------intent---------------------------------------
	
	public void addIntent(String jsonBody) throws StatusException
	{
		IntentData intentData = gson.fromJson(jsonBody, IntentData.class);
		
		if(intentData.tag == null)
			throw new StatusException(400,"missing tag");
		
		if(intentData.pattern != null && intentData.pattern.compareTo("") != 0)
		{
			intentData.pattern = intentData.pattern.replace("'", "\\" + "'");
			sql.executeUpdate(String.format("call %s.insert_Pattern('%s', '%s');", sql.schema, intentData.tag, intentData.pattern));
		}
		
		sql.closeConnection();
		
		if(intentData.response != null && intentData.response.compareTo("") != 0)
		{
			intentData.response = intentData.response.replace("'", "\\" + "'");
			sql.executeUpdate(String.format("call %s.insert_response('%s', '%s');", sql.schema, intentData.tag, intentData.response));
		}
		
		sql.closeConnection();
	}
	
	public LinkedList<Intent> getIntent(int range) throws StatusException
	{
		boolean limit = range > 0;
		
		HashMap<String, Intent> map = new HashMap<>();
		
		try(ResultSet rs = sql.executeQuery(String.format("call %s.get_all_intents();", sql.schema)))
		{
			while(rs.next())
			{
				String tag = rs.getString(1);
				String question = rs.getString(2);
				String answer = rs.getString(3);
				
				if(map.containsKey(tag))
				{
					map.get(tag).patterns.add(question);
					map.get(tag).responses.add(answer);
				}
				else
				{
					map.put(tag, new Intent(tag, new String[] {question}, new String[] {answer}));
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new StatusException(500);
		}
		
		sql.closeConnection();
		
		LinkedList<Intent> intentList = new LinkedList<>();
		
		for(Entry<String, Intent> entry : map.entrySet()) 
		{
			
			Intent t = entry.getValue();
		    intentList.add(t);
		    
			if(limit && intentList.size() >= range)
				break;
		}
		
		return intentList;
	}
	
	public Intent getIntent(String tag) throws StatusException
	{
		Intent intent = new Intent(tag);
		
		try(ResultSet rs = sql.executeQuery(String.format("call %s.get_all_pattern('%s');", sql.schema, tag)))
		{
			while(rs.next())
			{
				intent.patterns.add(rs.getString(1));
			}
			
			rs.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new StatusException(500);
		}
		
		sql.closeConnection();
		
		try(ResultSet rs = sql.executeQuery(String.format("call %s.get_all_response('%s');", sql.schema, tag)))
		{
			while(rs.next())
			{
				intent.responses.add(rs.getString(1));
			}
			
			rs.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new StatusException(500);
		}
		
		sql.closeConnection();
		
		return intent;
	}
}
