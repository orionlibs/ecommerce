package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.SyncAttributeDescriptorConfig;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractCatalogVersionSyncMaster
{
    private final CatalogVersionSyncJob job;
    private final CatalogVersionSyncCronJob cronJob;
    private static final String NO_CV_ATTRIBUTE = "<...>";
    private final ThreadsafeLongToObjectMap<Collection<String>> catalogRequiredAttributesCache = new ThreadsafeLongToObjectMap();
    private final ThreadsafeLongToObjectMap<Set<String>> dontCopyCache = new ThreadsafeLongToObjectMap();
    private final ThreadsafeLongToObjectMap<String> catalogVersionAttributeCache = new ThreadsafeLongToObjectMap();
    private final ThreadsafeLongToObjectMap<SyncAttributeDescriptorData> _attribute2Configurations = new ThreadsafeLongToObjectMap();
    private final ThreadsafeLongToObjectMap<Collection<SyncAttributeDescriptorData>> _type2Configurations = new ThreadsafeLongToObjectMap();
    private CatalogManager catalogManager;


    public AbstractCatalogVersionSyncMaster(CatalogVersionSyncJob job, CatalogVersionSyncCronJob cronJob)
    {
        this.job = job;
        this.cronJob = cronJob;
    }


    public final CatalogVersionSyncJob getJob()
    {
        return this.job;
    }


    public final CatalogVersionSyncCronJob getCronJob()
    {
        return this.cronJob;
    }


    protected CatalogManager getCatalogManager()
    {
        if(this.catalogManager == null)
        {
            this.catalogManager = CatalogManager.getInstance();
        }
        return this.catalogManager;
    }


    protected String getCatalogVersionAttribute(ComposedType composedType)
    {
        long key = composedType.getPK().getLongValue();
        return (String)this.catalogVersionAttributeCache.getOrPutIfDoesntExist(key, (ValueProvider)new Object(this, composedType));
    }


    private final SyncAttributeDescriptorConfig findConfig(AttributeDescriptor attributeDescriptor)
    {
        Map<String, Object> params = new HashMap<>(3);
        params.put("job", getJob());
        params.put("ad", attributeDescriptor.isInherited() ?
                        attributeDescriptor.getDeclaringEnclosingType().getAttributeDescriptorIncludingPrivate(attributeDescriptor
                                        .getQualifier()) : attributeDescriptor);
        List<SyncAttributeDescriptorConfig> matches = FlexibleSearch.getInstance().search(null, "SELECT {PK} FROM {" + GeneratedCatalogConstants.TC.SYNCATTRIBUTEDESCRIPTORCONFIG + "*} WHERE {syncJob}=?job AND {attributeDescriptor}=?ad", params, SyncAttributeDescriptorConfig.class).getResult();
        return matches.isEmpty() ? null : matches.get(0);
    }


    protected SyncAttributeDescriptorData getAttributeConfig(AttributeDescriptor attributeDescriptor)
    {
        long key = attributeDescriptor.getPK().getLongValue();
        return (SyncAttributeDescriptorData)this._attribute2Configurations.getOrPutIfDoesntExist(key, (ValueProvider)new Object(this, attributeDescriptor));
    }


    protected Collection<SyncAttributeDescriptorData> getAllAttributeConfig(ComposedType type)
    {
        long key = type.getPK().getLongValue();
        return (Collection<SyncAttributeDescriptorData>)this._type2Configurations.getOrPutIfDoesntExist(key, (ValueProvider)new Object(this, type));
    }


    private boolean canSelectAttribute(AttributeDescriptor attributeDescriptor)
    {
        if(!attributeDescriptor.isInitial() && !attributeDescriptor.isWritable())
        {
            return false;
        }
        String qualifier = attributeDescriptor.getDeclaringEnclosingType().getCode() + "." + attributeDescriptor.getDeclaringEnclosingType().getCode();
        if(SyncItemJob.HIDDEN_ATTRIBUTES.contains(qualifier))
        {
            return false;
        }
        return true;
    }


    final Set<String> getDontCopySet(ComposedType composedType)
    {
        long key = composedType.getPK().getLongValue();
        return (Set<String>)this.dontCopyCache.getOrPutIfDoesntExist(key, (ValueProvider)new Object(this, composedType));
    }


    protected Collection<String> getCatalogRequiredAttributes(ComposedType myType)
    {
        long key = myType.getPK().getLongValue();
        return (Collection<String>)this.catalogRequiredAttributesCache.getOrPutIfDoesntExist(key, (ValueProvider)new Object(this, myType));
    }
}
