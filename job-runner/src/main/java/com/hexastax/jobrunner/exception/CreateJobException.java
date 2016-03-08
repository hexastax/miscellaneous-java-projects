package com.hexastax.jobrunner.exception;

/**
 * Thrown upon an error while creating a job.
 * 
 * @author dgoldenberg
 */
public class CreateJobException extends JobRunnerException {

  private static final long serialVersionUID = 4700027309427375357L;

  public CreateJobException(String message, Throwable cause) {
    super(message, cause);
  }

}
