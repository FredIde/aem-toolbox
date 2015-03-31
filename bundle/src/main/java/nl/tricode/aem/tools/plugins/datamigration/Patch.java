package nl.tricode.aem.tools.plugins.datamigration;

public interface Patch {

    public String getName();

    public Double getVersion();

    public PatchResult dryrun();

    public PatchResult execute();
}
