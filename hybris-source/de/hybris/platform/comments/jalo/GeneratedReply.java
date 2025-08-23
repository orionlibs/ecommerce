package de.hybris.platform.comments.jalo;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedReply extends AbstractComment
{
    public static final String REPLIES = "replies";
    public static final String PARENTPOS = "parentPOS";
    public static final String PARENT = "parent";
    public static final String COMMENTPOS = "commentPOS";
    public static final String COMMENT = "comment";
    protected static final OneToManyHandler<Reply> REPLIESHANDLER = new OneToManyHandler(GeneratedCommentsConstants.TC.REPLY, true, "parent", "parentPOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedReply> PARENTHANDLER = new BidirectionalOneToManyHandler(GeneratedCommentsConstants.TC.REPLY, false, "parent", "parentPOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedReply> COMMENTHANDLER = new BidirectionalOneToManyHandler(GeneratedCommentsConstants.TC.REPLY, false, "comment", "commentPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractComment.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("parentPOS", Item.AttributeMode.INITIAL);
        tmp.put("parent", Item.AttributeMode.INITIAL);
        tmp.put("commentPOS", Item.AttributeMode.INITIAL);
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


    Integer getCommentPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "commentPOS");
    }


    Integer getCommentPOS()
    {
        return getCommentPOS(getSession().getSessionContext());
    }


    int getCommentPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getCommentPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getCommentPOSAsPrimitive()
    {
        return getCommentPOSAsPrimitive(getSession().getSessionContext());
    }


    void setCommentPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "commentPOS", value);
    }


    void setCommentPOS(Integer value)
    {
        setCommentPOS(getSession().getSessionContext(), value);
    }


    void setCommentPOS(SessionContext ctx, int value)
    {
        setCommentPOS(ctx, Integer.valueOf(value));
    }


    void setCommentPOS(int value)
    {
        setCommentPOS(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PARENTHANDLER.newInstance(ctx, allAttributes);
        COMMENTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Reply getParent(SessionContext ctx)
    {
        return (Reply)getProperty(ctx, "parent");
    }


    public Reply getParent()
    {
        return getParent(getSession().getSessionContext());
    }


    protected void setParent(SessionContext ctx, Reply value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'parent' is not changeable", 0);
        }
        PARENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setParent(Reply value)
    {
        setParent(getSession().getSessionContext(), value);
    }


    Integer getParentPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "parentPOS");
    }


    Integer getParentPOS()
    {
        return getParentPOS(getSession().getSessionContext());
    }


    int getParentPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getParentPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getParentPOSAsPrimitive()
    {
        return getParentPOSAsPrimitive(getSession().getSessionContext());
    }


    void setParentPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "parentPOS", value);
    }


    void setParentPOS(Integer value)
    {
        setParentPOS(getSession().getSessionContext(), value);
    }


    void setParentPOS(SessionContext ctx, int value)
    {
        setParentPOS(ctx, Integer.valueOf(value));
    }


    void setParentPOS(int value)
    {
        setParentPOS(getSession().getSessionContext(), value);
    }


    public List<Reply> getReplies(SessionContext ctx)
    {
        return (List<Reply>)REPLIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<Reply> getReplies()
    {
        return getReplies(getSession().getSessionContext());
    }


    public void setReplies(SessionContext ctx, List<Reply> value)
    {
        REPLIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setReplies(List<Reply> value)
    {
        setReplies(getSession().getSessionContext(), value);
    }


    public void addToReplies(SessionContext ctx, Reply value)
    {
        REPLIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToReplies(Reply value)
    {
        addToReplies(getSession().getSessionContext(), value);
    }


    public void removeFromReplies(SessionContext ctx, Reply value)
    {
        REPLIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromReplies(Reply value)
    {
        removeFromReplies(getSession().getSessionContext(), value);
    }
}
