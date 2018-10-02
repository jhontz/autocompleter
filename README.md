Java library for "Mobile Device Keyboard" challenge.

To use this library in your code, simply add `src` to your project's source path. For instance, if compiling with `javac`, use the `-sourcepath` flag. After building, add the output directory containing the binaries for this library to your project's classpath. Then you should be able to run your code with the `java` command.

For example, to run the file `test/autocomplete/TestRunner.java`, run the following commands in the project's root directory:
```
javac -d "bin" -sourcepath "test;src" -cp "lib/*;bin" "test/autocomplete/TestRunner.java"
java -cp "lib/*;bin" autocomplete.TestRunner
```
