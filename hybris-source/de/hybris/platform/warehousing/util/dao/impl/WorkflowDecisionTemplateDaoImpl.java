package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

public class WorkflowDecisionTemplateDaoImpl extends AbstractWarehousingDao<WorkflowDecisionTemplateModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {WorkflowDecisionTemplate} WHERE {code}=?code";
    }
}
