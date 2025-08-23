package de.hybris.platform.cms2lib.components;

import de.hybris.platform.cms2.jalo.pages.ContentPage;
import de.hybris.platform.cms2lib.constants.GeneratedCms2LibConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedFlashComponent extends AbstractBannerComponent
{
    public static final String PLAY = "play";
    public static final String LOOP = "loop";
    public static final String MENU = "menu";
    public static final String QUALITY = "quality";
    public static final String SCALE = "scale";
    public static final String WMODE = "wmode";
    public static final String SALIGN = "sAlign";
    public static final String BGCOLOR = "bgcolor";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String PAGELABELORID = "pageLabelOrId";
    public static final String PAGE = "page";
    protected static final BidirectionalOneToManyHandler<GeneratedFlashComponent> PAGEHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2LibConstants.TC.FLASHCOMPONENT, false, "page", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractBannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("play", Item.AttributeMode.INITIAL);
        tmp.put("loop", Item.AttributeMode.INITIAL);
        tmp.put("menu", Item.AttributeMode.INITIAL);
        tmp.put("quality", Item.AttributeMode.INITIAL);
        tmp.put("scale", Item.AttributeMode.INITIAL);
        tmp.put("wmode", Item.AttributeMode.INITIAL);
        tmp.put("sAlign", Item.AttributeMode.INITIAL);
        tmp.put("bgcolor", Item.AttributeMode.INITIAL);
        tmp.put("width", Item.AttributeMode.INITIAL);
        tmp.put("height", Item.AttributeMode.INITIAL);
        tmp.put("page", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getBgcolor(SessionContext ctx)
    {
        return (String)getProperty(ctx, "bgcolor");
    }


    public String getBgcolor()
    {
        return getBgcolor(getSession().getSessionContext());
    }


    public void setBgcolor(SessionContext ctx, String value)
    {
        setProperty(ctx, "bgcolor", value);
    }


    public void setBgcolor(String value)
    {
        setBgcolor(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PAGEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Integer getHeight(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "height");
    }


    public Integer getHeight()
    {
        return getHeight(getSession().getSessionContext());
    }


    public int getHeightAsPrimitive(SessionContext ctx)
    {
        Integer value = getHeight(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getHeightAsPrimitive()
    {
        return getHeightAsPrimitive(getSession().getSessionContext());
    }


    public void setHeight(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "height", value);
    }


    public void setHeight(Integer value)
    {
        setHeight(getSession().getSessionContext(), value);
    }


    public void setHeight(SessionContext ctx, int value)
    {
        setHeight(ctx, Integer.valueOf(value));
    }


    public void setHeight(int value)
    {
        setHeight(getSession().getSessionContext(), value);
    }


    public Boolean isLoop(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "loop");
    }


    public Boolean isLoop()
    {
        return isLoop(getSession().getSessionContext());
    }


    public boolean isLoopAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLoop(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLoopAsPrimitive()
    {
        return isLoopAsPrimitive(getSession().getSessionContext());
    }


    public void setLoop(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "loop", value);
    }


    public void setLoop(Boolean value)
    {
        setLoop(getSession().getSessionContext(), value);
    }


    public void setLoop(SessionContext ctx, boolean value)
    {
        setLoop(ctx, Boolean.valueOf(value));
    }


    public void setLoop(boolean value)
    {
        setLoop(getSession().getSessionContext(), value);
    }


    public Boolean isMenu(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "menu");
    }


    public Boolean isMenu()
    {
        return isMenu(getSession().getSessionContext());
    }


    public boolean isMenuAsPrimitive(SessionContext ctx)
    {
        Boolean value = isMenu(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isMenuAsPrimitive()
    {
        return isMenuAsPrimitive(getSession().getSessionContext());
    }


    public void setMenu(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "menu", value);
    }


    public void setMenu(Boolean value)
    {
        setMenu(getSession().getSessionContext(), value);
    }


    public void setMenu(SessionContext ctx, boolean value)
    {
        setMenu(ctx, Boolean.valueOf(value));
    }


    public void setMenu(boolean value)
    {
        setMenu(getSession().getSessionContext(), value);
    }


    public ContentPage getPage(SessionContext ctx)
    {
        return (ContentPage)getProperty(ctx, "page");
    }


    public ContentPage getPage()
    {
        return getPage(getSession().getSessionContext());
    }


    public void setPage(SessionContext ctx, ContentPage value)
    {
        PAGEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setPage(ContentPage value)
    {
        setPage(getSession().getSessionContext(), value);
    }


    public String getPageLabelOrId()
    {
        return getPageLabelOrId(getSession().getSessionContext());
    }


    public Boolean isPlay(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "play");
    }


    public Boolean isPlay()
    {
        return isPlay(getSession().getSessionContext());
    }


    public boolean isPlayAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPlay(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPlayAsPrimitive()
    {
        return isPlayAsPrimitive(getSession().getSessionContext());
    }


    public void setPlay(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "play", value);
    }


    public void setPlay(Boolean value)
    {
        setPlay(getSession().getSessionContext(), value);
    }


    public void setPlay(SessionContext ctx, boolean value)
    {
        setPlay(ctx, Boolean.valueOf(value));
    }


    public void setPlay(boolean value)
    {
        setPlay(getSession().getSessionContext(), value);
    }


    public EnumerationValue getQuality(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "quality");
    }


    public EnumerationValue getQuality()
    {
        return getQuality(getSession().getSessionContext());
    }


    public void setQuality(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "quality", value);
    }


    public void setQuality(EnumerationValue value)
    {
        setQuality(getSession().getSessionContext(), value);
    }


    public EnumerationValue getSAlign(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "sAlign");
    }


    public EnumerationValue getSAlign()
    {
        return getSAlign(getSession().getSessionContext());
    }


    public void setSAlign(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "sAlign", value);
    }


    public void setSAlign(EnumerationValue value)
    {
        setSAlign(getSession().getSessionContext(), value);
    }


    public EnumerationValue getScale(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "scale");
    }


    public EnumerationValue getScale()
    {
        return getScale(getSession().getSessionContext());
    }


    public void setScale(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "scale", value);
    }


    public void setScale(EnumerationValue value)
    {
        setScale(getSession().getSessionContext(), value);
    }


    public Integer getWidth(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "width");
    }


    public Integer getWidth()
    {
        return getWidth(getSession().getSessionContext());
    }


    public int getWidthAsPrimitive(SessionContext ctx)
    {
        Integer value = getWidth(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getWidthAsPrimitive()
    {
        return getWidthAsPrimitive(getSession().getSessionContext());
    }


    public void setWidth(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "width", value);
    }


    public void setWidth(Integer value)
    {
        setWidth(getSession().getSessionContext(), value);
    }


    public void setWidth(SessionContext ctx, int value)
    {
        setWidth(ctx, Integer.valueOf(value));
    }


    public void setWidth(int value)
    {
        setWidth(getSession().getSessionContext(), value);
    }


    public EnumerationValue getWmode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "wmode");
    }


    public EnumerationValue getWmode()
    {
        return getWmode(getSession().getSessionContext());
    }


    public void setWmode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "wmode", value);
    }


    public void setWmode(EnumerationValue value)
    {
        setWmode(getSession().getSessionContext(), value);
    }


    public abstract String getPageLabelOrId(SessionContext paramSessionContext);
}
