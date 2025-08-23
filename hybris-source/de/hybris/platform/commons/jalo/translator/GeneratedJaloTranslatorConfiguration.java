package de.hybris.platform.commons.jalo.translator;

import de.hybris.platform.commons.constants.GeneratedCommonsConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedJaloTranslatorConfiguration extends GenericItem
{
    public static final String CODE = "code";
    public static final String RENDERERS = "renderers";
    public static final String RENDERERSPROPERTIES = "renderersProperties";
    public static final String PARSERPROPERTIES = "parserProperties";
    protected static final OneToManyHandler<JaloVelocityRenderer> RENDERERSHANDLER = new OneToManyHandler(GeneratedCommonsConstants.TC.JALOVELOCITYRENDERER, true, "translatorConfiguration", "translatorConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<RenderersProperty> RENDERERSPROPERTIESHANDLER = new OneToManyHandler(GeneratedCommonsConstants.TC.RENDERERSPROPERTY, true, "translatorConfiguration", "translatorConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<ParserProperty> PARSERPROPERTIESHANDLER = new OneToManyHandler(GeneratedCommonsConstants.TC.PARSERPROPERTY, true, "translatorConfiguration", "translatorConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
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


    public List<ParserProperty> getParserProperties(SessionContext ctx)
    {
        return (List<ParserProperty>)PARSERPROPERTIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<ParserProperty> getParserProperties()
    {
        return getParserProperties(getSession().getSessionContext());
    }


    public void setParserProperties(SessionContext ctx, List<ParserProperty> value)
    {
        PARSERPROPERTIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setParserProperties(List<ParserProperty> value)
    {
        setParserProperties(getSession().getSessionContext(), value);
    }


    public void addToParserProperties(SessionContext ctx, ParserProperty value)
    {
        PARSERPROPERTIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToParserProperties(ParserProperty value)
    {
        addToParserProperties(getSession().getSessionContext(), value);
    }


    public void removeFromParserProperties(SessionContext ctx, ParserProperty value)
    {
        PARSERPROPERTIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromParserProperties(ParserProperty value)
    {
        removeFromParserProperties(getSession().getSessionContext(), value);
    }


    public List<JaloVelocityRenderer> getRenderers(SessionContext ctx)
    {
        return (List<JaloVelocityRenderer>)RENDERERSHANDLER.getValues(ctx, (Item)this);
    }


    public List<JaloVelocityRenderer> getRenderers()
    {
        return getRenderers(getSession().getSessionContext());
    }


    public void setRenderers(SessionContext ctx, List<JaloVelocityRenderer> value)
    {
        RENDERERSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRenderers(List<JaloVelocityRenderer> value)
    {
        setRenderers(getSession().getSessionContext(), value);
    }


    public void addToRenderers(SessionContext ctx, JaloVelocityRenderer value)
    {
        RENDERERSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRenderers(JaloVelocityRenderer value)
    {
        addToRenderers(getSession().getSessionContext(), value);
    }


    public void removeFromRenderers(SessionContext ctx, JaloVelocityRenderer value)
    {
        RENDERERSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRenderers(JaloVelocityRenderer value)
    {
        removeFromRenderers(getSession().getSessionContext(), value);
    }


    public List<RenderersProperty> getRenderersProperties(SessionContext ctx)
    {
        return (List<RenderersProperty>)RENDERERSPROPERTIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<RenderersProperty> getRenderersProperties()
    {
        return getRenderersProperties(getSession().getSessionContext());
    }


    public void setRenderersProperties(SessionContext ctx, List<RenderersProperty> value)
    {
        RENDERERSPROPERTIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRenderersProperties(List<RenderersProperty> value)
    {
        setRenderersProperties(getSession().getSessionContext(), value);
    }


    public void addToRenderersProperties(SessionContext ctx, RenderersProperty value)
    {
        RENDERERSPROPERTIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRenderersProperties(RenderersProperty value)
    {
        addToRenderersProperties(getSession().getSessionContext(), value);
    }


    public void removeFromRenderersProperties(SessionContext ctx, RenderersProperty value)
    {
        RENDERERSPROPERTIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRenderersProperties(RenderersProperty value)
    {
        removeFromRenderersProperties(getSession().getSessionContext(), value);
    }
}
