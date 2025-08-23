package de.hybris.platform.comments.jalo;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedComment extends AbstractComment
{
    public static final String CODE = "code";
    public static final String PRIORITY = "priority";
    public static final String RELATEDITEMS = "relatedItems";
    public static final String REPLIES = "replies";
    public static final String ASSIGNEDTO = "assignedTo";
    protected static String COMMENTASSIGNEERELATION_SRC_ORDERED = "relation.CommentAssigneeRelation.source.ordered";
    protected static String COMMENTASSIGNEERELATION_TGT_ORDERED = "relation.CommentAssigneeRelation.target.ordered";
    protected static String COMMENTASSIGNEERELATION_MARKMODIFIED = "relation.CommentAssigneeRelation.markmodified";
    public static final String WATCHERS = "watchers";
    protected static String COMMENTWATCHERRELATION_SRC_ORDERED = "relation.CommentWatcherRelation.source.ordered";
    protected static String COMMENTWATCHERRELATION_TGT_ORDERED = "relation.CommentWatcherRelation.target.ordered";
    protected static String COMMENTWATCHERRELATION_MARKMODIFIED = "relation.CommentWatcherRelation.markmodified";
    public static final String COMPONENT = "component";
    public static final String COMMENTTYPE = "commentType";
    protected static final OneToManyHandler<Reply> REPLIESHANDLER = new OneToManyHandler(GeneratedCommentsConstants.TC.REPLY, true, "comment", "commentPOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedComment> COMPONENTHANDLER = new BidirectionalOneToManyHandler(GeneratedCommentsConstants.TC.COMMENT, false, "component", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedComment> COMMENTTYPEHANDLER = new BidirectionalOneToManyHandler(GeneratedCommentsConstants.TC.COMMENT, false, "commentType", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractComment.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("component", Item.AttributeMode.INITIAL);
        tmp.put("commentType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<User> getAssignedTo(SessionContext ctx)
    {
        List<User> items = getLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMMENTASSIGNEERELATION, "User", null, false,
                        Utilities.getRelationOrderingOverride(COMMENTASSIGNEERELATION_TGT_ORDERED, true));
        return items;
    }


    public Collection<User> getAssignedTo()
    {
        return getAssignedTo(getSession().getSessionContext());
    }


    public long getAssignedToCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCommentsConstants.Relations.COMMENTASSIGNEERELATION, "User", null);
    }


    public long getAssignedToCount()
    {
        return getAssignedToCount(getSession().getSessionContext());
    }


    public void setAssignedTo(SessionContext ctx, Collection<User> value)
    {
        setLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMMENTASSIGNEERELATION, null, value, false,
                        Utilities.getRelationOrderingOverride(COMMENTASSIGNEERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTASSIGNEERELATION_MARKMODIFIED));
    }


    public void setAssignedTo(Collection<User> value)
    {
        setAssignedTo(getSession().getSessionContext(), value);
    }


    public void addToAssignedTo(SessionContext ctx, User value)
    {
        addLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMMENTASSIGNEERELATION, null,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(COMMENTASSIGNEERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTASSIGNEERELATION_MARKMODIFIED));
    }


    public void addToAssignedTo(User value)
    {
        addToAssignedTo(getSession().getSessionContext(), value);
    }


    public void removeFromAssignedTo(SessionContext ctx, User value)
    {
        removeLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMMENTASSIGNEERELATION, null,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(COMMENTASSIGNEERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTASSIGNEERELATION_MARKMODIFIED));
    }


    public void removeFromAssignedTo(User value)
    {
        removeFromAssignedTo(getSession().getSessionContext(), value);
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


    public CommentType getCommentType(SessionContext ctx)
    {
        return (CommentType)getProperty(ctx, "commentType");
    }


    public CommentType getCommentType()
    {
        return getCommentType(getSession().getSessionContext());
    }


    protected void setCommentType(SessionContext ctx, CommentType value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'commentType' is not changeable", 0);
        }
        COMMENTTYPEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setCommentType(CommentType value)
    {
        setCommentType(getSession().getSessionContext(), value);
    }


    public Component getComponent(SessionContext ctx)
    {
        return (Component)getProperty(ctx, "component");
    }


    public Component getComponent()
    {
        return getComponent(getSession().getSessionContext());
    }


    protected void setComponent(SessionContext ctx, Component value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'component' is not changeable", 0);
        }
        COMPONENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setComponent(Component value)
    {
        setComponent(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        COMPONENTHANDLER.newInstance(ctx, allAttributes);
        COMMENTTYPEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("User");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(COMMENTASSIGNEERELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(COMMENTWATCHERRELATION_MARKMODIFIED);
        }
        return true;
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


    public Collection<Item> getRelatedItems()
    {
        return getRelatedItems(getSession().getSessionContext());
    }


    public void setRelatedItems(Collection<Item> value)
    {
        setRelatedItems(getSession().getSessionContext(), value);
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


    public Collection<Principal> getWatchers(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMMENTWATCHERRELATION, "Principal", null, false,
                        Utilities.getRelationOrderingOverride(COMMENTWATCHERRELATION_TGT_ORDERED, true));
        return items;
    }


    public Collection<Principal> getWatchers()
    {
        return getWatchers(getSession().getSessionContext());
    }


    public long getWatchersCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCommentsConstants.Relations.COMMENTWATCHERRELATION, "Principal", null);
    }


    public long getWatchersCount()
    {
        return getWatchersCount(getSession().getSessionContext());
    }


    public void setWatchers(SessionContext ctx, Collection<Principal> value)
    {
        setLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMMENTWATCHERRELATION, null, value, false,
                        Utilities.getRelationOrderingOverride(COMMENTWATCHERRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTWATCHERRELATION_MARKMODIFIED));
    }


    public void setWatchers(Collection<Principal> value)
    {
        setWatchers(getSession().getSessionContext(), value);
    }


    public void addToWatchers(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMMENTWATCHERRELATION, null,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(COMMENTWATCHERRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTWATCHERRELATION_MARKMODIFIED));
    }


    public void addToWatchers(Principal value)
    {
        addToWatchers(getSession().getSessionContext(), value);
    }


    public void removeFromWatchers(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMMENTWATCHERRELATION, null,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(COMMENTWATCHERRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(COMMENTWATCHERRELATION_MARKMODIFIED));
    }


    public void removeFromWatchers(Principal value)
    {
        removeFromWatchers(getSession().getSessionContext(), value);
    }


    public abstract Collection<Item> getRelatedItems(SessionContext paramSessionContext);


    public abstract void setRelatedItems(SessionContext paramSessionContext, Collection<Item> paramCollection);
}
