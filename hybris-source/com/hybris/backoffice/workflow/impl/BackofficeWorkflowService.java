package com.hybris.backoffice.workflow.impl;

import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.impl.DefaultWorkflowService;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class BackofficeWorkflowService extends DefaultWorkflowService
{
    public List<WorkflowModel> getAllWorkflows(EnumSet<WorkflowStatus> workflowsStatuses, Date dateFrom, Date dateTo)
    {
        if(dateToBeforeDateFrom(dateFrom, dateTo))
        {
            return Collections.emptyList();
        }
        return super.getAllWorkflows(workflowsStatuses, dateFrom, dateTo);
    }


    public SearchResult<WorkflowModel> getAllWorkflows(EnumSet<WorkflowStatus> workflowsStatuses, Date dateFrom, Date dateTo, int startIndex, int pageSize)
    {
        if(dateToBeforeDateFrom(dateFrom, dateTo))
        {
            return (SearchResult<WorkflowModel>)new SearchResultImpl(Collections.emptyList(), 0, pageSize, startIndex);
        }
        return super.getAllWorkflows(workflowsStatuses, dateFrom, dateTo, startIndex, pageSize);
    }


    public List<WorkflowModel> getAllAdhocWorkflows(EnumSet<WorkflowStatus> workflowsStatuses, Date adhocDateFrom, Date adhocDateTo)
    {
        if(dateToBeforeDateFrom(adhocDateFrom, adhocDateTo))
        {
            return Collections.emptyList();
        }
        return super.getAllAdhocWorkflows(workflowsStatuses, adhocDateFrom, adhocDateTo);
    }


    public SearchResult<WorkflowModel> getAllAdhocWorkflows(EnumSet<WorkflowStatus> workflowsStatuses, Date adhocDateFrom, Date adhocDateTo, int startIndex, int pageSize)
    {
        if(dateToBeforeDateFrom(adhocDateFrom, adhocDateTo))
        {
            return (SearchResult<WorkflowModel>)new SearchResultImpl(Collections.emptyList(), 0, pageSize, startIndex);
        }
        return super.getAllAdhocWorkflows(workflowsStatuses, adhocDateFrom, adhocDateTo, startIndex, pageSize);
    }


    protected boolean dateToBeforeDateFrom(Date dateFrom, Date dateTo)
    {
        return (dateFrom != null && dateTo != null && dateTo.before(dateFrom));
    }
}
