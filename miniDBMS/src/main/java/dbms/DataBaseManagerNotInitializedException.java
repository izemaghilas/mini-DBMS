package dbms;

public class DataBaseManagerNotInitializedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6718144951522223996L;
	
	public DataBaseManagerNotInitializedException() {
		super("DataBaseManager must be initialized");
	}
}
