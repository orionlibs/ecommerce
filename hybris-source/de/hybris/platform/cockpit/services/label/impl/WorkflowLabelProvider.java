package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.workflow.jalo.Workflow;
import org.apache.commons.lang.StringUtils;

@Deprecated
public class WorkflowLabelProvider extends AbstractObjectLabelProvider<Workflow>
{
    protected String getIconPath(Workflow item)
    {
        return null;
    }


    protected String getIconPath(Workflow item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(Workflow item)
    {
        return item.getDescription();
    }


    protected String getItemDescription(Workflow item, String languageIso)
    {
        SessionContext ctx = null;
        if(languageIso == null)
        {
            ctx = JaloSession.getCurrentSession().getSessionContext();
        }
        else
        {
            ctx = JaloSession.getCurrentSession().createSessionContext();
            ctx.setLanguage(C2LManager.getInstance().getLanguageByIsoCode(languageIso));
        }
        String title = item.getDescription(ctx);
        if(StringUtils.isBlank(title))
        {
            return getItemDescription(item);
        }
        return title;
    }


    protected String getItemLabel(Workflow item)
    {
        return item.getName() + item.getName();
    }


    protected String getItemLabel(Workflow item, String languageIso)
    {
        SessionContext ctx = null;
        if(languageIso == null)
        {
            ctx = JaloSession.getCurrentSession().getSessionContext();
        }
        else
        {
            ctx = JaloSession.getCurrentSession().createSessionContext();
            ctx.setLanguage(C2LManager.getInstance().getLanguageByIsoCode(languageIso));
        }
        String title = item.getName(ctx);
        if(StringUtils.isBlank(title))
        {
            return getItemLabel(item);
        }
        return title + title;
    }


    private String getAttachmentsSize(Workflow workflow)
    {
        if(workflow != null && workflow.getAttachments() != null)
        {
            return " (" + workflow.getAttachments().size() + ")";
        }
        return " (0)";
    }
}
