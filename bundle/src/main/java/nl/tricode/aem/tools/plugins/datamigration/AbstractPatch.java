package nl.tricode.aem.tools.plugins.datamigration;

import com.day.cq.search.PredicateConverter;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.jcr.resource.JcrResourceConstants;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(label = "AEM toolbox - abstract data migration patch", description = "AEM toolbox - abstract data migration patch", componentAbstract = true)
@Service(value = Patch.class)
public abstract class AbstractPatch implements Patch {

    private static final String PROP_VERSION = "ATDmgVersion";

    @Reference
    protected SlingRepository slingRepository;

    @Reference
    protected QueryBuilder queryBuilder;

    protected abstract String getResourceType();

    protected abstract String[] getPaths();

    protected abstract void migrateNode(Node node);

    @Override
    public PatchResult dryrun() {
        return executeQuery(true);
    }

    @Override
    public PatchResult execute() {
        return executeQuery(false);
    }

    protected PatchResult executeQuery(boolean dryrun) {
        PatchResult patchResult = new PatchResult();
        Session session = null;

        try {
            session = slingRepository.loginAdministrative(null);
            Query query = queryBuilder.createQuery(createQueryPredicates(), session);
            query.setHitsPerPage(0);

            SearchResult result = query.getResult();
            Iterator<Node> nodeIterator =  result.getNodes();

            while (nodeIterator.hasNext()) {
                Node node = nodeIterator.next();
                if (!node.hasProperty(PROP_VERSION) || node.getProperty(PROP_VERSION).getDouble() < getVersion()) {
                    patchResult.getMigratedPaths().add(node.getPath());
                    if (!dryrun) {
                        migrateNode(node);
                        upgradeVersion(node, getVersion());
                    }
                } else {
                    patchResult.getSkippedPaths().add(node.getPath());
                }
            }

            session.save();
        } catch (Exception e) {
            patchResult.setError(new PatchException(e.getMessage(), e));
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
        }

        return patchResult;
    }

    private void upgradeVersion(Node node, Double version) throws RepositoryException {
        node.setProperty(PROP_VERSION, version);
    }

    private PredicateGroup createQueryPredicates() {
        Map<String, String> predicateMap = new HashMap<String, String>();

        predicateMap.put("1_group.p.or", "true");
        for (int i = 0; i < getPaths().length; i++) {
            predicateMap.put("1_group." + (i + 1) + "_path", getPaths()[i]);
        }

        predicateMap.put("2_group.p.or", "true");
        predicateMap.put("2_group.1_property", JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
        predicateMap.put("2_group.1_property.value", getResourceType());

       return PredicateConverter.createPredicates(predicateMap);
    }
}
