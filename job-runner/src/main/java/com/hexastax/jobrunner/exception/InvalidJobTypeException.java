package com.hexastax.jobrunner.exception;

/**
 * Thrown if an invalid job type is encountered.
 * 
 * @author dgoldenberg
 */
public class InvalidJobTypeException extends JobRunnerException {

  private static final long serialVersionUID = -3101731820181281874L;

  public InvalidJobTypeException(String message) {
    super(message);
  }

}
