package nl.tricode.aem.tools.plugins.packagesize;

import nl.tricode.aem.tools.util.OsgiUtil;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;

public class PackageSizeComponent {

    private SlingHttpServletRequest request;

    public boolean getShowResult() {
        return request.getParameter(CrxPackageSizeEstimationPlugin.PARAM_PACKAGES) != null;
    }

    public List<PackageInfo> getSelectedPackageInfo() {
        CrxPackageSizeEstimationPlugin plugin = OsgiUtil.getPlugin(CrxPackageSizeEstimationPlugin.class);
        if (plugin != null) {
            return (List<PackageInfo>) plugin.execute(request).get("packages");
        }
        return null;
    }

    public List<PackageInfo> getCandidatePackages() {
        CrxPackageSizeEstimationPlugin plugin = OsgiUtil.getPlugin(CrxPackageSizeEstimationPlugin.class);
        if (plugin != null) {
            return plugin.getCandidatePackages();
        }
        return null;
    }

    public void setRequest(SlingHttpServletRequest request) {
        this.request = request;
    }
}
