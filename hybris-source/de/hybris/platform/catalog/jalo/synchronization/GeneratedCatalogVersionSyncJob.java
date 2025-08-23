package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCatalogVersionSyncJob extends SyncItemJob
{
    public static final String COPYCACHESIZE = "copyCacheSize";
    public static final String ENABLETRANSACTIONS = "enableTransactions";
    public static final String MAXTHREADS = "maxThreads";
    public static final String MAXSCHEDULERTHREADS = "maxSchedulerThreads";
    public static final String DEPENDENTSYNCJOBS = "dependentSyncJobs";
    protected static String DEPENDENTCATALOGVERSIONSYNCJOBRELATION_SRC_ORDERED = "relation.DependentCatalogVersionSyncJobRelation.source.ordered";
    protected static String DEPENDENTCATALOGVERSIONSYNCJOBRELATION_TGT_ORDERED = "relation.DependentCatalogVersionSyncJobRelation.target.ordered";
    protected static String DEPENDENTCATALOGVERSIONSYNCJOBRELATION_MARKMODIFIED = "relation.DependentCatalogVersionSyncJobRelation.markmodified";
    public static final String DEPENDSONSYNCJOBS = "dependsOnSyncJobs";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SyncItemJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("copyCacheSize", Item.AttributeMode.INITIAL);
        tmp.put("enableTransactions", Item.AttributeMode.INITIAL);
        tmp.put("maxThreads", Item.AttributeMode.INITIAL);
        tmp.put("maxSchedulerThreads", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getCopyCacheSize(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "copyCacheSize");
    }


    public Integer getCopyCacheSize()
    {
        return getCopyCacheSize(getSession().getSessionContext());
    }


    public int getCopyCacheSizeAsPrimitive(SessionContext ctx)
    {
        Integer value = getCopyCacheSize(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getCopyCacheSizeAsPrimitive()
    {
        return getCopyCacheSizeAsPrimitive(getSession().getSessionContext());
    }


    public void setCopyCacheSize(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "copyCacheSize", value);
    }


    public void setCopyCacheSize(Integer value)
    {
        setCopyCacheSize(getSession().getSessionContext(), value);
    }


    public void setCopyCacheSize(SessionContext ctx, int value)
    {
        setCopyCacheSize(ctx, Integer.valueOf(value));
    }


    public void setCopyCacheSize(int value)
    {
        setCopyCacheSize(getSession().getSessionContext(), value);
    }


    public Set<CatalogVersionSyncJob> getDependentSyncJobs(SessionContext ctx)
    {
        List<CatalogVersionSyncJob> items = getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.DEPENDENTCATALOGVERSIONSYNCJOBRELATION, "CatalogVersionSyncJob", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<CatalogVersionSyncJob> getDependentSyncJobs()
    {
        return getDependentSyncJobs(getSession().getSessionContext());
    }


    public long getDependentSyncJobsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.DEPENDENTCATALOGVERSIONSYNCJOBRELATION, "CatalogVersionSyncJob", null);
    }


    public long getDependentSyncJobsCount()
    {
        return getDependentSyncJobsCount(getSession().getSessionContext());
    }


    public void setDependentSyncJobs(SessionContext ctx, Set<CatalogVersionSyncJob> value)
    {
        setLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.DEPENDENTCATALOGVERSIONSYNCJOBRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(DEPENDENTCATALOGVERSIONSYNCJOBRELATION_MARKMODIFIED));
    }


    public void setDependentSyncJobs(Set<CatalogVersionSyncJob> value)
    {
        setDependentSyncJobs(getSession().getSessionContext(), value);
    }


    public void addToDependentSyncJobs(SessionContext ctx, CatalogVersionSyncJob value)
    {
        addLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.DEPENDENTCATALOGVERSIONSYNCJOBRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(DEPENDENTCATALOGVERSIONSYNCJOBRELATION_MARKMODIFIED));
    }


    public void addToDependentSyncJobs(CatalogVersionSyncJob value)
    {
        addToDependentSyncJobs(getSession().getSessionContext(), value);
    }


    public void removeFromDependentSyncJobs(SessionContext ctx, CatalogVersionSyncJob value)
    {
        removeLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.DEPENDENTCATALOGVERSIONSYNCJOBRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(DEPENDENTCATALOGVERSIONSYNCJOBRELATION_MARKMODIFIED));
    }


    public void removeFromDependentSyncJobs(CatalogVersionSyncJob value)
    {
        removeFromDependentSyncJobs(getSession().getSessionContext(), value);
    }


    public Set<CatalogVersionSyncJob> getDependsOnSyncJobs(SessionContext ctx)
    {
        List<CatalogVersionSyncJob> items = getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.DEPENDENTCATALOGVERSIONSYNCJOBRELATION, "CatalogVersionSyncJob", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<CatalogVersionSyncJob> getDependsOnSyncJobs()
    {
        return getDependsOnSyncJobs(getSession().getSessionContext());
    }


    public long getDependsOnSyncJobsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.DEPENDENTCATALOGVERSIONSYNCJOBRELATION, "CatalogVersionSyncJob", null);
    }


    public long getDependsOnSyncJobsCount()
    {
        return getDependsOnSyncJobsCount(getSession().getSessionContext());
    }


    public void setDependsOnSyncJobs(SessionContext ctx, Set<CatalogVersionSyncJob> value)
    {
        setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.DEPENDENTCATALOGVERSIONSYNCJOBRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(DEPENDENTCATALOGVERSIONSYNCJOBRELATION_MARKMODIFIED));
    }


    public void setDependsOnSyncJobs(Set<CatalogVersionSyncJob> value)
    {
        setDependsOnSyncJobs(getSession().getSessionContext(), value);
    }


    public void addToDependsOnSyncJobs(SessionContext ctx, CatalogVersionSyncJob value)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.DEPENDENTCATALOGVERSIONSYNCJOBRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(DEPENDENTCATALOGVERSIONSYNCJOBRELATION_MARKMODIFIED));
    }


    public void addToDependsOnSyncJobs(CatalogVersionSyncJob value)
    {
        addToDependsOnSyncJobs(getSession().getSessionContext(), value);
    }


    public void removeFromDependsOnSyncJobs(SessionContext ctx, CatalogVersionSyncJob value)
    {
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.DEPENDENTCATALOGVERSIONSYNCJOBRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(DEPENDENTCATALOGVERSIONSYNCJOBRELATION_MARKMODIFIED));
    }


    public void removeFromDependsOnSyncJobs(CatalogVersionSyncJob value)
    {
        removeFromDependsOnSyncJobs(getSession().getSessionContext(), value);
    }


    public Boolean isEnableTransactions(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "enableTransactions");
    }


    public Boolean isEnableTransactions()
    {
        return isEnableTransactions(getSession().getSessionContext());
    }


    public boolean isEnableTransactionsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEnableTransactions(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnableTransactionsAsPrimitive()
    {
        return isEnableTransactionsAsPrimitive(getSession().getSessionContext());
    }


    public void setEnableTransactions(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "enableTransactions", value);
    }


    public void setEnableTransactions(Boolean value)
    {
        setEnableTransactions(getSession().getSessionContext(), value);
    }


    public void setEnableTransactions(SessionContext ctx, boolean value)
    {
        setEnableTransactions(ctx, Boolean.valueOf(value));
    }


    public void setEnableTransactions(boolean value)
    {
        setEnableTransactions(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CatalogVersionSyncJob");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(DEPENDENTCATALOGVERSIONSYNCJOBRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Integer getMaxSchedulerThreads(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxSchedulerThreads");
    }


    public Integer getMaxSchedulerThreads()
    {
        return getMaxSchedulerThreads(getSession().getSessionContext());
    }


    public int getMaxSchedulerThreadsAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxSchedulerThreads(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxSchedulerThreadsAsPrimitive()
    {
        return getMaxSchedulerThreadsAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxSchedulerThreads(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxSchedulerThreads", value);
    }


    public void setMaxSchedulerThreads(Integer value)
    {
        setMaxSchedulerThreads(getSession().getSessionContext(), value);
    }


    public void setMaxSchedulerThreads(SessionContext ctx, int value)
    {
        setMaxSchedulerThreads(ctx, Integer.valueOf(value));
    }


    public void setMaxSchedulerThreads(int value)
    {
        setMaxSchedulerThreads(getSession().getSessionContext(), value);
    }


    public Integer getMaxThreads(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxThreads");
    }


    public Integer getMaxThreads()
    {
        return getMaxThreads(getSession().getSessionContext());
    }


    public int getMaxThreadsAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxThreads(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxThreadsAsPrimitive()
    {
        return getMaxThreadsAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxThreads(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxThreads", value);
    }


    public void setMaxThreads(Integer value)
    {
        setMaxThreads(getSession().getSessionContext(), value);
    }


    public void setMaxThreads(SessionContext ctx, int value)
    {
        setMaxThreads(ctx, Integer.valueOf(value));
    }


    public void setMaxThreads(int value)
    {
        setMaxThreads(getSession().getSessionContext(), value);
    }
}
