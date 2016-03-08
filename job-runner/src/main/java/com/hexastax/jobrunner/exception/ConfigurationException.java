package com.hexastax.jobrunner.exception;

/**
 * Thrown upon a configuration error.
 * 
 * @author dgoldenberg
 */
public class ConfigurationException extends JobRunnerException {

  private static final long serialVersionUID = 4700027309427375357L;

  public ConfigurationException(String message) {
    super(message);
  }

  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

}
