package de.hybris.platform.personalizationservices.occ.voters.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.action.CxActionResultService;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.model.CxResultsModel;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.occ.CxOccAttributesStrategy;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.personalizationservices.voters.Vote;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Required;

public class CxOccTimeVoter extends AbstractCxOccVoter
{
    public static final int PRECEDENCE = 10;
    private static final Long DEFAULT_TTL = Long.valueOf(600000L);
    private TimeService timeService;
    private CxOccAttributesStrategy cxOccAttributesStrategy;
    private CxConfigurationService cxConfigurationService;
    private CxActionResultService cxActionResultService;
    private CxCatalogService cxCatalogService;
    private UserService userService;


    public CxOccTimeVoter()
    {
        super(10);
    }


    public Vote getVote(HttpServletRequest request)
    {
        Vote result = getDefaultVote();
        long ttl = ((Long)this.cxConfigurationService.getConfiguration().map(CxConfigModel::getOccTTL).orElse(DEFAULT_TTL)).longValue();
        Optional<Long> calcTime = getLastCalculationTime(request);
        long currentTime = this.timeService.getCurrentTime().getTime();
        boolean isCalcTimeEmptyOrDifference = (!calcTime.isPresent() || currentTime - ((Long)calcTime.get()).longValue() > ttl);
        if(isCalcTimeEmptyOrDifference && isCxResultRequireCalculation(currentTime, ttl))
        {
            result.getRecalculateActions().add(RecalculateAction.ASYNC_PROCESS);
        }
        return result;
    }


    private boolean isCxResultRequireCalculation(long currentTime, long ttlTime)
    {
        UserModel userModel = this.userService.getCurrentUser();
        boolean resultsAvailable = false;
        boolean cxResultRequireRecalculation = false;
        for(CatalogVersionModel cv : this.cxCatalogService.getConfiguredCatalogVersions())
        {
            CxResultsModel resultsModel = this.cxActionResultService.getCxResults(userModel, cv).orElse(null);
            if(resultsModel != null)
            {
                resultsAvailable = true;
                if(resultsModel.getCalculationTime().getTime() < currentTime - ttlTime)
                {
                    cxResultRequireRecalculation = true;
                    break;
                }
            }
        }
        return (cxResultRequireRecalculation || !resultsAvailable);
    }


    protected Optional<Long> getLastCalculationTime(HttpServletRequest request)
    {
        Optional<Long> time = this.cxOccAttributesStrategy.readPersonalizationCalculationTime(request);
        return time;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    protected CxOccAttributesStrategy getCxOccAttributesStrategy()
    {
        return this.cxOccAttributesStrategy;
    }


    @Required
    public void setCxOccAttributesStrategy(CxOccAttributesStrategy cxOccAttributesStrategy)
    {
        this.cxOccAttributesStrategy = cxOccAttributesStrategy;
    }


    protected CxActionResultService getCxActionResultService()
    {
        return this.cxActionResultService;
    }


    @Required
    public void setCxActionResultService(CxActionResultService cxActionResultService)
    {
        this.cxActionResultService = cxActionResultService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected CxCatalogService getCxCatalogService()
    {
        return this.cxCatalogService;
    }


    @Required
    public void setCxCatalogService(CxCatalogService cxCatalogService)
    {
        this.cxCatalogService = cxCatalogService;
    }
}
