package com.kona.winnowing;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.big_oh.algorithms.fingerprint.winnowing.WinnowingFingerprinter;
import net.big_oh.algorithms.fingerprint.winnowing.WinnowingTextTransformer;
import net.big_oh.algorithms.fingerprint.winnowing.WinnowingWhitespaceFilter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

// input documents dir
// document to check
// k - The size of the document k-grams that will be fingerprinted. Set k to the largest size that will not preclude the detection of "interesting" duplicates.
// t - The guarantee threshold. Duplicates of length t or greater are guaranteed to be detectable using the resulting document fingerprints.

// Sample usage:
// -corpus src/main/resources/corpus-documents
// -doc src/main/resources/input-documents/input-doc-0001.txt
// -hashAlg MD5 
// -k 3
// -t 100

// Sample output:
// RANKING:
//  >> 1 : Rank [docPath=src/main/resources/corpus-documents/text-doc-0001.txt, percentage=100.0]
//  >> 2 : Rank [docPath=src/main/resources/corpus-documents/text-doc-0002.txt, percentage=68.0]
//  >> 3 : Rank [docPath=src/main/resources/corpus-documents/text-doc-0003.txt, percentage=50.0]
//  >> 4 : Rank [docPath=src/main/resources/corpus-documents/text-doc-0005.txt, percentage=37.0]
//  >> 5 : Rank [docPath=src/main/resources/corpus-documents/text-doc-0004.txt, percentage=31.0]

/**
 * Winnowing is an algorithm for document fingerprinting which is used for such applications as for
 * example plagiarism detection.
 * <p>
 * This is elaborated on in detail by Schleimer, Wilkerson, and Aiken in their article
 * "Winnowing: local algorithms for document fingerprinting" (2003).
 * <p>
 * <code>WinnowRunner</code> performs document winnowing. It runs fingerprinting on the given
 * document corpus, then makes an assessment of how close the given input document is to any of the
 * corpus documents.
 * <p>
 * 
 * @author dgoldenberg
 */
public class WinnowRunner {

  private static final String OPTION_THRESHOLD = "t";
  private static final String OPTION_K_GRAM_SIZE = "k";
  private static final String OPTION_CORPUS_LOC = "corpus";
  private static final String OPTION_INPUT_DOC_LOC = "doc";
  private static final String OPTION_HASHING_ALGO = "hashAlg";

  private static final String OPTION_HELP_3 = "h";
  private static final String OPTION_HELP_2 = "?";
  private static final String OPTION_HELP_1 = "help";

  private static final String DEFAULT_HASHING_ALGO = "MD5";

  private Options options;

  @SuppressWarnings("unchecked")
  public void execute(String[] args) throws ParseException, NoSuchAlgorithmException, IOException {
    // Create the parser.
    CommandLineParser parser = new GnuParser();

    // Parse the command line arguments.
    CommandLine cmdLine = parser.parse(getOptions(), args);

    // Check if we're being asked for the usage.
    // (This should be an unlikely scenario since we should be running within Spark).
    if (isBeingAskedForHelp(cmdLine)) {
      usage();
    }

    // Check any mandatory args.
    checkMandatoryOptions(cmdLine);

    List<WinnowingWhitespaceFilter> filters = Arrays.asList(new TextCleaner());
    List<WinnowingTextTransformer> transformers = Collections.EMPTY_LIST;
    int k = Integer.parseInt(cmdLine.getOptionValue(OPTION_K_GRAM_SIZE));
    int t = Integer.parseInt(cmdLine.getOptionValue(OPTION_THRESHOLD));
    String hashingAlgo = cmdLine.getOptionValue(OPTION_HASHING_ALGO, DEFAULT_HASHING_ALGO);

    File corpusDir = new File(cmdLine.getOptionValue(OPTION_CORPUS_LOC));
    if (!corpusDir.isDirectory()) {
      throw new IllegalArgumentException("The corpus dir '" + corpusDir.getPath() + "' is invalid.");
    }
    List<File> corpusFiles = new ArrayList<File>();
    File[] files = corpusDir.listFiles();
    for (File f : files) {
      if (f.isFile()) {
        corpusFiles.add(f);
      }
    }
    if (corpusFiles.isEmpty()) {
      throw new IllegalArgumentException("The corpus dir '" + corpusDir.getPath() + "' has no input files.");
    }

    File inputFile = new File(cmdLine.getOptionValue(OPTION_INPUT_DOC_LOC));
    if (!inputFile.isFile()) {
      throw new IllegalArgumentException("Invalid input file '" + inputFile.getPath() + "'.");
    }

    runWinnowing(filters, transformers, k, t, hashingAlgo, corpusFiles, inputFile);
  }

