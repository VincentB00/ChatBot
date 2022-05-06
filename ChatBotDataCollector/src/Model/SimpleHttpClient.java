package Model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javafx.util.Pair;

public class SimpleHttpClient 
{
	CloseableHttpClient httpClient; //= HttpClients.createDefault();
	public String defaultURL;
	
	public SimpleHttpClient(String defaultURL)
	{
		this.defaultURL = defaultURL;
	}
	
	public SimpleHttpClient()
	{
		
	}
	
	public HttpResponseData makeGetRequest(String host, String path, BasicNameValuePair[] querryParameters) throws URISyntaxException, ParseException, IOException
	{
		if(querryParameters == null)
			querryParameters = new BasicNameValuePair[0];
		
		HttpResponseData responseData;
		URI URI = new URIBuilder()
		        .setScheme("http")
		        .setHost(host)
		        .setPath(path)
		        .setParameters(querryParameters)
		        .build();
		
		HttpGet request = new HttpGet(URI);
		
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
	             CloseableHttpResponse response = httpClient.execute(request)) 
		{

            // Get HttpResponse Status
            String protocolVersion = response.getProtocolVersion().toString();             
            int statusCode = response.getStatusLine().getStatusCode();   
            String reasonPhrase = response.getStatusLine().getReasonPhrase(); 

            HttpEntity entity = response.getEntity();
            String body = null;
            if (entity != null)
                body = EntityUtils.toString(entity);
            
            responseData = new HttpResponseData(request.getURI().toString(), protocolVersion, statusCode, reasonPhrase, body);
        }
		
		return responseData;
	}
	
	public HttpResponseData makeGetRequest(String path, BasicNameValuePair[] querryParameters) throws URISyntaxException, ParseException, IOException
	{
		return makeGetRequest(this.defaultURL, path, querryParameters);
	}
	
	public HttpResponseData makePostRequest(String host, String path, BasicNameValuePair[] querryParameters, String rawBody) throws URISyntaxException, ParseException, IOException
	{
		if(querryParameters == null)
			querryParameters = new BasicNameValuePair[0];
		
		HttpResponseData responseData;
		URI URI = new URIBuilder()
		        .setScheme("http")
		        .setHost(host)
		        .setPath(path)
		        .setParameters(querryParameters)
		        .build();
		
		HttpPost request = new HttpPost(URI);
		
		request.setEntity(new StringEntity(rawBody));
		
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
	             CloseableHttpResponse response = httpClient.execute(request)) 
		{

            // Get HttpResponse Status
            String protocolVersion = response.getProtocolVersion().toString();             
            int statusCode = response.getStatusLine().getStatusCode();   
            String reasonPhrase = response.getStatusLine().getReasonPhrase(); 

            HttpEntity entity = response.getEntity();
            String body = null;
            if (entity != null)
                body = EntityUtils.toString(entity);
            
            responseData = new HttpResponseData(request.getURI().toString(), protocolVersion, statusCode, reasonPhrase, body);
        }
		
		return responseData;
	}
	
	public HttpResponseData makePostRequest(String path, BasicNameValuePair[] querryParameters, String rawBody) throws URISyntaxException, ParseException, IOException
	{
		return makePostRequest(this.defaultURL, path, querryParameters, rawBody);
	}
	
	public HttpResponseData makePutRequest(String host, String path, BasicNameValuePair[] querryParameters, String rawBody) throws URISyntaxException, ParseException, IOException
	{
		if(querryParameters == null)
			querryParameters = new BasicNameValuePair[0];
		
		HttpResponseData responseData;
		URI URI = new URIBuilder()
		        .setScheme("http")
		        .setHost(host)
		        .setPath(path)
		        .setParameters(querryParameters)
		        .build();
		
		HttpPut request = new HttpPut(URI);
		
		request.setEntity(new StringEntity(rawBody));
		
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
	             CloseableHttpResponse response = httpClient.execute(request)) 
		{

            // Get HttpResponse Status
            String protocolVersion = response.getProtocolVersion().toString();             
            int statusCode = response.getStatusLine().getStatusCode();   
            String reasonPhrase = response.getStatusLine().getReasonPhrase(); 

            HttpEntity entity = response.getEntity();
            String body = null;
            if (entity != null)
                body = EntityUtils.toString(entity);
            
            responseData = new HttpResponseData(request.getURI().toString(), protocolVersion, statusCode, reasonPhrase, body);
        }
		
		return responseData;
	}
	
	public HttpResponseData makePutRequest(String path, BasicNameValuePair[] querryParameters, String rawBody) throws URISyntaxException, ParseException, IOException
	{
		return makePutRequest(this.defaultURL, path, querryParameters, rawBody);
	}
	
	public HttpResponseData makeDeleteRequest(String host, String path, BasicNameValuePair[] querryParameters) throws URISyntaxException, ParseException, IOException
	{
		if(querryParameters == null)
			querryParameters = new BasicNameValuePair[0];
		
		HttpResponseData responseData;
		URI URI = new URIBuilder()
		        .setScheme("http")
		        .setHost(host)
		        .setPath(path)
		        .setParameters(querryParameters)
		        .build();
		
		HttpDelete request = new HttpDelete(URI);
		
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
	             CloseableHttpResponse response = httpClient.execute(request)) 
		{

            // Get HttpResponse Status
            String protocolVersion = response.getProtocolVersion().toString();             
            int statusCode = response.getStatusLine().getStatusCode();   
            String reasonPhrase = response.getStatusLine().getReasonPhrase(); 

            HttpEntity entity = response.getEntity();
            String body = null;
            if (entity != null)
                body = EntityUtils.toString(entity);
            
            responseData = new HttpResponseData(request.getURI().toString(), protocolVersion, statusCode, reasonPhrase, body);
        }
		
		return responseData;
	}
	
	public HttpResponseData makeDeleteRequest(String path, BasicNameValuePair[] querryParameters) throws URISyntaxException, ParseException, IOException
	{
		return makeGetRequest(this.defaultURL, path, querryParameters);
	}
}
