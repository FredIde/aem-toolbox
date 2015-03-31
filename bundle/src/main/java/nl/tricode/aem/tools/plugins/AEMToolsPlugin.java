package nl.tricode.aem.tools.plugins;

import org.apache.sling.api.SlingHttpServletRequest;

import java.util.Map;

public interface AEMToolsPlugin {

    public String getName();

    public String getResourceType();

    public Map<String, Object> execute(SlingHttpServletRequest slingRequest);
}
