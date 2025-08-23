package de.hybris.platform.comments.jalo;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedComponent extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String AVAILABLECOMMENTTYPES = "availableCommentTypes";
    public static final String DOMAIN = "domain";
    public static final String COMMENT = "comment";
    public static final String READPERMITTED = "readPermitted";
    protected static String COMPONENTREADPRINCIPALRELATION_SRC_ORDERED = "relation.ComponentReadPrincipalRelation.source.ordered";
    protected static String COMPONENTREADPRINCIPALRELATION_TGT_ORDERED = "relation.ComponentReadPrincipalRelation.target.ordered";
    protected static String COMPONENTREADPRINCIPALRELATION_MARKMODIFIED = "relation.ComponentReadPrincipalRelation.markmodified";
    public static final String WRITEPERMITTED = "writePermitted";
    protected static String COMPONENTWRITEPRINCIPALRELATION_SRC_ORDERED = "relation.ComponentWritePrincipalRelation.source.ordered";
    protected static String COMPONENTWRITEPRINCIPALRELATION_TGT_ORDERED = "relation.ComponentWritePrincipalRelation.target.ordered";
    protected static String COMPONENTWRITEPRINCIPALRELATION_MARKMODIFIED = "relation.ComponentWritePrincipalRelation.markmodified";
    public static final String CREATEPERMITTED = "createPermitted";
    protected static String COMPONENTCREATEPRINCIPALRELATION_SRC_ORDERED = "relation.ComponentCreatePrincipalRelation.source.ordered";
    protected static String COMPONENTCREATEPRINCIPALRELATION_TGT_ORDERED = "relation.ComponentCreatePrincipalRelation.target.ordered";
    protected static String COMPONENTCREATEPRINCIPALRELATION_MARKMODIFIED = "relation.ComponentCreatePrincipalRelation.markmodified";
    public static final String REMOVEPERMITTED = "removePermitted";
    protected static String COMPONENTREMOVEPRINCIPALRELATION_SRC_ORDERED = "relation.ComponentRemovePrincipalRelation.source.ordered";
    protected static String COMPONENTREMOVEPRINCIPALRELATION_TGT_ORDERED = "relation.ComponentRemovePrincipalRelation.target.ordered";
    protected static String COMPONENTREMOVEPRINCIPALRELATION_MARKMODIFIED = "relation.ComponentRemovePrincipalRelation.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedComponent> DOMAINHANDLER = new BidirectionalOneToManyHandler(GeneratedCommentsConstants.TC.COMPONENT, false, "domain", null, false, true, 0);
    protected static final OneToManyHandler<Comment> COMMENTHANDLER = new OneToManyHandler(GeneratedCommentsConstants.TC.COMMENT, false, "component", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("domain", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<CommentType> getAvailableCommentTypes()
    {
        return getAvailableCommentTypes(getSession().getSessionContext());
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    Collection<Comment> getComment(SessionContext ctx)
    {
        return COMMENTHANDLER.getValues(ctx, (Item)this);
    }


    Collection<Comment> getComment()
    {
        return getComment(getSession().getSessionContext());
    }


    void setComment(SessionContext ctx, Collection<Comment> value)
    {
        COMMENTHANDLER.setValues(ctx, (Item)this, value);
    }


    void setComment(Collection<Comment> value)
    {
        setComment(getSession().getSessionContext(), value);
    }


    void addToComment(SessionContext ctx, Comment value)
    {
        COMMENTHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    void addToComment(Comment value)
    {
        addToComment(getSession().getSessionContext(), value);
    }


    void removeFromComment(SessionContext ctx, Comment value)
    {
        COMMENTHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    void removeFromComment(Comment value)
    {
        removeFromComment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        DOMAINHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Collection<Principal> getCreatePermitted(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.getCreatePermitted requires a session language", 0);
        }
        List<Principal> items = getLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTCREATEPRINCIPALRELATION, "Principal", ctx
                        .getLanguage(), false, false);
        return items;
    }


    public Collection<Principal> getCreatePermitted()
    {
        return getCreatePermitted(getSession().getSessionContext());
    }


    public Map<Language, Collection<Principal>> getAllCreatePermitted(SessionContext ctx)
    {
        Map<Language, Collection<Principal>> values = getAllLinkedItems(true, GeneratedCommentsConstants.Relations.COMPONENTCREATEPRINCIPALRELATION);
        return values;
    }


    public Map<Language, Collection<Principal>> getAllCreatePermitted()
    {
        return getAllCreatePermitted(getSession().getSessionContext());
    }


    public long getCreatePermittedCount(SessionContext ctx, Language lang)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTCREATEPRINCIPALRELATION, "Principal", lang);
    }


    public long getCreatePermittedCount(Language lang)
    {
        return getCreatePermittedCount(getSession().getSessionContext(), lang);
    }


    public long getCreatePermittedCount(SessionContext ctx)
    {
        return getCreatePermittedCount(ctx, ctx.getLanguage());
    }


    public long getCreatePermittedCount()
    {
        return getCreatePermittedCount(getSession().getSessionContext(), getSession().getSessionContext().getLanguage());
    }


    public void setCreatePermitted(SessionContext ctx, Collection<Principal> value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.setCreatePermitted requires a session language", 0);
        }
        setLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTCREATEPRINCIPALRELATION, ctx
                                        .getLanguage(), value, false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTCREATEPRINCIPALRELATION_MARKMODIFIED));
    }


    public void setCreatePermitted(Collection<Principal> value)
    {
        setCreatePermitted(getSession().getSessionContext(), value);
    }


    public void setAllCreatePermitted(SessionContext ctx, Map<Language, Collection<Principal>> value)
    {
        setAllLinkedItems(
                        getAllValuesSessionContext(ctx), true, GeneratedCommentsConstants.Relations.COMPONENTCREATEPRINCIPALRELATION, value);
    }


    public void setAllCreatePermitted(Map<Language, Collection<Principal>> value)
    {
        setAllCreatePermitted(getSession().getSessionContext(), value);
    }


    public void addToCreatePermitted(SessionContext ctx, Language lang, Principal value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.addToCreatePermitted requires a language language", 0);
        }
        addLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTCREATEPRINCIPALRELATION, lang,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTCREATEPRINCIPALRELATION_MARKMODIFIED));
    }


    public void addToCreatePermitted(Language lang, Principal value)
    {
        addToCreatePermitted(getSession().getSessionContext(), lang, value);
    }


    public void removeFromCreatePermitted(SessionContext ctx, Language lang, Principal value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.removeFromCreatePermitted requires a session language", 0);
        }
        removeLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTCREATEPRINCIPALRELATION, lang,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTCREATEPRINCIPALRELATION_MARKMODIFIED));
    }


    public void removeFromCreatePermitted(Language lang, Principal value)
    {
        removeFromCreatePermitted(getSession().getSessionContext(), lang, value);
    }


    public Domain getDomain(SessionContext ctx)
    {
        return (Domain)getProperty(ctx, "domain");
    }


    public Domain getDomain()
    {
        return getDomain(getSession().getSessionContext());
    }


    protected void setDomain(SessionContext ctx, Domain value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'domain' is not changeable", 0);
        }
        DOMAINHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setDomain(Domain value)
    {
        setDomain(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(COMPONENTREADPRINCIPALRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(COMPONENTWRITEPRINCIPALRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd2 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd2.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(COMPONENTCREATEPRINCIPALRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd3 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd3.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(COMPONENTREMOVEPRINCIPALRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public Collection<Principal> getReadPermitted(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.getReadPermitted requires a session language", 0);
        }
        List<Principal> items = getLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTREADPRINCIPALRELATION, "Principal", ctx
                        .getLanguage(), false, false);
        return items;
    }


    public Collection<Principal> getReadPermitted()
    {
        return getReadPermitted(getSession().getSessionContext());
    }


    public Map<Language, Collection<Principal>> getAllReadPermitted(SessionContext ctx)
    {
        Map<Language, Collection<Principal>> values = getAllLinkedItems(true, GeneratedCommentsConstants.Relations.COMPONENTREADPRINCIPALRELATION);
        return values;
    }


    public Map<Language, Collection<Principal>> getAllReadPermitted()
    {
        return getAllReadPermitted(getSession().getSessionContext());
    }


    public long getReadPermittedCount(SessionContext ctx, Language lang)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTREADPRINCIPALRELATION, "Principal", lang);
    }


    public long getReadPermittedCount(Language lang)
    {
        return getReadPermittedCount(getSession().getSessionContext(), lang);
    }


    public long getReadPermittedCount(SessionContext ctx)
    {
        return getReadPermittedCount(ctx, ctx.getLanguage());
    }


    public long getReadPermittedCount()
    {
        return getReadPermittedCount(getSession().getSessionContext(), getSession().getSessionContext().getLanguage());
    }


    public void setReadPermitted(SessionContext ctx, Collection<Principal> value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.setReadPermitted requires a session language", 0);
        }
        setLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTREADPRINCIPALRELATION, ctx
                                        .getLanguage(), value, false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTREADPRINCIPALRELATION_MARKMODIFIED));
    }


    public void setReadPermitted(Collection<Principal> value)
    {
        setReadPermitted(getSession().getSessionContext(), value);
    }


    public void setAllReadPermitted(SessionContext ctx, Map<Language, Collection<Principal>> value)
    {
        setAllLinkedItems(
                        getAllValuesSessionContext(ctx), true, GeneratedCommentsConstants.Relations.COMPONENTREADPRINCIPALRELATION, value);
    }


    public void setAllReadPermitted(Map<Language, Collection<Principal>> value)
    {
        setAllReadPermitted(getSession().getSessionContext(), value);
    }


    public void addToReadPermitted(SessionContext ctx, Language lang, Principal value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.addToReadPermitted requires a language language", 0);
        }
        addLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTREADPRINCIPALRELATION, lang,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTREADPRINCIPALRELATION_MARKMODIFIED));
    }


    public void addToReadPermitted(Language lang, Principal value)
    {
        addToReadPermitted(getSession().getSessionContext(), lang, value);
    }


    public void removeFromReadPermitted(SessionContext ctx, Language lang, Principal value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.removeFromReadPermitted requires a session language", 0);
        }
        removeLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTREADPRINCIPALRELATION, lang,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTREADPRINCIPALRELATION_MARKMODIFIED));
    }


    public void removeFromReadPermitted(Language lang, Principal value)
    {
        removeFromReadPermitted(getSession().getSessionContext(), lang, value);
    }


    public Collection<Principal> getRemovePermitted(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.getRemovePermitted requires a session language", 0);
        }
        List<Principal> items = getLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTREMOVEPRINCIPALRELATION, "Principal", ctx
                        .getLanguage(), false, false);
        return items;
    }


    public Collection<Principal> getRemovePermitted()
    {
        return getRemovePermitted(getSession().getSessionContext());
    }


    public Map<Language, Collection<Principal>> getAllRemovePermitted(SessionContext ctx)
    {
        Map<Language, Collection<Principal>> values = getAllLinkedItems(true, GeneratedCommentsConstants.Relations.COMPONENTREMOVEPRINCIPALRELATION);
        return values;
    }


    public Map<Language, Collection<Principal>> getAllRemovePermitted()
    {
        return getAllRemovePermitted(getSession().getSessionContext());
    }


    public long getRemovePermittedCount(SessionContext ctx, Language lang)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTREMOVEPRINCIPALRELATION, "Principal", lang);
    }


    public long getRemovePermittedCount(Language lang)
    {
        return getRemovePermittedCount(getSession().getSessionContext(), lang);
    }


    public long getRemovePermittedCount(SessionContext ctx)
    {
        return getRemovePermittedCount(ctx, ctx.getLanguage());
    }


    public long getRemovePermittedCount()
    {
        return getRemovePermittedCount(getSession().getSessionContext(), getSession().getSessionContext().getLanguage());
    }


    public void setRemovePermitted(SessionContext ctx, Collection<Principal> value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.setRemovePermitted requires a session language", 0);
        }
        setLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTREMOVEPRINCIPALRELATION, ctx
                                        .getLanguage(), value, false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTREMOVEPRINCIPALRELATION_MARKMODIFIED));
    }


    public void setRemovePermitted(Collection<Principal> value)
    {
        setRemovePermitted(getSession().getSessionContext(), value);
    }


    public void setAllRemovePermitted(SessionContext ctx, Map<Language, Collection<Principal>> value)
    {
        setAllLinkedItems(
                        getAllValuesSessionContext(ctx), true, GeneratedCommentsConstants.Relations.COMPONENTREMOVEPRINCIPALRELATION, value);
    }


    public void setAllRemovePermitted(Map<Language, Collection<Principal>> value)
    {
        setAllRemovePermitted(getSession().getSessionContext(), value);
    }


    public void addToRemovePermitted(SessionContext ctx, Language lang, Principal value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.addToRemovePermitted requires a language language", 0);
        }
        addLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTREMOVEPRINCIPALRELATION, lang,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTREMOVEPRINCIPALRELATION_MARKMODIFIED));
    }


    public void addToRemovePermitted(Language lang, Principal value)
    {
        addToRemovePermitted(getSession().getSessionContext(), lang, value);
    }


    public void removeFromRemovePermitted(SessionContext ctx, Language lang, Principal value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.removeFromRemovePermitted requires a session language", 0);
        }
        removeLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTREMOVEPRINCIPALRELATION, lang,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTREMOVEPRINCIPALRELATION_MARKMODIFIED));
    }


    public void removeFromRemovePermitted(Language lang, Principal value)
    {
        removeFromRemovePermitted(getSession().getSessionContext(), lang, value);
    }


    public Collection<Principal> getWritePermitted(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.getWritePermitted requires a session language", 0);
        }
        List<Principal> items = getLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTWRITEPRINCIPALRELATION, "Principal", ctx
                        .getLanguage(), false, false);
        return items;
    }


    public Collection<Principal> getWritePermitted()
    {
        return getWritePermitted(getSession().getSessionContext());
    }


    public Map<Language, Collection<Principal>> getAllWritePermitted(SessionContext ctx)
    {
        Map<Language, Collection<Principal>> values = getAllLinkedItems(true, GeneratedCommentsConstants.Relations.COMPONENTWRITEPRINCIPALRELATION);
        return values;
    }


    public Map<Language, Collection<Principal>> getAllWritePermitted()
    {
        return getAllWritePermitted(getSession().getSessionContext());
    }


    public long getWritePermittedCount(SessionContext ctx, Language lang)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTWRITEPRINCIPALRELATION, "Principal", lang);
    }


    public long getWritePermittedCount(Language lang)
    {
        return getWritePermittedCount(getSession().getSessionContext(), lang);
    }


    public long getWritePermittedCount(SessionContext ctx)
    {
        return getWritePermittedCount(ctx, ctx.getLanguage());
    }


    public long getWritePermittedCount()
    {
        return getWritePermittedCount(getSession().getSessionContext(), getSession().getSessionContext().getLanguage());
    }


    public void setWritePermitted(SessionContext ctx, Collection<Principal> value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.setWritePermitted requires a session language", 0);
        }
        setLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTWRITEPRINCIPALRELATION, ctx
                                        .getLanguage(), value, false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTWRITEPRINCIPALRELATION_MARKMODIFIED));
    }


    public void setWritePermitted(Collection<Principal> value)
    {
        setWritePermitted(getSession().getSessionContext(), value);
    }


    public void setAllWritePermitted(SessionContext ctx, Map<Language, Collection<Principal>> value)
    {
        setAllLinkedItems(
                        getAllValuesSessionContext(ctx), true, GeneratedCommentsConstants.Relations.COMPONENTWRITEPRINCIPALRELATION, value);
    }


    public void setAllWritePermitted(Map<Language, Collection<Principal>> value)
    {
        setAllWritePermitted(getSession().getSessionContext(), value);
    }


    public void addToWritePermitted(SessionContext ctx, Language lang, Principal value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.addToWritePermitted requires a language language", 0);
        }
        addLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTWRITEPRINCIPALRELATION, lang,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTWRITEPRINCIPALRELATION_MARKMODIFIED));
    }


    public void addToWritePermitted(Language lang, Principal value)
    {
        addToWritePermitted(getSession().getSessionContext(), lang, value);
    }


    public void removeFromWritePermitted(SessionContext ctx, Language lang, Principal value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponent.removeFromWritePermitted requires a session language", 0);
        }
        removeLinkedItems(ctx, true, GeneratedCommentsConstants.Relations.COMPONENTWRITEPRINCIPALRELATION, lang,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTWRITEPRINCIPALRELATION_MARKMODIFIED));
    }


    public void removeFromWritePermitted(Language lang, Principal value)
    {
        removeFromWritePermitted(getSession().getSessionContext(), lang, value);
    }


    public abstract Collection<CommentType> getAvailableCommentTypes(SessionContext paramSessionContext);
}
