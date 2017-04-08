package ru.vyukov.pojocli;

/**
 * General parsing exception
 * @author gelo
 *
 */
public class ParseException extends Exception {

	private static final long serialVersionUID = -6717332679564291741L;

	public ParseException() {
		super();
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(Throwable cause) {
		super(cause);
	}

}
