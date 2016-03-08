package com.hexastax.jobrunner.job;

import java.util.Map;

/**
 * Represents a job definition which is the blueprint for an actual runnable job (i.e. the various
 * needed metadata).
 * 
 * @author dgoldenberg
 * 
 */
public class JobDefinition {

  private String jobName = null;
  private String type = null;
  private String description = null;
  private Map<String, String> properties = null;

  /**
   * Creates a job definition.
   * 
   * @param jobName
   *          the job name
   * @param type
   *          the job type
   * @param description
   *          the job description
   * @param properties
   *          the job properties
   */
  public JobDefinition(String jobName, String type, String description, Map<String, String> properties) {
    this.jobName = jobName;
    this.type = type;
    this.description = description;
    this.properties = properties;
  }

  /**
   * @return the job name
   */
  public String getJobName() {
    return jobName;
  }

  /**
   * @return the job type
   */
  public String getType() {
    return type;
  }

  /**
   * @return the job description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the job properties
   */
  public Map<String, String> getProperties() {
    return properties;
  }

}
