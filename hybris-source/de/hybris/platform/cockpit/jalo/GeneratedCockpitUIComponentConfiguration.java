package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCockpitUIComponentConfiguration extends GenericItem
{
    public static final String FACTORYBEAN = "factoryBean";
    public static final String CODE = "code";
    public static final String OBJECTTEMPLATECODE = "objectTemplateCode";
    public static final String MEDIA = "media";
    public static final String PRINCIPAL = "principal";
    protected static final BidirectionalOneToManyHandler<GeneratedCockpitUIComponentConfiguration> PRINCIPALHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.COCKPITUICOMPONENTCONFIGURATION, false, "principal", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("factoryBean", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("objectTemplateCode", Item.AttributeMode.INITIAL);
        tmp.put("media", Item.AttributeMode.INITIAL);
        tmp.put("principal", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PRINCIPALHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getFactoryBean(SessionContext ctx)
    {
        return (String)getProperty(ctx, "factoryBean");
    }


    public String getFactoryBean()
    {
        return getFactoryBean(getSession().getSessionContext());
    }


    public void setFactoryBean(SessionContext ctx, String value)
    {
        setProperty(ctx, "factoryBean", value);
    }


    public void setFactoryBean(String value)
    {
        setFactoryBean(getSession().getSessionContext(), value);
    }


    public Media getMedia(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "media");
    }


    public Media getMedia()
    {
        return getMedia(getSession().getSessionContext());
    }


    public void setMedia(SessionContext ctx, Media value)
    {
        setProperty(ctx, "media", value);
    }


    public void setMedia(Media value)
    {
        setMedia(getSession().getSessionContext(), value);
    }


    public String getObjectTemplateCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "objectTemplateCode");
    }


    public String getObjectTemplateCode()
    {
        return getObjectTemplateCode(getSession().getSessionContext());
    }


    public void setObjectTemplateCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "objectTemplateCode", value);
    }


    public void setObjectTemplateCode(String value)
    {
        setObjectTemplateCode(getSession().getSessionContext(), value);
    }


    public Principal getPrincipal(SessionContext ctx)
    {
        return (Principal)getProperty(ctx, "principal");
    }


    public Principal getPrincipal()
    {
        return getPrincipal(getSession().getSessionContext());
    }


    public void setPrincipal(SessionContext ctx, Principal value)
    {
        PRINCIPALHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setPrincipal(Principal value)
    {
        setPrincipal(getSession().getSessionContext(), value);
    }
}
