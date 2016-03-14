package com.kona.winnowing;

/**
 * Represents a ranking for how similar a given input document is to a given document in the corpus.
 * 
 * @author dgoldenberg
 */
public class Rank implements Comparable<Rank> {

  private String docPath;
  private double percentage;

  public Rank(String docPath, double percentage) {
    this.docPath = docPath;
    this.percentage = percentage;
  }

  public String getDocPath() {
    return docPath;
  }

  public double getPercentage() {
    return percentage;
  }

  @Override
  public String toString() {
    return "Rank [docPath=" + docPath + ", percentage=" + percentage + "]";
  }

  @Override
  public int compareTo(Rank o) {
    int comp = 0;
    if (percentage < o.getPercentage()) {
      comp = 1;
    } else if (percentage > o.getPercentage()) {
      comp = -1;
    }
    return comp;
  }
}
