package com.hexastax.jobrunner.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hexastax.jobrunner.exception.ConfigurationException;
import com.hexastax.jobrunner.factory.JobFactory;
import com.hexastax.jobrunner.job.Job;
import com.hexastax.jobrunner.job.JobResult;
import com.hexastax.jobrunner.loader.JobLoader;

/**
 * Runs a set of configurable jobs.
 * 
 * @author dgoldenberg
 */
public class JobRunner {

  private JobFactory jobFactory = null;
  private int numThreads;

  /**
   * Creates a job runner.
   * 
   * @param jobTypeDefLocation
   *          the path of the XML job type definition file
   * @param numThreadsToUse
   *          the number of threads to use
   * @throws IOException
   *           on an I/O error
   * @throws ConfigurationException
   *           on a configuration error
   */
  public JobRunner(File jobTypeDefLocation, int numThreadsToUse) throws IOException, ConfigurationException {
    jobFactory = new JobFactory(jobTypeDefLocation);
    numThreads = numThreadsToUse;
  }

  /**
   * Runs the jobs whose definitions are located in the passed in directory.
   * 
   * @param jobDefDirectory
   *          the directory where the job definitions are located
   * @return the list of results collected from all the executed jobs
   * @throws IOException
   * @throws ConfigurationException
   */
  public List<JobResult> runJobs(File jobDefDirectory) throws IOException, ConfigurationException {
    // TODO add a signature which takes a listener listening for the results so we don't collect
    // them all in memory necessarily...

    JobLoader loader = new JobLoader(jobFactory);
    List<Job> jobs = loader.loadJobs(jobDefDirectory);

    List<JobResult> results = null;

    if (jobs.isEmpty()) {
      System.out.println(">> Note: no job definitions were found.");
      results = new ArrayList<JobResult>();
    } else {
      System.out.println(">> Job fetching OK, #jobs: " + jobs.size());

      System.out.println(">> Running the jobs...");
      results = runJobs(jobs);
      System.out.println(">> Job Runner done.");
    }

    return results;
  }

  /**
   * Runs the jobs, given a set of job instances.
   * 
   * @param jobs
   *          the job
   * @return the job results
   */
  public List<JobResult> runJobs(List<Job> jobs) {
    List<JobResult> results = new ArrayList<JobResult>();

    ExecutorService taskExecutor = Executors.newFixedThreadPool(numThreads);
    CompletionService<JobResult> taskCompletionService = new ExecutorCompletionService<JobResult>(taskExecutor);

    int numSubmittedJobs = jobs.size();
    for (Job job : jobs) {
      taskCompletionService.submit(job);
    }
    for (int idx = 0; idx < numSubmittedJobs; idx++) {
      try {
        // This call blocks till at least one job is completed.
        Future<JobResult> result = taskCompletionService.take();

        // Process the job result.
        JobResult res = result.get();
        results.add(res);

      } catch (InterruptedException | ExecutionException ex) {
        // TODO log this
        ex.printStackTrace();
      }
    }

    taskExecutor.shutdown();

    return results;
  }

  // TODO replace sysouts with logging
}
