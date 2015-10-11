package de.wellnerbou.cliopt;

public class MissingReqiredOptionException extends RuntimeException {

	public MissingReqiredOptionException(final String message) {
		super(message);
	}
}
