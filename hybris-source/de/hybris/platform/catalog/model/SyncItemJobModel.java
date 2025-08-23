package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SyncItemJobModel extends JobModel
{
    public static final String _TYPECODE = "SyncItemJob";
    public static final String _CATALOGVERSION2SYNCHRONIZATIONS = "CatalogVersion2Synchronizations";
    public static final String _CATALOGVERSION2INCOMINGSYNCHRONIZATIONS = "CatalogVersion2IncomingSynchronizations";
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
    public static final String SYNCLANGUAGES = "syncLanguages";
    public static final String SYNCPRINCIPALS = "syncPrincipals";


    public SyncItemJobModel()
    {
    }


    public SyncItemJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SyncItemJobModel(String _code, CatalogVersionModel _sourceVersion, CatalogVersionModel _targetVersion)
    {
        setCode(_code);
        setSourceVersion(_sourceVersion);
        setTargetVersion(_targetVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SyncItemJobModel(String _code, Integer _nodeID, ItemModel _owner, CatalogVersionModel _sourceVersion, CatalogVersionModel _targetVersion)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSourceVersion(_sourceVersion);
        setTargetVersion(_targetVersion);
    }


    @Accessor(qualifier = "createNewItems", type = Accessor.Type.GETTER)
    public Boolean getCreateNewItems()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("createNewItems");
    }


    @Accessor(qualifier = "effectiveSyncLanguages", type = Accessor.Type.GETTER)
    public Collection<LanguageModel> getEffectiveSyncLanguages()
    {
        return (Collection<LanguageModel>)getPersistenceContext().getPropertyValue("effectiveSyncLanguages");
    }


    @Accessor(qualifier = "exclusiveMode", type = Accessor.Type.GETTER)
    public Boolean getExclusiveMode()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("exclusiveMode");
    }


    @Accessor(qualifier = "executions", type = Accessor.Type.GETTER)
    public Collection<SyncItemCronJobModel> getExecutions()
    {
        return (Collection<SyncItemCronJobModel>)getPersistenceContext().getPropertyValue("executions");
    }


    @Accessor(qualifier = "exportAttributeDescriptors", type = Accessor.Type.GETTER)
    public Map<AttributeDescriptorModel, Boolean> getExportAttributeDescriptors()
    {
        return (Map<AttributeDescriptorModel, Boolean>)getPersistenceContext().getPropertyValue("exportAttributeDescriptors");
    }


    @Accessor(qualifier = "removeMissingItems", type = Accessor.Type.GETTER)
    public Boolean getRemoveMissingItems()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("removeMissingItems");
    }


    @Accessor(qualifier = "rootTypes", type = Accessor.Type.GETTER)
    public List<ComposedTypeModel> getRootTypes()
    {
        return (List<ComposedTypeModel>)getPersistenceContext().getPropertyValue("rootTypes");
    }


    @Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getSourceVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("sourceVersion");
    }


    @Accessor(qualifier = "syncAttributeConfigurations", type = Accessor.Type.GETTER)
    public Collection<SyncAttributeDescriptorConfigModel> getSyncAttributeConfigurations()
    {
        return (Collection<SyncAttributeDescriptorConfigModel>)getPersistenceContext().getPropertyValue("syncAttributeConfigurations");
    }


    @Accessor(qualifier = "syncLanguages", type = Accessor.Type.GETTER)
    public Set<LanguageModel> getSyncLanguages()
    {
        return (Set<LanguageModel>)getPersistenceContext().getPropertyValue("syncLanguages");
    }


    @Accessor(qualifier = "syncOrder", type = Accessor.Type.GETTER)
    public Integer getSyncOrder()
    {
        return (Integer)getPersistenceContext().getPropertyValue("syncOrder");
    }


    @Accessor(qualifier = "syncPrincipals", type = Accessor.Type.GETTER)
    public List<PrincipalModel> getSyncPrincipals()
    {
        return (List<PrincipalModel>)getPersistenceContext().getPropertyValue("syncPrincipals");
    }


    @Accessor(qualifier = "syncPrincipalsOnly", type = Accessor.Type.GETTER)
    public Boolean getSyncPrincipalsOnly()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("syncPrincipalsOnly");
    }


    @Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getTargetVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("targetVersion");
    }


    @Accessor(qualifier = "createNewItems", type = Accessor.Type.SETTER)
    public void setCreateNewItems(Boolean value)
    {
        getPersistenceContext().setPropertyValue("createNewItems", value);
    }


    @Accessor(qualifier = "exclusiveMode", type = Accessor.Type.SETTER)
    public void setExclusiveMode(Boolean value)
    {
        getPersistenceContext().setPropertyValue("exclusiveMode", value);
    }


    @Accessor(qualifier = "exportAttributeDescriptors", type = Accessor.Type.SETTER)
    public void setExportAttributeDescriptors(Map<AttributeDescriptorModel, Boolean> value)
    {
        getPersistenceContext().setPropertyValue("exportAttributeDescriptors", value);
    }


    @Accessor(qualifier = "removeMissingItems", type = Accessor.Type.SETTER)
    public void setRemoveMissingItems(Boolean value)
    {
        getPersistenceContext().setPropertyValue("removeMissingItems", value);
    }


    @Accessor(qualifier = "rootTypes", type = Accessor.Type.SETTER)
    public void setRootTypes(List<ComposedTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("rootTypes", value);
    }


    @Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
    public void setSourceVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("sourceVersion", value);
    }


    @Accessor(qualifier = "syncAttributeConfigurations", type = Accessor.Type.SETTER)
    public void setSyncAttributeConfigurations(Collection<SyncAttributeDescriptorConfigModel> value)
    {
        getPersistenceContext().setPropertyValue("syncAttributeConfigurations", value);
    }


    @Accessor(qualifier = "syncLanguages", type = Accessor.Type.SETTER)
    public void setSyncLanguages(Set<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("syncLanguages", value);
    }


    @Accessor(qualifier = "syncOrder", type = Accessor.Type.SETTER)
    public void setSyncOrder(Integer value)
    {
        getPersistenceContext().setPropertyValue("syncOrder", value);
    }


    @Accessor(qualifier = "syncPrincipals", type = Accessor.Type.SETTER)
    public void setSyncPrincipals(List<PrincipalModel> value)
    {
        getPersistenceContext().setPropertyValue("syncPrincipals", value);
    }


    @Accessor(qualifier = "syncPrincipalsOnly", type = Accessor.Type.SETTER)
    public void setSyncPrincipalsOnly(Boolean value)
    {
        getPersistenceContext().setPropertyValue("syncPrincipalsOnly", value);
    }


    @Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
    public void setTargetVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("targetVersion", value);
    }
}
