package nl.tricode.aem.tools.util;

import org.apache.sling.api.resource.*;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public final class InstanceUtil {

    public static final String PROD_INFO_PATH = "/libs/cq/core/productinfo";
    public static final String PROP_PROD_VERSION = "version";

    public static String getAEMVersion() {
        BundleContext bundleContext = FrameworkUtil.getBundle(InstanceUtil.class).getBundleContext();
        ServiceReference serviceReference = bundleContext.getServiceReference(ResourceResolverFactory.class.getName());
        ResourceResolverFactory resourceResolverFactory = (ResourceResolverFactory)bundleContext.getService(serviceReference);
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            Resource prodInfoRes = resourceResolver.getResource(PROD_INFO_PATH);
            if (prodInfoRes != null) {
                ValueMap props = prodInfoRes.adaptTo(ValueMap.class);
                if (props.containsKey(PROP_PROD_VERSION)) {
                    return props.get(PROP_PROD_VERSION, String.class);
                }
            }
        } catch (LoginException e) {
            // fall through
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }

        return "unknown";
    }

    public static String[] getRunModes() {
        BundleContext bundleContext = FrameworkUtil.getBundle(InstanceUtil.class).getBundleContext();
        ServiceReference serviceReference = bundleContext.getServiceReference(SlingSettingsService.class.getName());
        SlingSettingsService slingSettingsService = (SlingSettingsService)bundleContext.getService(serviceReference);
        return slingSettingsService.getRunModes().toArray(new String[] {});
    }

    public static String getAEMToolsVersion() {
        return FrameworkUtil.getBundle(InstanceUtil.class).getVersion().toString();
    }
}
