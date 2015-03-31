package nl.tricode.aem.tools.util;

import nl.tricode.aem.tools.plugins.AEMToolsPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class OsgiUtil {

    public static <T>T getService(Class<T> clazz) {
        BundleContext bc = FrameworkUtil.getBundle(OsgiUtil.class).getBundleContext();
        if (bc != null) {
            ServiceReference serviceReference = bc.getServiceReference(clazz.getName());
            if (serviceReference != null) {
                return clazz.cast(bc.getService(serviceReference));
            }
        }
        return null;
    }

    public static <T>T getPlugin(Class<T> clazz) {
        BundleContext bc = FrameworkUtil.getBundle(OsgiUtil.class).getBundleContext();
        if (bc != null) {
            ServiceReference[] serviceReferences = new ServiceReference[0];
            try {
                serviceReferences = bc.getServiceReferences(AEMToolsPlugin.class.getName(), null);
            } catch (InvalidSyntaxException e) {
                return null;
            }
            for (ServiceReference reference : serviceReferences) {
                String refCompName = (String) reference.getProperty("component.name");
                if (refCompName != null && refCompName.equalsIgnoreCase(clazz.getName())) {
                    return clazz.cast(bc.getService(reference));
                }
            }
        }
        return null;
    }
}
