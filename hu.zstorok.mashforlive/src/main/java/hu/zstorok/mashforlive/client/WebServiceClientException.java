package hu.zstorok.mashforlive.client;

/**
 * Custom runtime exception subclass to indicate an error in the communication with various web services.
 * 
 * @author zstorok
 */
public class WebServiceClientException extends RuntimeException {

	private static final long serialVersionUID = 2318236463823443827L;

	public WebServiceClientException() {
		super();
	}

	public WebServiceClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebServiceClientException(String message) {
		super(message);
	}

	public WebServiceClientException(Throwable cause) {
		super(cause);
	}

}
