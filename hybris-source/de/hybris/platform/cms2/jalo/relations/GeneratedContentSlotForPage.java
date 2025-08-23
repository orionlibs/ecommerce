package de.hybris.platform.cms2.jalo.relations;

import de.hybris.platform.cms2.jalo.contents.contentslot.ContentSlot;
import de.hybris.platform.cms2.jalo.pages.AbstractPage;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedContentSlotForPage extends CMSRelation
{
    public static final String POSITION = "position";
    public static final String PAGE = "page";
    public static final String CONTENTSLOT = "contentSlot";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CMSRelation.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("position", Item.AttributeMode.INITIAL);
        tmp.put("page", Item.AttributeMode.INITIAL);
        tmp.put("contentSlot", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public ContentSlot getContentSlot(SessionContext ctx)
    {
        return (ContentSlot)getProperty(ctx, "contentSlot");
    }


    public ContentSlot getContentSlot()
    {
        return getContentSlot(getSession().getSessionContext());
    }


    public void setContentSlot(SessionContext ctx, ContentSlot value)
    {
        setProperty(ctx, "contentSlot", value);
    }


    public void setContentSlot(ContentSlot value)
    {
        setContentSlot(getSession().getSessionContext(), value);
    }


    public AbstractPage getPage(SessionContext ctx)
    {
        return (AbstractPage)getProperty(ctx, "page");
    }


    public AbstractPage getPage()
    {
        return getPage(getSession().getSessionContext());
    }


    public void setPage(SessionContext ctx, AbstractPage value)
    {
        setProperty(ctx, "page", value);
    }


    public void setPage(AbstractPage value)
    {
        setPage(getSession().getSessionContext(), value);
    }


    public String getPosition(SessionContext ctx)
    {
        return (String)getProperty(ctx, "position");
    }


    public String getPosition()
    {
        return getPosition(getSession().getSessionContext());
    }


    public void setPosition(SessionContext ctx, String value)
    {
        setProperty(ctx, "position", value);
    }


    public void setPosition(String value)
    {
        setPosition(getSession().getSessionContext(), value);
    }
}
