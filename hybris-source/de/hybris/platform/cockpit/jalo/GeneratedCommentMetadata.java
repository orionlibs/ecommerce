package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.comments.jalo.Comment;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCommentMetadata extends GenericItem
{
    public static final String X = "x";
    public static final String Y = "y";
    public static final String PAGEINDEX = "pageIndex";
    public static final String ITEM = "item";
    public static final String COMMENT = "comment";
    protected static final BidirectionalOneToManyHandler<GeneratedCommentMetadata> COMMENTHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.COMMENTMETADATA, false, "comment", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("x", Item.AttributeMode.INITIAL);
        tmp.put("y", Item.AttributeMode.INITIAL);
        tmp.put("pageIndex", Item.AttributeMode.INITIAL);
        tmp.put("item", Item.AttributeMode.INITIAL);
        tmp.put("comment", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Comment getComment(SessionContext ctx)
    {
        return (Comment)getProperty(ctx, "comment");
    }


    public Comment getComment()
    {
        return getComment(getSession().getSessionContext());
    }


    protected void setComment(SessionContext ctx, Comment value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'comment' is not changeable", 0);
        }
        COMMENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setComment(Comment value)
    {
        setComment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        COMMENTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Item getItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "item");
    }


    public Item getItem()
    {
        return getItem(getSession().getSessionContext());
    }


    public void setItem(SessionContext ctx, Item value)
    {
        setProperty(ctx, "item", value);
    }


    public void setItem(Item value)
    {
        setItem(getSession().getSessionContext(), value);
    }


    public Integer getPageIndex(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "pageIndex");
    }


    public Integer getPageIndex()
    {
        return getPageIndex(getSession().getSessionContext());
    }


    public int getPageIndexAsPrimitive(SessionContext ctx)
    {
        Integer value = getPageIndex(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPageIndexAsPrimitive()
    {
        return getPageIndexAsPrimitive(getSession().getSessionContext());
    }


    public void setPageIndex(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "pageIndex", value);
    }


    public void setPageIndex(Integer value)
    {
        setPageIndex(getSession().getSessionContext(), value);
    }


    public void setPageIndex(SessionContext ctx, int value)
    {
        setPageIndex(ctx, Integer.valueOf(value));
    }


    public void setPageIndex(int value)
    {
        setPageIndex(getSession().getSessionContext(), value);
    }


    public Integer getX(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "x");
    }


    public Integer getX()
    {
        return getX(getSession().getSessionContext());
    }


    public int getXAsPrimitive(SessionContext ctx)
    {
        Integer value = getX(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getXAsPrimitive()
    {
        return getXAsPrimitive(getSession().getSessionContext());
    }


    public void setX(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "x", value);
    }


    public void setX(Integer value)
    {
        setX(getSession().getSessionContext(), value);
    }


    public void setX(SessionContext ctx, int value)
    {
        setX(ctx, Integer.valueOf(value));
    }


    public void setX(int value)
    {
        setX(getSession().getSessionContext(), value);
    }


    public Integer getY(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "y");
    }


    public Integer getY()
    {
        return getY(getSession().getSessionContext());
    }


    public int getYAsPrimitive(SessionContext ctx)
    {
        Integer value = getY(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getYAsPrimitive()
    {
        return getYAsPrimitive(getSession().getSessionContext());
    }


    public void setY(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "y", value);
    }


    public void setY(Integer value)
    {
        setY(getSession().getSessionContext(), value);
    }


    public void setY(SessionContext ctx, int value)
    {
        setY(ctx, Integer.valueOf(value));
    }


    public void setY(int value)
    {
        setY(getSession().getSessionContext(), value);
    }
}
