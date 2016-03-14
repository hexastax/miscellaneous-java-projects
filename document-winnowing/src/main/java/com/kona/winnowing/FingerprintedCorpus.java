package com.kona.winnowing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

/**
 * Almost named "FingerprintedCorpse" :) this represents
 * 
 * @author dgoldenberg
 */
public class FingerprintedCorpus {

  private List<FingerprintedDoc> fc = new ArrayList<FingerprintedDoc>();

  public FingerprintedCorpus() {
  }

  public void addDocumentInfo(FingerprintedDoc doc) {
    fc.add(doc);
  }

  public List<FingerprintedDoc> getFc() {
    return fc;
  }

  public List<Rank> generateRanking(FingerprintedDoc inDoc) {
    List<Rank> candidates = new ArrayList<Rank>();

    for (FingerprintedDoc doc : fc) {
      SetView<BigInteger> intersection = Sets.intersection(doc.getFingerprint(), inDoc.getFingerprint());
      double percentage = intersection.size() * 100 / inDoc.getFingerprint().size();
      candidates.add(new Rank(doc.getDocPath(), percentage));
    }

    Collections.sort(candidates);

    return candidates;
  }
}
