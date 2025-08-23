package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.template.CockpitItemTemplateModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.internal.i18n.I18NConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class ItemTemplate extends DefaultObjectType implements ObjectTemplate
{
    private final CockpitItemTemplateModel cockpitItemTemplate;
    private final ItemType baseType;
    private final String code;
    private final Collection<ExtendedType> extendedTypes;


    public ItemTemplate(CockpitItemTemplateModel cockpitItemTemplate, ItemType baseType, Collection<? extends ExtendedType> extendedTypes, String code)
    {
        super(code);
        this.cockpitItemTemplate = cockpitItemTemplate;
        this.baseType = baseType;
        this.extendedTypes = new ArrayList<>(extendedTypes);
        this.code = code;
    }


    public CockpitItemTemplateModel getCockpitItemTemplate()
    {
        return this.cockpitItemTemplate;
    }


    public String getBaseTypeCode()
    {
        int delimPosition = this.code.lastIndexOf('.');
        boolean typeOnly = (delimPosition == -1);
        if(!typeOnly)
        {
            return this.code.substring(0, delimPosition);
        }
        return this.code;
    }


    public String getTemplateCode()
    {
        int delimPosition = this.code.lastIndexOf('.');
        boolean typeOnly = (delimPosition == -1);
        if(!typeOnly)
        {
            String templateCode = this.code.substring(delimPosition + 1);
            if(StringUtils.isBlank(templateCode))
            {
                return this.code.substring(0, delimPosition);
            }
            return templateCode;
        }
        return this.code;
    }


    public ItemType getBaseType()
    {
        return this.baseType;
    }


    public Collection<? extends ExtendedType> getExtendedTypes()
    {
        return this.extendedTypes;
    }


    public Set<PropertyDescriptor> getDeclaredPropertyDescriptors()
    {
        Set<PropertyDescriptor> props = new HashSet<>();
        props.addAll(getBaseType().getDeclaredPropertyDescriptors());
        Collection<? extends ExtendedType> types = getExtendedTypes();
        for(ExtendedType ctype : types)
        {
            props.addAll(ctype.getDeclaredPropertyDescriptors());
        }
        return props;
    }


    public Set<PropertyDescriptor> getPropertyDescriptors()
    {
        Set<PropertyDescriptor> props = new HashSet<>();
        props.addAll(getBaseType().getPropertyDescriptors());
        Collection<? extends ExtendedType> types = getExtendedTypes();
        for(ExtendedType ctype : types)
        {
            props.addAll(ctype.getPropertyDescriptors());
        }
        return props;
    }


    public Set<ObjectType> getSubtypes()
    {
        return Collections.EMPTY_SET;
    }


    public Set<ObjectType> getSupertypes()
    {
        return Collections.EMPTY_SET;
    }


    public boolean isDefaultTemplate()
    {
        return getBaseTypeCode().equalsIgnoreCase(getTemplateCode());
    }


    public boolean isAbstract()
    {
        return getBaseType().isAbstract();
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
            name = (this.cockpitItemTemplate != null) ? this.cockpitItemTemplate.getName(ctx.getLocale()) : this.baseType.getName(languageIsoCode);
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
            SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext(
                            JaloSession.getCurrentSession().getSessionContext());
            ctx.setAttribute("enable.language.fallback.serviceLayer", Boolean.TRUE);
            ctx.setAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);
            if(languageIsoCode != null)
            {
                ctx.setLanguage(JaloSession.getCurrentSession().getC2LManager().getLanguageByIsoCode(languageIsoCode));
            }
            description = (this.cockpitItemTemplate != null) ? this.cockpitItemTemplate.getDescription(ctx.getLocale()) : this.baseType.getDescription(languageIsoCode);
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
        return description;
    }
}
