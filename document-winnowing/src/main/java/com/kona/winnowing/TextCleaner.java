package com.kona.winnowing;

import net.big_oh.algorithms.fingerprint.winnowing.WinnowingWhitespaceFilter;

import org.apache.commons.lang.StringUtils;

/**
 * Cleans the incoming text by stripping off any non-alphanumerics.
 * 
 * @author dgoldenberg
 */
public class TextCleaner implements WinnowingWhitespaceFilter {

  @Override
  public String doFilter(String sourceDocument) {
    return sourceDocument.replaceAll("\\P{Alnum}", StringUtils.EMPTY).toLowerCase();
  }

  public static void main(String[] args) {
    System.out.println(new TextCleaner().doFilter("Da @^&%()do 12run run run+++ *^%&%$ ,../."));
  }

}
