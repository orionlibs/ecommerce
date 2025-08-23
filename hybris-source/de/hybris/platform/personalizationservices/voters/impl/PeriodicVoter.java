package de.hybris.platform.personalizationservices.voters.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.enums.CxUserType;
import de.hybris.platform.personalizationservices.model.config.CxAbstractCalcConfigModel;
import de.hybris.platform.personalizationservices.model.config.CxPeriodicVoterConfigModel;
import de.hybris.platform.personalizationservices.voters.Vote;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class PeriodicVoter extends AbstractVoter
{
    private static final Logger LOG = LoggerFactory.getLogger(PeriodicVoter.class);
    public static final int PRECEDENCE = 10;
    private static final String REQUEST_COUNTER = "personalizationPeriodicVoterRequestCounter";
    private static final String TIME_COUNTER = "personalizationPeriodicVoterTimeCounter";
    private CxConfigurationService cxConfigurationService;
    private SessionService sessionService;
    private UserService userService;
    private TimeService timeService;


    public PeriodicVoter()
    {
        super(10);
    }


    public Vote getVote(HttpServletRequest request, HttpServletResponse response)
    {
        Vote result = getDefaultVote();
        Collection<CxPeriodicVoterConfigModel> periodicVoterCalcConfigurations = this.cxConfigurationService.getPeriodicVoterConfigurations();
        Set<RecalculateAction> periodicActions = (Set<RecalculateAction>)periodicVoterCalcConfigurations.stream().filter(this::isConfigurationValidForUserType).filter(this::shouldProvideVote).map(CxAbstractCalcConfigModel::getActions).flatMap(Collection::stream).filter(this::actionExist)
                        .map(RecalculateAction::valueOf).collect(Collectors.toSet());
        if(!periodicActions.isEmpty())
        {
            result.setRecalculateActions(periodicActions);
        }
        return result;
    }


    protected boolean isAnonymous()
    {
        UserModel currentUser = this.userService.getCurrentUser();
        return this.userService.isAnonymousUser(currentUser);
    }


    private boolean isConfigurationValidForUserType(CxPeriodicVoterConfigModel cxPeriodicVoterConfigModel)
    {
        CxUserType cxUserType = cxPeriodicVoterConfigModel.getUserType();
        if(isAnonymous())
        {
            return (CxUserType.REGISTERED != cxUserType);
        }
        return (CxUserType.ANONYMOUS != cxUserType);
    }


    protected boolean actionExist(String actionName)
    {
        try
        {
            RecalculateAction.valueOf(actionName);
        }
        catch(IllegalArgumentException e)
        {
            LOG.warn("Recalculate action doesn't exist :" + actionName, e);
            return false;
        }
        return true;
    }


    protected boolean shouldProvideVote(CxPeriodicVoterConfigModel cxPeriodicVoterConfigModel)
    {
        String VOTER_CONFIG_REQUEST_COUNTER = cxPeriodicVoterConfigModel.getCode() + "personalizationPeriodicVoterRequestCounter";
        String VOTER_CONFIG_TIME_COUNTER = cxPeriodicVoterConfigModel.getCode() + "personalizationPeriodicVoterTimeCounter";
        boolean result = checkRequestNumber(VOTER_CONFIG_REQUEST_COUNTER, cxPeriodicVoterConfigModel.getUserMinRequestNumber());
        result &= checkRequestTime(VOTER_CONFIG_TIME_COUNTER, cxPeriodicVoterConfigModel.getUserMinTime());
        if(result)
        {
            resetCounters(VOTER_CONFIG_REQUEST_COUNTER, VOTER_CONFIG_TIME_COUNTER);
        }
        return result;
    }


    protected boolean checkRequestNumber(String voterConfigRequestCounter, Integer voterConfigMinRequestNumber)
    {
        if(voterConfigMinRequestNumber.intValue() < 0)
        {
            return false;
        }
        return (voterConfigMinRequestNumber.intValue() <= getCurrentRequestNumber(voterConfigRequestCounter));
    }


    protected int getCurrentRequestNumber(String voterConfigRequestCounter)
    {
        return ((AtomicInteger)this.sessionService.getOrLoadAttribute(voterConfigRequestCounter, () -> new AtomicInteger(1))).getAndIncrement();
    }


    protected boolean checkRequestTime(String voterConfigTimeCounter, Long voterConfigMinTime)
    {
        if(voterConfigMinTime.longValue() < 0L)
        {
            return false;
        }
        return (getStoredTime(voterConfigTimeCounter) + voterConfigMinTime.longValue() <= this.timeService.getCurrentTime().getTime());
    }


    protected long getStoredTime(String voterConfigTimeCounter)
    {
        return ((AtomicLong)this.sessionService
                        .getOrLoadAttribute(voterConfigTimeCounter, () -> new AtomicLong(this.timeService.getCurrentTime().getTime()))).get();
    }


    protected void resetCounters(String voterConfigRequestCounter, String voterConfigTimeCounter)
    {
        AtomicInteger num = (AtomicInteger)this.sessionService.getAttribute(voterConfigRequestCounter);
        num.set(1);
        AtomicLong timeStamp = (AtomicLong)this.sessionService.getAttribute(voterConfigTimeCounter);
        timeStamp.set(this.timeService.getCurrentTime().getTime());
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


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
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
}
