package com.hexastax.jobrunner.loader;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.hexastax.jobrunner.exception.ConfigurationException;
import com.hexastax.jobrunner.factory.JobFactory;
import com.hexastax.jobrunner.job.Job;
import com.hexastax.jobrunner.job.impl.ConcatenateValues;
import com.hexastax.jobrunner.job.impl.ReverseValues;
import com.hexastax.jobrunner.loader.JobLoader;

/**
 * Tests the general case of job loading.
 * 
 * @author dgoldenberg
 */
public class JobLoaderTest {

  /**
   * Tests the general case of job loading.
   * 
   * @throws IOException
   * @throws ConfigurationException
   */
  @SuppressWarnings("serial")
  @Test
  public void loadJobs() throws IOException, ConfigurationException {
    final JobFactory jf = new JobFactory(new File("./conf/job-types.xml"));

    JobLoader loader = new JobLoader(jf);
    List<Job> jobs = loader.loadJobs(new File("./jobs"));

    Job concatJob = new ConcatenateValues();
    concatJob.setJobName("Concatenate Property Values");
    concatJob.setDescription("Test job: concatenates the prop values");
    concatJob.setProperties(new LinkedHashMap<String, String>() {
      {
        put("prop1", "value1");
        put("prop2", "value2");
        put("prop3", "value3");
        put("prop4", "value4");
      }
    });

    Job reverseJob = new ReverseValues();
    reverseJob.setJobName("Reverse Order of Property Values");
    reverseJob.setDescription("Test job: prints the values in reverse order than the order below");
    reverseJob.setProperties(new LinkedHashMap<String, String>() {
      {
        put("prop1", "value1");
        put("prop2", "value2");
        put("prop3", "value3");
        put("prop4", "value4");
      }
    });

    Set<Job> expectedJobs = new HashSet<Job>();
    expectedJobs.add(concatJob);
    expectedJobs.add(reverseJob);

    Assert.assertNotNull(jobs);
    Assert.assertEquals(expectedJobs.size(), jobs.size());
    Assert.assertEquals(expectedJobs, new HashSet<Job>(jobs));
  }

}
