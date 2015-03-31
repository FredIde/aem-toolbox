package nl.tricode.aem.tools.plugins.datamigration.patches;

import nl.tricode.aem.tools.plugins.datamigration.AbstractPatch;
import nl.tricode.aem.tools.plugins.datamigration.Patch;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import javax.jcr.Node;

@Component(immediate = true)
@Service(value = Patch.class)
public class ExamplePatch extends AbstractPatch {

    @Override
    public String getName() {
        return "Example patch (no modifications)";
    }

    @Override
    public Double getVersion() {
        return 1d;
    }

    @Override
    protected String getResourceType() {
        return "foundation/components/image";
    }

    @Override
    protected String[] getPaths() {
        return new String[] { "/content" };
    }

    @Override
    protected void migrateNode(Node node) {
        // left empty since this is only for demo purposes
    }
}
