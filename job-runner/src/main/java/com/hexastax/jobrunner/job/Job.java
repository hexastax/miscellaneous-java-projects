package com.hexastax.jobrunner.job;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Represents common functionality and attributes of a job at an abstract level.
 * 
 * @author dgoldenberg
 */
public abstract class Job implements Callable<JobResult> {

  protected String jobName = null;
  protected String description = null;
  protected Map<String, String> properties = null;

  /**
   * Constructs a job object.
   */
  public Job() {
  }

  /**
   * Sets the job definition.
   * 
   * @param def
   *          the job definition
   */
  public void setDefinition(JobDefinition def) {
    this.jobName = def.getJobName();
    this.description = def.getDescription();
    this.properties = def.getProperties();
  }

  @Override
  public JobResult call() throws Exception {
    return computeResult();
  }

  /**
   * @return the result of the job's execution
   */
  protected abstract JobResult computeResult();

  /**
   * @return the job name
   */
  public String getJobName() {
    return jobName;
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

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
    result = prime * result + ((properties == null) ? 0 : properties.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Job other = (Job) obj;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (jobName == null) {
      if (other.jobName != null)
        return false;
    } else if (!jobName.equals(other.jobName))
      return false;
    if (properties == null) {
      if (other.properties != null)
        return false;
    } else if (!properties.equals(other.properties))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Job [jobName=" + jobName + ", description=" + description + ", properties=" + properties + "]";
  }

}
