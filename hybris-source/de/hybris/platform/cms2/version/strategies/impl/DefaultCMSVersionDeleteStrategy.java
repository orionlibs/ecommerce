package de.hybris.platform.cms2.version.strategies.impl;

import de.hybris.platform.cms2.version.strategies.CMSVersionDeleteStrategy;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.persistence.PersistenceUtils;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultCMSVersionDeleteStrategy implements CMSVersionDeleteStrategy
{
    private final ModelService modelService;


    public DefaultCMSVersionDeleteStrategy(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public Optional<PerformResult> deleteVersions(Collection<PK> toBeDeleted) throws Exception
    {
        PersistenceUtils.doWithSLDPersistence(() -> {
            Set<Object> modelsToDelete = (Set<Object>)toBeDeleted.stream().map(()).filter(Objects::nonNull).collect(Collectors.toSet());
            this.modelService.removeAll(modelsToDelete);
            Objects.requireNonNull(this.modelService);
            modelsToDelete.forEach(this.modelService::detach);
            return Boolean.valueOf(true);
        });
        return Optional.empty();
    }
}
