package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

public class WorkflowActionTemplateModelLabelProvider extends AbstractModelLabelProvider<WorkflowActionTemplateModel>
{
    protected String getIconPath(WorkflowActionTemplateModel item)
    {
        return null;
    }


    protected String getIconPath(WorkflowActionTemplateModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(WorkflowActionTemplateModel item)
    {
        return item.getDescription();
    }


    protected String getItemDescription(WorkflowActionTemplateModel item, String languageIso)
    {
        String title = item.getDescription(new Locale(languageIso));
        if(StringUtils.isBlank(title))
        {
            return getItemDescription(item);
        }
        return title;
    }


    protected String getItemLabel(WorkflowActionTemplateModel item)
    {
        return item.getName();
    }


    protected String getItemLabel(WorkflowActionTemplateModel item, String languageIso)
    {
        String title = item.getName(new Locale(languageIso));
        if(StringUtils.isBlank(title))
        {
            return getItemLabel(item);
        }
        return title;
    }
}
