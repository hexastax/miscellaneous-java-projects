# document-winnowing
Simple prototype for document winnowing.

## Notes
Winnowing is an algorithm for document fingerprinting which is used for such applications as for
example plagiarism detection.

This is elaborated on in detail by Schleimer, Wilkerson, and Aiken in their article
"Winnowing: local algorithms for document fingerprinting" (2003).

WinnowRunner performs document winnowing. It runs fingerprinting on the given document corpus,
then makes an assessment of how close the given input document is to any of the
corpus documents.

## Execution
To exercise the functionality:

```
usage: com.kona.winnowing.WinnowRunner
 -?                         Print this message.
 -corpus <corpusDirPath>    The filepath to the corpus files location
 -doc <inputDocPath>        The filepath to the document to check
 -h                         Print this message.
 -hashAlg <hashingAlgo>     Hashing algorithm: MD5, SHA1
 -help                      Print this message.
 -k <kGramSize>             The size of the document k-grams that will be
                            fingerprinted
 -t <tGuaranteeThreshold>   The guarantee threshold. Duplicates of length
                            t or greater are guaranteed to be detectable
                            using the resulting document fingerprints.
```
## Sample invokation
```
java com.kona.winnowing.WinnowRunner
  -corpus src/main/resources/corpus-documents
  -doc src/main/resources/input-documents/input-doc-0001.txt
  -hashAlg MD5 
  -k 3
  -t 100
```

