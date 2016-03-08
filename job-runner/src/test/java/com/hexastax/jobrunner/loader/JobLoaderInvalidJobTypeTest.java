package com.hexastax.jobrunner.loader;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.hexastax.jobrunner.exception.ConfigurationException;
import com.hexastax.jobrunner.factory.JobFactory;
import com.hexastax.jobrunner.loader.JobLoader;

/**
 * Tests what happens if there is a job definition with an invalid (unknown) job type.
 * 
 * @author dgoldenberg
 * 
 */
public class JobLoaderInvalidJobTypeTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  /**
   * Tests what happens if there is a job definition with an invalid (unknown) job type.
   * 
   * @throws IOException
   * @throws ConfigurationException
   */
  @Test
  public void testEmptyJobFile() throws IOException, ConfigurationException {
    expectedEx.expect(ConfigurationException.class);
    // Note: can add a more specific assertion here.
    // The actual cause error message is in the cause exception: Invalid job type: 'junk'
    expectedEx.expectMessage("Error while fetching jobs to run");

    final JobFactory jf = new JobFactory(new File("./conf/job-types.xml"));

    JobLoader loader = new JobLoader(jf);
    loader.loadJobs(new File("./jobs-err-2"));
  }

}
