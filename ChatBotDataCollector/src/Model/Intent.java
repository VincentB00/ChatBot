package Model;

import java.util.LinkedList;

public class Intent 
{
	public String tag;
	public LinkedList<String> patterns;
	public LinkedList<String> responses;
	
	public Intent(String tag)
	{
		this.tag = tag;
		patterns = new LinkedList<>();
		responses = new LinkedList<>();
	}
	
	public Intent(String tag, String[] patterns, String[] responses)
	{
		this.tag = tag;
		this.patterns = new LinkedList<>();
		this.responses = new LinkedList<>();
		
		for(String str : patterns)
			this.patterns.add(str);
		
		for(String str : responses)
			this.responses.add(str);
	}
	
	
}
