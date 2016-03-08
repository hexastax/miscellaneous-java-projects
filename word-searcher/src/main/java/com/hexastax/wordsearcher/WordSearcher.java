package com.hexastax.wordsearcher;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Given a rectangular board filled with letters, find all the occurrences of the specified input
 * word on the board.
 * <p>
 * Rules:
 * <ul>
 * <li>The letters of the word must be contiguous.
 * <li>Letters of the word may go left to right, right to left, bottom to top, and top to bottom.
 * <li>Letters of the word may not go diagonally.
 * <li>Letters of the word may make turns, e.g. a left or right turn, going up or down.
 * </ul>
 * Output:
 * <p>
 * Print out all of the word's occurrences, each as a list of coordinates on the board, with each
 * letter represented as a tuple of { x, y } coordinates (0-based).
 * 
 * @author dgoldenberg
 */
public class WordSearcher {

  /**
   * Finds all occurrences of a given word on the rectangular letter board.
   * 
   * @param letterBoard
   *          the letter board
   * @param word
   *          the word to look for
   * @return the word occurrences, each as a list of coordinates of the word's letters on the board
   */
  public static Set<WordOccurrence> findAllOccurrences(char[][] letterBoard, String word) {
    // Perform some sanity checks.
    if (letterBoard == null || word == null || word.isEmpty()) {
      return Collections.emptySet();
    }

    Set<WordOccurrence> occurrences = new LinkedHashSet<WordOccurrence>();
    WordOccurrence occurrenceCandidate = new WordOccurrence();

    // For each row of the letter board
    for (int row = 0; row < letterBoard.length; row++) {
      // Just in case, account for "holes" in the board
      if (letterBoard[row] == null) {
        continue;
      }
      // For each column in the row
      for (int col = 0; col < letterBoard[row].length; col++) {
        performDepthFirstSearch(letterBoard, row, col, word, 0, occurrences, occurrenceCandidate);
      }
    }
    return occurrences;
  }

  // Performs a depth-first search
  private static void performDepthFirstSearch(
    char[][] letterBoard,
    int row,
    int col,
    String word,
    int wordIndex,
    Set<WordOccurrence> occurrences,
    WordOccurrence occurrenceCandidate) {

    // Make sure we stay within the board
    if (row >= letterBoard.length) {
      return;
    }
    if (row < 0) {
      return;
    }
    if (col >= letterBoard[row].length) {
      return;
    }
    if (col < 0) {
      return;
    }

    // Check if we've exhausted the word
    if (wordIndex >= word.length()) {
      return;
    }

    // See if a given letter on the board matches the letter in the word
    if (letterBoard[row][col] != word.charAt(wordIndex)) {
      return;
    }

    if (wordIndex == word.length() - 1) {
      WordOccurrence occurrenceToReturn = new WordOccurrence(occurrenceCandidate);
      occurrenceToReturn.add(new Coordinate(row, col));
      occurrences.add(occurrenceToReturn);
    } else {
      occurrenceCandidate.add(new Coordinate(row, col));

      performDepthFirstSearch(letterBoard, row + 1, col, word, wordIndex + 1, occurrences, occurrenceCandidate);
      performDepthFirstSearch(letterBoard, row - 1, col, word, wordIndex + 1, occurrences, occurrenceCandidate);
      performDepthFirstSearch(letterBoard, row, col + 1, word, wordIndex + 1, occurrences, occurrenceCandidate);
      performDepthFirstSearch(letterBoard, row, col - 1, word, wordIndex + 1, occurrences, occurrenceCandidate);

      occurrenceCandidate.remove(occurrenceCandidate.size() - 1);
    }
  }
  
  public static void main(String[] args) {
    // TODO add a cmdline interface
  }
}
