package com.hexastax.jobrunner.exception;

/**
 * Generic Job Runner exception.
 * 
 * @author dgoldenberg
 */
public class JobRunnerException extends Exception {

  private static final long serialVersionUID = -2021187575985443554L;

  public JobRunnerException(String message) {
    super(message);
  }
  
  public JobRunnerException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
