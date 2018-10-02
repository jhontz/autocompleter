package autocomplete;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestRunner {

  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(AutocompleteProviderTests.class);

    System.out.println("All tests passed: " + result.wasSuccessful());
  }
  
}
