package com.hexastax.jobrunner.runner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;

import com.hexastax.jobrunner.exception.ConfigurationException;
import com.hexastax.jobrunner.job.JobResult;
import com.hexastax.jobrunner.util.JrStringUtils;

/**
 * Command-line utility for running jobs.
 * 
 * @author dgoldenberg
 */
public class JobRunnerCmd {

  private static final String OPTION_NUM_THREADS = "nt";
  private static final String OPTION_JOB_TYPE_DEF_LOC = "types";
  private static final String OPTION_JOB_DEF_LOC = "jobs";

  private static final String ARG_HELP = "help";
  private static final String ARG_HELP_H = "h";
  private static final String ARG_HELP_QMARK = "?";
  private static final String ARG_NUM_THREADS = "numThreads";
  private static final String ARG_JOB_TYPE_DEF_LOC = "jobTypeDefLoc";
  private static final String ARG_JOB_DEF_LOC = "jobDefLoc";

  private static final int DEFAULT_NUM_THREADS = 5;

  private static Options options = null;

  /**
   * Main for the utility.
   * 
   * @param args
   *          the arguments
   * @throws IOException
   * @throws ParseException
   * @throws ConfigurationException
   */
  public static void main(String[] args) throws IOException, ParseException, ConfigurationException {

    final CommandLineParser parser = new GnuParser();
    final CommandLine cmdLine = parser.parse(getOptions(), args, true);

    if (args.length < 1 || cmdLine.hasOption(ARG_HELP) || cmdLine.hasOption(ARG_HELP_QMARK) || cmdLine.hasOption(ARG_HELP_H)) {
      usage();
    }

    final Object[] opts = processOptions(cmdLine);
    final File jobTypeDefLocation = (File) opts[0];
    final File jobDefDirectory = (File) opts[1];
    final int numThreadsToUse = (int) opts[2];
    final JobRunner jobRunner = new JobRunner(jobTypeDefLocation, numThreadsToUse);

    System.out.println(">> Starting the Job Runner...");
    long start = System.currentTimeMillis();

    List<JobResult> results = jobRunner.runJobs(jobDefDirectory);
    for (JobResult result : results) {
      System.out.println("    >> Job '" + result.getJobName() + "'. Result : " + result.getResult());
    }

    long finish = System.currentTimeMillis();
    System.out.println(">> All finished.");
    System.out.println(">> Execution time: " + JrStringUtils.millisecondsToString(finish - start));
  }

  private static Object[] processOptions(CommandLine cmdLine) {
    int numThreadsToUse = DEFAULT_NUM_THREADS;

    final String jobTypeDefLoc = cmdLine.getOptionValue(OPTION_JOB_TYPE_DEF_LOC);
    if (StringUtils.isBlank(jobTypeDefLoc)) {
      throw new IllegalArgumentException("No valid job type definitions file location was provided.");
    }
    File jobTypeDefLocation = new File(jobTypeDefLoc);
    if (!jobTypeDefLocation.exists()) {
      throw new IllegalArgumentException("Invalid job type definitions file location was provided: '" + jobTypeDefLoc + "' does not exist.");
    }
    if (!jobTypeDefLocation.isFile()) {
      throw new IllegalArgumentException("Invalid job type definitions file location was provided: '" + jobTypeDefLoc + "' is not a file.");
    }

    final String jobDefLoc = cmdLine.getOptionValue(OPTION_JOB_DEF_LOC);
    if (StringUtils.isBlank(jobDefLoc)) {
      throw new IllegalArgumentException("No valid job definitions directory location was provided.");
    }
    File jobDefDirectory = new File(jobDefLoc);
    if (!jobDefDirectory.exists()) {
      throw new IllegalArgumentException("Invalid job definitions directory was provided: '" + jobDefLoc + "' does not exist.");
    }
    if (!jobDefDirectory.isDirectory()) {
      throw new IllegalArgumentException("Invalid job definitions directory was provided: '" + jobDefLoc + "' is not a directory.");
    }

    final String threads = cmdLine.getOptionValue(OPTION_NUM_THREADS);
    if (threads != null) {
      numThreadsToUse = Integer.parseInt(threads);
    }

    return new Object[] { jobTypeDefLocation, jobDefDirectory, numThreadsToUse };
  }

  private static void usage() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(JobRunnerCmd.class.getName(), getOptions());
    System.exit(1);
  }

  @SuppressWarnings("static-access")
  private static Options getOptions() {
    if (options == null) {
      options = new Options();

      Option help = new Option(ARG_HELP, "Print this message.");
      Option help2 = new Option(ARG_HELP_QMARK, "Print this message.");
      Option help3 = new Option(ARG_HELP_H, "Print this message.");
      Option numThreads = OptionBuilder.withArgName(ARG_NUM_THREADS).hasArg().withDescription("Optional number of threads to use, defaults to " + DEFAULT_NUM_THREADS + ".").create(OPTION_NUM_THREADS);
      Option jobTypeDefLoc = OptionBuilder.withArgName(ARG_JOB_TYPE_DEF_LOC).hasArg().withDescription("Mandatory path to the job type definitions XML file.").create(OPTION_JOB_TYPE_DEF_LOC);
      Option jobDefLoc = OptionBuilder.withArgName(ARG_JOB_DEF_LOC).hasArg().withDescription("Mandatory path to the job definitions directory.").create(OPTION_JOB_DEF_LOC);

      options.addOption(help);
      options.addOption(help2);
      options.addOption(help3);
      options.addOption(numThreads);
      options.addOption(jobTypeDefLoc);
      options.addOption(jobDefLoc);
    }
    return options;
  }

}
