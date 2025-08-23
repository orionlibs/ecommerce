package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.servicelayer.cronjob.TriggerDao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTriggerDao implements TriggerDao
{
    private GenericSearchService genericSearchService;


    @Required
    public void setGenericSearchService(GenericSearchService genericSearchService)
    {
        this.genericSearchService = genericSearchService;
    }


    public List<TriggerModel> findActiveTriggers(Calendar calendar)
    {
        GenericQuery query = new GenericQuery("Trigger");
        query.addConditions(new GenericCondition[] {GenericCondition.equals("active", Boolean.TRUE),
                        GenericCondition.createLessOrEqualCondition(new GenericSearchField("activationTime"), calendar
                                        .getTime())});
        List<TriggerModel> triggers = this.genericSearchService.search(query).getResult();
        List<TriggerModel> returntriggers = new ArrayList<>();
        int clusterID = MasterTenant.getInstance().getClusterID();
        for(TriggerModel trigger : triggers)
        {
            if(trigger.getCronJob() != null && (
                            (trigger.getCronJob().getNodeID() == null) ? 0 : trigger.getCronJob().getNodeID().intValue()) == clusterID)
            {
                returntriggers.add(trigger);
                continue;
            }
            if(trigger.getJob() != null && (
                            (trigger.getJob().getNodeID() == null) ? 0 : trigger.getJob().getNodeID().intValue()) == clusterID)
            {
                returntriggers.add(trigger);
            }
        }
        return returntriggers;
    }
}
