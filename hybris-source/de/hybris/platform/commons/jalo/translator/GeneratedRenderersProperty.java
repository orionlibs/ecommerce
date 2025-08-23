package de.hybris.platform.commons.jalo.translator;

import de.hybris.platform.commons.constants.GeneratedCommonsConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRenderersProperty extends GenericItem
{
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String TRANSLATORCONFIGURATIONPOS = "translatorConfigurationPOS";
    public static final String TRANSLATORCONFIGURATION = "translatorConfiguration";
    protected static final BidirectionalOneToManyHandler<GeneratedRenderersProperty> TRANSLATORCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedCommonsConstants.TC.RENDERERSPROPERTY, false, "translatorConfiguration", "translatorConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("key", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("translatorConfigurationPOS", Item.AttributeMode.INITIAL);
        tmp.put("translatorConfiguration", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        TRANSLATORCONFIGURATIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getKey(SessionContext ctx)
    {
        return (String)getProperty(ctx, "key");
    }


    public String getKey()
    {
        return getKey(getSession().getSessionContext());
    }


    public void setKey(SessionContext ctx, String value)
    {
        setProperty(ctx, "key", value);
    }


    public void setKey(String value)
    {
        setKey(getSession().getSessionContext(), value);
    }


    public JaloTranslatorConfiguration getTranslatorConfiguration(SessionContext ctx)
    {
        return (JaloTranslatorConfiguration)getProperty(ctx, "translatorConfiguration");
    }


    public JaloTranslatorConfiguration getTranslatorConfiguration()
    {
        return getTranslatorConfiguration(getSession().getSessionContext());
    }


    public void setTranslatorConfiguration(SessionContext ctx, JaloTranslatorConfiguration value)
    {
        TRANSLATORCONFIGURATIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setTranslatorConfiguration(JaloTranslatorConfiguration value)
    {
        setTranslatorConfiguration(getSession().getSessionContext(), value);
    }


    Integer getTranslatorConfigurationPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "translatorConfigurationPOS");
    }


    Integer getTranslatorConfigurationPOS()
    {
        return getTranslatorConfigurationPOS(getSession().getSessionContext());
    }


    int getTranslatorConfigurationPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getTranslatorConfigurationPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getTranslatorConfigurationPOSAsPrimitive()
    {
        return getTranslatorConfigurationPOSAsPrimitive(getSession().getSessionContext());
    }


    void setTranslatorConfigurationPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "translatorConfigurationPOS", value);
    }


    void setTranslatorConfigurationPOS(Integer value)
    {
        setTranslatorConfigurationPOS(getSession().getSessionContext(), value);
    }


    void setTranslatorConfigurationPOS(SessionContext ctx, int value)
    {
        setTranslatorConfigurationPOS(ctx, Integer.valueOf(value));
    }


    void setTranslatorConfigurationPOS(int value)
    {
        setTranslatorConfigurationPOS(getSession().getSessionContext(), value);
    }


    public String getValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "value");
    }


    public String getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(String value)
    {
        setValue(getSession().getSessionContext(), value);
    }
}
