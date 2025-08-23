package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.IndexMode;
import de.hybris.platform.solrfacetsearch.enums.SolrCommitMode;
import de.hybris.platform.solrfacetsearch.enums.SolrOptimizeMode;

public class SolrIndexConfigModel extends ItemModel
{
    public static final String _TYPECODE = "SolrIndexConfig";
    public static final String NAME = "name";
    public static final String BATCHSIZE = "batchSize";
    public static final String EXPORTPATH = "exportPath";
    public static final String NUMBEROFTHREADS = "numberOfThreads";
    public static final String INDEXMODE = "indexMode";
    public static final String COMMITMODE = "commitMode";
    public static final String OPTIMIZEMODE = "optimizeMode";
    public static final String IGNOREERRORS = "ignoreErrors";
    public static final String LEGACYMODE = "legacyMode";
    public static final String MAXRETRIES = "maxRetries";
    public static final String MAXBATCHRETRIES = "maxBatchRetries";
    public static final String DISTRIBUTEDINDEXING = "distributedIndexing";
    public static final String NODEGROUP = "nodeGroup";


    public SolrIndexConfigModel()
    {
    }


    public SolrIndexConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexConfigModel(String _name)
    {
        setName(_name);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexConfigModel(String _name, ItemModel _owner)
    {
        setName(_name);
        setOwner(_owner);
    }


    @Accessor(qualifier = "batchSize", type = Accessor.Type.GETTER)
    public int getBatchSize()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("batchSize"));
    }


    @Accessor(qualifier = "commitMode", type = Accessor.Type.GETTER)
    public SolrCommitMode getCommitMode()
    {
        return (SolrCommitMode)getPersistenceContext().getPropertyValue("commitMode");
    }


    @Accessor(qualifier = "exportPath", type = Accessor.Type.GETTER)
    public String getExportPath()
    {
        return (String)getPersistenceContext().getPropertyValue("exportPath");
    }


    @Accessor(qualifier = "indexMode", type = Accessor.Type.GETTER)
    public IndexMode getIndexMode()
    {
        return (IndexMode)getPersistenceContext().getPropertyValue("indexMode");
    }


    @Accessor(qualifier = "maxBatchRetries", type = Accessor.Type.GETTER)
    public int getMaxBatchRetries()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("maxBatchRetries"));
    }


    @Accessor(qualifier = "maxRetries", type = Accessor.Type.GETTER)
    public int getMaxRetries()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("maxRetries"));
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "nodeGroup", type = Accessor.Type.GETTER)
    public String getNodeGroup()
    {
        return (String)getPersistenceContext().getPropertyValue("nodeGroup");
    }


    @Accessor(qualifier = "numberOfThreads", type = Accessor.Type.GETTER)
    public int getNumberOfThreads()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("numberOfThreads"));
    }


    @Accessor(qualifier = "optimizeMode", type = Accessor.Type.GETTER)
    public SolrOptimizeMode getOptimizeMode()
    {
        return (SolrOptimizeMode)getPersistenceContext().getPropertyValue("optimizeMode");
    }


    @Accessor(qualifier = "distributedIndexing", type = Accessor.Type.GETTER)
    public boolean isDistributedIndexing()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("distributedIndexing"));
    }


    @Accessor(qualifier = "ignoreErrors", type = Accessor.Type.GETTER)
    public boolean isIgnoreErrors()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("ignoreErrors"));
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "legacyMode", type = Accessor.Type.GETTER)
    public boolean isLegacyMode()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("legacyMode"));
    }


    @Accessor(qualifier = "batchSize", type = Accessor.Type.SETTER)
    public void setBatchSize(int value)
    {
        getPersistenceContext().setPropertyValue("batchSize", toObject(value));
    }


    @Accessor(qualifier = "commitMode", type = Accessor.Type.SETTER)
    public void setCommitMode(SolrCommitMode value)
    {
        getPersistenceContext().setPropertyValue("commitMode", value);
    }


    @Accessor(qualifier = "distributedIndexing", type = Accessor.Type.SETTER)
    public void setDistributedIndexing(boolean value)
    {
        getPersistenceContext().setPropertyValue("distributedIndexing", toObject(value));
    }


    @Accessor(qualifier = "exportPath", type = Accessor.Type.SETTER)
    public void setExportPath(String value)
    {
        getPersistenceContext().setPropertyValue("exportPath", value);
    }


    @Accessor(qualifier = "ignoreErrors", type = Accessor.Type.SETTER)
    public void setIgnoreErrors(boolean value)
    {
        getPersistenceContext().setPropertyValue("ignoreErrors", toObject(value));
    }


    @Accessor(qualifier = "indexMode", type = Accessor.Type.SETTER)
    public void setIndexMode(IndexMode value)
    {
        getPersistenceContext().setPropertyValue("indexMode", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "legacyMode", type = Accessor.Type.SETTER)
    public void setLegacyMode(boolean value)
    {
        getPersistenceContext().setPropertyValue("legacyMode", toObject(value));
    }


    @Accessor(qualifier = "maxBatchRetries", type = Accessor.Type.SETTER)
    public void setMaxBatchRetries(int value)
    {
        getPersistenceContext().setPropertyValue("maxBatchRetries", toObject(value));
    }


    @Accessor(qualifier = "maxRetries", type = Accessor.Type.SETTER)
    public void setMaxRetries(int value)
    {
        getPersistenceContext().setPropertyValue("maxRetries", toObject(value));
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "nodeGroup", type = Accessor.Type.SETTER)
    public void setNodeGroup(String value)
    {
        getPersistenceContext().setPropertyValue("nodeGroup", value);
    }


    @Accessor(qualifier = "numberOfThreads", type = Accessor.Type.SETTER)
    public void setNumberOfThreads(int value)
    {
        getPersistenceContext().setPropertyValue("numberOfThreads", toObject(value));
    }


    @Accessor(qualifier = "optimizeMode", type = Accessor.Type.SETTER)
    public void setOptimizeMode(SolrOptimizeMode value)
    {
        getPersistenceContext().setPropertyValue("optimizeMode", value);
    }
}
