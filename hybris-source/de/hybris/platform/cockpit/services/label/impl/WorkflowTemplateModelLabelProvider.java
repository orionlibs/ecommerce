package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

public class WorkflowTemplateModelLabelProvider extends AbstractModelLabelProvider<WorkflowTemplateModel>
{
    protected String getIconPath(WorkflowTemplateModel item)
    {
        return null;
    }


    protected String getIconPath(WorkflowTemplateModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(WorkflowTemplateModel item)
    {
        return item.getDescription();
    }


    protected String getItemDescription(WorkflowTemplateModel item, String languageIso)
    {
        String title = item.getDescription(new Locale(languageIso));
        if(StringUtils.isBlank(title))
        {
            return getItemDescription(item);
        }
        return title;
    }


    protected String getItemLabel(WorkflowTemplateModel item)
    {
        return item.getName();
    }


    protected String getItemLabel(WorkflowTemplateModel item, String languageIso)
    {
        String title = item.getName(new Locale(languageIso));
        if(StringUtils.isBlank(title))
        {
            return getItemLabel(item);
        }
        return title;
    }
}
