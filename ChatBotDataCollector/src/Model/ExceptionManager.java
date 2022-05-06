package Model;

import com.google.gson.Gson;

public class ExceptionManager 
{
	public int code = -1;
	public String message;
	
	public ExceptionManager(int code, String message)
	{
		this.code = code;
		this.message = message;
	}
	
	public static String toJson(int code, String message)
	{
		Gson gson = new Gson();
		ExceptionManager exceptionManager = new ExceptionManager(code, message);
		
		return gson.toJson(exceptionManager);
	}
	
	public static String toJson(int code, String message, Gson gson)
	{
		ExceptionManager exceptionManager = new ExceptionManager(code, message);
		
		return gson.toJson(exceptionManager);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
