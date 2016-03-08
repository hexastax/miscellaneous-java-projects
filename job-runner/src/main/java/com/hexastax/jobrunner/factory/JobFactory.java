package com.hexastax.jobrunner.factory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hexastax.jobrunner.exception.ConfigurationException;
import com.hexastax.jobrunner.exception.CreateJobException;
import com.hexastax.jobrunner.exception.InvalidJobTypeException;
import com.hexastax.jobrunner.job.Job;
import com.hexastax.jobrunner.job.JobDefinition;

/**
 * Serves as a factory for creating runnable jobs.
 * 
 * @author dgoldenberg
 */
public class JobFactory {

  private Map<String, String> jobTypeMap = new HashMap<String, String>();

  /**
   * Creates an initializes the factory.
   * 
   * @param jobTypeRegistryLocation
   *          the filepath to the XML job type definition file
   * @throws IOException
   *           on an I/O error
   * @throws ConfigurationException 
   */
  @SuppressWarnings("unchecked")
  public JobFactory(File jobTypeRegistryLocation) throws IOException, ConfigurationException {
    SAXReader xmlReader = new SAXReader();
    try {
      Document jobTypeDefDoc = xmlReader.read(jobTypeRegistryLocation);
      List<Element> elems = jobTypeDefDoc.selectNodes("/job-types/job-type");
      
      if (elems == null || elems.isEmpty()) {
        throw new ConfigurationException(
            "No job type definitions found in job type configuration file: '" + jobTypeRegistryLocation.getAbsolutePath() + ".");
      }
      
      for (Element elem : elems) {
        String typeName = elem.element("job-type-name").getText();
        String implClass = elem.element("impl-class").getText();
        
        if (jobTypeMap.containsKey(typeName)) {
          throw new ConfigurationException("Duplicate job type definitions detected for type '" + typeName + "'.");
        }
        
        jobTypeMap.put(typeName, implClass);
      }
    } catch (DocumentException ex) {
      throw new IOException("Error while reading job type definitions from: '" + jobTypeRegistryLocation + "'.", ex);
    }
  }

  /**
   * Creates an instance of a job.
   * 
   * @param def
   *          the job definition
   * @return an instance of the job
   * @throws InvalidJobTypeException
   *           if the job type is not known to the factory
   * @throws CreateJobException
   *           if the factory fails to create an instance of the job
   */
  public Job createJob(JobDefinition def) throws InvalidJobTypeException, CreateJobException {
    Job job = null;
    String jobType = def.getType();
    String implClass = jobTypeMap.get(jobType);
    if (implClass == null) {
      throw new InvalidJobTypeException("Invalid job type: '" + jobType + "'.");
    }

    try {
      job = (Job) Class.forName(implClass).newInstance();
      job.setDefinition(def);
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex ) {
      throw new CreateJobException("Error while creating an instance of job, impl class: " + implClass, ex);
    }

    return job;
  }

  /**
   * @return the map of job types to respective job implementation class names
   */
  public Map<String, String> getJobTypeMap() {
    return jobTypeMap;
  }

}
