package jscover.instrument.sourcemap;

import com.google.debugging.sourcemap.proto.Mapping;

import java.util.HashSet;
import java.util.Set;

public class NoOpSourceMap implements SourceMap {

    private final Set<Integer> instrumentedLines = new HashSet<>();

    @Override
    public Set<Integer> getValidLines(String sourceFileName) {
        return instrumentedLines;
    }

    @Override
    public SourceLocation translate(String generatedFileName, int lineNumber, int columnNumber) {
        return new SourceLocation(generatedFileName, lineNumber);
    }

    @Override
    public void markInstrumented(SourceLocation location) {
        instrumentedLines.add(location.lineNumber);
    }

    @Override
    public boolean hasBeenInstrumented(SourceLocation location) {
        return instrumentedLines.contains(location.lineNumber);
    }

}
