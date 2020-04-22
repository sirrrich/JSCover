package jscover.instrument.sourcemap;

import java.util.List;
import java.util.SortedSet;

public interface SourceMap {
    SortedSet<Integer> getValidLines(String sourceFileName);

    List<String> getOriginalSourceFiles();

    SourceLocation translate(String generatedFileName, int lineNumber, int columnNumber);

    void markInstrumented(SourceLocation location);

    boolean hasBeenInstrumented(SourceLocation location);
}
