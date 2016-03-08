package com.hexastax.wordsearcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Tests the word searcher.
 * 
 * @author dgoldenberg
 */
@RunWith(Parameterized.class)
public class WordSearcherTest {

  private static final boolean DEBUG = false;

  private char[][] letterBoard;
  private String word;
  private Set<WordOccurrence> expectedOccurrences;
  private String useCase;

  @Parameterized.Parameters
  public static Iterable<Object[]> getData() {
    return Arrays.asList(new Object[][] {

      // Multiple occurrences
      { new char[][] {
        { 'R', 'S', 'C', 'L', 'S' },
        { 'D', 'C', 'O', 'P', 'E' },
        { 'G', 'N', 'T', 'E', 'P' },
        { 'I', 'A', 'E', 'P', 'O' },
        { 'L', 'M', 'I', 'D', 'C' } },
        "COPE",
        makeOccSet(
          makeOcc("0, 2", "1, 2", "1, 3", "2, 3"),
          makeOcc("0, 2", "1, 2", "1, 3", "1, 4"),
          makeOcc("1, 1", "1, 2", "1, 3", "2, 3"),
          makeOcc("1, 1", "1, 2", "1, 3", "1, 4"),
          makeOcc("4, 4", "3, 4", "2, 4", "1, 4"),
          makeOcc("4, 4", "3, 4", "2, 4", "2, 3"),
          makeOcc("4, 4", "3, 4", "3, 3", "2, 3"),
          makeOcc("4, 4", "3, 4", "3, 3", "3, 2")),
        "Multiple occurrences" },

      // Single occurrence
      { new char[][] {
        { 'R', 'S', 'C', 'L', 'S' },
        { 'D', 'X', 'O', 'P', 'E' } },
        "COPE",
        makeOccSet(makeOcc("0, 2", "1, 2", "1, 3", "1, 4")),
        "singleOccurrence" },

      // No infinite loop
      { new char[][] {
        { 'R', 'E', 'C', 'L', 'S' },
        { 'D', 'P', 'O', 'P', 'X' },
        { 'G', 'N', 'T', 'E', 'P' },
        { 'I', 'A', 'E', 'P', 'O' }, },
        "COPEC",
        makeOccSet(makeOcc("0, 2", "1, 2", "1, 1", "0, 1", "0, 2")),
        "No infinite loop" },

      // Empty board
      { new char[][] {},
        "COPE",
        makeOccSet(),
        "Empty board" },

      // No occurrences
      { new char[][] {
        { 'R', 'E', 'C', 'L', 'S' },
        { 'D', 'P', 'O', 'P', 'E' } },
        "NONE",
        makeOccSet(),
        "No occurrences" },

      // Check all directions
      { new char[][] {
        { 'E', 'P', 'O', 'P', 'E' },
        { 'R', 'E', 'C', 'L', 'S' },
        { 'E', 'P', 'O', 'P', 'E' } },
        "COPE",
        makeOccSet(
          makeOcc("1, 2", "2, 2", "2, 3", "2, 4"),
          makeOcc("1, 2", "2, 2", "2, 1", "1, 1"),
          makeOcc("1, 2", "2, 2", "2, 1", "2, 0"),
          makeOcc("1, 2", "0, 2", "0, 3", "0, 4"),
          makeOcc("1, 2", "0, 2", "0, 1", "1, 1"),
          makeOcc("1, 2", "0, 2", "0, 1", "0, 0")),
        "Check all directions" },
    });
  }

  public WordSearcherTest(char[][] letterBoard, String word, Set<WordOccurrence> expectedOccurrences, String useCase) {
    this.letterBoard = letterBoard;
    this.word = word;
    this.expectedOccurrences = expectedOccurrences;
    this.useCase = useCase;
  }

  @Test
  public void testWordSearcher() {
    Set<WordOccurrence> actualOccurrences = WordSearcher.findAllOccurrences(letterBoard, word);
    if (DEBUG) {
      System.out.println("============================================================");
      System.out.println(useCase + ":");
      System.out.println("Occurrences of the word '" + word + "':");
      if (actualOccurrences.isEmpty()) {
        System.out.println("==> None found.");
      } else {
        int i = 1;
        List<Character> currWord = new ArrayList<Character>();
        for (WordOccurrence occurrence : actualOccurrences) {
          for (Coordinate coord : occurrence) {
            currWord.add(letterBoard[coord.getRowNum()][coord.getColNum()]);
          }
          System.out.println(i + ":  " + occurrence + " -> " + currWord);
          currWord.clear();
          i++;
        }
        System.out.println("============================================================\n");
      }
    } else {
      Assert.assertEquals(expectedOccurrences, actualOccurrences);
    }
  }

  // Creates a word occurrence from N coordinates represented as strings of the form "X, Y".
  private static WordOccurrence makeOcc(String... strCoords) {
    WordOccurrence occ = new WordOccurrence();
    if (strCoords != null) {
      for (String strCoord : strCoords) {
        occ.add(Coordinate.makeCoord(strCoord));
      }
    }
    return occ;
  }

  // Creates a set of word occurrences
  private static Set<WordOccurrence> makeOccSet(WordOccurrence... occurrences) {
    Set<WordOccurrence> occSet = new LinkedHashSet<WordOccurrence>();
    if (occurrences != null) {
      for (WordOccurrence occurrence : occurrences) {
        occSet.add(occurrence);
      }
    }
    return occSet;
  }
}
