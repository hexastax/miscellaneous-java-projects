package com.hexastax.jobrunner.factory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.hexastax.jobrunner.exception.ConfigurationException;
import com.hexastax.jobrunner.exception.CreateJobException;
import com.hexastax.jobrunner.exception.InvalidJobTypeException;
import com.hexastax.jobrunner.factory.JobFactory;
import com.hexastax.jobrunner.job.Job;
import com.hexastax.jobrunner.job.JobDefinition;
import com.hexastax.jobrunner.job.impl.ConcatenateValues;

/**
 * Tests the job factory.
 * 
 * @author dgoldenberg
 */
@SuppressWarnings("serial")
public class JobFactoryTest {

  private static final Map<String, String> EXPECTED_JOB_TYPES = new HashMap<String, String>() {
    {
      put("job-type-concat", "com.hexastax.jobrunner.job.impl.ConcatenateValues");
      put("job-type-reverse", "com.hexastax.jobrunner.job.impl.ReverseValues");
    }
  };

  private static final JobDefinition TEST_JOB_DEF = new JobDefinition(
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
    });

  /**
   * Tests the job factory.
   * 
   * @throws IOException
   * @throws InvalidJobTypeException
   * @throws CreateJobException
   * @throws ConfigurationException
   */
  @Test
  public void testJobFactory() throws IOException, InvalidJobTypeException, CreateJobException, ConfigurationException {
    // TODO: test invalid XML
    // TODO: test edge case where no job type defs are found
    // TODO: test edge case with duplicate job definitions

    final JobFactory jf = new JobFactory(new File("./conf/job-types.xml"));

    final Map<String, String> actualJobTypes = jf.getJobTypeMap();
    Assert.assertNotNull(actualJobTypes);
    Assert.assertEquals(EXPECTED_JOB_TYPES, actualJobTypes);

    final ConcatenateValues expectedJob = new ConcatenateValues();
    expectedJob.setDefinition(TEST_JOB_DEF);

    Job actualJob = jf.createJob(TEST_JOB_DEF);
    Assert.assertEquals(expectedJob, actualJob);
  }

}
