package autocomplete;

import java.util.Objects;

/**
 * Represents an autocompletion candidate for a certain word. An instance of
 * this class also maintains a reference to a numerical "confidence" of its
 * associated word relative to some AutocompleteProvider. This confidence
 * represents how likely this word is to be chosen based on prior autocompletion
 * training.
 * 
 * @author Justin Hontz
 */
public class Candidate {

  private String word;
  private int confidence;

  /**
   * Constructs a Candidate instance with a given word and confidence.
   * 
   * @param word       the given word
   * @param confidence the given confidence
   */
  public Candidate(String word, int confidence) {
    this.word = word;
    this.confidence = confidence;
  }

  /**
   * Returns the word associated with this instance.
   * 
   * @return the word
   */
  public String getWord() {
    return word;
  }

  /**
   * Returns the confidence associated with this instance.
   * 
   * @return the confidence
   */
  public int getConfidence() {
    return confidence;
  }
  
  @Override
  public String toString() {
    return "\"" + word + "\" (" + confidence + ")";
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof Candidate)) {
      return false;
    }
    Candidate realOther = (Candidate) other;
    return word.equals(realOther.word) && confidence == realOther.confidence;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(word, confidence);
  }

}
