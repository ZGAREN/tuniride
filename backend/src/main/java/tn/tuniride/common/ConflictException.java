package tn.tuniride.common;

/** The request clashes with existing data (e.g. a duplicate phone). Mapped to HTTP 409. */
public class ConflictException extends RuntimeException {

	public ConflictException(String message) {
		super(message);
	}

}
