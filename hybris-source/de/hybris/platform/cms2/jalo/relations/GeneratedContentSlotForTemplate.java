package de.hybris.platform.cms2.jalo.relations;

import de.hybris.platform.cms2.jalo.contents.contentslot.ContentSlot;
import de.hybris.platform.cms2.jalo.pages.PageTemplate;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedContentSlotForTemplate extends CMSRelation
{
    public static final String ALLOWOVERWRITE = "allowOverwrite";
    public static final String POSITION = "position";
    public static final String PAGETEMPLATE = "pageTemplate";
    public static final String CONTENTSLOT = "contentSlot";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CMSRelation.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("allowOverwrite", Item.AttributeMode.INITIAL);
        tmp.put("position", Item.AttributeMode.INITIAL);
        tmp.put("pageTemplate", Item.AttributeMode.INITIAL);
        tmp.put("contentSlot", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAllowOverwrite(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "allowOverwrite");
    }


    public Boolean isAllowOverwrite()
    {
        return isAllowOverwrite(getSession().getSessionContext());
    }


    public boolean isAllowOverwriteAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAllowOverwrite(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAllowOverwriteAsPrimitive()
    {
        return isAllowOverwriteAsPrimitive(getSession().getSessionContext());
    }


    public void setAllowOverwrite(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "allowOverwrite", value);
    }


    public void setAllowOverwrite(Boolean value)
    {
        setAllowOverwrite(getSession().getSessionContext(), value);
    }


    public void setAllowOverwrite(SessionContext ctx, boolean value)
    {
        setAllowOverwrite(ctx, Boolean.valueOf(value));
    }


    public void setAllowOverwrite(boolean value)
    {
        setAllowOverwrite(getSession().getSessionContext(), value);
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


    public PageTemplate getPageTemplate(SessionContext ctx)
    {
        return (PageTemplate)getProperty(ctx, "pageTemplate");
    }


    public PageTemplate getPageTemplate()
    {
        return getPageTemplate(getSession().getSessionContext());
    }


    public void setPageTemplate(SessionContext ctx, PageTemplate value)
    {
        setProperty(ctx, "pageTemplate", value);
    }


    public void setPageTemplate(PageTemplate value)
    {
        setPageTemplate(getSession().getSessionContext(), value);
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
