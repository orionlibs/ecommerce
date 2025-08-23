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

public abstract class GeneratedParserProperty extends GenericItem
{
    public static final String NAME = "name";
    public static final String STARTEXP = "startExp";
    public static final String ENDEXP = "endExp";
    public static final String PARSERCLASS = "parserClass";
    public static final String TRANSLATORCONFIGURATIONPOS = "translatorConfigurationPOS";
    public static final String TRANSLATORCONFIGURATION = "translatorConfiguration";
    protected static final BidirectionalOneToManyHandler<GeneratedParserProperty> TRANSLATORCONFIGURATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedCommonsConstants.TC.PARSERPROPERTY, false, "translatorConfiguration", "translatorConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("startExp", Item.AttributeMode.INITIAL);
        tmp.put("endExp", Item.AttributeMode.INITIAL);
        tmp.put("parserClass", Item.AttributeMode.INITIAL);
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


    public String getEndExp(SessionContext ctx)
    {
        return (String)getProperty(ctx, "endExp");
    }


    public String getEndExp()
    {
        return getEndExp(getSession().getSessionContext());
    }


    public void setEndExp(SessionContext ctx, String value)
    {
        setProperty(ctx, "endExp", value);
    }


    public void setEndExp(String value)
    {
        setEndExp(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public String getParserClass(SessionContext ctx)
    {
        return (String)getProperty(ctx, "parserClass");
    }


    public String getParserClass()
    {
        return getParserClass(getSession().getSessionContext());
    }


    public void setParserClass(SessionContext ctx, String value)
    {
        setProperty(ctx, "parserClass", value);
    }


    public void setParserClass(String value)
    {
        setParserClass(getSession().getSessionContext(), value);
    }


    public String getStartExp(SessionContext ctx)
    {
        return (String)getProperty(ctx, "startExp");
    }


    public String getStartExp()
    {
        return getStartExp(getSession().getSessionContext());
    }


    public void setStartExp(SessionContext ctx, String value)
    {
        setProperty(ctx, "startExp", value);
    }


    public void setStartExp(String value)
    {
        setStartExp(getSession().getSessionContext(), value);
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
}
