package de.hybris.platform.catalog.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSyncAttributeDescriptorConfig extends GenericItem
{
    public static final String SYNCJOB = "syncJob";
    public static final String ATTRIBUTEDESCRIPTOR = "attributeDescriptor";
    public static final String INCLUDEDINSYNC = "includedInSync";
    public static final String COPYBYVALUE = "copyByValue";
    public static final String UNTRANSLATABLE = "untranslatable";
    public static final String TRANSLATEVALUE = "translateValue";
    public static final String PRESETVALUE = "presetValue";
    public static final String PARTIALLYTRANSLATABLE = "partiallyTranslatable";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("syncJob", Item.AttributeMode.INITIAL);
        tmp.put("attributeDescriptor", Item.AttributeMode.INITIAL);
        tmp.put("includedInSync", Item.AttributeMode.INITIAL);
        tmp.put("copyByValue", Item.AttributeMode.INITIAL);
        tmp.put("untranslatable", Item.AttributeMode.INITIAL);
        tmp.put("translateValue", Item.AttributeMode.INITIAL);
        tmp.put("presetValue", Item.AttributeMode.INITIAL);
        tmp.put("partiallyTranslatable", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public AttributeDescriptor getAttributeDescriptor(SessionContext ctx)
    {
        return (AttributeDescriptor)getProperty(ctx, "attributeDescriptor");
    }


    public AttributeDescriptor getAttributeDescriptor()
    {
        return getAttributeDescriptor(getSession().getSessionContext());
    }


    protected void setAttributeDescriptor(SessionContext ctx, AttributeDescriptor value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'attributeDescriptor' is not changeable", 0);
        }
        setProperty(ctx, "attributeDescriptor", value);
    }


    protected void setAttributeDescriptor(AttributeDescriptor value)
    {
        setAttributeDescriptor(getSession().getSessionContext(), value);
    }


    public Boolean isCopyByValue(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "copyByValue");
    }


    public Boolean isCopyByValue()
    {
        return isCopyByValue(getSession().getSessionContext());
    }


    public boolean isCopyByValueAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCopyByValue(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCopyByValueAsPrimitive()
    {
        return isCopyByValueAsPrimitive(getSession().getSessionContext());
    }


    public void setCopyByValue(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "copyByValue", value);
    }


    public void setCopyByValue(Boolean value)
    {
        setCopyByValue(getSession().getSessionContext(), value);
    }


    public void setCopyByValue(SessionContext ctx, boolean value)
    {
        setCopyByValue(ctx, Boolean.valueOf(value));
    }


    public void setCopyByValue(boolean value)
    {
        setCopyByValue(getSession().getSessionContext(), value);
    }


    public Boolean isIncludedInSync(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "includedInSync");
    }


    public Boolean isIncludedInSync()
    {
        return isIncludedInSync(getSession().getSessionContext());
    }


    public boolean isIncludedInSyncAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIncludedInSync(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIncludedInSyncAsPrimitive()
    {
        return isIncludedInSyncAsPrimitive(getSession().getSessionContext());
    }


    public void setIncludedInSync(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "includedInSync", value);
    }


    public void setIncludedInSync(Boolean value)
    {
        setIncludedInSync(getSession().getSessionContext(), value);
    }


    public void setIncludedInSync(SessionContext ctx, boolean value)
    {
        setIncludedInSync(ctx, Boolean.valueOf(value));
    }


    public void setIncludedInSync(boolean value)
    {
        setIncludedInSync(getSession().getSessionContext(), value);
    }


    public Boolean isPartiallyTranslatable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "partiallyTranslatable");
    }


    public Boolean isPartiallyTranslatable()
    {
        return isPartiallyTranslatable(getSession().getSessionContext());
    }


    public boolean isPartiallyTranslatableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPartiallyTranslatable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPartiallyTranslatableAsPrimitive()
    {
        return isPartiallyTranslatableAsPrimitive(getSession().getSessionContext());
    }


    public void setPartiallyTranslatable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "partiallyTranslatable", value);
    }


    public void setPartiallyTranslatable(Boolean value)
    {
        setPartiallyTranslatable(getSession().getSessionContext(), value);
    }


    public void setPartiallyTranslatable(SessionContext ctx, boolean value)
    {
        setPartiallyTranslatable(ctx, Boolean.valueOf(value));
    }


    public void setPartiallyTranslatable(boolean value)
    {
        setPartiallyTranslatable(getSession().getSessionContext(), value);
    }


    public Object getPresetValue(SessionContext ctx)
    {
        return getProperty(ctx, "presetValue");
    }


    public Object getPresetValue()
    {
        return getPresetValue(getSession().getSessionContext());
    }


    public void setPresetValue(SessionContext ctx, Object value)
    {
        setProperty(ctx, "presetValue", value);
    }


    public void setPresetValue(Object value)
    {
        setPresetValue(getSession().getSessionContext(), value);
    }


    public SyncItemJob getSyncJob(SessionContext ctx)
    {
        return (SyncItemJob)getProperty(ctx, "syncJob");
    }


    public SyncItemJob getSyncJob()
    {
        return getSyncJob(getSession().getSessionContext());
    }


    protected void setSyncJob(SessionContext ctx, SyncItemJob value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'syncJob' is not changeable", 0);
        }
        setProperty(ctx, "syncJob", value);
    }


    protected void setSyncJob(SyncItemJob value)
    {
        setSyncJob(getSession().getSessionContext(), value);
    }


    public Boolean isTranslateValue(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "translateValue");
    }


    public Boolean isTranslateValue()
    {
        return isTranslateValue(getSession().getSessionContext());
    }


    public boolean isTranslateValueAsPrimitive(SessionContext ctx)
    {
        Boolean value = isTranslateValue(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isTranslateValueAsPrimitive()
    {
        return isTranslateValueAsPrimitive(getSession().getSessionContext());
    }


    public void setTranslateValue(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "translateValue", value);
    }


    public void setTranslateValue(Boolean value)
    {
        setTranslateValue(getSession().getSessionContext(), value);
    }


    public void setTranslateValue(SessionContext ctx, boolean value)
    {
        setTranslateValue(ctx, Boolean.valueOf(value));
    }


    public void setTranslateValue(boolean value)
    {
        setTranslateValue(getSession().getSessionContext(), value);
    }


    public Boolean isUntranslatable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "untranslatable");
    }


    public Boolean isUntranslatable()
    {
        return isUntranslatable(getSession().getSessionContext());
    }


    public boolean isUntranslatableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUntranslatable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUntranslatableAsPrimitive()
    {
        return isUntranslatableAsPrimitive(getSession().getSessionContext());
    }


    public void setUntranslatable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "untranslatable", value);
    }


    public void setUntranslatable(Boolean value)
    {
        setUntranslatable(getSession().getSessionContext(), value);
    }


    public void setUntranslatable(SessionContext ctx, boolean value)
    {
        setUntranslatable(ctx, Boolean.valueOf(value));
    }


    public void setUntranslatable(boolean value)
    {
        setUntranslatable(getSession().getSessionContext(), value);
    }
}