  private void runWinnowing(
    List<WinnowingWhitespaceFilter> whitespaceFilters,
    List<WinnowingTextTransformer> transformers,
    int k,
    int t,
    String hashingAlgo,
    List<File> corpusFiles,
    File inputFile) throws IOException, NoSuchAlgorithmException {

    WinnowingFingerprinter wf = new WinnowingFingerprinter(whitespaceFilters, transformers, k, t, hashingAlgo);
    FingerprintedCorpus fc = new FingerprintedCorpus();
    for (File corpusFile : corpusFiles) {
      String text = FileUtils.readFileToString(corpusFile);
      fc.addDocumentInfo(new FingerprintedDoc(corpusFile.getAbsolutePath(), wf.fingerprint(text)));
    }

    String text = FileUtils.readFileToString(inputFile);
    Set<BigInteger> inputFileFingerpint = wf.fingerprint(text);
    FingerprintedDoc inDoc = new FingerprintedDoc(inputFile.getAbsolutePath(), inputFileFingerpint);
    System.out.println();
    System.out.println("==================================================");
    System.out.println("INPUT DOC:");
    System.out.println("==================================================");
    inDoc.print();
    System.out.println();

    // ------------------------------------------------------------------

    System.out.println();
    System.out.println("==================================================");
    System.out.println("CORPUS:");
    System.out.println("==================================================");
    System.out.println();
    for (FingerprintedDoc doc : fc.getFc()) {
      doc.print();
      System.out.println();
    }

    // ------------------------------------------------------------------
    System.out.println();
    System.out.println("==================================================");
    System.out.println("==================================================");
    System.out.println("RANKING:");

    List<Rank> ranking = fc.generateRanking(inDoc);
    int num = 1;
    for (Rank rank : ranking) {
      System.out.println("    >> " + num + " : " + rank);
      num++;
    }
  }

  @SuppressWarnings("static-access")
  private Options getOptions() {
    if (options == null) {
      options = new Options();

      Option help = new Option(OPTION_HELP_1, "Print this message.");
      Option help2 = new Option(OPTION_HELP_2, "Print this message.");
      Option help3 = new Option(OPTION_HELP_3, "Print this message.");
      Option kGramSize = OptionBuilder.withArgName("kGramSize")
        .hasArg()
        .withDescription("The size of the document k-grams that will be fingerprinted")
        .create(OPTION_K_GRAM_SIZE);
      Option guaranteeThreshold = OptionBuilder.withArgName("tGuaranteeThreshold")
        .hasArg()
        .withDescription("The guarantee threshold. Duplicates of length t or greater are guaranteed to be detectable using the resulting document fingerprints.")
        .create(OPTION_THRESHOLD);
      Option corpusDir = OptionBuilder.withArgName("corpusDirPath")
        .hasArg()
        .withDescription("The filepath to the corpus files location")
        .create(OPTION_CORPUS_LOC);
      Option inputDoc = OptionBuilder.withArgName("inputDocPath")
        .hasArg()
        .withDescription("The filepath to the document to check")
        .create(OPTION_INPUT_DOC_LOC);
      Option hashingAlgo = OptionBuilder.withArgName("hashingAlgo")
        .hasArg()
        .withDescription("Hashing algorithm: MD5, SHA1")
        .create(OPTION_HASHING_ALGO);

      options.addOption(help);
      options.addOption(help2);
      options.addOption(help3);
      options.addOption(kGramSize);
      options.addOption(guaranteeThreshold);
      options.addOption(corpusDir);
      options.addOption(inputDoc);
      options.addOption(hashingAlgo);
    }

    return options;
  }

  private boolean isBeingAskedForHelp(CommandLine cmdLine) {
    return cmdLine.hasOption(OPTION_HELP_1) || cmdLine.hasOption(OPTION_HELP_2) || cmdLine.hasOption(OPTION_HELP_3);
  }

  private void checkMandatoryOptions(CommandLine cmdLine) {
    if (!cmdLine.hasOption(OPTION_K_GRAM_SIZE)) {
      throw new IllegalArgumentException("No k-gram size was specified");
    }
    if (!cmdLine.hasOption(OPTION_THRESHOLD)) {
      throw new IllegalArgumentException("No threshold value was specified");
    }
    if (!cmdLine.hasOption(OPTION_CORPUS_LOC)) {
      throw new IllegalArgumentException("No corpus location directory was specified");
    }
    if (!cmdLine.hasOption(OPTION_INPUT_DOC_LOC)) {
      throw new IllegalArgumentException("No input document to check was specified");
    }
  }

  private void usage() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(this.getClass().getName(), getOptions());
    System.exit(1);
  }

  public static void main(String[] args) throws Exception {
    new WinnowRunner().execute(args);
  }
}
