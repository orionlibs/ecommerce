package de.hybris.platform.personalizationintegration.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.personalizationintegration.constants.GeneratedPersonalizationintegrationConstants;
import de.hybris.platform.personalizationservices.jalo.config.CxConfig;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPersonalizationintegrationManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("autoCreateSegments", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.personalizationservices.jalo.config.CxConfig", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Boolean isAutoCreateSegments(SessionContext ctx, CxConfig item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedPersonalizationintegrationConstants.Attributes.CxConfig.AUTOCREATESEGMENTS);
    }


    public Boolean isAutoCreateSegments(CxConfig item)
    {
        return isAutoCreateSegments(getSession().getSessionContext(), item);
    }


    public boolean isAutoCreateSegmentsAsPrimitive(SessionContext ctx, CxConfig item)
    {
        Boolean value = isAutoCreateSegments(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAutoCreateSegmentsAsPrimitive(CxConfig item)
    {
        return isAutoCreateSegmentsAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setAutoCreateSegments(SessionContext ctx, CxConfig item, Boolean value)
    {
        item.setProperty(ctx, GeneratedPersonalizationintegrationConstants.Attributes.CxConfig.AUTOCREATESEGMENTS, value);
    }


    public void setAutoCreateSegments(CxConfig item, Boolean value)
    {
        setAutoCreateSegments(getSession().getSessionContext(), item, value);
    }


    public void setAutoCreateSegments(SessionContext ctx, CxConfig item, boolean value)
    {
        setAutoCreateSegments(ctx, item, Boolean.valueOf(value));
    }


    public void setAutoCreateSegments(CxConfig item, boolean value)
    {
        setAutoCreateSegments(getSession().getSessionContext(), item, value);
    }


    public CxMapperScript createCxMapperScript(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationintegrationConstants.TC.CXMAPPERSCRIPT);
            return (CxMapperScript)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxMapperScript : " + e.getMessage(), 0);
        }
    }


    public CxMapperScript createCxMapperScript(Map attributeValues)
    {
        return createCxMapperScript(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "personalizationintegration";
    }
}
