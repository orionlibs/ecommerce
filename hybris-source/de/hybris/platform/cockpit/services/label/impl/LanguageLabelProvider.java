package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;

@Deprecated
public class LanguageLabelProvider extends AbstractObjectLabelProvider<Language>
{
    protected String getItemLabel(Language item)
    {
        String ret = item.getName();
        if(ret == null)
        {
            ret = item.getIsoCode();
        }
        return ret;
    }


    protected String getItemLabel(Language item, String languageIso)
    {
        SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
        ctx.setLanguage(C2LManager.getInstance().getLanguageByIsoCode(languageIso));
        String ret = item.getName(ctx);
        if(ret == null)
        {
            ret = item.getIsoCode();
        }
        return ret;
    }


    protected String getIconPath(Language item)
    {
        return null;
    }


    protected String getIconPath(Language item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(Language item)
    {
        return "";
    }


    protected String getItemDescription(Language item, String languageIso)
    {
        return "";
    }
}
