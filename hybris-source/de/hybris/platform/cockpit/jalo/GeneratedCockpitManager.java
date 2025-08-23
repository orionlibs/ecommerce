package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.jalo.template.CockpitItemTemplate;
import de.hybris.platform.cockpit.reports.jalo.CompiledJasperMedia;
import de.hybris.platform.cockpit.reports.jalo.JasperMedia;
import de.hybris.platform.cockpit.reports.jalo.JasperWidgetPreferences;
import de.hybris.platform.comments.jalo.Comment;
import de.hybris.platform.comments.jalo.CommentUserSetting;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCockpitManager extends Extension
{
    protected static final OneToManyHandler<CockpitObjectAbstractCollection> USER2COCKPITOBJECTABSTRACTCOLLECTIONRELATIONCOLLECTIONSHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.COCKPITOBJECTABSTRACTCOLLECTION, false, "user", null, false, true, 0);
    protected static final OneToManyHandler<CockpitSavedQuery> USER2COCKPITSAVEDQUERYRELATIONCOCKPITSAVEDQUERIESHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.COCKPITSAVEDQUERY, false, "user", null, false, true, 0);
    protected static final OneToManyHandler<CockpitFavoriteCategory> USER2COCKPITFAVORITECATEGORYRELATIONCOCKPITFAVORITECATEGORIESHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.COCKPITFAVORITECATEGORY, false, "user", null, false, true, 0);
    protected static final OneToManyHandler<WidgetPreferences> WIDGETPREFERENCESTOUSERRELATIONWIDGETPREFERENCESHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.WIDGETPREFERENCES, false, "ownerUser", null, false, true, 0);
    protected static String READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_SRC_ORDERED = "relation.ReadPrincipal2CockpitObjectAbstractCollectionRelation.source.ordered";
    protected static String READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_TGT_ORDERED = "relation.ReadPrincipal2CockpitObjectAbstractCollectionRelation.target.ordered";
    protected static String READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED = "relation.ReadPrincipal2CockpitObjectAbstractCollectionRelation.markmodified";
    protected static String WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_SRC_ORDERED = "relation.WritePrincipal2CockpitObjectAbstractCollectionRelation.source.ordered";
    protected static String WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_TGT_ORDERED = "relation.WritePrincipal2CockpitObjectAbstractCollectionRelation.target.ordered";
    protected static String WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED = "relation.WritePrincipal2CockpitObjectAbstractCollectionRelation.markmodified";
    protected static String PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_SRC_ORDERED = "relation.Principal2CockpitUIComponentReadAccessRelation.source.ordered";
    protected static String PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_TGT_ORDERED = "relation.Principal2CockpitUIComponentReadAccessRelation.target.ordered";
    protected static String PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_MARKMODIFIED = "relation.Principal2CockpitUIComponentReadAccessRelation.markmodified";
    protected static String PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_SRC_ORDERED = "relation.Principal2CockpitUIComponentWriteAccessRelation.source.ordered";
    protected static String PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_TGT_ORDERED = "relation.Principal2CockpitUIComponentWriteAccessRelation.target.ordered";
    protected static String PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_MARKMODIFIED = "relation.Principal2CockpitUIComponentWriteAccessRelation.markmodified";
    protected static String READPRINCIPAL2COCKPITSAVEDQUERYRELATION_SRC_ORDERED = "relation.ReadPrincipal2CockpitSavedQueryRelation.source.ordered";
    protected static String READPRINCIPAL2COCKPITSAVEDQUERYRELATION_TGT_ORDERED = "relation.ReadPrincipal2CockpitSavedQueryRelation.target.ordered";
    protected static String READPRINCIPAL2COCKPITSAVEDQUERYRELATION_MARKMODIFIED = "relation.ReadPrincipal2CockpitSavedQueryRelation.markmodified";
    protected static final OneToManyHandler<CockpitUIComponentConfiguration> PRINCIPAL2COCKPITUICOMPONENTCONFIGURATIONRELATIONCOCKPITUICONFIGURATIONSHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.COCKPITUICOMPONENTCONFIGURATION, false, "principal", null, false, true, 0);
    protected static String ITEM2COCKPITITEMTEMPLATERELATION_SRC_ORDERED = "relation.Item2CockpitItemTemplateRelation.source.ordered";
    protected static String ITEM2COCKPITITEMTEMPLATERELATION_TGT_ORDERED = "relation.Item2CockpitItemTemplateRelation.target.ordered";
    protected static String ITEM2COCKPITITEMTEMPLATERELATION_MARKMODIFIED = "relation.Item2CockpitItemTemplateRelation.markmodified";
    protected static final OneToManyHandler<CockpitItemTemplate> COCKPITITEMTEMPLATE2COMPOSEDTYPERELATIONCOCKPITITEMTEMPLATESHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE, false, "relatedType", null, false, true, 1);
    protected static String COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_SRC_ORDERED = "relation.CockpitItemTemplate2ClassificationClassRelation.source.ordered";
    protected static String COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_TGT_ORDERED = "relation.CockpitItemTemplate2ClassificationClassRelation.target.ordered";
    protected static String COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_MARKMODIFIED = "relation.CockpitItemTemplate2ClassificationClassRelation.markmodified";
    protected static final OneToManyHandler<CommentMetadata> COMMENTTOCOMMENTMETADATACOMMENTMETADATAHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.COMMENTMETADATA, true, "comment", null, false, true, 0);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("mnemonic", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.catalog.jalo.CatalogVersion", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("workStatus", Item.AttributeMode.INITIAL);
        tmp.put("hidden", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.comments.jalo.CommentUserSetting", Collections.unmodifiableMap(tmp));
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


    public Set<CockpitItemTemplate> getAssignedCockpitItemTemplates(SessionContext ctx, Item item)
    {
        List<CockpitItemTemplate> items = item.getLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.ITEM2COCKPITITEMTEMPLATERELATION, "CockpitItemTemplate", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<CockpitItemTemplate> getAssignedCockpitItemTemplates(Item item)
    {
        return getAssignedCockpitItemTemplates(getSession().getSessionContext(), item);
    }


    public long getAssignedCockpitItemTemplatesCount(SessionContext ctx, Item item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedCockpitConstants.Relations.ITEM2COCKPITITEMTEMPLATERELATION, "CockpitItemTemplate", null);
    }


    public long getAssignedCockpitItemTemplatesCount(Item item)
    {
        return getAssignedCockpitItemTemplatesCount(getSession().getSessionContext(), item);
    }


    public void setAssignedCockpitItemTemplates(SessionContext ctx, Item item, Set<CockpitItemTemplate> value)
    {
        item.setLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.ITEM2COCKPITITEMTEMPLATERELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(ITEM2COCKPITITEMTEMPLATERELATION_MARKMODIFIED));
    }


    public void setAssignedCockpitItemTemplates(Item item, Set<CockpitItemTemplate> value)
    {
        setAssignedCockpitItemTemplates(getSession().getSessionContext(), item, value);
    }


    public void addToAssignedCockpitItemTemplates(SessionContext ctx, Item item, CockpitItemTemplate value)
    {
        item.addLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.ITEM2COCKPITITEMTEMPLATERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(ITEM2COCKPITITEMTEMPLATERELATION_MARKMODIFIED));
    }


    public void addToAssignedCockpitItemTemplates(Item item, CockpitItemTemplate value)
    {
        addToAssignedCockpitItemTemplates(getSession().getSessionContext(), item, value);
    }


    public void removeFromAssignedCockpitItemTemplates(SessionContext ctx, Item item, CockpitItemTemplate value)
    {
        item.removeLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.ITEM2COCKPITITEMTEMPLATERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(ITEM2COCKPITITEMTEMPLATERELATION_MARKMODIFIED));
    }


    public void removeFromAssignedCockpitItemTemplates(Item item, CockpitItemTemplate value)
    {
        removeFromAssignedCockpitItemTemplates(getSession().getSessionContext(), item, value);
    }


    public Collection<CockpitFavoriteCategory> getCockpitFavoriteCategories(SessionContext ctx, User item)
    {
        return USER2COCKPITFAVORITECATEGORYRELATIONCOCKPITFAVORITECATEGORIESHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CockpitFavoriteCategory> getCockpitFavoriteCategories(User item)
    {
        return getCockpitFavoriteCategories(getSession().getSessionContext(), item);
    }


    public void setCockpitFavoriteCategories(SessionContext ctx, User item, Collection<CockpitFavoriteCategory> value)
    {
        USER2COCKPITFAVORITECATEGORYRELATIONCOCKPITFAVORITECATEGORIESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCockpitFavoriteCategories(User item, Collection<CockpitFavoriteCategory> value)
    {
        setCockpitFavoriteCategories(getSession().getSessionContext(), item, value);
    }


    public void addToCockpitFavoriteCategories(SessionContext ctx, User item, CockpitFavoriteCategory value)
    {
        USER2COCKPITFAVORITECATEGORYRELATIONCOCKPITFAVORITECATEGORIESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCockpitFavoriteCategories(User item, CockpitFavoriteCategory value)
    {
        addToCockpitFavoriteCategories(getSession().getSessionContext(), item, value);
    }


    public void removeFromCockpitFavoriteCategories(SessionContext ctx, User item, CockpitFavoriteCategory value)
    {
        USER2COCKPITFAVORITECATEGORYRELATIONCOCKPITFAVORITECATEGORIESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCockpitFavoriteCategories(User item, CockpitFavoriteCategory value)
    {
        removeFromCockpitFavoriteCategories(getSession().getSessionContext(), item, value);
    }


    public Set<CockpitItemTemplate> getCockpitItemTemplates(SessionContext ctx, ComposedType item)
    {
        return (Set<CockpitItemTemplate>)COCKPITITEMTEMPLATE2COMPOSEDTYPERELATIONCOCKPITITEMTEMPLATESHANDLER.getValues(ctx, (Item)item);
    }


    public Set<CockpitItemTemplate> getCockpitItemTemplates(ComposedType item)
    {
        return getCockpitItemTemplates(getSession().getSessionContext(), item);
    }


    public void setCockpitItemTemplates(SessionContext ctx, ComposedType item, Set<CockpitItemTemplate> value)
    {
        COCKPITITEMTEMPLATE2COMPOSEDTYPERELATIONCOCKPITITEMTEMPLATESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCockpitItemTemplates(ComposedType item, Set<CockpitItemTemplate> value)
    {
        setCockpitItemTemplates(getSession().getSessionContext(), item, value);
    }


    public void addToCockpitItemTemplates(SessionContext ctx, ComposedType item, CockpitItemTemplate value)
    {
        COCKPITITEMTEMPLATE2COMPOSEDTYPERELATIONCOCKPITITEMTEMPLATESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCockpitItemTemplates(ComposedType item, CockpitItemTemplate value)
    {
        addToCockpitItemTemplates(getSession().getSessionContext(), item, value);
    }


    public void removeFromCockpitItemTemplates(SessionContext ctx, ComposedType item, CockpitItemTemplate value)
    {
        COCKPITITEMTEMPLATE2COMPOSEDTYPERELATIONCOCKPITITEMTEMPLATESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCockpitItemTemplates(ComposedType item, CockpitItemTemplate value)
    {
        removeFromCockpitItemTemplates(getSession().getSessionContext(), item, value);
    }


    public Set<CockpitItemTemplate> getCockpitItemTemplates(SessionContext ctx, ClassificationClass item)
    {
        List<CockpitItemTemplate> items = item.getLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION, "CockpitItemTemplate", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<CockpitItemTemplate> getCockpitItemTemplates(ClassificationClass item)
    {
        return getCockpitItemTemplates(getSession().getSessionContext(), item);
    }


    public long getCockpitItemTemplatesCount(SessionContext ctx, ClassificationClass item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCockpitConstants.Relations.COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION, "CockpitItemTemplate", null);
    }


    public long getCockpitItemTemplatesCount(ClassificationClass item)
    {
        return getCockpitItemTemplatesCount(getSession().getSessionContext(), item);
    }


    public void setCockpitItemTemplates(SessionContext ctx, ClassificationClass item, Set<CockpitItemTemplate> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_MARKMODIFIED));
    }


    public void setCockpitItemTemplates(ClassificationClass item, Set<CockpitItemTemplate> value)
    {
        setCockpitItemTemplates(getSession().getSessionContext(), item, value);
    }


    public void addToCockpitItemTemplates(SessionContext ctx, ClassificationClass item, CockpitItemTemplate value)
    {
        item.addLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_MARKMODIFIED));
    }


    public void addToCockpitItemTemplates(ClassificationClass item, CockpitItemTemplate value)
    {
        addToCockpitItemTemplates(getSession().getSessionContext(), item, value);
    }


    public void removeFromCockpitItemTemplates(SessionContext ctx, ClassificationClass item, CockpitItemTemplate value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION_MARKMODIFIED));
    }


    public void removeFromCockpitItemTemplates(ClassificationClass item, CockpitItemTemplate value)
    {
        removeFromCockpitItemTemplates(getSession().getSessionContext(), item, value);
    }


    public Collection<CockpitSavedQuery> getCockpitSavedQueries(SessionContext ctx, User item)
    {
        return USER2COCKPITSAVEDQUERYRELATIONCOCKPITSAVEDQUERIESHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CockpitSavedQuery> getCockpitSavedQueries(User item)
    {
        return getCockpitSavedQueries(getSession().getSessionContext(), item);
    }


    public void setCockpitSavedQueries(SessionContext ctx, User item, Collection<CockpitSavedQuery> value)
    {
        USER2COCKPITSAVEDQUERYRELATIONCOCKPITSAVEDQUERIESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCockpitSavedQueries(User item, Collection<CockpitSavedQuery> value)
    {
        setCockpitSavedQueries(getSession().getSessionContext(), item, value);
    }


    public void addToCockpitSavedQueries(SessionContext ctx, User item, CockpitSavedQuery value)
    {
        USER2COCKPITSAVEDQUERYRELATIONCOCKPITSAVEDQUERIESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCockpitSavedQueries(User item, CockpitSavedQuery value)
    {
        addToCockpitSavedQueries(getSession().getSessionContext(), item, value);
    }


    public void removeFromCockpitSavedQueries(SessionContext ctx, User item, CockpitSavedQuery value)
    {
        USER2COCKPITSAVEDQUERYRELATIONCOCKPITSAVEDQUERIESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCockpitSavedQueries(User item, CockpitSavedQuery value)
    {
        removeFromCockpitSavedQueries(getSession().getSessionContext(), item, value);
    }


    public Collection<CockpitUIComponentConfiguration> getCockpitUIConfigurations(SessionContext ctx, Principal item)
    {
        return PRINCIPAL2COCKPITUICOMPONENTCONFIGURATIONRELATIONCOCKPITUICONFIGURATIONSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CockpitUIComponentConfiguration> getCockpitUIConfigurations(Principal item)
    {
        return getCockpitUIConfigurations(getSession().getSessionContext(), item);
    }


    public void setCockpitUIConfigurations(SessionContext ctx, Principal item, Collection<CockpitUIComponentConfiguration> value)
    {
        PRINCIPAL2COCKPITUICOMPONENTCONFIGURATIONRELATIONCOCKPITUICONFIGURATIONSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCockpitUIConfigurations(Principal item, Collection<CockpitUIComponentConfiguration> value)
    {
        setCockpitUIConfigurations(getSession().getSessionContext(), item, value);
    }


    public void addToCockpitUIConfigurations(SessionContext ctx, Principal item, CockpitUIComponentConfiguration value)
    {
        PRINCIPAL2COCKPITUICOMPONENTCONFIGURATIONRELATIONCOCKPITUICONFIGURATIONSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCockpitUIConfigurations(Principal item, CockpitUIComponentConfiguration value)
    {
        addToCockpitUIConfigurations(getSession().getSessionContext(), item, value);
    }


    public void removeFromCockpitUIConfigurations(SessionContext ctx, Principal item, CockpitUIComponentConfiguration value)
    {
        PRINCIPAL2COCKPITUICOMPONENTCONFIGURATIONRELATIONCOCKPITUICONFIGURATIONSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCockpitUIConfigurations(Principal item, CockpitUIComponentConfiguration value)
    {
        removeFromCockpitUIConfigurations(getSession().getSessionContext(), item, value);
    }


    public Collection<CockpitObjectAbstractCollection> getCollections(SessionContext ctx, User item)
    {
        return USER2COCKPITOBJECTABSTRACTCOLLECTIONRELATIONCOLLECTIONSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CockpitObjectAbstractCollection> getCollections(User item)
    {
        return getCollections(getSession().getSessionContext(), item);
    }


    public void setCollections(SessionContext ctx, User item, Collection<CockpitObjectAbstractCollection> value)
    {
        USER2COCKPITOBJECTABSTRACTCOLLECTIONRELATIONCOLLECTIONSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCollections(User item, Collection<CockpitObjectAbstractCollection> value)
    {
        setCollections(getSession().getSessionContext(), item, value);
    }


    public void addToCollections(SessionContext ctx, User item, CockpitObjectAbstractCollection value)
    {
        USER2COCKPITOBJECTABSTRACTCOLLECTIONRELATIONCOLLECTIONSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCollections(User item, CockpitObjectAbstractCollection value)
    {
        addToCollections(getSession().getSessionContext(), item, value);
    }


    public void removeFromCollections(SessionContext ctx, User item, CockpitObjectAbstractCollection value)
    {
        USER2COCKPITOBJECTABSTRACTCOLLECTIONRELATIONCOLLECTIONSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCollections(User item, CockpitObjectAbstractCollection value)
    {
        removeFromCollections(getSession().getSessionContext(), item, value);
    }


    public Collection<CommentMetadata> getCommentMetadata(SessionContext ctx, Comment item)
    {
        return COMMENTTOCOMMENTMETADATACOMMENTMETADATAHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CommentMetadata> getCommentMetadata(Comment item)
    {
        return getCommentMetadata(getSession().getSessionContext(), item);
    }


    public void setCommentMetadata(SessionContext ctx, Comment item, Collection<CommentMetadata> value)
    {
        COMMENTTOCOMMENTMETADATACOMMENTMETADATAHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCommentMetadata(Comment item, Collection<CommentMetadata> value)
    {
        setCommentMetadata(getSession().getSessionContext(), item, value);
    }


    public void addToCommentMetadata(SessionContext ctx, Comment item, CommentMetadata value)
    {
        COMMENTTOCOMMENTMETADATACOMMENTMETADATAHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCommentMetadata(Comment item, CommentMetadata value)
    {
        addToCommentMetadata(getSession().getSessionContext(), item, value);
    }


    public void removeFromCommentMetadata(SessionContext ctx, Comment item, CommentMetadata value)
    {
        COMMENTTOCOMMENTMETADATACOMMENTMETADATAHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCommentMetadata(Comment item, CommentMetadata value)
    {
        removeFromCommentMetadata(getSession().getSessionContext(), item, value);
    }


    public CockpitFavoriteCategory createCockpitFavoriteCategory(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITFAVORITECATEGORY);
            return (CockpitFavoriteCategory)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitFavoriteCategory : " + e.getMessage(), 0);
        }
    }


    public CockpitFavoriteCategory createCockpitFavoriteCategory(Map attributeValues)
    {
        return createCockpitFavoriteCategory(getSession().getSessionContext(), attributeValues);
    }


    public CockpitItemTemplate createCockpitItemTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE);
            return (CockpitItemTemplate)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitItemTemplate : " + e.getMessage(), 0);
        }
    }


    public CockpitItemTemplate createCockpitItemTemplate(Map attributeValues)
    {
        return createCockpitItemTemplate(getSession().getSessionContext(), attributeValues);
    }


    public CockpitObjectAbstractCollection createCockpitObjectAbstractCollection(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITOBJECTABSTRACTCOLLECTION);
            return (CockpitObjectAbstractCollection)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitObjectAbstractCollection : " + e.getMessage(), 0);
        }
    }


    public CockpitObjectAbstractCollection createCockpitObjectAbstractCollection(Map attributeValues)
    {
        return createCockpitObjectAbstractCollection(getSession().getSessionContext(), attributeValues);
    }


    public CockpitObjectCollection createCockpitObjectCollection(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITOBJECTCOLLECTION);
            return (CockpitObjectCollection)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitObjectCollection : " + e.getMessage(), 0);
        }
    }


    public CockpitObjectCollection createCockpitObjectCollection(Map attributeValues)
    {
        return createCockpitObjectCollection(getSession().getSessionContext(), attributeValues);
    }


    public CockpitObjectSpecialCollection createCockpitObjectSpecialCollection(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITOBJECTSPECIALCOLLECTION);
            return (CockpitObjectSpecialCollection)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitObjectSpecialCollection : " + e.getMessage(), 0);
        }
    }


    public CockpitObjectSpecialCollection createCockpitObjectSpecialCollection(Map attributeValues)
    {
        return createCockpitObjectSpecialCollection(getSession().getSessionContext(), attributeValues);
    }


    public CockpitSavedFacetValue createCockpitSavedFacetValue(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITSAVEDFACETVALUE);
            return (CockpitSavedFacetValue)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitSavedFacetValue : " + e.getMessage(), 0);
        }
    }


    public CockpitSavedFacetValue createCockpitSavedFacetValue(Map attributeValues)
    {
        return createCockpitSavedFacetValue(getSession().getSessionContext(), attributeValues);
    }


    public CockpitSavedParameterValue createCockpitSavedParameterValue(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITSAVEDPARAMETERVALUE);
            return (CockpitSavedParameterValue)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitSavedParameterValue : " + e.getMessage(), 0);
        }
    }


    public CockpitSavedParameterValue createCockpitSavedParameterValue(Map attributeValues)
    {
        return createCockpitSavedParameterValue(getSession().getSessionContext(), attributeValues);
    }


    public CockpitSavedQuery createCockpitSavedQuery(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITSAVEDQUERY);
            return (CockpitSavedQuery)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitSavedQuery : " + e.getMessage(), 0);
        }
    }


    public CockpitSavedQuery createCockpitSavedQuery(Map attributeValues)
    {
        return createCockpitSavedQuery(getSession().getSessionContext(), attributeValues);
    }


    public CockpitSavedSortCriterion createCockpitSavedSortCriterion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITSAVEDSORTCRITERION);
            return (CockpitSavedSortCriterion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitSavedSortCriterion : " + e.getMessage(), 0);
        }
    }


    public CockpitSavedSortCriterion createCockpitSavedSortCriterion(Map attributeValues)
    {
        return createCockpitSavedSortCriterion(getSession().getSessionContext(), attributeValues);
    }


    public CockpitUIComponentAccessRight createCockpitUIComponentAccessRight(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITUICOMPONENTACCESSRIGHT);
            return (CockpitUIComponentAccessRight)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitUIComponentAccessRight : " + e.getMessage(), 0);
        }
    }


    public CockpitUIComponentAccessRight createCockpitUIComponentAccessRight(Map attributeValues)
    {
        return createCockpitUIComponentAccessRight(getSession().getSessionContext(), attributeValues);
    }


    public CockpitUIComponentConfiguration createCockpitUIComponentConfiguration(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITUICOMPONENTCONFIGURATION);
            return (CockpitUIComponentConfiguration)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitUIComponentConfiguration : " + e.getMessage(), 0);
        }
    }


    public CockpitUIComponentConfiguration createCockpitUIComponentConfiguration(Map attributeValues)
    {
        return createCockpitUIComponentConfiguration(getSession().getSessionContext(), attributeValues);
    }


    public CockpitUIConfigurationMedia createCockpitUIConfigurationMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITUICONFIGURATIONMEDIA);
            return (CockpitUIConfigurationMedia)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitUIConfigurationMedia : " + e.getMessage(), 0);
        }
    }


    public CockpitUIConfigurationMedia createCockpitUIConfigurationMedia(Map attributeValues)
    {
        return createCockpitUIConfigurationMedia(getSession().getSessionContext(), attributeValues);
    }


    public CockpitUIScriptConfigMedia createCockpitUIScriptConfigMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COCKPITUISCRIPTCONFIGMEDIA);
            return (CockpitUIScriptConfigMedia)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CockpitUIScriptConfigMedia : " + e.getMessage(), 0);
        }
    }


    public CockpitUIScriptConfigMedia createCockpitUIScriptConfigMedia(Map attributeValues)
    {
        return createCockpitUIScriptConfigMedia(getSession().getSessionContext(), attributeValues);
    }


    public CommentMetadata createCommentMetadata(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COMMENTMETADATA);
            return (CommentMetadata)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CommentMetadata : " + e.getMessage(), 0);
        }
    }


    public CommentMetadata createCommentMetadata(Map attributeValues)
    {
        return createCommentMetadata(getSession().getSessionContext(), attributeValues);
    }


    public CompiledJasperMedia createCompiledJasperMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.COMPILEDJASPERMEDIA);
            return (CompiledJasperMedia)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CompiledJasperMedia : " + e.getMessage(), 0);
        }
    }


    public CompiledJasperMedia createCompiledJasperMedia(Map attributeValues)
    {
        return createCompiledJasperMedia(getSession().getSessionContext(), attributeValues);
    }


    public JasperMedia createJasperMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.JASPERMEDIA);
            return (JasperMedia)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating JasperMedia : " + e.getMessage(), 0);
        }
    }


    public JasperMedia createJasperMedia(Map attributeValues)
    {
        return createJasperMedia(getSession().getSessionContext(), attributeValues);
    }


    public JasperWidgetPreferences createJasperWidgetPreferences(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.JASPERWIDGETPREFERENCES);
            return (JasperWidgetPreferences)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating JasperWidgetPreferences : " + e.getMessage(), 0);
        }
    }


    public JasperWidgetPreferences createJasperWidgetPreferences(Map attributeValues)
    {
        return createJasperWidgetPreferences(getSession().getSessionContext(), attributeValues);
    }


    public ObjectCollectionItemReference createObjectCollectionItemReference(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.OBJECTCOLLECTIONITEMREFERENCE);
            return (ObjectCollectionItemReference)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ObjectCollectionItemReference : " + e.getMessage(), 0);
        }
    }


    public ObjectCollectionItemReference createObjectCollectionItemReference(Map attributeValues)
    {
        return createObjectCollectionItemReference(getSession().getSessionContext(), attributeValues);
    }


    public WidgetParameter createWidgetParameter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCockpitConstants.TC.WIDGETPARAMETER);
            return (WidgetParameter)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating WidgetParameter : " + e.getMessage(), 0);
        }
    }


    public WidgetParameter createWidgetParameter(Map attributeValues)
    {
        return createWidgetParameter(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "cockpit";
    }


    public Boolean isHidden(SessionContext ctx, CommentUserSetting item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedCockpitConstants.Attributes.CommentUserSetting.HIDDEN);
    }


    public Boolean isHidden(CommentUserSetting item)
    {
        return isHidden(getSession().getSessionContext(), item);
    }


    public boolean isHiddenAsPrimitive(SessionContext ctx, CommentUserSetting item)
    {
        Boolean value = isHidden(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isHiddenAsPrimitive(CommentUserSetting item)
    {
        return isHiddenAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setHidden(SessionContext ctx, CommentUserSetting item, Boolean value)
    {
        item.setProperty(ctx, GeneratedCockpitConstants.Attributes.CommentUserSetting.HIDDEN, value);
    }


    public void setHidden(CommentUserSetting item, Boolean value)
    {
        setHidden(getSession().getSessionContext(), item, value);
    }


    public void setHidden(SessionContext ctx, CommentUserSetting item, boolean value)
    {
        setHidden(ctx, item, Boolean.valueOf(value));
    }


    public void setHidden(CommentUserSetting item, boolean value)
    {
        setHidden(getSession().getSessionContext(), item, value);
    }


    public String getMnemonic(SessionContext ctx, CatalogVersion item)
    {
        return (String)item.getProperty(ctx, GeneratedCockpitConstants.Attributes.CatalogVersion.MNEMONIC);
    }


    public String getMnemonic(CatalogVersion item)
    {
        return getMnemonic(getSession().getSessionContext(), item);
    }


    public void setMnemonic(SessionContext ctx, CatalogVersion item, String value)
    {
        item.setProperty(ctx, GeneratedCockpitConstants.Attributes.CatalogVersion.MNEMONIC, value);
    }


    public void setMnemonic(CatalogVersion item, String value)
    {
        setMnemonic(getSession().getSessionContext(), item, value);
    }


    public Collection<CockpitUIComponentAccessRight> getReadableCockpitUIComponents(SessionContext ctx, Principal item)
    {
        List<CockpitUIComponentAccessRight> items = item.getLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION, "CockpitUIComponentAccessRight", null, false, false);
        return items;
    }


    public Collection<CockpitUIComponentAccessRight> getReadableCockpitUIComponents(Principal item)
    {
        return getReadableCockpitUIComponents(getSession().getSessionContext(), item);
    }


    public long getReadableCockpitUIComponentsCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION, "CockpitUIComponentAccessRight", null);
    }


    public long getReadableCockpitUIComponentsCount(Principal item)
    {
        return getReadableCockpitUIComponentsCount(getSession().getSessionContext(), item);
    }


    public void setReadableCockpitUIComponents(SessionContext ctx, Principal item, Collection<CockpitUIComponentAccessRight> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_MARKMODIFIED));
    }


    public void setReadableCockpitUIComponents(Principal item, Collection<CockpitUIComponentAccessRight> value)
    {
        setReadableCockpitUIComponents(getSession().getSessionContext(), item, value);
    }


    public void addToReadableCockpitUIComponents(SessionContext ctx, Principal item, CockpitUIComponentAccessRight value)
    {
        item.addLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_MARKMODIFIED));
    }


    public void addToReadableCockpitUIComponents(Principal item, CockpitUIComponentAccessRight value)
    {
        addToReadableCockpitUIComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromReadableCockpitUIComponents(SessionContext ctx, Principal item, CockpitUIComponentAccessRight value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION_MARKMODIFIED));
    }


    public void removeFromReadableCockpitUIComponents(Principal item, CockpitUIComponentAccessRight value)
    {
        removeFromReadableCockpitUIComponents(getSession().getSessionContext(), item, value);
    }


    public Collection<CockpitObjectAbstractCollection> getReadCollections(SessionContext ctx, Principal item)
    {
        List<CockpitObjectAbstractCollection> items = item.getLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, "CockpitObjectAbstractCollection", null, false, false);
        return items;
    }


    public Collection<CockpitObjectAbstractCollection> getReadCollections(Principal item)
    {
        return getReadCollections(getSession().getSessionContext(), item);
    }


    public long getReadCollectionsCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, "CockpitObjectAbstractCollection", null);
    }


    public long getReadCollectionsCount(Principal item)
    {
        return getReadCollectionsCount(getSession().getSessionContext(), item);
    }


    public void setReadCollections(SessionContext ctx, Principal item, Collection<CockpitObjectAbstractCollection> value)
    {
        item.setLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void setReadCollections(Principal item, Collection<CockpitObjectAbstractCollection> value)
    {
        setReadCollections(getSession().getSessionContext(), item, value);
    }


    public void addToReadCollections(SessionContext ctx, Principal item, CockpitObjectAbstractCollection value)
    {
        item.addLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void addToReadCollections(Principal item, CockpitObjectAbstractCollection value)
    {
        addToReadCollections(getSession().getSessionContext(), item, value);
    }


    public void removeFromReadCollections(SessionContext ctx, Principal item, CockpitObjectAbstractCollection value)
    {
        item.removeLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void removeFromReadCollections(Principal item, CockpitObjectAbstractCollection value)
    {
        removeFromReadCollections(getSession().getSessionContext(), item, value);
    }


    public Collection<CockpitSavedQuery> getReadSavedQueries(SessionContext ctx, Principal item)
    {
        List<CockpitSavedQuery> items = item.getLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION, "CockpitSavedQuery", null, false, false);
        return items;
    }


    public Collection<CockpitSavedQuery> getReadSavedQueries(Principal item)
    {
        return getReadSavedQueries(getSession().getSessionContext(), item);
    }


    public long getReadSavedQueriesCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION, "CockpitSavedQuery", null);
    }


    public long getReadSavedQueriesCount(Principal item)
    {
        return getReadSavedQueriesCount(getSession().getSessionContext(), item);
    }


    public void setReadSavedQueries(SessionContext ctx, Principal item, Collection<CockpitSavedQuery> value)
    {
        item.setLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITSAVEDQUERYRELATION_MARKMODIFIED));
    }


    public void setReadSavedQueries(Principal item, Collection<CockpitSavedQuery> value)
    {
        setReadSavedQueries(getSession().getSessionContext(), item, value);
    }


    public void addToReadSavedQueries(SessionContext ctx, Principal item, CockpitSavedQuery value)
    {
        item.addLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITSAVEDQUERYRELATION_MARKMODIFIED));
    }


    public void addToReadSavedQueries(Principal item, CockpitSavedQuery value)
    {
        addToReadSavedQueries(getSession().getSessionContext(), item, value);
    }


    public void removeFromReadSavedQueries(SessionContext ctx, Principal item, CockpitSavedQuery value)
    {
        item.removeLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITSAVEDQUERYRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITSAVEDQUERYRELATION_MARKMODIFIED));
    }


    public void removeFromReadSavedQueries(Principal item, CockpitSavedQuery value)
    {
        removeFromReadSavedQueries(getSession().getSessionContext(), item, value);
    }


    public Collection<WidgetPreferences> getWidgetPreferences(SessionContext ctx, User item)
    {
        return WIDGETPREFERENCESTOUSERRELATIONWIDGETPREFERENCESHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<WidgetPreferences> getWidgetPreferences(User item)
    {
        return getWidgetPreferences(getSession().getSessionContext(), item);
    }


    public void setWidgetPreferences(SessionContext ctx, User item, Collection<WidgetPreferences> value)
    {
        WIDGETPREFERENCESTOUSERRELATIONWIDGETPREFERENCESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setWidgetPreferences(User item, Collection<WidgetPreferences> value)
    {
        setWidgetPreferences(getSession().getSessionContext(), item, value);
    }


    public void addToWidgetPreferences(SessionContext ctx, User item, WidgetPreferences value)
    {
        WIDGETPREFERENCESTOUSERRELATIONWIDGETPREFERENCESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToWidgetPreferences(User item, WidgetPreferences value)
    {
        addToWidgetPreferences(getSession().getSessionContext(), item, value);
    }


    public void removeFromWidgetPreferences(SessionContext ctx, User item, WidgetPreferences value)
    {
        WIDGETPREFERENCESTOUSERRELATIONWIDGETPREFERENCESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromWidgetPreferences(User item, WidgetPreferences value)
    {
        removeFromWidgetPreferences(getSession().getSessionContext(), item, value);
    }


    public Boolean isWorkStatus(SessionContext ctx, CommentUserSetting item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedCockpitConstants.Attributes.CommentUserSetting.WORKSTATUS);
    }


    public Boolean isWorkStatus(CommentUserSetting item)
    {
        return isWorkStatus(getSession().getSessionContext(), item);
    }


    public boolean isWorkStatusAsPrimitive(SessionContext ctx, CommentUserSetting item)
    {
        Boolean value = isWorkStatus(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isWorkStatusAsPrimitive(CommentUserSetting item)
    {
        return isWorkStatusAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setWorkStatus(SessionContext ctx, CommentUserSetting item, Boolean value)
    {
        item.setProperty(ctx, GeneratedCockpitConstants.Attributes.CommentUserSetting.WORKSTATUS, value);
    }


    public void setWorkStatus(CommentUserSetting item, Boolean value)
    {
        setWorkStatus(getSession().getSessionContext(), item, value);
    }


    public void setWorkStatus(SessionContext ctx, CommentUserSetting item, boolean value)
    {
        setWorkStatus(ctx, item, Boolean.valueOf(value));
    }


    public void setWorkStatus(CommentUserSetting item, boolean value)
    {
        setWorkStatus(getSession().getSessionContext(), item, value);
    }


    public Collection<CockpitUIComponentAccessRight> getWritableCockpitUIComponents(SessionContext ctx, Principal item)
    {
        List<CockpitUIComponentAccessRight> items = item.getLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION, "CockpitUIComponentAccessRight", null, false, false);
        return items;
    }


    public Collection<CockpitUIComponentAccessRight> getWritableCockpitUIComponents(Principal item)
    {
        return getWritableCockpitUIComponents(getSession().getSessionContext(), item);
    }


    public long getWritableCockpitUIComponentsCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION, "CockpitUIComponentAccessRight", null);
    }


    public long getWritableCockpitUIComponentsCount(Principal item)
    {
        return getWritableCockpitUIComponentsCount(getSession().getSessionContext(), item);
    }


    public void setWritableCockpitUIComponents(SessionContext ctx, Principal item, Collection<CockpitUIComponentAccessRight> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_MARKMODIFIED));
    }


    public void setWritableCockpitUIComponents(Principal item, Collection<CockpitUIComponentAccessRight> value)
    {
        setWritableCockpitUIComponents(getSession().getSessionContext(), item, value);
    }


    public void addToWritableCockpitUIComponents(SessionContext ctx, Principal item, CockpitUIComponentAccessRight value)
    {
        item.addLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_MARKMODIFIED));
    }


    public void addToWritableCockpitUIComponents(Principal item, CockpitUIComponentAccessRight value)
    {
        addToWritableCockpitUIComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromWritableCockpitUIComponents(SessionContext ctx, Principal item, CockpitUIComponentAccessRight value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION_MARKMODIFIED));
    }


    public void removeFromWritableCockpitUIComponents(Principal item, CockpitUIComponentAccessRight value)
    {
        removeFromWritableCockpitUIComponents(getSession().getSessionContext(), item, value);
    }


    public Collection<CockpitObjectAbstractCollection> getWriteCollections(SessionContext ctx, Principal item)
    {
        List<CockpitObjectAbstractCollection> items = item.getLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, "CockpitObjectAbstractCollection", null, false, false);
        return items;
    }


    public Collection<CockpitObjectAbstractCollection> getWriteCollections(Principal item)
    {
        return getWriteCollections(getSession().getSessionContext(), item);
    }


    public long getWriteCollectionsCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, "CockpitObjectAbstractCollection", null);
    }


    public long getWriteCollectionsCount(Principal item)
    {
        return getWriteCollectionsCount(getSession().getSessionContext(), item);
    }


    public void setWriteCollections(SessionContext ctx, Principal item, Collection<CockpitObjectAbstractCollection> value)
    {
        item.setLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void setWriteCollections(Principal item, Collection<CockpitObjectAbstractCollection> value)
    {
        setWriteCollections(getSession().getSessionContext(), item, value);
    }


    public void addToWriteCollections(SessionContext ctx, Principal item, CockpitObjectAbstractCollection value)
    {
        item.addLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void addToWriteCollections(Principal item, CockpitObjectAbstractCollection value)
    {
        addToWriteCollections(getSession().getSessionContext(), item, value);
    }


    public void removeFromWriteCollections(SessionContext ctx, Principal item, CockpitObjectAbstractCollection value)
    {
        item.removeLinkedItems(ctx, true, GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void removeFromWriteCollections(Principal item, CockpitObjectAbstractCollection value)
    {
        removeFromWriteCollections(getSession().getSessionContext(), item, value);
    }
}
