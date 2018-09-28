package autocomplete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Provides autocomplete suggestions for word fragments. An instance of this
 * class can be trained to recognize which words tend to be the most popular,
 * thus boosting the confidence of these search candidates.
 * 
 * @author Justin Hontz
 */
public class AutocompleteProvider {

  private TrieNode trieRoot = new TrieNode();

  /**
   * Returns a list of Candidates descending in confidence whose corresponding
   * words begin with the given string fragment.
   * 
   * @param fragment the given string fragment
   * @return the ordered list of candidates
   */
  public List<Candidate> getWords(String fragment) {
    TrieNode baseNode = trieRoot;
    // Used for descending sort order
    Comparator<Candidate> comparator = (c1, c2) -> {
      return ((Integer) c2.getConfidence()).compareTo(c1.getConfidence());
    };
    ArrayList<Candidate> words = new ArrayList<>();
    StringBuilder currentString = new StringBuilder();

    // Search for node in trie corresponding to fragment, if it exists
    for (int i = 0; i < fragment.length(); i++) {
      char nextChar = fragment.charAt(i);
      baseNode = baseNode.getChild(nextChar);
      currentString.append(nextChar);
      if (baseNode == null) {
        break;
      }
    }
    if (baseNode != null) {
      // Only necessary if node corresponding to fragment exists
      baseNode.getWords(words, currentString);
    }

    Collections.sort(words, comparator);
    return words;
  }

  /**
   * Trains this instance using words in a given string passage. The passage is
   * first split into words separated by non-word characters, and for each word in
   * the passage, the confidence of that word is incremented.
   * 
   * @param passage the given string passage
   */
  public void train(String passage) {
    // Split upon non-word characters
    String[] words = passage.split("\\W+");

    // Add each word in passage to the trie
    for (String word : words) {
      createTrieNode(word.toLowerCase()).incrementInstanceCount();
    }
  }

  /**
   * Obtains a trie node corresponding to the given word. If such a node already
   * exists, then that node is returned. Otherwise, a new node representing that
   * word is added to the trie and returned.
   * 
   * @param word the given word
   * @return the corresponding trie node
   */
  private TrieNode createTrieNode(String word) {
    TrieNode currentNode = trieRoot;
    for (int i = 0; i < word.length(); i++) {
      currentNode = currentNode.createChild(word.charAt(i));
    }
    return currentNode;
  }

  /**
   * Represents a trie node for this instance's trie data structure. Each trie
   * node corresponds to a certain word, and in turn, every such node maintains an
   * instance count representing the confidence of that word. Each child of a node
   * represents a new word obtained by appending some character to the original
   * node's word.
   */
  private class TrieNode {

    /**
     * Maps character to node representing the result of appending the character to
     * this node's word.
     */
    private Map<Character, TrieNode> children;

    /**
     * Instance count for this node's word.
     */
    private int instanceCount = 0;

    public TrieNode() {
      /*
       * Tree map is used instead of hash map to avoid the overhead of a hash table.
       * The difference in performance is insignificant considering at most 26 keys
       * will be used at any point.
       */
      children = new TreeMap<>();
    }

    /**
     * Obtains the child node, if it exists, corresponding to the given letter.
     * 
     * @param nextLetter the letter corresponding to the child node
     * @return the child node if it exists, otherwise null
     */
    public TrieNode getChild(char nextLetter) {
      if (children.containsKey(nextLetter)) {
        return children.get(nextLetter);
      } else {
        return null;
      }
    }

    /**
     * Obtains the child node corresponding to the given letter, creating the node
     * and adding it as a child if it does not already exist.
     * 
     * @param nextLetter the letter corresponding to the child node
     * @return the child node
     */
    public TrieNode createChild(char nextLetter) {
      if (children.containsKey(nextLetter)) {
        return children.get(nextLetter);
      } else {
        TrieNode newChild = new TrieNode();
        children.put(nextLetter, newChild);
        return newChild;
      }
    }

    /**
     * Increments the confidence of this node's word
     */
    public void incrementInstanceCount() {
      instanceCount++;
    }

    /**
     * Recursively obtains a list of candidates corresponding to words associated
     * with descendant nodes of this node.
     * 
     * @param words         the list of candidates that is updated by this method
     * @param currentString mutable buffer containing the word associated with the
     *                      current node
     */
    public void getWords(ArrayList<Candidate> words, StringBuilder currentString) {
      if (instanceCount > 0) {
        // Only add this word to candidate list if it has been obtained via training
        words.add(new Candidate(currentString.toString(), instanceCount));
      }
      // After each iteration, currentString should return to its original state
      for (Map.Entry<Character, TrieNode> entry : children.entrySet()) {
        currentString.append(entry.getKey());
        entry.getValue().getWords(words, currentString);
        currentString.deleteCharAt(currentString.length() - 1);
      }
    }

  }

}
