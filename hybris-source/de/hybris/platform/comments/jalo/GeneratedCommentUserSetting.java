package de.hybris.platform.comments.jalo;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCommentUserSetting extends GenericItem
{
    public static final String READ = "read";
    public static final String IGNORED = "ignored";
    public static final String PRIORITY = "priority";
    public static final String COMMENT = "comment";
    public static final String USER = "user";
    protected static final BidirectionalOneToManyHandler<GeneratedCommentUserSetting> COMMENTHANDLER = new BidirectionalOneToManyHandler(GeneratedCommentsConstants.TC.COMMENTUSERSETTING, false, "comment", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedCommentUserSetting> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedCommentsConstants.TC.COMMENTUSERSETTING, false, "user", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("read", Item.AttributeMode.INITIAL);
        tmp.put("ignored", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("comment", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public AbstractComment getComment(SessionContext ctx)
    {
        return (AbstractComment)getProperty(ctx, "comment");
    }


    public AbstractComment getComment()
    {
        return getComment(getSession().getSessionContext());
    }


    protected void setComment(SessionContext ctx, AbstractComment value)
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


    protected void setComment(AbstractComment value)
    {
        setComment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        COMMENTHANDLER.newInstance(ctx, allAttributes);
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isIgnored(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ignored");
    }


    public Boolean isIgnored()
    {
        return isIgnored(getSession().getSessionContext());
    }


    public boolean isIgnoredAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIgnored(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIgnoredAsPrimitive()
    {
        return isIgnoredAsPrimitive(getSession().getSessionContext());
    }


    public void setIgnored(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ignored", value);
    }


    public void setIgnored(Boolean value)
    {
        setIgnored(getSession().getSessionContext(), value);
    }


    public void setIgnored(SessionContext ctx, boolean value)
    {
        setIgnored(ctx, Boolean.valueOf(value));
    }


    public void setIgnored(boolean value)
    {
        setIgnored(getSession().getSessionContext(), value);
    }


    public Integer getPriority(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "priority");
    }


    public Integer getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public int getPriorityAsPrimitive(SessionContext ctx)
    {
        Integer value = getPriority(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityAsPrimitive()
    {
        return getPriorityAsPrimitive(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(Integer value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public void setPriority(SessionContext ctx, int value)
    {
        setPriority(ctx, Integer.valueOf(value));
    }


    public void setPriority(int value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public Boolean isRead(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "read");
    }


    public Boolean isRead()
    {
        return isRead(getSession().getSessionContext());
    }


    public boolean isReadAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRead(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isReadAsPrimitive()
    {
        return isReadAsPrimitive(getSession().getSessionContext());
    }


    public void setRead(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "read", value);
    }


    public void setRead(Boolean value)
    {
        setRead(getSession().getSessionContext(), value);
    }


    public void setRead(SessionContext ctx, boolean value)
    {
        setRead(ctx, Boolean.valueOf(value));
    }


    public void setRead(boolean value)
    {
        setRead(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    protected void setUser(SessionContext ctx, User value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'user' is not changeable", 0);
        }
        USERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
