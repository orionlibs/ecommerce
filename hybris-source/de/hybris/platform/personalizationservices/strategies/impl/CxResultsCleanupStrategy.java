package de.hybris.platform.personalizationservices.strategies.impl;

import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.personalizationservices.model.CxResultsCleaningCronJobModel;
import de.hybris.platform.personalizationservices.model.CxResultsModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;

public class CxResultsCleanupStrategy implements MaintenanceCleanupStrategy<CxResultsModel, CxResultsCleaningCronJobModel>
{
    private static final Logger LOG = Logger.getLogger(CxResultsCleanupStrategy.class.getName());
    private static final String SELECT_RESULTS = "SELECT {pk} FROM {CxResults} WHERE {anonymous} = ?anonymous AND {default} != ?default AND {calculationTime} <= ?calculatedBefore";
    private static final String ANONYMOUS_PARAM = "anonymous";
    private static final String DEFAULT_PARAM = "default";
    private static final String CALCULATED_BEFORE_PARAM = "calculatedBefore";
    private TimeService timeService;
    private ModelService modelService;


    public FlexibleSearchQuery createFetchQuery(CxResultsCleaningCronJobModel cjm)
    {
        ServicesUtil.validateParameterNotNull(cjm, "CxResultsCleaningCronJobModel must not be null");
        validateResultMaxAge(cjm.getMaxResultsAge());
        Date calculationDate = (new DateTime(this.timeService.getCurrentTime())).minusSeconds(cjm.getMaxResultsAge()).toDate();
        Map<String, Object> params = new HashMap<>();
        params.put("calculatedBefore", calculationDate);
        params.put("anonymous", Boolean.valueOf(cjm.isAnonymous()));
        params.put("default", Boolean.TRUE);
        return new FlexibleSearchQuery("SELECT {pk} FROM {CxResults} WHERE {anonymous} = ?anonymous AND {default} != ?default AND {calculationTime} <= ?calculatedBefore", params);
    }


    public void process(List<CxResultsModel> elements)
    {
        LOG.info("Removing " + elements.size() + " items from type CxResults");
        this.modelService.removeAll(elements);
    }


    protected void validateResultMaxAge(int resultMaxAge)
    {
        if(resultMaxAge <= 0)
        {
            throw new IllegalArgumentException("CxResultsCleaningCronJobModel.resultMaxAge=" + resultMaxAge + " It should be greater than 0");
        }
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
