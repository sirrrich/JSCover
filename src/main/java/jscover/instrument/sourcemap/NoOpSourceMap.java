package jscover.instrument.sourcemap;

import java.util.*;

public class NoOpSourceMap implements SourceMap {

    private final SortedSet<Integer> instrumentedLines = new TreeSet<>();
    private final String sourceFile;

    public NoOpSourceMap(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    @Override
    public SortedSet<Integer> getValidLines(String sourceFileName) {
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

    @Override
    public List<String> getOriginalSourceFiles() {
        return Collections.singletonList(sourceFile);
    }
}
