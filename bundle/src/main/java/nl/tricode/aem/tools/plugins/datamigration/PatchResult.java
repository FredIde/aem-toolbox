package nl.tricode.aem.tools.plugins.datamigration;

import java.util.ArrayList;
import java.util.List;

public class PatchResult {
    private List<String> migratedPaths = new ArrayList<>();
    private List<String> skippedPaths = new ArrayList<>();;
    private PatchException error;

    public List<String> getMigratedPaths() {
        return migratedPaths;
    }

    public void setMigratedPaths(List<String> migratedPaths) {
        this.migratedPaths = migratedPaths;
    }

    public List<String> getSkippedPaths() {
        return skippedPaths;
    }

    public void setSkippedPaths(List<String> skippedPaths) {
        this.skippedPaths = skippedPaths;
    }

    public PatchException getError() {
        return error;
    }

    public void setError(PatchException error) {
        this.error = error;
    }
}
