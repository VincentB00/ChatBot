package Model;

public class SQLData 
{
	public String ip;
	public int port;
	public String username;
	public String password;
	public String schema;
	
	public SQLData(String ip, int port, String username, String password, String schema) {
		super();
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
		this.schema = schema;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	
}
