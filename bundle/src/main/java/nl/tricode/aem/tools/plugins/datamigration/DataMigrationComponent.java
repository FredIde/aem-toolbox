package nl.tricode.aem.tools.plugins.datamigration;

import nl.tricode.aem.tools.util.OsgiUtil;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;
import java.util.Map;

public class DataMigrationComponent {

    private SlingHttpServletRequest request;

    public boolean getShowResult() {
        return request.getParameter(DataMigrationPlugin.PARAM_PATCHES) != null;
    }

    public String getAction() {
        return request.getParameter(DataMigrationPlugin.PARAM_ACTION);
    }

    public List<Patch> getPatches() {
        DataMigrationPlugin dmPlugin = OsgiUtil.getPlugin(DataMigrationPlugin.class);
        if (dmPlugin != null) {
            return dmPlugin.getPatches();
        }
        return null;
    }

    public Map<String, Object> getPatchResults() {
        DataMigrationPlugin dmPlugin = OsgiUtil.getPlugin(DataMigrationPlugin.class);
        if (dmPlugin != null) {
            return dmPlugin.execute(request);
        }
        return null;
    }

    public void setRequest(SlingHttpServletRequest request) {
        this.request = request;
    }
}
