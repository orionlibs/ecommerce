package de.hybris.platform.solrfacetsearch.indexer.workers;

import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class IndexerWorkerParameters implements Serializable
{
    private static final long serialVersionUID = 1L;
    private long workerNumber;
    private String tenant;
    private String sessionUser;
    private String sessionLanguage;
    private String sessionCurrency;
    private boolean sessionUseReadOnlyDataSource;
    private long indexOperationId;
    private IndexOperation indexOperation;
    private boolean externalIndexOperation;
    private String facetSearchConfig;
    private String indexedType;
    private Collection<String> indexedProperties;
    private List<PK> pks;
    private String index;
    private Map<String, String> indexerHints;


    public void setWorkerNumber(long workerNumber)
    {
        this.workerNumber = workerNumber;
    }


    public long getWorkerNumber()
    {
        return this.workerNumber;
    }


    public void setTenant(String tenant)
    {
        this.tenant = tenant;
    }


    public String getTenant()
    {
        return this.tenant;
    }


    public void setSessionUser(String sessionUser)
    {
        this.sessionUser = sessionUser;
    }


    public String getSessionUser()
    {
        return this.sessionUser;
    }


    public void setSessionLanguage(String sessionLanguage)
    {
        this.sessionLanguage = sessionLanguage;
    }


    public String getSessionLanguage()
    {
        return this.sessionLanguage;
    }


    public void setSessionCurrency(String sessionCurrency)
    {
        this.sessionCurrency = sessionCurrency;
    }


    public String getSessionCurrency()
    {
        return this.sessionCurrency;
    }


    public void setSessionUseReadOnlyDataSource(boolean sessionUseReadOnlyDataSource)
    {
        this.sessionUseReadOnlyDataSource = sessionUseReadOnlyDataSource;
    }


    public boolean isSessionUseReadOnlyDataSource()
    {
        return this.sessionUseReadOnlyDataSource;
    }


    public void setIndexOperationId(long indexOperationId)
    {
        this.indexOperationId = indexOperationId;
    }


    public long getIndexOperationId()
    {
        return this.indexOperationId;
    }


    public void setIndexOperation(IndexOperation indexOperation)
    {
        this.indexOperation = indexOperation;
    }


    public IndexOperation getIndexOperation()
    {
        return this.indexOperation;
    }


    public void setExternalIndexOperation(boolean externalIndexOperation)
    {
        this.externalIndexOperation = externalIndexOperation;
    }


    public boolean isExternalIndexOperation()
    {
        return this.externalIndexOperation;
    }


    public void setFacetSearchConfig(String facetSearchConfig)
    {
        this.facetSearchConfig = facetSearchConfig;
    }


    public String getFacetSearchConfig()
    {
        return this.facetSearchConfig;
    }


    public void setIndexedType(String indexedType)
    {
        this.indexedType = indexedType;
    }


    public String getIndexedType()
    {
        return this.indexedType;
    }


    public void setIndexedProperties(Collection<String> indexedProperties)
    {
        this.indexedProperties = indexedProperties;
    }


    public Collection<String> getIndexedProperties()
    {
        return this.indexedProperties;
    }


    public void setPks(List<PK> pks)
    {
        this.pks = pks;
    }


    public List<PK> getPks()
    {
        return this.pks;
    }


    public void setIndex(String index)
    {
        this.index = index;
    }


    public String getIndex()
    {
        return this.index;
    }


    public void setIndexerHints(Map<String, String> indexerHints)
    {
        this.indexerHints = indexerHints;
    }


    public Map<String, String> getIndexerHints()
    {
        return this.indexerHints;
    }
}
