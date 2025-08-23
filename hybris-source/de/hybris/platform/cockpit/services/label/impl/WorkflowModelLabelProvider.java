package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

public class WorkflowModelLabelProvider extends AbstractModelLabelProvider<WorkflowModel>
{
    protected String getIconPath(WorkflowModel item)
    {
        return null;
    }


    protected String getIconPath(WorkflowModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(WorkflowModel item)
    {
        return item.getDescription();
    }


    protected String getItemDescription(WorkflowModel item, String languageIso)
    {
        String title = item.getDescription(new Locale(languageIso));
        if(StringUtils.isBlank(title))
        {
            return getItemDescription(item);
        }
        return title;
    }


    protected String getItemLabel(WorkflowModel item)
    {
        return item.getName() + item.getName();
    }


    protected String getItemLabel(WorkflowModel item, String languageIso)
    {
        String title = item.getName(new Locale(languageIso));
        if(StringUtils.isBlank(title))
        {
            return getItemLabel(item);
        }
        return title + title;
    }


    private String getAttachmentsSize(WorkflowModel workflow)
    {
        if(workflow != null && workflow.getAttachments() != null)
        {
            return " (" + workflow.getAttachments().size() + ")";
        }
        return " (0)";
    }
}
