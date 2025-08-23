package de.hybris.platform.servicelayer.user.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.model.user.BruteForceLoginAttemptsModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserAuditLoginStrategy;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractUserAuditLoginStrategy implements UserAuditLoginStrategy
{
    protected FlexibleSearchService flexibleSearchService;
    protected ModelService modelService;


    protected BruteForceLoginAttemptsModel loadOrCreateAttempts(String uid)
    {
        return loadAttempts(uid).orElseGet(() -> {
            BruteForceLoginAttemptsModel attempts = (BruteForceLoginAttemptsModel)this.modelService.create(BruteForceLoginAttemptsModel.class);
            attempts.setUid(uid);
            attempts.setAttempts(Integer.valueOf(0));
            try
            {
                this.modelService.save(attempts);
                return attempts;
            }
            catch(Exception e)
            {
                if(this.modelService.isUniqueConstraintErrorAsRootCause(e))
                {
                    return loadOrCreateAttempts(uid);
                }
                throw e;
            }
        });
    }


    protected Optional<BruteForceLoginAttemptsModel> loadAttempts(String uid)
    {
        try
        {
            return Optional.of((BruteForceLoginAttemptsModel)this.flexibleSearchService.searchUnique(new FlexibleSearchQuery(String.format("select {pk} from {%s} where {%s}= ?uid", new Object[] {"BruteForceLoginAttempts", "uid"}), (Map)ImmutableMap.of("uid", uid))));
        }
        catch(ModelNotFoundException e)
        {
            return Optional.empty();
        }
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
}
