package jscover.instrument.sourcemap;

import com.google.debugging.sourcemap.proto.Mapping;

import java.util.Objects;

public class SourceLocation {

    public final String sourceFile;
    public final int lineNumber;

    public SourceLocation(String sourceFile, int lineNumber) {
        this.sourceFile = sourceFile;
        this.lineNumber = lineNumber;
    }

    public SourceLocation(Mapping.OriginalMapping mapping) {
        this(mapping.getOriginalFile(), mapping.getLineNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourceLocation that = (SourceLocation) o;
        return lineNumber == that.lineNumber &&
                sourceFile.equals(that.sourceFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceFile, lineNumber);
    }
}
