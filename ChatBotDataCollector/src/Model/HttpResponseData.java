package Model;

public class HttpResponseData 
{
	public String requestURL;
	public String protocolVersion;
	public int statusCode;
	public String reasonPhrase;
	public String rawBody;
	
	
	
	public HttpResponseData(String requestURL, String protocolVersion, int statusCode, String reasonPhrase,
			String rawBody) {
		super();
		this.requestURL = requestURL;
		this.protocolVersion = protocolVersion;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
		this.rawBody = rawBody;
	}

	public HttpResponseData(String protocolVersion, int statusCode, String reasonPhrase, String rawBody) {
		super();
		this.protocolVersion = protocolVersion;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
		this.rawBody = rawBody;
	}
	
	public HttpResponseData(String protocolVersion, String statusCode, String reasonPhrase, String rawBody) {
		super();
		this.protocolVersion = protocolVersion;
		this.statusCode = Integer.parseInt(statusCode);
		this.reasonPhrase = reasonPhrase;
		this.rawBody = rawBody;
	}

	@Override
	public String toString() {
		return "HttpResponseData [requestURL=" + requestURL + ", protocolVersion=" + protocolVersion + ", statusCode="
				+ statusCode + ", reasonPhrase=" + reasonPhrase + ", rawBody=" + rawBody + "]";
	}

	
	
}