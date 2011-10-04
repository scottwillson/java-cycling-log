package com.butlerpress.cyclinglog;

public class CyclingLogException extends RuntimeException {
	private static final long serialVersionUID = 1514887774184317572L;

public CyclingLogException() {
    super();
  }

  public CyclingLogException(String message) {
    super(message);
  }

  public CyclingLogException(String message, Throwable cause) {
    super(message, cause);
  }

  public CyclingLogException(Throwable cause) {
    super(cause);
  }
}
