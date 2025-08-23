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
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractComment extends GenericItem
{
    public static final String SUBJECT = "subject";
    public static final String TEXT = "text";
    public static final String ATTACHMENTS = "attachments";
    public static final String AUTHOR = "author";
    public static final String COMMENTUSERSETTINGS = "commentUserSettings";
    protected static final OneToManyHandler<CommentAttachment> ATTACHMENTSHANDLER = new OneToManyHandler(GeneratedCommentsConstants.TC.COMMENTATTACHMENT, true, "abstractComment", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractComment> AUTHORHANDLER = new BidirectionalOneToManyHandler(GeneratedCommentsConstants.TC.ABSTRACTCOMMENT, false, "author", null, false, true, 2);
    protected static final OneToManyHandler<CommentUserSetting> COMMENTUSERSETTINGSHANDLER = new OneToManyHandler(GeneratedCommentsConstants.TC.COMMENTUSERSETTING, true, "comment", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("subject", Item.AttributeMode.INITIAL);
        tmp.put("text", Item.AttributeMode.INITIAL);
        tmp.put("author", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<CommentAttachment> getAttachments(SessionContext ctx)
    {
        return ATTACHMENTSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<CommentAttachment> getAttachments()
    {
        return getAttachments(getSession().getSessionContext());
    }


    public void setAttachments(SessionContext ctx, Collection<CommentAttachment> value)
    {
        ATTACHMENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setAttachments(Collection<CommentAttachment> value)
    {
        setAttachments(getSession().getSessionContext(), value);
    }


    public void addToAttachments(SessionContext ctx, CommentAttachment value)
    {
        ATTACHMENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToAttachments(CommentAttachment value)
    {
        addToAttachments(getSession().getSessionContext(), value);
    }


    public void removeFromAttachments(SessionContext ctx, CommentAttachment value)
    {
        ATTACHMENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromAttachments(CommentAttachment value)
    {
        removeFromAttachments(getSession().getSessionContext(), value);
    }


    public User getAuthor(SessionContext ctx)
    {
        return (User)getProperty(ctx, "author");
    }


    public User getAuthor()
    {
        return getAuthor(getSession().getSessionContext());
    }


    protected void setAuthor(SessionContext ctx, User value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'author' is not changeable", 0);
        }
        AUTHORHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setAuthor(User value)
    {
        setAuthor(getSession().getSessionContext(), value);
    }


    Collection<CommentUserSetting> getCommentUserSettings(SessionContext ctx)
    {
        return COMMENTUSERSETTINGSHANDLER.getValues(ctx, (Item)this);
    }


    Collection<CommentUserSetting> getCommentUserSettings()
    {
        return getCommentUserSettings(getSession().getSessionContext());
    }


    void setCommentUserSettings(SessionContext ctx, Collection<CommentUserSetting> value)
    {
        COMMENTUSERSETTINGSHANDLER.setValues(ctx, (Item)this, value);
    }


    void setCommentUserSettings(Collection<CommentUserSetting> value)
    {
        setCommentUserSettings(getSession().getSessionContext(), value);
    }


    void addToCommentUserSettings(SessionContext ctx, CommentUserSetting value)
    {
        COMMENTUSERSETTINGSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    void addToCommentUserSettings(CommentUserSetting value)
    {
        addToCommentUserSettings(getSession().getSessionContext(), value);
    }


    void removeFromCommentUserSettings(SessionContext ctx, CommentUserSetting value)
    {
        COMMENTUSERSETTINGSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    void removeFromCommentUserSettings(CommentUserSetting value)
    {
        removeFromCommentUserSettings(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        AUTHORHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getSubject(SessionContext ctx)
    {
        return (String)getProperty(ctx, "subject");
    }


    public String getSubject()
    {
        return getSubject(getSession().getSessionContext());
    }


    public void setSubject(SessionContext ctx, String value)
    {
        setProperty(ctx, "subject", value);
    }


    public void setSubject(String value)
    {
        setSubject(getSession().getSessionContext(), value);
    }


    public String getText(SessionContext ctx)
    {
        return (String)getProperty(ctx, "text");
    }


    public String getText()
    {
        return getText(getSession().getSessionContext());
    }


    public void setText(SessionContext ctx, String value)
    {
        setProperty(ctx, "text", value);
    }


    public void setText(String value)
    {
        setText(getSession().getSessionContext(), value);
    }
}
