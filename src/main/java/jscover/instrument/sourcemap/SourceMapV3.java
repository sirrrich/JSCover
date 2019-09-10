package jscover.instrument.sourcemap;

import com.google.debugging.sourcemap.SourceMapConsumerFactory;
import com.google.debugging.sourcemap.SourceMapConsumerV3;
import com.google.debugging.sourcemap.SourceMapParseException;
import com.google.debugging.sourcemap.proto.Mapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SourceMapV3 implements SourceMap {

    private final SourceMapConsumerV3 sourceMapping;
    private final Map<String, Set<Integer>> validLines = new HashMap<>();
    private final Map<String, Set<Integer>> instrumentedLinesBySourceFile = new HashMap<>();


    public SourceMapV3(String contents) throws SourceMapParseException {
        // at the moment only v3 is supported, thus we can safely cast to that
        // concrete implementation. We need it since the generic interface does
        // not support all operations we need.
        sourceMapping = (SourceMapConsumerV3) SourceMapConsumerFactory.parse(contents);

        calculateValidLines();
    }

    @Override
    public void markInstrumented(SourceLocation location) {
        instrumentedLinesBySourceFile.merge(location.sourceFile, Collections.singleton(location.lineNumber), (set1, set2) -> {
            set1.addAll(set2);
            return set1;
        });
    }

    @Override
    public boolean hasBeenInstrumented(SourceLocation location) {
        return instrumentedLinesBySourceFile.getOrDefault(location.sourceFile, Collections.emptySet())
                .contains(location.lineNumber);
    }

    private void calculateValidLines() {
        sourceMapping.visitMappings((sourceName, symbolName, sourceStartPosition, startPosition, endPosition) -> {
            int line = sourceStartPosition.getLine();
            validLines.merge(sourceName, Collections.singleton(line), (set1, set2) -> {
                set1.addAll(set2);
                return set1;
            });
        });
    }

    @Override
    public Set<Integer> getValidLines(String sourceFileName) {
        return validLines.get(sourceFileName);
    }

    @Override
    public SourceLocation translate(String generatedFileName, int lineNumber, int columnNumber) {
        Mapping.OriginalMapping mapping = sourceMapping.getMappingForLine(lineNumber, columnNumber);
        if (mapping == null || !mapping.hasLineNumber() || !mapping.hasOriginalFile()) {
            return null;
        }
        return new SourceLocation(mapping);
    }

}
