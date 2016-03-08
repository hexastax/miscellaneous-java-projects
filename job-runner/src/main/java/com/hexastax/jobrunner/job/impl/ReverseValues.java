package com.hexastax.jobrunner.job.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.hexastax.jobrunner.job.Job;
import com.hexastax.jobrunner.job.JobResult;

/**
 * An example of a simple job.
 * <p>
 * Generates a concatenated string from the property values in the job's definition; the values are
 * added in reverse order of their occurrence in the job definition; comma is used as the separator
 * character.
 * 
 * @author dgoldenberg
 */
public class ReverseValues extends Job {

  public ReverseValues() {
  }

  @Override
  protected JobResult computeResult() {
    List<String> values = new ArrayList<String>();
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      values.add(entry.getValue());
    }
    Collections.reverse(values);

    StringBuilder buff = new StringBuilder();
    boolean isFirst = true;
    for (String value : values) {
      if (isFirst) {
        buff.append(value);
        isFirst = false;
      } else {
        buff.append(',').append(value);
      }
    }

    JobResult res = new JobResult(jobName, buff.toString());
    return res;
  }
}
