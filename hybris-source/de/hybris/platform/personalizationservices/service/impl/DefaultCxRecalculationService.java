package de.hybris.platform.personalizationservices.service.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.service.CxRecalculationService;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxRecalculationService implements CxRecalculationService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxRecalculationService.class);
    protected static final String UPDATE_PREFIX = RecalculateAction.UPDATE.name() + "_";
    protected static final int UPDATE_PREFIX_LENGTH = UPDATE_PREFIX.length();
    protected static final String ASYNC_PROCESS_PREFIX = RecalculateAction.ASYNC_PROCESS.name() + "_";
    protected static final int ASYNC_PROCESS_PREFIX_LENGTH = ASYNC_PROCESS_PREFIX.length();
    private CxService cxService;
    private CxSegmentService segmentService;
    private UserService userService;
    private List<RecalculateAction> orderedActionList = Arrays.asList(new RecalculateAction[] {RecalculateAction.UPDATE, RecalculateAction.RECALCULATE, RecalculateAction.LOAD, RecalculateAction.ASYNC_PROCESS});


    public void recalculate(List<RecalculateAction> recalculateActions)
    {
        UserModel user = this.userService.getCurrentUser();
        recalculate(user, recalculateActions);
    }


    public void recalculate(UserModel user, List<RecalculateAction> recalculateActions)
    {
        RecalculationConfigData configData = createRecalculationConfigData(recalculateActions);
        for(RecalculateAction recalculateAction : configData.getActionsToExecute())
        {
            switch(null.$SwitchMap$de$hybris$platform$personalizationservices$RecalculateAction[recalculateAction.ordinal()])
            {
                case 1:
                    calculateAndLoadInSession(user);
                    continue;
                case 2:
                    asyncRecalculate(user, configData.getAsyncProcessProviders());
                    continue;
                case 3:
                    loadResult(user);
                    continue;
                case 4:
                    updateSegments(user, configData.getUpdateProviders());
                    continue;
                case 5:
                    continue;
            }
            LOG.warn("Unknown recalculate action :{}", recalculateAction);
        }
    }


    protected RecalculationConfigData createRecalculationConfigData(List<RecalculateAction> recalculateActions)
    {
        RecalculationConfigData config = new RecalculationConfigData();
        config.init(recalculateActions, this::compareActionOrder);
        return config;
    }


    protected int compareActionOrder(RecalculateAction a1, RecalculateAction a2)
    {
        return Integer.compare(this.orderedActionList.indexOf(a1), this.orderedActionList.indexOf(a2));
    }


    protected void calculateAndLoadInSession(UserModel user)
    {
        LOG.debug("recalculating now for {}", user.getUid());
        this.cxService.calculateAndLoadPersonalizationInSession(user);
    }


    protected void asyncRecalculate(UserModel user, Set<String> asyncProcessProviders)
    {
        LOG.debug("start asynchronous process for {}", user.getUid());
        if(CollectionUtils.isEmpty(asyncProcessProviders))
        {
            this.cxService.startPersonalizationCalculationProcesses(user);
        }
        else
        {
            this.cxService.startPersonalizationCalculationProcesses(user, createCalculationContext(asyncProcessProviders));
        }
    }


    protected void loadResult(UserModel user)
    {
        LOG.debug("load personalization result for {}", user.getUid());
        this.cxService.loadPersonalizationInSession(user);
    }


    protected void updateSegments(UserModel user, Set<String> updateProviders)
    {
        LOG.debug("update segments for {}", user.getUid());
        if(CollectionUtils.isEmpty(updateProviders))
        {
            this.segmentService.updateUserSegments(user);
        }
        else
        {
            this.segmentService.updateUserSegments(user, createCalculationContext(updateProviders));
        }
    }


    protected CxCalculationContext createCalculationContext(Set<String> providers)
    {
        CxCalculationContext context = new CxCalculationContext();
        context.setSegmentUpdateProviders(providers);
        return context;
    }


    protected CxService getCxService()
    {
        return this.cxService;
    }


    @Required
    public void setCxService(CxService cxService)
    {
        this.cxService = cxService;
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


    protected CxSegmentService getSegmentService()
    {
        return this.segmentService;
    }


    @Required
    public void setSegmentService(CxSegmentService segmentService)
    {
        this.segmentService = segmentService;
    }


    protected List<RecalculateAction> getOrderedActionList()
    {
        return this.orderedActionList;
    }


    public void setOrderedActionList(List<RecalculateAction> orderedActionList)
    {
        this.orderedActionList = orderedActionList;
    }
}
