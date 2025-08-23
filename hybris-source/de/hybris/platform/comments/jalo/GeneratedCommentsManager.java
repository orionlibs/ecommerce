package de.hybris.platform.comments.jalo;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCommentsManager extends Extension
{
    protected static String COMMENTWATCHERRELATION_SRC_ORDERED = "relation.CommentWatcherRelation.source.ordered";
    protected static String COMMENTWATCHERRELATION_TGT_ORDERED = "relation.CommentWatcherRelation.target.ordered";
    protected static String COMMENTWATCHERRELATION_MARKMODIFIED = "relation.CommentWatcherRelation.markmodified";
    protected static String COMMENTITEMRELATION_SRC_ORDERED = "relation.CommentItemRelation.source.ordered";
    protected static String COMMENTITEMRELATION_TGT_ORDERED = "relation.CommentItemRelation.target.ordered";
    protected static String COMMENTITEMRELATION_MARKMODIFIED = "relation.CommentItemRelation.markmodified";
    protected static String COMMENTASSIGNEERELATION_SRC_ORDERED = "relation.CommentAssigneeRelation.source.ordered";
    protected static String COMMENTASSIGNEERELATION_TGT_ORDERED = "relation.CommentAssigneeRelation.target.ordered";
    protected static String COMMENTASSIGNEERELATION_MARKMODIFIED = "relation.CommentAssigneeRelation.markmodified";
    protected static final OneToManyHandler<AbstractComment> ABSTRACTCOMMENTAUTHORRELATIONCREATEDCOMMENTSHANDLER = new OneToManyHandler(GeneratedCommentsConstants.TC.ABSTRACTCOMMENT, true, "author", null, false, true, 2);
    protected static final OneToManyHandler<CommentUserSetting> COMMENTUSERSETTINGUSERRELATIONCOMMENTUSERSETTINGSHANDLER = new OneToManyHandler(GeneratedCommentsConstants.TC.COMMENTUSERSETTING, false, "user", null, false, true, 0);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("profilePicture", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.security.Principal", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public List<Comment> getAssignedComments(SessionContext ctx, User item)
    {
        List<Comment> items = item.getLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTASSIGNEERELATION, "Comment", null, false,
                        Utilities.getRelationOrderingOverride(COMMENTASSIGNEERELATION_TGT_ORDERED, true));
        return items;
    }


    public List<Comment> getAssignedComments(User item)
    {
        return getAssignedComments(getSession().getSessionContext(), item);
    }


    public long getAssignedCommentsCount(SessionContext ctx, User item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCommentsConstants.Relations.COMMENTASSIGNEERELATION, "Comment", null);
    }


    public long getAssignedCommentsCount(User item)
    {
        return getAssignedCommentsCount(getSession().getSessionContext(), item);
    }


    public void setAssignedComments(SessionContext ctx, User item, List<Comment> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTASSIGNEERELATION, null, value, false,
                        Utilities.getRelationOrderingOverride(COMMENTASSIGNEERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTASSIGNEERELATION_MARKMODIFIED));
    }


    public void setAssignedComments(User item, List<Comment> value)
    {
        setAssignedComments(getSession().getSessionContext(), item, value);
    }


    public void addToAssignedComments(SessionContext ctx, User item, Comment value)
    {
        item.addLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTASSIGNEERELATION, null,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(COMMENTASSIGNEERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTASSIGNEERELATION_MARKMODIFIED));
    }


    public void addToAssignedComments(User item, Comment value)
    {
        addToAssignedComments(getSession().getSessionContext(), item, value);
    }


    public void removeFromAssignedComments(SessionContext ctx, User item, Comment value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTASSIGNEERELATION, null,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(COMMENTASSIGNEERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTASSIGNEERELATION_MARKMODIFIED));
    }


    public void removeFromAssignedComments(User item, Comment value)
    {
        removeFromAssignedComments(getSession().getSessionContext(), item, value);
    }


    public List<Comment> getComments(SessionContext ctx, Item item)
    {
        List<Comment> items = item.getLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTITEMRELATION, "Comment", null, false,
                        Utilities.getRelationOrderingOverride(COMMENTITEMRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<Comment> getComments(Item item)
    {
        return getComments(getSession().getSessionContext(), item);
    }


    public long getCommentsCount(SessionContext ctx, Item item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCommentsConstants.Relations.COMMENTITEMRELATION, "Comment", null);
    }


    public long getCommentsCount(Item item)
    {
        return getCommentsCount(getSession().getSessionContext(), item);
    }


    public void setComments(SessionContext ctx, Item item, List<Comment> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTITEMRELATION, null, value, false,
                        Utilities.getRelationOrderingOverride(COMMENTITEMRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTITEMRELATION_MARKMODIFIED));
    }


    public void setComments(Item item, List<Comment> value)
    {
        setComments(getSession().getSessionContext(), item, value);
    }


    public void addToComments(SessionContext ctx, Item item, Comment value)
    {
        item.addLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTITEMRELATION, null,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(COMMENTITEMRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTITEMRELATION_MARKMODIFIED));
    }


    public void addToComments(Item item, Comment value)
    {
        addToComments(getSession().getSessionContext(), item, value);
    }


    public void removeFromComments(SessionContext ctx, Item item, Comment value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTITEMRELATION, null,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(COMMENTITEMRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTITEMRELATION_MARKMODIFIED));
    }


    public void removeFromComments(Item item, Comment value)
    {
        removeFromComments(getSession().getSessionContext(), item, value);
    }


    Collection<CommentUserSetting> getCommentUserSettings(SessionContext ctx, User item)
    {
        return COMMENTUSERSETTINGUSERRELATIONCOMMENTUSERSETTINGSHANDLER.getValues(ctx, (Item)item);
    }


    Collection<CommentUserSetting> getCommentUserSettings(User item)
    {
        return getCommentUserSettings(getSession().getSessionContext(), item);
    }


    void setCommentUserSettings(SessionContext ctx, User item, Collection<CommentUserSetting> value)
    {
        COMMENTUSERSETTINGUSERRELATIONCOMMENTUSERSETTINGSHANDLER.setValues(ctx, (Item)item, value);
    }


    void setCommentUserSettings(User item, Collection<CommentUserSetting> value)
    {
        setCommentUserSettings(getSession().getSessionContext(), item, value);
    }


    void addToCommentUserSettings(SessionContext ctx, User item, CommentUserSetting value)
    {
        COMMENTUSERSETTINGUSERRELATIONCOMMENTUSERSETTINGSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    void addToCommentUserSettings(User item, CommentUserSetting value)
    {
        addToCommentUserSettings(getSession().getSessionContext(), item, value);
    }


    void removeFromCommentUserSettings(SessionContext ctx, User item, CommentUserSetting value)
    {
        COMMENTUSERSETTINGUSERRELATIONCOMMENTUSERSETTINGSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    void removeFromCommentUserSettings(User item, CommentUserSetting value)
    {
        removeFromCommentUserSettings(getSession().getSessionContext(), item, value);
    }


    public Comment createComment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommentsConstants.TC.COMMENT);
            return (Comment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Comment : " + e.getMessage(), 0);
        }
    }


    public Comment createComment(Map attributeValues)
    {
        return createComment(getSession().getSessionContext(), attributeValues);
    }


    public CommentAttachment createCommentAttachment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommentsConstants.TC.COMMENTATTACHMENT);
            return (CommentAttachment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CommentAttachment : " + e.getMessage(), 0);
        }
    }


    public CommentAttachment createCommentAttachment(Map attributeValues)
    {
        return createCommentAttachment(getSession().getSessionContext(), attributeValues);
    }


    public CommentType createCommentType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommentsConstants.TC.COMMENTTYPE);
            return (CommentType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CommentType : " + e.getMessage(), 0);
        }
    }


    public CommentType createCommentType(Map attributeValues)
    {
        return createCommentType(getSession().getSessionContext(), attributeValues);
    }


    public CommentUserSetting createCommentUserSetting(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommentsConstants.TC.COMMENTUSERSETTING);
            return (CommentUserSetting)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CommentUserSetting : " + e.getMessage(), 0);
        }
    }


    public CommentUserSetting createCommentUserSetting(Map attributeValues)
    {
        return createCommentUserSetting(getSession().getSessionContext(), attributeValues);
    }


    public Component createComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommentsConstants.TC.COMPONENT);
            return (Component)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Component : " + e.getMessage(), 0);
        }
    }


    public Component createComponent(Map attributeValues)
    {
        return createComponent(getSession().getSessionContext(), attributeValues);
    }


    public List<AbstractComment> getCreatedComments(SessionContext ctx, User item)
    {
        return (List<AbstractComment>)ABSTRACTCOMMENTAUTHORRELATIONCREATEDCOMMENTSHANDLER.getValues(ctx, (Item)item);
    }


    public List<AbstractComment> getCreatedComments(User item)
    {
        return getCreatedComments(getSession().getSessionContext(), item);
    }


    public void setCreatedComments(SessionContext ctx, User item, List<AbstractComment> value)
    {
        ABSTRACTCOMMENTAUTHORRELATIONCREATEDCOMMENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCreatedComments(User item, List<AbstractComment> value)
    {
        setCreatedComments(getSession().getSessionContext(), item, value);
    }


    public void addToCreatedComments(SessionContext ctx, User item, AbstractComment value)
    {
        ABSTRACTCOMMENTAUTHORRELATIONCREATEDCOMMENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCreatedComments(User item, AbstractComment value)
    {
        addToCreatedComments(getSession().getSessionContext(), item, value);
    }


    public void removeFromCreatedComments(SessionContext ctx, User item, AbstractComment value)
    {
        ABSTRACTCOMMENTAUTHORRELATIONCREATEDCOMMENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCreatedComments(User item, AbstractComment value)
    {
        removeFromCreatedComments(getSession().getSessionContext(), item, value);
    }


    public Domain createDomain(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommentsConstants.TC.DOMAIN);
            return (Domain)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Domain : " + e.getMessage(), 0);
        }
    }


    public Domain createDomain(Map attributeValues)
    {
        return createDomain(getSession().getSessionContext(), attributeValues);
    }


    public Reply createReply(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommentsConstants.TC.REPLY);
            return (Reply)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Reply : " + e.getMessage(), 0);
        }
    }


    public Reply createReply(Map attributeValues)
    {
        return createReply(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "comments";
    }


    public Media getProfilePicture(SessionContext ctx, Principal item)
    {
        return (Media)item.getProperty(ctx, GeneratedCommentsConstants.Attributes.Principal.PROFILEPICTURE);
    }


    public Media getProfilePicture(Principal item)
    {
        return getProfilePicture(getSession().getSessionContext(), item);
    }


    public void setProfilePicture(SessionContext ctx, Principal item, Media value)
    {
        item.setProperty(ctx, GeneratedCommentsConstants.Attributes.Principal.PROFILEPICTURE, value);
    }


    public void setProfilePicture(Principal item, Media value)
    {
        setProfilePicture(getSession().getSessionContext(), item, value);
    }


    public List<Comment> getWatchedComments(SessionContext ctx, Principal item)
    {
        List<Comment> items = item.getLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTWATCHERRELATION, "Comment", null, false,
                        Utilities.getRelationOrderingOverride(COMMENTWATCHERRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<Comment> getWatchedComments(Principal item)
    {
        return getWatchedComments(getSession().getSessionContext(), item);
    }


    public long getWatchedCommentsCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCommentsConstants.Relations.COMMENTWATCHERRELATION, "Comment", null);
    }


    public long getWatchedCommentsCount(Principal item)
    {
        return getWatchedCommentsCount(getSession().getSessionContext(), item);
    }


    public void setWatchedComments(SessionContext ctx, Principal item, List<Comment> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTWATCHERRELATION, null, value, false,
                        Utilities.getRelationOrderingOverride(COMMENTWATCHERRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTWATCHERRELATION_MARKMODIFIED));
    }


    public void setWatchedComments(Principal item, List<Comment> value)
    {
        setWatchedComments(getSession().getSessionContext(), item, value);
    }


    public void addToWatchedComments(SessionContext ctx, Principal item, Comment value)
    {
        item.addLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTWATCHERRELATION, null,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(COMMENTWATCHERRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTWATCHERRELATION_MARKMODIFIED));
    }


    public void addToWatchedComments(Principal item, Comment value)
    {
        addToWatchedComments(getSession().getSessionContext(), item, value);
    }


    public void removeFromWatchedComments(SessionContext ctx, Principal item, Comment value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCommentsConstants.Relations.COMMENTWATCHERRELATION, null,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(COMMENTWATCHERRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTWATCHERRELATION_MARKMODIFIED));
    }


    public void removeFromWatchedComments(Principal item, Comment value)
    {
        removeFromWatchedComments(getSession().getSessionContext(), item, value);
    }
}
