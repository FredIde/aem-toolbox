package nl.tricode.aem.tools.plugins.packagesize;

import com.day.jcr.vault.fs.api.PathFilterSet;
import com.day.jcr.vault.packaging.JcrPackage;
import com.day.jcr.vault.packaging.Packaging;
import nl.tricode.aem.tools.plugins.AEMToolsPlugin;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = AEMToolsPlugin.class)
@Component(immediate = true, description = "Package size estimation tool", label = "AEM tools plugin - Package size estimation")
public class CrxPackageSizeEstimationPlugin implements AEMToolsPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(CrxPackageSizeEstimationPlugin.class);

    static final String PARAM_PACKAGES = "packages";

    @SuppressWarnings("UnusedDeclaration")
    @Reference
    private Packaging packaging;

    @SuppressWarnings("UnusedDeclaration")
    @Reference
    private SlingRepository slingRepository;

    @Override
    public String getName() {
        return "Package size estimation";
    }

    @Override
    public String getResourceType() {
        return "aemtools/plugins/packagesize";
    }

    @Override
    public Map<String, Object> execute(SlingHttpServletRequest slingRequest) {
        Map model = new HashMap();
        if (slingRequest.getRequestParameterMap().containsKey(PARAM_PACKAGES)) {
            List<String> packageNames = new ArrayList<>();
            for (RequestParameter requestParameter : slingRequest.getRequestParameters(PARAM_PACKAGES)) {
                packageNames.add(requestParameter.getString());
            }

            List<PackageInfo> packages = calculateSizes(packageNames);

            model.put("packages", packages);
            return model;
        }
        return null;
    }

    private List<PackageInfo> calculateSizes(List<String> packageIds) {
        Session session = null;
        List<PackageInfo> packages = new ArrayList<>();
        try {
            session = slingRepository.loginAdministrative(null);
            for (JcrPackage pkg : packaging.getPackageManager(session).listPackages()) {
                if (packageIds.contains(pkg.getNode().getIdentifier())) {
                    PackageInfo info = new PackageInfo();
                    info.setName(pkg.getDefinition().getId().getName());
                    info.setGroup(pkg.getDefinition().getId().getGroup());
                    info.setVersion(pkg.getDefinition().getId().getVersionString());
                    info.setUuid(pkg.getNode().getIdentifier());
                    info.setPath(pkg.getNode().getPath());
                    info.setSize(calculatePackageSizeInMb(pkg, session));
                    packages.add(info);
                }
            }
        } catch (RepositoryException e) {
            LOG.error("Repo error", e);
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
        }
        return packages;
    }

    private long calculatePackageSizeInMb(JcrPackage pkg, Session session) {
        long totalSize = 0l;
        // read package filter
        try {
            for (PathFilterSet pfs : pkg.getDefinition().getMetaInf().getFilter().getFilterSets()) {
                totalSize += getTreeSize(pfs, session);
            }
        } catch (RepositoryException e) {
            LOG.error("Repos exception", e);
            return -1l;
        }

        return totalSize / (1024*1024);
    }

    private long getTreeSize(PathFilterSet pfs, Session session) throws RepositoryException {
        QueryManager manager = session.getWorkspace().getQueryManager();
        String queryStatement = "select * from nt:file where (jcr:path LIKE '" + pfs.getRoot() + "/%')";
        Query query = manager.createQuery(queryStatement, Query.SQL);
        NodeIterator nodeIterator = query.execute().getNodes();
        Node node;
        long size = 0l;

        while (nodeIterator.hasNext()) {
            node = nodeIterator.nextNode();
            if (pfs.contains(node.getPath())) {
                size += node.getProperty("jcr:content/jcr:data").getLength();
            }
        }

        return size;
    }

    public List<PackageInfo> getCandidatePackages() {
        Session session = null;
        List<PackageInfo> packages = new ArrayList<>();
        try {
            session = slingRepository.loginAdministrative(null);
            for (JcrPackage pkg : packaging.getPackageManager(session).listPackages()) {
                if (pkg.getSize() == 0l || pkg.getDefinition().isModified()) {
                    PackageInfo info = new PackageInfo();
                    info.setName(pkg.getDefinition().getId().getName());
                    info.setGroup(pkg.getDefinition().getId().getGroup());
                    info.setVersion(pkg.getDefinition().getId().getVersionString());
                    info.setUuid(pkg.getNode().getIdentifier());
                    info.setPath(pkg.getNode().getPath());
                    packages.add(info);
                }
            }
        } catch (RepositoryException e) {
            LOG.error("Repo error", e);
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
        }
        return packages;
    }
}
