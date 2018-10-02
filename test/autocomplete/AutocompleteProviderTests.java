package autocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.*;

public class AutocompleteProviderTests {

  private static List<Set<Candidate>> splitTopCandidatesList(List<Candidate> list) {
    int listIndex = 0;
    ArrayList<Set<Candidate>> result = new ArrayList<>();
    while (listIndex < list.size()) {
      HashSet<Candidate> equalConfidenceCandidates = new HashSet<>();
      int firstConfidence = list.get(listIndex).getConfidence();
      while (listIndex < list.size() && list.get(listIndex).getConfidence() == firstConfidence) {
        equalConfidenceCandidates.add(list.get(listIndex++));
      }
      result.add(equalConfidenceCandidates);
    }
    return result;
  }

  /**
   * Determines whether two top candidates lists are equal up to permutation of
   * equally confident words.
   * 
   * @param list1 the first list
   * @param list2 the second list
   * @return whether the lists are equivalent
   */
  private static boolean areTopCandidatesListsEqual(List<Candidate> list1, List<Candidate> list2) {
    return splitTopCandidatesList(list1).equals(splitTopCandidatesList(list2));
  }

  @Test
  public void testOneTrainingUpperAndLowerCase() {
    AutocompleteProvider provider = new AutocompleteProvider();
    List<Candidate> expectedList = Arrays.asList(
        new Candidate("that", 2),
        new Candidate("thing", 2),
        new Candidate("think", 1),
        new Candidate("this", 1),
        new Candidate("third", 1),
        new Candidate("the", 1),
        new Candidate("thoroughly", 1)
    );
    
    provider.train("The THIRD thing that I nEED to tELL you is thAT this THING does not think tHoRouGHly");
    assertTrue(areTopCandidatesListsEqual(expectedList, provider.getWords("th")));
  }
  
  @Test
  public void testMultipleTrainings() {
    AutocompleteProvider provider = new AutocompleteProvider();
    List<Candidate> expectedList = Arrays.asList(
        new Candidate("a", 5),
        new Candidate("aa", 5),
        new Candidate("ab", 4),
        new Candidate("b", 3),
        new Candidate("ba", 2),
        new Candidate("aaa", 1)
    );
    
    provider.train("a aa ab b");
    provider.train("ab a aaa aa");
    provider.train("ba aa a aa");
    provider.train("a ab b a ba");
    provider.train("ab aa b");
    
    assertTrue(areTopCandidatesListsEqual(expectedList, provider.getWords("")));
  }
  
  @Test
  public void testWithSymbols() {
    AutocompleteProvider provider = new AutocompleteProvider();
    List<Candidate> expectedList = Arrays.asList(
        new Candidate("a", 4),
        new Candidate("b", 2)
    );
    
    provider.train("a, b - a : - b a . . ' a");
    assertTrue(areTopCandidatesListsEqual(expectedList, provider.getWords("")));
  }

}
