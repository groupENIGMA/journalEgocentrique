package journalEgocentrique.model;

public class ConnectionException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionException(){}
	
	public ConnectionException(String msg){
		super(msg);
	}
}
