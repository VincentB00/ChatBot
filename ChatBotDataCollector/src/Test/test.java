package Test;

import com.google.gson.Gson;

import Model.Intent;
import Model.SimpleHttpClient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.github.javafaker.Faker;
import com.google.gson.JsonElement;
import javafx.util.Pair;

public class test {

	public static void main(String[] args) throws ParseException, URISyntaxException, IOException 
	{
//		HashMap<String, Intent> map = new HashMap<>();
//		Gson gson = new Gson();
//		map.put("hello", new Intent("hello", new String[] {"hello"}, new String[] {"hi"}));
//		map.put("bye", new Intent("bye", new String[] {"bye"}, new String[] {"bye"}));
//		
//		
//		System.out.println(gson.toJson(map));
		setUpDefault();
	}
	
	public static void setUpDefault() throws ParseException, URISyntaxException, IOException
	{
		String url = "vincentprivatenas.mynetgear.com:7070/ChatBotDataCollector/rest";
//		String url = "localhost:8080/ChatBotDataCollector/rest";
		
		SimpleHttpClient client = new SimpleHttpClient(url);
		
		String body = "{\"ip\":\"192.168.1.6\",\"port\":3308,\"username\":\"vbui\",\"password\":\"123456\",\"schema\":\"ChatBot\"}";
		
		System.out.println(client.makePutRequest("/sql", null, body));
		
//		body = "{\"ip\":\"192.168.1.6\",\"port\":21,\"username\":\"frontEnd\",\"password\":\"123456\",\"defaultFolderPath\":\"/FrontEndServer/Public/file_manager/reGood\"}";
//		
//		System.out.println(client.makePutRequest("/ftp", null, body));
	}
}
