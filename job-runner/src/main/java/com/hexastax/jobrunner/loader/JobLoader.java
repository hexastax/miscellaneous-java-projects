package com.hexastax.jobrunner.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hexastax.jobrunner.exception.ConfigurationException;
import com.hexastax.jobrunner.exception.CreateJobException;
import com.hexastax.jobrunner.exception.InvalidJobTypeException;
import com.hexastax.jobrunner.factory.JobFactory;
import com.hexastax.jobrunner.job.Job;
import com.hexastax.jobrunner.job.JobDefinition;

/**
 * Loads job definitions into memory.
 * 
 * @author dgoldenberg
 */
public class JobLoader {

  private JobFactory jobFactory = null;
  private List<Job> jobs = new ArrayList<Job>();

  public JobLoader(JobFactory jobFactory) {
    this.jobFactory = jobFactory;
  }

  public List<Job> loadJobs(File jobDefDirectory) throws ConfigurationException {
    System.out.println(">> Fetching job definitions from: " + jobDefDirectory.getAbsolutePath() + "...");

    Collection<File> jobDefinitionFiles = FileUtils.listFiles(jobDefDirectory, new WildcardFileFilter("*.xml"), null);

    if (jobDefinitionFiles.isEmpty()) {
      System.out.println(">> No job definitions found.");
    } else {
      loadJobs(jobDefinitionFiles, jobs);
      System.out.println(">> Job fetching OK, #jobs: " + jobs.size());
    }

    return jobs;
  }

  public void clear() {
    jobs.clear();
  }

  /**
   * Reads in a set of runnable jobs, given the respective XML job definition files.
   * 
   * @param jobDefinitionFiles
   *          the job definition files
   * @param jobs
   *          the output list of jobs to load into
   * @throws ConfigurationException
   */
  public void loadJobs(Collection<File> jobDefinitionFiles, List<Job> jobs) throws ConfigurationException {
    SAXReader xmlReader = new SAXReader();

    // TODO: add option to load as many as possible (i.e. don't halt on error).
    try {
      for (File jobDefinitionFile : jobDefinitionFiles) {
        final JobDefinition jobDef = readJobDefinition(xmlReader, jobDefinitionFile);
        jobs.add(jobFactory.createJob(jobDef));
      }

    } catch (DocumentException | InvalidJobTypeException | CreateJobException ex) {
      throw new ConfigurationException("Error while fetching jobs to run.", ex);
    }
  }

  @SuppressWarnings("unchecked")
  private JobDefinition readJobDefinition(SAXReader xmlReader, File jobDefinitionFile) throws DocumentException {
    // TODO may need an option to read from an input stream rather than a file...
    // Can generally minimize usage of File in signatures and genericize to streams...

    final Document jobDefDoc = xmlReader.read(jobDefinitionFile);

    final Element elem = (Element) jobDefDoc.selectSingleNode("/job-definition");
    final String name = elem.element("name").getText();
    final String type = elem.element("type").getText();
    final String description = elem.element("description").getText();
    final List<Element> propElems = jobDefDoc.selectNodes("/job-definition/properties/property");
    final Map<String, String> properties = new LinkedHashMap<String, String>();
    if (propElems != null) {
      for (Element propElem : propElems) {
        properties.put(propElem.attributeValue("name"), propElem.attributeValue("value"));
      }
    }

    return new JobDefinition(name, type, description, properties);
  }
}
