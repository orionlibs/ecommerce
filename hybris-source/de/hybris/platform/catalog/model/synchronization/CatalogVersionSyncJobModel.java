package de.hybris.platform.catalog.model.synchronization;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class CatalogVersionSyncJobModel extends SyncItemJobModel
{
    public static final String _TYPECODE = "CatalogVersionSyncJob";
    public static final String _DEPENDENTCATALOGVERSIONSYNCJOBRELATION = "DependentCatalogVersionSyncJobRelation";
    public static final String COPYCACHESIZE = "copyCacheSize";
    public static final String ENABLETRANSACTIONS = "enableTransactions";
    public static final String MAXTHREADS = "maxThreads";
    public static final String MAXSCHEDULERTHREADS = "maxSchedulerThreads";
    public static final String DEPENDENTSYNCJOBS = "dependentSyncJobs";
    public static final String DEPENDSONSYNCJOBS = "dependsOnSyncJobs";


    public CatalogVersionSyncJobModel()
    {
    }


    public CatalogVersionSyncJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionSyncJobModel(String _code, CatalogVersionModel _sourceVersion, CatalogVersionModel _targetVersion)
    {
        setCode(_code);
        setSourceVersion(_sourceVersion);
        setTargetVersion(_targetVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionSyncJobModel(String _code, Integer _nodeID, ItemModel _owner, CatalogVersionModel _sourceVersion, CatalogVersionModel _targetVersion)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSourceVersion(_sourceVersion);
        setTargetVersion(_targetVersion);
    }


    @Accessor(qualifier = "copyCacheSize", type = Accessor.Type.GETTER)
    public Integer getCopyCacheSize()
    {
        return (Integer)getPersistenceContext().getPropertyValue("copyCacheSize");
    }


    @Accessor(qualifier = "dependentSyncJobs", type = Accessor.Type.GETTER)
    public Set<CatalogVersionSyncJobModel> getDependentSyncJobs()
    {
        return (Set<CatalogVersionSyncJobModel>)getPersistenceContext().getPropertyValue("dependentSyncJobs");
    }


    @Accessor(qualifier = "dependsOnSyncJobs", type = Accessor.Type.GETTER)
    public Set<CatalogVersionSyncJobModel> getDependsOnSyncJobs()
    {
        return (Set<CatalogVersionSyncJobModel>)getPersistenceContext().getPropertyValue("dependsOnSyncJobs");
    }


    @Accessor(qualifier = "enableTransactions", type = Accessor.Type.GETTER)
    public Boolean getEnableTransactions()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("enableTransactions");
    }


    @Accessor(qualifier = "maxSchedulerThreads", type = Accessor.Type.GETTER)
    public Integer getMaxSchedulerThreads()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxSchedulerThreads");
    }


    @Accessor(qualifier = "maxThreads", type = Accessor.Type.GETTER)
    public Integer getMaxThreads()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxThreads");
    }


    @Accessor(qualifier = "copyCacheSize", type = Accessor.Type.SETTER)
    public void setCopyCacheSize(Integer value)
    {
        getPersistenceContext().setPropertyValue("copyCacheSize", value);
    }


    @Accessor(qualifier = "dependentSyncJobs", type = Accessor.Type.SETTER)
    public void setDependentSyncJobs(Set<CatalogVersionSyncJobModel> value)
    {
        getPersistenceContext().setPropertyValue("dependentSyncJobs", value);
    }


    @Accessor(qualifier = "dependsOnSyncJobs", type = Accessor.Type.SETTER)
    public void setDependsOnSyncJobs(Set<CatalogVersionSyncJobModel> value)
    {
        getPersistenceContext().setPropertyValue("dependsOnSyncJobs", value);
    }


    @Accessor(qualifier = "enableTransactions", type = Accessor.Type.SETTER)
    public void setEnableTransactions(Boolean value)
    {
        getPersistenceContext().setPropertyValue("enableTransactions", value);
    }


    @Accessor(qualifier = "maxSchedulerThreads", type = Accessor.Type.SETTER)
    public void setMaxSchedulerThreads(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxSchedulerThreads", value);
    }


    @Accessor(qualifier = "maxThreads", type = Accessor.Type.SETTER)
    public void setMaxThreads(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxThreads", value);
    }
}
