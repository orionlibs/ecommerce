package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.workflow.model.WorkflowTemplateModel;

public class WorkflowTemplateDaoImpl extends AbstractWarehousingDao<WorkflowTemplateModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {WorkflowTemplate} WHERE {code}=?code";
    }
}
