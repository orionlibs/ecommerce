package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.workflow.jalo.WorkflowTemplate;
import org.apache.commons.lang.StringUtils;

@Deprecated
public class WorkflowTemplateLabelProvider extends AbstractObjectLabelProvider<WorkflowTemplate>
{
    protected String getIconPath(WorkflowTemplate item)
    {
        return null;
    }


    protected String getIconPath(WorkflowTemplate item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(WorkflowTemplate item)
    {
        return item.getDescription();
    }


    protected String getItemDescription(WorkflowTemplate item, String languageIso)
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


    protected String getItemLabel(WorkflowTemplate item)
    {
        return item.getName();
    }


    protected String getItemLabel(WorkflowTemplate item, String languageIso)
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
        return title;
    }
}
