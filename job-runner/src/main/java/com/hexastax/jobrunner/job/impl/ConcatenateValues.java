package com.hexastax.jobrunner.job.impl;

import java.util.Map;

import com.hexastax.jobrunner.job.Job;
import com.hexastax.jobrunner.job.JobResult;

/**
 * An example of a simple job.
 * <p>
 * Concatenates together the property values configured on the job definition, using hyphen as a
 * separator character.
 * 
 * @author dgoldenberg
 */
public class ConcatenateValues extends Job {

  public ConcatenateValues() {
  }

  @Override
  protected JobResult computeResult() {
    StringBuilder buff = new StringBuilder();
    boolean isFirst = true;
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      if (isFirst) {
        buff.append(entry.getValue());
        isFirst = false;
      } else {
        buff.append('-').append(entry.getValue());
      }
    }

    JobResult res = new JobResult(jobName, buff.toString());
    return res;
  }

}
