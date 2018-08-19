package br.com.jonyfs.user;

public class EmailExistsException extends RuntimeException {

	private static final long serialVersionUID = -1477743203987346532L;

	public EmailExistsException(String message) {
		super(message);
	}
}
