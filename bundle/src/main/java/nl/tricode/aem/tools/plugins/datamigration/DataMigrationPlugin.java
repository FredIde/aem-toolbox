package nl.tricode.aem.tools.plugins.datamigration;

import nl.tricode.aem.tools.plugins.AEMToolsPlugin;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = AEMToolsPlugin.class)
@Component(immediate = true, description = "Service based data migration tool", label = "AEM tools plugin - Data migration")
public class DataMigrationPlugin implements AEMToolsPlugin {

    static final String ACTION_EXECUTE = "execute";
    static final String ACTION_DRYRUN = "dryrun";

    static final String PARAM_ACTION = "action";
    static final String PARAM_PATCHES = "patches";

    private static final String MODEL_ACTION = "action";
    private static final String MODEL_RESULT = "result";
    private static final String MODEL_PATCHES = "patches";

    private static final Logger LOG = LoggerFactory.getLogger(DataMigrationPlugin.class);

    @Reference(referenceInterface = Patch.class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    private List<Patch> patches = new ArrayList<>();

    protected synchronized void bindPatch(Patch plugin) {
        patches.add(plugin);
        LOG.debug("Registered patch {}", plugin.getName());
    }

    protected synchronized void unbindPatch(Patch plugin) {
        patches.remove(plugin);
        LOG.debug("Unregistered patch {}", plugin.getName());
    }

    @Override
    public String getName() {
        return "Data migration";
    }

    @Override
    public Map<String, Object> execute(SlingHttpServletRequest slingRequest) {
        Map model = new HashMap();
        if (slingRequest.getParameterMap().containsKey(PARAM_ACTION) && slingRequest.getParameterMap().containsKey(PARAM_PATCHES)) {
            String action = slingRequest.getParameter(PARAM_ACTION);
            model.put(MODEL_ACTION, action);

            List<String> patchNames = new ArrayList<>();
            for (RequestParameter requestParameter : slingRequest.getRequestParameters(PARAM_PATCHES)) {
                patchNames.add(requestParameter.getString());
            }

            if (ACTION_EXECUTE.equalsIgnoreCase(action)) {
                LOG.info("Patch process started by user {}", (slingRequest.getUserPrincipal() != null ? slingRequest.getUserPrincipal().getName() : "unknown"));
            }

            Map<String, Object> resultMap = new HashMap<>(patchNames.size());
            for (Patch patch : patches) {
                if (patchNames.contains(patch.getClass().getSimpleName())) {
                    PatchResult patchResult = null;
                    if (ACTION_EXECUTE.equalsIgnoreCase(action)) {
                        patchResult = patch.execute();
                        LOG.info("Executed patch {} version {}", patch.getName(), patch.getVersion());
                        LOG.info("{} nodes affected", patchResult.getMigratedPaths().size());
                        LOG.info("{} nodes skipped", patchResult.getSkippedPaths().size());

                        if (patchResult.getError() != null) {
                            LOG.error("Error during patch execution", patchResult.getError());
                        }

                        if (LOG.isDebugEnabled()) {
                            for (String path : patchResult.getMigratedPaths()) {
                                LOG.debug("Migrated {}", path);
                            }
                            for (String path : patchResult.getSkippedPaths()) {
                                LOG.debug("Skipped {}", path);
                            }
                        }
                    } else if (ACTION_DRYRUN.equalsIgnoreCase(action)) {
                        patchResult = patch.dryrun();

                    }
                    resultMap.put(patch.getName(), patchResult);
                }
            }
            return resultMap;
        }
        return null;
    }

    @Override
    public String getResourceType() {
        return "aemtools/plugins/datamigration";
    }

    public List<Patch> getPatches() {
        return patches;
    }
}