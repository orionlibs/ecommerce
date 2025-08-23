package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.List;

public class WorkflowDaoImpl extends AbstractWarehousingDao<WorkflowModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {Workflow}";
    }


    public List<Object> findAllWorkflows()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(getQuery());
        return getFlexibleSearchService().search(query).getResult();
    }
}
