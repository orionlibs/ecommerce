package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.internal.i18n.I18NConstants;

public class ClassificationType extends DefaultObjectType implements ExtendedType
{
    private final ClassificationClassPath classPath;


    public ClassificationType(String code)
    {
        super(code);
        this.classPath = new ClassificationClassPath(code);
    }


    public ClassificationClass getClassificationClass()
    {
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            return CatalogManager.getInstance().getClassificationClass(this.classPath.getClassSystem(), this.classPath
                            .getClassVersion(), this.classPath.getClassClass());
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }


    public boolean isAbstract()
    {
        return false;
    }


    public String getName()
    {
        return getName(null);
    }


    public String getName(String languageIsoCode)
    {
        String name = "";
        try
        {
            SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext(
                            JaloSession.getCurrentSession().getSessionContext());
            ctx.setAttribute("enable.language.fallback.serviceLayer", Boolean.TRUE);
            ctx.setAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);
            if(languageIsoCode != null)
            {
                ctx.setLanguage(JaloSession.getCurrentSession().getC2LManager().getLanguageByIsoCode(languageIsoCode));
            }
            name = getClassificationClass().getName(ctx);
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
        return name;
    }


    public String getDescription()
    {
        return null;
    }


    public String getDescription(String languageIsoCode)
    {
        return null;
    }
}
