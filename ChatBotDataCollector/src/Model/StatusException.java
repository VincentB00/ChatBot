package Model;

import com.google.gson.Gson;

public class StatusException extends Exception
{
	public StatusException(int code, String message, Gson gson)
	{
		super(ExceptionManager.toJson(code, message, gson));
	}
	public StatusException(int code, String message)
	{
		super(ExceptionManager.toJson(code, message));
	}
	public StatusException(int code, Gson gson)
	{
		super(ExceptionManager.toJson(code, "", gson));
	}
	public StatusException(int code)
	{
		super(ExceptionManager.toJson(code, ""));
	}
}
