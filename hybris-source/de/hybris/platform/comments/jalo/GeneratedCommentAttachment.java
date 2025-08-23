package de.hybris.platform.comments.jalo;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
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

public abstract class GeneratedCommentAttachment extends GenericItem
{
    public static final String ITEM = "item";
    public static final String ABSTRACTCOMMENT = "abstractComment";
    protected static final BidirectionalOneToManyHandler<GeneratedCommentAttachment> ABSTRACTCOMMENTHANDLER = new BidirectionalOneToManyHandler(GeneratedCommentsConstants.TC.COMMENTATTACHMENT, false, "abstractComment", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("item", Item.AttributeMode.INITIAL);
        tmp.put("abstractComment", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public AbstractComment getAbstractComment(SessionContext ctx)
    {
        return (AbstractComment)getProperty(ctx, "abstractComment");
    }


    public AbstractComment getAbstractComment()
    {
        return getAbstractComment(getSession().getSessionContext());
    }


    public void setAbstractComment(SessionContext ctx, AbstractComment value)
    {
        ABSTRACTCOMMENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setAbstractComment(AbstractComment value)
    {
        setAbstractComment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ABSTRACTCOMMENTHANDLER.newInstance(ctx, allAttributes);
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
}
