package jscover.instrument.sourcemap;

import com.google.debugging.sourcemap.proto.Mapping;

import java.util.Set;

public interface SourceMap {
    Set<Integer> getValidLines(String sourceFileName);

    SourceLocation translate(String generatedFileName, int lineNumber, int columnNumber);

    void markInstrumented(SourceLocation location);

    boolean hasBeenInstrumented(SourceLocation location);
}
