package de.hybris.platform.core.initialization.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.core.initialization.SystemSetupAuditDAO;
import de.hybris.platform.core.initialization.SystemSetupCollectorResult;
import de.hybris.platform.core.model.initialization.SystemSetupAuditModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSystemSetupAuditDAO implements SystemSetupAuditDAO
{
    private static final String APPLIED_PATCHES_QUERY = "SELECT count({pk}) FROM {SystemSetupAudit} WHERE {hash}=?hash";
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;
    private UserService userService;


    public boolean isPatchApplied(String patchHash)
    {
        Objects.requireNonNull(patchHash, "patchHash is required");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT count({pk}) FROM {SystemSetupAudit} WHERE {hash}=?hash");
        fQuery.addQueryParameter("hash", patchHash);
        fQuery.setResultClassList(Lists.newArrayList((Object[])new Class[] {Integer.class}));
        SearchResult<Integer> searchResult = this.flexibleSearchService.search(fQuery);
        return (((Integer)searchResult.getResult().get(0)).intValue() == 1);
    }


    public SystemSetupAuditModel storeSystemPatchAction(SystemSetupCollectorResult collectorResult)
    {
        SystemSetupAuditModel systemPatch = (SystemSetupAuditModel)this.modelService.create(SystemSetupAuditModel.class);
        systemPatch.setHash(collectorResult.getHash());
        systemPatch.setDescription(collectorResult.getDescription());
        systemPatch.setName(collectorResult.getName());
        systemPatch.setUser(this.userService.getCurrentUser());
        systemPatch.setClassName(collectorResult.getObject().getClass().getCanonicalName());
        systemPatch.setMethodName(collectorResult.getMethod().getName());
        systemPatch.setRequired(true);
        systemPatch.setExtensionName(collectorResult.getExtensionName());
        this.modelService.save(systemPatch);
        return systemPatch;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
