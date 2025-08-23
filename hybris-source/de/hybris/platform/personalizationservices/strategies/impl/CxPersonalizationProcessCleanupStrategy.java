package de.hybris.platform.personalizationservices.strategies.impl;

import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessCleanupCronJobModel;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
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

public class CxPersonalizationProcessCleanupStrategy implements MaintenanceCleanupStrategy<CxPersonalizationProcessModel, CxPersonalizationProcessCleanupCronJobModel>
{
    private static final Logger LOG = Logger.getLogger(CxPersonalizationProcessCleanupStrategy.class.getName());
    private static final String PROCESS_STATES_PARAM = "states";
    private static final String DATETIME_TO_REM_PARAM = "dateTimeToRem";
    private TimeService timeService;
    private ModelService modelService;


    public FlexibleSearchQuery createFetchQuery(CxPersonalizationProcessCleanupCronJobModel cjm)
    {
        ServicesUtil.validateParameterNotNull(cjm, "CxPersonalizationProcessCleanupCronJobModel must not be null");
        ServicesUtil.validateParameterNotNull(cjm.getProcessStates(), "CxPersonalizationProcessCleanupCronJobModel process states collection must not be null");
        ServicesUtil.validateParameterNotNull(cjm.getMaxProcessAge(), "CxPersonalizationProcessCleanupCronJobModel maxProcessAge must not be null");
        String query = "SELECT {p.pk} FROM {CxPersonalizationProcess as p}WHERE {p.state} IN (?states)  AND {p.creationtime} <= ?dateTimeToRem ";
        Date dateForProcessRemove = (new DateTime(this.timeService.getCurrentTime())).minusMinutes(Integer.parseInt(cjm.getMaxProcessAge())).toDate();
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("states", cjm.getProcessStates());
        queryParams.put("dateTimeToRem", dateForProcessRemove);
        return new FlexibleSearchQuery("SELECT {p.pk} FROM {CxPersonalizationProcess as p}WHERE {p.state} IN (?states)  AND {p.creationtime} <= ?dateTimeToRem ", queryParams);
    }


    public void process(List<CxPersonalizationProcessModel> elements)
    {
        LOG.info("Removing " + elements.size() + " items from type CxPersonalizationProcess");
        this.modelService.removeAll(elements);
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
