package journalEgocentrique.model;

public class InvalidOperationException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidOperationException(){}
	
	public InvalidOperationException(String msg){
		super(msg);
	}
}
