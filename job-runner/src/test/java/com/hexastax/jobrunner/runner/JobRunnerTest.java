package com.hexastax.jobrunner.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.hexastax.jobrunner.exception.ConfigurationException;
import com.hexastax.jobrunner.job.Job;
import com.hexastax.jobrunner.job.JobDefinition;
import com.hexastax.jobrunner.job.JobResult;
import com.hexastax.jobrunner.job.impl.ConcatenateValues;
import com.hexastax.jobrunner.job.impl.ReverseValues;
import com.hexastax.jobrunner.runner.JobRunner;

/**
 * Tests the Job Runner.
 * 
 * @author DG
 * 
 */
@SuppressWarnings("serial")
public class JobRunnerTest {

  private static final Set<JobResult> EXPECTED_RESULTS = new HashSet<JobResult>() {
    {
      add(new JobResult("Reverse Order of Property Values", "value4,value3,value2,value1"));
      add(new JobResult("Concatenate Property Values", "value1-value2-value3-value4"));
    }
  };

  /**
   * Tests the job runner in more of an integration pattern, with a simple configuration.
   * 
   * @throws IOException
   * @throws ConfigurationException
   */
  @Test
  public void testJobRunner() throws IOException, ConfigurationException {
    final JobRunner jobRunner = new JobRunner(new File("./conf/job-types.xml"), 3);
    final List<JobResult> results = jobRunner.runJobs(new File("./jobs"));
    checkResults(results);
  }

  /**
   * Tests the job runner more at the API level, with simple jobs created on the fly.
   * 
   * @throws IOException
   * @throws ConfigurationException
   */
  @Test
  public void testJobRunnerInternal() throws IOException, ConfigurationException {
    final JobRunner jobRunner = new JobRunner(new File("./conf/job-types.xml"), 3);

    List<Job> testJobs = new ArrayList<Job>();
    ConcatenateValues concat = new ConcatenateValues();
    concat.setDefinition(
        new JobDefinition(
            "Concatenate Property Values",
            "job-type-concat",
            "Test job: concatenates the prop values",
            new LinkedHashMap<String, String>() {
              {
                put("type1-prop1", "value1");
                put("type1-prop2", "value2");
                put("type1-prop3", "value3");
                put("type1-prop4", "value4");
              }
            }));
    testJobs.add(concat);

    ReverseValues reverse = new ReverseValues();
    reverse.setDefinition(
        new JobDefinition(
            "Reverse Order of Property Values",
            "job-type-reverse",
            "Test job: prints the values in reverse order than the order below",
            new LinkedHashMap<String, String>() {
              {
                put("type1-prop1", "value1");
                put("type1-prop2", "value2");
                put("type1-prop3", "value3");
                put("type1-prop4", "value4");
              }
            }));
    testJobs.add(reverse);

    final List<JobResult> results = jobRunner.runJobs(testJobs);
    checkResults(results);
  }

  private void checkResults(final List<JobResult> results) {
    Assert.assertNotNull(results);
    Assert.assertEquals(2, results.size());

    Set<JobResult> actualResults = new HashSet<JobResult>(results);
    Assert.assertEquals(EXPECTED_RESULTS, actualResults);
  }

}
