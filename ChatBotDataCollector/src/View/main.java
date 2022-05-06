package View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.message.BasicNameValuePair;

import Model.ExceptionManager;
import Model.HttpResponseData;
import Model.Intent;
import Model.Manager;
import Model.StatusException;

/**
 * Servlet implementation class main
 */
@WebServlet("/rest/*")
public class main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TOKENHEADER = "access_token";
    Manager manager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public main() {
        super();
        // TODO Auto-generated constructor stub
        manager = new Manager();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		setAccessControlHeaders(response);
		PrintWriter pw = response.getWriter();
		String pathInfo = request.getPathInfo();
		String body = getBody(request);
		response.setContentType("application/json");
		
		try
		{
			if(pathInfo.contains("/intent"))
			{
				Object temp = tryGetParameter(request, "tag");
				String tag = temp == null ? "" : temp.toString();
				temp = tryGetParameter(request, "range");
				int range = Integer.parseInt(temp == null ? "0" : temp.toString());
				
				
				if(tag != null && tag.compareTo("") != 0)
				{
					Intent intent = manager.getIntent(tag);
					pw.append(manager.gson.toJson(intent).replace("\\u0027", "'").replace("\\u0026","&").replace("\\u003c", "<").replace("\\u003e", ">").replace("\\u003d", "=").replace("é", "e"));
				}
				else
				{
					LinkedList<Intent> intents = manager.getIntent(range);
					pw.append(manager.gson.toJson(intents).replace("\\u0027", "'").replace("\\u0026","&").replace("\\u003c", "<").replace("\\u003e", ">").replace("\\u003d", "=").replace("é", "e"));
				}
				
				return;
			}
			
			
			
			throw new StatusException(404);
		}
		catch(StatusException ex)
		{
			statusHandler(ex, response);
		}
		catch(Exception ex)
		{
			response.setStatus(500);
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		setAccessControlHeaders(response);
		PrintWriter pw = response.getWriter();
		String pathInfo = request.getPathInfo();
//		String body = getBody(request);
		response.setContentType("application/json");
		
		try
		{
			if(pathInfo.contains("/intent"))
			{
				manager.addIntent(getBody(request));
				response.setStatus(201);
				return;
			}
			
			if(pathInfo.contains("/answer"))
			{
//				Object temp = tryGetParameter(request, "question");
//				String question = temp == null ? "" : temp.toString();
				
				HttpResponseData hrs = manager.client.makePostRequest("/get_answer", null, getBody(request).trim());
				response.setStatus(hrs.statusCode);
				pw.append(hrs.rawBody);
				return;
			}
			
			if(pathInfo.contains("/train_model"))
			{
				Object temp = tryGetParameter(request, "range");
				int range = Integer.parseInt(temp == null ? "0" : temp.toString());
				
				String password = getBody(request).trim();
				if(password.compareTo("159357") != 0)
				{
					response.setStatus(403);
					return;
				}
				
				LinkedList<Intent> intents = manager.getIntent(range);
				String jsonBody = manager.gson.toJson(intents);
				manager.client.makePostRequest("/train", null, jsonBody);
				response.setStatus(201);
				return;
			}
			
			throw new StatusException(404);
		}
		catch(StatusException ex)
		{
			statusHandler(ex, response);
		}
		catch(Exception ex)
		{
			response.setStatus(500);
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		setAccessControlHeaders(response);
		PrintWriter pw = response.getWriter();
		String pathInfo = request.getPathInfo();
		String jsonBody = getBody(request);
		response.setContentType("application/json");
		
		try
		{
			if(pathInfo.compareTo("/sql") == 0)
			{
				manager.setSQL(jsonBody);
				pw.append(manager.checkServerStatus());
				response.setStatus(201);
				return;
			}
			
			throw new StatusException(404);
		}
		catch(StatusException ex)
		{
			statusHandler(ex, response);
		}
		catch(Exception ex)
		{
			response.setStatus(500);
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	//----------------------------------------------------------------------------------------------
	private String getBody(HttpServletRequest request) throws IOException
	{
		StringBuilder sb = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    try 
	    {
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	            sb.append(line).append('\n');
//	            this.wait(100);
	        }
	        
	        
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    }
	    finally 
	    {
	        reader.close();
	    }
	    
	    return sb.toString();
	}
	
	
	private void setAccessControlHeaders(HttpServletResponse resp) 
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Headers", "Content-Type,Authorization,Accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Content-Length,Connection");
		resp.addHeader("Access-Control-Expose-Headers", "Authorization,Access-Control-Allow-Origin,Access-Control-Allow-Credentials,Content-Type,Content-Length,Content-Encoding,Connection");
		resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");

		
		
//		resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
//		resp.addHeader("Access-Control-Max-Age", "1728000");
//		resp.setContentType("application/json");
	}
	
	private void statusHandler(Exception exception, HttpServletResponse res)
	{
		try 
		{
			ExceptionManager exceptionManager = manager.gson.fromJson(exception.getMessage(), ExceptionManager.class);
			
			if(exceptionManager == null || exceptionManager.code < 0)
			{
				res.sendError(500);
				return;
			}
			
			
			if(exceptionManager.message.compareTo("") != 0)
				res.sendError(exceptionManager.code, exceptionManager.message);
			else
				res.sendError(exceptionManager.code);
			
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String tryGetTokenFromHeader(HttpServletRequest request) throws StatusException
	{
		try
		{
			String token = request.getHeader(TOKENHEADER);
			
			if(token == null || token.compareTo("") == 0)
				throw new StatusException(401, "invalid or missing token");
			
			return token;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		throw new StatusException(401, "invalid or missing token");
	}
	
	private Object tryGetParameter(HttpServletRequest request, String parameterName)
	{
		try
		{
			Object obj = request.getParameter(parameterName);
			return obj;
		}
		catch(Exception ex)
		{
			return null;
		}
	}

}
