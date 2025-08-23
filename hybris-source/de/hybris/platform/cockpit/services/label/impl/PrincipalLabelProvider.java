package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import org.apache.commons.lang.StringUtils;

@Deprecated
public class PrincipalLabelProvider extends AbstractObjectLabelProvider<Principal>
{
    protected String getIconPath(Principal item)
    {
        return null;
    }


    protected String getIconPath(Principal item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(Principal item)
    {
        String title = item.getDescription();
        return StringUtils.isBlank(title) ? "" : title;
    }


    protected String getItemDescription(Principal item, String languageIso)
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


    protected String getItemLabel(Principal item)
    {
        String label;
        if(item instanceof PrincipalGroup)
        {
            label = ((PrincipalGroup)item).getLocName();
        }
        else
        {
            label = item.getName();
        }
        return StringUtils.isBlank(label) ? item.getUID() : label;
    }


    protected String getItemLabel(Principal item, String languageIso)
    {
        String title;
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
        if(item instanceof PrincipalGroup)
        {
            title = ((PrincipalGroup)item).getLocName(ctx);
        }
        else
        {
            title = item.getName(ctx);
        }
        if(StringUtils.isBlank(title))
        {
            return getItemLabel(item);
        }
        return title;
    }
}
