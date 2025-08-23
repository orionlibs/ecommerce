package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.internal.i18n.I18NConstants;

public class ItemType extends DefaultObjectType implements BaseType
{
    private final ComposedTypeModel composedType;
    private final boolean isAbstractType;
    private final boolean isJaloOnlyType;
    private final boolean isSingletonType;


    public ItemType(ComposedTypeModel composedType)
    {
        super(composedType.getCode());
        this.composedType = composedType;
        this.isAbstractType = TypeTools.primitiveValue(this.composedType.getAbstract());
        this.isJaloOnlyType = TypeTools.primitiveValue(this.composedType.getJaloonly());
        this.isSingletonType = TypeTools.primitiveValue(this.composedType.getSingleton());
    }


    public ComposedTypeModel getComposedType()
    {
        return this.composedType;
    }


    public boolean isAbstract()
    {
        return this.isAbstractType;
    }


    public boolean isJaloOnly()
    {
        return this.isJaloOnlyType;
    }


    public boolean isSingleton()
    {
        return this.isSingletonType;
    }


    public String toString()
    {
        ComposedTypeModel composedTypeModel = getComposedType();
        return super.toString() + " (" + super.toString() + ")";
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
            SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext(JaloSession.getCurrentSession().getSessionContext());
            ctx.setAttribute("enable.language.fallback.serviceLayer", Boolean.TRUE);
            ctx.setAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);
            if(languageIsoCode != null)
            {
                ctx.setLanguage(JaloSession.getCurrentSession().getC2LManager().getLanguageByIsoCode(languageIsoCode));
            }
            name = getComposedType().getName(ctx.getLocale());
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
        return name;
    }


    public String getDescription()
    {
        return getDescription(null);
    }


    public String getDescription(String languageIsoCode)
    {
        String description = "";
        try
        {
            SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext(JaloSession.getCurrentSession().getSessionContext());
            ctx.setAttribute("enable.language.fallback.serviceLayer", Boolean.TRUE);
            ctx.setAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);
            if(languageIsoCode != null)
            {
                ctx.setLanguage(JaloSession.getCurrentSession().getC2LManager().getLanguageByIsoCode(languageIsoCode));
            }
            description = getComposedType().getDescription(ctx.getLocale());
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
        return description;
    }
}
