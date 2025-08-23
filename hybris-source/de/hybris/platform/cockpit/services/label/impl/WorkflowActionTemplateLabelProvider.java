package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.workflow.jalo.WorkflowActionTemplate;
import org.apache.commons.lang.StringUtils;

@Deprecated
public class WorkflowActionTemplateLabelProvider extends AbstractObjectLabelProvider<WorkflowActionTemplate>
{
    protected String getIconPath(WorkflowActionTemplate item)
    {
        return null;
    }


    protected String getIconPath(WorkflowActionTemplate item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(WorkflowActionTemplate item)
    {
        return item.getDescription();
    }


    protected String getItemDescription(WorkflowActionTemplate item, String languageIso)
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


    protected String getItemLabel(WorkflowActionTemplate item)
    {
        return item.getName();
    }


    protected String getItemLabel(WorkflowActionTemplate item, String languageIso)
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
