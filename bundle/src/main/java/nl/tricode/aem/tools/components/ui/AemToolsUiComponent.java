package nl.tricode.aem.tools.components.ui;

import nl.tricode.aem.tools.plugins.AEMToolsPlugin;
import nl.tricode.aem.tools.util.InstanceUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AemToolsUiComponent {

    private static final Logger LOG = LoggerFactory.getLogger(AemToolsUiComponent.class);

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;

    public void setRequest(SlingHttpServletRequest request) {
        this.request = request;
    }

    public void setResponse(SlingHttpServletResponse response) {
        this.response = response;
    }

    public List<AEMToolsPlugin> getPlugins() {
        List<AEMToolsPlugin> plugins = new ArrayList<>();
        BundleContext bc = FrameworkUtil.getBundle(AemToolsUiComponent.class).getBundleContext();
        try {
            for (ServiceReference ref : bc.getServiceReferences(AEMToolsPlugin.class.getName(), null)) {
                plugins.add((AEMToolsPlugin)bc.getService(ref));
            }
        } catch (InvalidSyntaxException e) {
            LOG.error("Error getting service references", e);
        }
        return plugins;
    }

    public String getAemVersion() {
        return InstanceUtil.getAEMVersion();
    }

    public String getAemToolsVersion() {
        return InstanceUtil.getAEMToolsVersion();
    }

    public String[] getRunModes() {
        return InstanceUtil.getRunModes();
    }

    public boolean getShowDefaultContent() {
        return getRequestedPlugin() == null;
    }

    public String getComponentPathToInclude() {
        return getRequestedPlugin().getResourceType();
    }

    private AEMToolsPlugin getRequestedPlugin() {
        String action = request.getRequestPathInfo().getSuffix();
        if (action != null && action.startsWith("/")) {
            action = action.substring(1);
        }
        for (AEMToolsPlugin plugin : getPlugins()) {
            if (plugin.getClass().getSimpleName().equalsIgnoreCase(action)) {
                return plugin;
            }
        }
        return null;
    }

}
