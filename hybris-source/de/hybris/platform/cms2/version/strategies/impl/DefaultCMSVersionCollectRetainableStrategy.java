package de.hybris.platform.cms2.version.strategies.impl;

import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.version.service.CMSVersionGCService;
import de.hybris.platform.cms2.version.strategies.CMSVersionCollectRetainableStrategy;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultCMSVersionCollectRetainableStrategy implements CMSVersionCollectRetainableStrategy
{
    private final ConfigurationService configurationService;
    private final CMSVersionGCService cmsVersionGCService;
    private final ModelService modelService;


    public DefaultCMSVersionCollectRetainableStrategy(ConfigurationService configurationService, CMSVersionGCService cmsVersionGCService, ModelService modelService)
    {
        this.configurationService = configurationService;
        this.cmsVersionGCService = cmsVersionGCService;
        this.modelService = modelService;
    }


    public Set<PK> fetchRetainableVersions() throws Exception
    {
        int maxAgeInDays = this.configurationService.getConfiguration().getInt("version.gc.maxAgeDays", 0);
        int maxNumberVersions = this.configurationService.getConfiguration().getInt("version.gc.maxNumberVersions", 20);
        if(maxNumberVersions <= 0 && maxAgeInDays <= 0)
        {
            throw new IllegalStateException("At least one of version.gc.maxAgeDays, version.gc.maxNumberVersions must be > 0");
        }
        List<CMSVersionModel> retainableVersions = getRetainableVersions(maxAgeInDays, maxNumberVersions);
        return collectAllRetainableVersionPKs(retainableVersions);
    }


    protected List<CMSVersionModel> getRetainableVersions(int maxAgeInDays, int maxNumberVersions)
    {
        return this.cmsVersionGCService.getRetainableVersions(maxAgeInDays, maxNumberVersions);
    }


    private Set<PK> collectAllRetainableVersionPKs(List<CMSVersionModel> retainableVersions)
    {
        Set<PK> retainablePKs = new HashSet<>();
        for(CMSVersionModel retainableVersion : retainableVersions)
        {
            if(retainableVersion == null)
            {
                continue;
            }
            retainablePKs.add(retainableVersion.getPk());
            if(CollectionUtils.isNotEmpty(retainableVersion.getRelatedChildren()))
            {
                retainableVersion.getRelatedChildren().stream().filter(Objects::nonNull).forEach(v -> {
                    retainablePKs.add(v.getPk());
                    this.modelService.detach(v);
                });
            }
            this.modelService.detach(retainableVersion);
        }
        return retainablePKs;
    }
}
