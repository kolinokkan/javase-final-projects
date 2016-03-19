package com.jdc.phoneshop.utils;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public enum ErrorType {
		Info, Warning, Error
	}
	
	private ErrorType type;
	
	public ApplicationException(ErrorType type, String message) {
		super(message);
		this.type = type;
	}
	
	public ApplicationException(Throwable cause) {
		super(cause.getMessage(), cause);
		if(cause instanceof ApplicationException) {
			this.type = ((ApplicationException)cause).getType();
		} else {
			this.type = ErrorType.Error;
		}
	}

	public ErrorType getType() {
		return type;
	}


}
