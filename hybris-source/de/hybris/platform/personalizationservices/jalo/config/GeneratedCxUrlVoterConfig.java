package de.hybris.platform.personalizationservices.jalo.config;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxUrlVoterConfig extends CxAbstractCalcConfig
{
    public static final String URLREGEXP = "urlRegexp";
    public static final String CXCONFIGPOS = "cxConfigPOS";
    public static final String CXCONFIG = "cxConfig";
    protected static final BidirectionalOneToManyHandler<GeneratedCxUrlVoterConfig> CXCONFIGHANDLER = new BidirectionalOneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXURLVOTERCONFIG, false, "cxConfig", "cxConfigPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CxAbstractCalcConfig.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("urlRegexp", Item.AttributeMode.INITIAL);
        tmp.put("cxConfigPOS", Item.AttributeMode.INITIAL);
        tmp.put("cxConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CXCONFIGHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public CxConfig getCxConfig(SessionContext ctx)
    {
        return (CxConfig)getProperty(ctx, "cxConfig");
    }


    public CxConfig getCxConfig()
    {
        return getCxConfig(getSession().getSessionContext());
    }


    public void setCxConfig(SessionContext ctx, CxConfig value)
    {
        CXCONFIGHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCxConfig(CxConfig value)
    {
        setCxConfig(getSession().getSessionContext(), value);
    }


    Integer getCxConfigPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "cxConfigPOS");
    }


    Integer getCxConfigPOS()
    {
        return getCxConfigPOS(getSession().getSessionContext());
    }


    int getCxConfigPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getCxConfigPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getCxConfigPOSAsPrimitive()
    {
        return getCxConfigPOSAsPrimitive(getSession().getSessionContext());
    }


    void setCxConfigPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "cxConfigPOS", value);
    }


    void setCxConfigPOS(Integer value)
    {
        setCxConfigPOS(getSession().getSessionContext(), value);
    }


    void setCxConfigPOS(SessionContext ctx, int value)
    {
        setCxConfigPOS(ctx, Integer.valueOf(value));
    }


    void setCxConfigPOS(int value)
    {
        setCxConfigPOS(getSession().getSessionContext(), value);
    }


    public String getUrlRegexp(SessionContext ctx)
    {
        return (String)getProperty(ctx, "urlRegexp");
    }


    public String getUrlRegexp()
    {
        return getUrlRegexp(getSession().getSessionContext());
    }


    public void setUrlRegexp(SessionContext ctx, String value)
    {
        setProperty(ctx, "urlRegexp", value);
    }


    public void setUrlRegexp(String value)
    {
        setUrlRegexp(getSession().getSessionContext(), value);
    }
}
