package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedSyncItemJob extends Job
{
    public static final String EXCLUSIVEMODE = "exclusiveMode";
    public static final String SYNCPRINCIPALSONLY = "syncPrincipalsOnly";
    public static final String CREATENEWITEMS = "createNewItems";
    public static final String REMOVEMISSINGITEMS = "removeMissingItems";
    public static final String EXECUTIONS = "executions";
    public static final String SYNCORDER = "syncOrder";
    public static final String EXPORTATTRIBUTEDESCRIPTORS = "exportAttributeDescriptors";
    public static final String SYNCATTRIBUTECONFIGURATIONS = "syncAttributeConfigurations";
    public static final String EFFECTIVESYNCLANGUAGES = "effectiveSyncLanguages";
    public static final String SOURCEVERSION = "sourceVersion";
    public static final String TARGETVERSION = "targetVersion";
    public static final String ROOTTYPES = "rootTypes";
    protected static String SYNCJOB2TYPEREL_SRC_ORDERED = "relation.SyncJob2TypeRel.source.ordered";
    protected static String SYNCJOB2TYPEREL_TGT_ORDERED = "relation.SyncJob2TypeRel.target.ordered";
    protected static String SYNCJOB2TYPEREL_MARKMODIFIED = "relation.SyncJob2TypeRel.markmodified";
    public static final String SYNCLANGUAGES = "syncLanguages";
    protected static String SYNCJOB2LANGREL_SRC_ORDERED = "relation.SyncJob2LangRel.source.ordered";
    protected static String SYNCJOB2LANGREL_TGT_ORDERED = "relation.SyncJob2LangRel.target.ordered";
    protected static String SYNCJOB2LANGREL_MARKMODIFIED = "relation.SyncJob2LangRel.markmodified";
    public static final String SYNCPRINCIPALS = "syncPrincipals";
    protected static String SYNCITEMJOB2PRINCIPAL_SRC_ORDERED = "relation.SyncItemJob2Principal.source.ordered";
    protected static String SYNCITEMJOB2PRINCIPAL_TGT_ORDERED = "relation.SyncItemJob2Principal.target.ordered";
    protected static String SYNCITEMJOB2PRINCIPAL_MARKMODIFIED = "relation.SyncItemJob2Principal.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedSyncItemJob> SOURCEVERSIONHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.SYNCITEMJOB, false, "sourceVersion", null, false, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedSyncItemJob> TARGETVERSIONHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.SYNCITEMJOB, false, "targetVersion", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Job.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("exclusiveMode", Item.AttributeMode.INITIAL);
        tmp.put("syncPrincipalsOnly", Item.AttributeMode.INITIAL);
        tmp.put("createNewItems", Item.AttributeMode.INITIAL);
        tmp.put("removeMissingItems", Item.AttributeMode.INITIAL);
        tmp.put("syncOrder", Item.AttributeMode.INITIAL);
        tmp.put("sourceVersion", Item.AttributeMode.INITIAL);
        tmp.put("targetVersion", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOURCEVERSIONHANDLER.newInstance(ctx, allAttributes);
        TARGETVERSIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isCreateNewItems(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "createNewItems");
    }


    public Boolean isCreateNewItems()
    {
        return isCreateNewItems(getSession().getSessionContext());
    }


    public boolean isCreateNewItemsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCreateNewItems(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCreateNewItemsAsPrimitive()
    {
        return isCreateNewItemsAsPrimitive(getSession().getSessionContext());
    }


    public void setCreateNewItems(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "createNewItems", value);
    }


    public void setCreateNewItems(Boolean value)
    {
        setCreateNewItems(getSession().getSessionContext(), value);
    }


    public void setCreateNewItems(SessionContext ctx, boolean value)
    {
        setCreateNewItems(ctx, Boolean.valueOf(value));
    }


    public void setCreateNewItems(boolean value)
    {
        setCreateNewItems(getSession().getSessionContext(), value);
    }


    public Collection<Language> getEffectiveSyncLanguages()
    {
        return getEffectiveSyncLanguages(getSession().getSessionContext());
    }


    public Boolean isExclusiveMode(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "exclusiveMode");
    }


    public Boolean isExclusiveMode()
    {
        return isExclusiveMode(getSession().getSessionContext());
    }


    public boolean isExclusiveModeAsPrimitive(SessionContext ctx)
    {
        Boolean value = isExclusiveMode(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isExclusiveModeAsPrimitive()
    {
        return isExclusiveModeAsPrimitive(getSession().getSessionContext());
    }


    protected void setExclusiveMode(SessionContext ctx, Boolean value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'exclusiveMode' is not changeable", 0);
        }
        setProperty(ctx, "exclusiveMode", value);
    }


    protected void setExclusiveMode(Boolean value)
    {
        setExclusiveMode(getSession().getSessionContext(), value);
    }


    protected void setExclusiveMode(SessionContext ctx, boolean value)
    {
        setExclusiveMode(ctx, Boolean.valueOf(value));
    }


    protected void setExclusiveMode(boolean value)
    {
        setExclusiveMode(getSession().getSessionContext(), value);
    }


    public Collection<SyncItemCronJob> getExecutions()
    {
        return getExecutions(getSession().getSessionContext());
    }


    public Map<AttributeDescriptor, Boolean> getAllExportAttributeDescriptors()
    {
        return getAllExportAttributeDescriptors(getSession().getSessionContext());
    }


    public void setAllExportAttributeDescriptors(Map<AttributeDescriptor, Boolean> value)
    {
        setAllExportAttributeDescriptors(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("ComposedType");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SYNCJOB2TYPEREL_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Language");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SYNCJOB2LANGREL_MARKMODIFIED);
        }
        ComposedType relationSecondEnd2 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd2.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SYNCITEMJOB2PRINCIPAL_MARKMODIFIED);
        }
        return true;
    }


    public Boolean isRemoveMissingItems(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "removeMissingItems");
    }


    public Boolean isRemoveMissingItems()
    {
        return isRemoveMissingItems(getSession().getSessionContext());
    }


    public boolean isRemoveMissingItemsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRemoveMissingItems(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRemoveMissingItemsAsPrimitive()
    {
        return isRemoveMissingItemsAsPrimitive(getSession().getSessionContext());
    }


    public void setRemoveMissingItems(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "removeMissingItems", value);
    }


    public void setRemoveMissingItems(Boolean value)
    {
        setRemoveMissingItems(getSession().getSessionContext(), value);
    }


    public void setRemoveMissingItems(SessionContext ctx, boolean value)
    {
        setRemoveMissingItems(ctx, Boolean.valueOf(value));
    }


    public void setRemoveMissingItems(boolean value)
    {
        setRemoveMissingItems(getSession().getSessionContext(), value);
    }


    public List<ComposedType> getRootTypes(SessionContext ctx)
    {
        List<ComposedType> items = getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCJOB2TYPEREL, "ComposedType", null,
                        Utilities.getRelationOrderingOverride(SYNCJOB2TYPEREL_SRC_ORDERED, true), false);
        return items;
    }


    public List<ComposedType> getRootTypes()
    {
        return getRootTypes(getSession().getSessionContext());
    }


    public long getRootTypesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.SYNCJOB2TYPEREL, "ComposedType", null);
    }


    public long getRootTypesCount()
    {
        return getRootTypesCount(getSession().getSessionContext());
    }


    public void setRootTypes(SessionContext ctx, List<ComposedType> value)
    {
        setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCJOB2TYPEREL, null, value,
                        Utilities.getRelationOrderingOverride(SYNCJOB2TYPEREL_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(SYNCJOB2TYPEREL_MARKMODIFIED));
    }


    public void setRootTypes(List<ComposedType> value)
    {
        setRootTypes(getSession().getSessionContext(), value);
    }


    public void addToRootTypes(SessionContext ctx, ComposedType value)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCJOB2TYPEREL, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SYNCJOB2TYPEREL_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(SYNCJOB2TYPEREL_MARKMODIFIED));
    }


    public void addToRootTypes(ComposedType value)
    {
        addToRootTypes(getSession().getSessionContext(), value);
    }


    public void removeFromRootTypes(SessionContext ctx, ComposedType value)
    {
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCJOB2TYPEREL, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SYNCJOB2TYPEREL_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(SYNCJOB2TYPEREL_MARKMODIFIED));
    }


    public void removeFromRootTypes(ComposedType value)
    {
        removeFromRootTypes(getSession().getSessionContext(), value);
    }


    public CatalogVersion getSourceVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "sourceVersion");
    }


    public CatalogVersion getSourceVersion()
    {
        return getSourceVersion(getSession().getSessionContext());
    }


    protected void setSourceVersion(SessionContext ctx, CatalogVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sourceVersion' is not changeable", 0);
        }
        SOURCEVERSIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setSourceVersion(CatalogVersion value)
    {
        setSourceVersion(getSession().getSessionContext(), value);
    }


    public Collection<SyncAttributeDescriptorConfig> getSyncAttributeConfigurations()
    {
        return getSyncAttributeConfigurations(getSession().getSessionContext());
    }


    public void setSyncAttributeConfigurations(Collection<SyncAttributeDescriptorConfig> value)
    {
        setSyncAttributeConfigurations(getSession().getSessionContext(), value);
    }


    public Set<Language> getSyncLanguages(SessionContext ctx)
    {
        List<Language> items = getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCJOB2LANGREL, "Language", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<Language> getSyncLanguages()
    {
        return getSyncLanguages(getSession().getSessionContext());
    }


    public long getSyncLanguagesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.SYNCJOB2LANGREL, "Language", null);
    }


    public long getSyncLanguagesCount()
    {
        return getSyncLanguagesCount(getSession().getSessionContext());
    }


    public void setSyncLanguages(SessionContext ctx, Set<Language> value)
    {
        setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCJOB2LANGREL, null, value, false, false,
                        Utilities.getMarkModifiedOverride(SYNCJOB2LANGREL_MARKMODIFIED));
    }


    public void setSyncLanguages(Set<Language> value)
    {
        setSyncLanguages(getSession().getSessionContext(), value);
    }


    public void addToSyncLanguages(SessionContext ctx, Language value)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCJOB2LANGREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(SYNCJOB2LANGREL_MARKMODIFIED));
    }


    public void addToSyncLanguages(Language value)
    {
        addToSyncLanguages(getSession().getSessionContext(), value);
    }


    public void removeFromSyncLanguages(SessionContext ctx, Language value)
    {
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCJOB2LANGREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(SYNCJOB2LANGREL_MARKMODIFIED));
    }


    public void removeFromSyncLanguages(Language value)
    {
        removeFromSyncLanguages(getSession().getSessionContext(), value);
    }


    public Integer getSyncOrder(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "syncOrder");
    }


    public Integer getSyncOrder()
    {
        return getSyncOrder(getSession().getSessionContext());
    }


    public int getSyncOrderAsPrimitive(SessionContext ctx)
    {
        Integer value = getSyncOrder(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getSyncOrderAsPrimitive()
    {
        return getSyncOrderAsPrimitive(getSession().getSessionContext());
    }


    public void setSyncOrder(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "syncOrder", value);
    }


    public void setSyncOrder(Integer value)
    {
        setSyncOrder(getSession().getSessionContext(), value);
    }


    public void setSyncOrder(SessionContext ctx, int value)
    {
        setSyncOrder(ctx, Integer.valueOf(value));
    }


    public void setSyncOrder(int value)
    {
        setSyncOrder(getSession().getSessionContext(), value);
    }


    public List<Principal> getSyncPrincipals(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCITEMJOB2PRINCIPAL, "Principal", null,
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_TGT_ORDERED, true));
        return items;
    }


    public List<Principal> getSyncPrincipals()
    {
        return getSyncPrincipals(getSession().getSessionContext());
    }


    public long getSyncPrincipalsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.SYNCITEMJOB2PRINCIPAL, "Principal", null);
    }


    public long getSyncPrincipalsCount()
    {
        return getSyncPrincipalsCount(getSession().getSessionContext());
    }


    public void setSyncPrincipals(SessionContext ctx, List<Principal> value)
    {
        setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCITEMJOB2PRINCIPAL, null, value,
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SYNCITEMJOB2PRINCIPAL_MARKMODIFIED));
    }


    public void setSyncPrincipals(List<Principal> value)
    {
        setSyncPrincipals(getSession().getSessionContext(), value);
    }


    public void addToSyncPrincipals(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCITEMJOB2PRINCIPAL, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SYNCITEMJOB2PRINCIPAL_MARKMODIFIED));
    }


    public void addToSyncPrincipals(Principal value)
    {
        addToSyncPrincipals(getSession().getSessionContext(), value);
    }


    public void removeFromSyncPrincipals(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.SYNCITEMJOB2PRINCIPAL, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SYNCITEMJOB2PRINCIPAL_MARKMODIFIED));
    }


    public void removeFromSyncPrincipals(Principal value)
    {
        removeFromSyncPrincipals(getSession().getSessionContext(), value);
    }


    public Boolean isSyncPrincipalsOnly(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "syncPrincipalsOnly");
    }


    public Boolean isSyncPrincipalsOnly()
    {
        return isSyncPrincipalsOnly(getSession().getSessionContext());
    }


    public boolean isSyncPrincipalsOnlyAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSyncPrincipalsOnly(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSyncPrincipalsOnlyAsPrimitive()
    {
        return isSyncPrincipalsOnlyAsPrimitive(getSession().getSessionContext());
    }


    public void setSyncPrincipalsOnly(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "syncPrincipalsOnly", value);
    }


    public void setSyncPrincipalsOnly(Boolean value)
    {
        setSyncPrincipalsOnly(getSession().getSessionContext(), value);
    }


    public void setSyncPrincipalsOnly(SessionContext ctx, boolean value)
    {
        setSyncPrincipalsOnly(ctx, Boolean.valueOf(value));
    }


    public void setSyncPrincipalsOnly(boolean value)
    {
        setSyncPrincipalsOnly(getSession().getSessionContext(), value);
    }


    public CatalogVersion getTargetVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "targetVersion");
    }


    public CatalogVersion getTargetVersion()
    {
        return getTargetVersion(getSession().getSessionContext());
    }


    protected void setTargetVersion(SessionContext ctx, CatalogVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'targetVersion' is not changeable", 0);
        }
        TARGETVERSIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setTargetVersion(CatalogVersion value)
    {
        setTargetVersion(getSession().getSessionContext(), value);
    }


    public abstract Collection<Language> getEffectiveSyncLanguages(SessionContext paramSessionContext);


    public abstract Collection<SyncItemCronJob> getExecutions(SessionContext paramSessionContext);


    public abstract Map<AttributeDescriptor, Boolean> getAllExportAttributeDescriptors(SessionContext paramSessionContext);


    public abstract void setAllExportAttributeDescriptors(SessionContext paramSessionContext, Map<AttributeDescriptor, Boolean> paramMap);


    public abstract Collection<SyncAttributeDescriptorConfig> getSyncAttributeConfigurations(SessionContext paramSessionContext);


    public abstract void setSyncAttributeConfigurations(SessionContext paramSessionContext, Collection<SyncAttributeDescriptorConfig> paramCollection);
}
