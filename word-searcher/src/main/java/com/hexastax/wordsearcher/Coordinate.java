package com.hexastax.wordsearcher;

/**
 * Represents a coordinate of a letter on the board: row number, column number (0-based).
 */
public class Coordinate {

  private int rowNum;
  private int colNum;

  /**
   * Creates a coordinate
   * 
   * @param r
   *          the row number (0-based)
   * @param c
   *          the column number (0-based)
   */
  public Coordinate(int r, int c) {
    this.rowNum = r;
    this.colNum = c;
  }

  /**
   * Creates a coordinate from a string form such as e.g. "0, 3" or "1, 2".
   * 
   * @param strCoord
   *          the coordinate in the string form
   * @return the parsed out coordinate
   */
  public static Coordinate makeCoord(String strCoord) {
    String bits[] = strCoord.split(", ");
    if (bits == null || bits.length != 2) {
      throw new IllegalArgumentException("Invalid coordinate spec format: '" + strCoord + "'.");
    }
    int row = Integer.parseInt(bits[0]);
    int col = Integer.parseInt(bits[1]);
    return new Coordinate(row, col);
  }

  /**
   * @return the row number (0-based)
   */
  public int getRowNum() {
    return rowNum;
  }

  /**
   * @return the column number (0-based)
   */
  public int getColNum() {
    return colNum;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + colNum;
    result = prime * result + rowNum;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Coordinate other = (Coordinate) obj;
    if (colNum != other.colNum)
      return false;
    if (rowNum != other.rowNum)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", rowNum, colNum);
  }
}
