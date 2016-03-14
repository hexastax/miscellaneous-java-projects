package com.kona.winnowing;

import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a fingerprinted document.
 * 
 * @author dgoldenberg
 */
public class FingerprintedDoc {

  private String docPath;
  private Set<BigInteger> fingerprint;

  public FingerprintedDoc(String docPath, Set<BigInteger> fp) {
    this.docPath = docPath;
    this.fingerprint = new TreeSet<BigInteger>();
    this.fingerprint.addAll(fp);
  }

  public String getDocPath() {
    return docPath;
  }

  public Set<BigInteger> getFingerprint() {
    return fingerprint;
  }

  public void print() {
    System.out.println(">> FILE: " + docPath);
    System.out.println(">> FINGERPRINT: (size: " + fingerprint.size() + ")");
    for (BigInteger fpVal : fingerprint) {
      System.out.println("    >> " + fpVal);
    }
  }

}
