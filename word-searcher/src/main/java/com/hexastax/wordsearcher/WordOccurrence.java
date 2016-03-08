package com.hexastax.wordsearcher;

import java.util.ArrayList;

/**
 * Represents a word occurrence, as a sequence of coordinates.
 * 
 * @author dgoldenberg
 */
public class WordOccurrence extends ArrayList<Coordinate> {

  private static final long serialVersionUID = 7956809766692090349L;

  /**
   * Creates a word occurrence.
   */
  public WordOccurrence() {
  }

  /**
   * Creates a word occurrence, from another word occurrence.
   * 
   * @param other
   *          the other word occurrence.
   */
  public WordOccurrence(WordOccurrence other) {
    super(other);
  }
}
