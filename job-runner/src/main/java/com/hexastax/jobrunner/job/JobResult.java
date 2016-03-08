package com.hexastax.jobrunner.job;

/**
 * Represents the result of running of a single job.
 * 
 * @author dgoldenberg
 */
public class JobResult {

  private String result;
  private String jobName;

  /**
   * Creates a job result
   * 
   * @param jobName
   *          the job name
   * @param result
   *          the job result
   */
  public JobResult(String jobName, String result) {
    this.jobName = jobName;
    this.result = result;
  }

  /**
   * @return the job name
   */
  public String getJobName() {
    return jobName;
  }

  /**
   * @return the job result
   */
  public String getResult() {
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((jobName == null)
      ? 0 : jobName.hashCode());
    result = prime * result + ((this.result == null)
      ? 0 : this.result.hashCode());
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
    JobResult other = (JobResult) obj;
    if (jobName == null) {
      if (other.jobName != null)
        return false;
    } else if (!jobName.equals(other.jobName))
      return false;
    if (result == null) {
      if (other.result != null)
        return false;
    } else if (!result.equals(other.result))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "JobResult [result=" + result + ", jobName=" + jobName + "]";
  }
}
