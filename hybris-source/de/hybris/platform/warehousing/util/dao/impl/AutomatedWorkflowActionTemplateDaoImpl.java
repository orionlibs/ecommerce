package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;

public class AutomatedWorkflowActionTemplateDaoImpl extends AbstractWarehousingDao<AutomatedWorkflowActionTemplateModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {AutomatedWorkflowActionTemplate} WHERE {code}=?code";
    }
}
