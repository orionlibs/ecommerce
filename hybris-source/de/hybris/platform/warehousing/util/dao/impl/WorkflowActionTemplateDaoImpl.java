package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;

public class WorkflowActionTemplateDaoImpl extends AbstractWarehousingDao<WorkflowActionTemplateModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {WorkflowActionTemplate} WHERE {code}=?code";
    }
}
