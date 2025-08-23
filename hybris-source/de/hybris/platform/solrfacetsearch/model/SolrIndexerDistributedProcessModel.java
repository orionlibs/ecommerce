package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.SimpleDistributedProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import java.util.Collection;
import java.util.Map;

public class SolrIndexerDistributedProcessModel extends SimpleDistributedProcessModel
{
    public static final String _TYPECODE = "SolrIndexerDistributedProcess";
    public static final String SESSIONUSER = "sessionUser";
    public static final String SESSIONLANGUAGE = "sessionLanguage";
    public static final String SESSIONCURRENCY = "sessionCurrency";
    public static final String INDEXOPERATIONID = "indexOperationId";
    public static final String INDEXOPERATION = "indexOperation";
    public static final String EXTERNALINDEXOPERATION = "externalIndexOperation";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";
    public static final String INDEXEDTYPE = "indexedType";
    public static final String INDEXEDPROPERTIES = "indexedProperties";
    public static final String INDEX = "index";
    public static final String INDEXERHINTS = "indexerHints";


    public SolrIndexerDistributedProcessModel()
    {
    }


    public SolrIndexerDistributedProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerDistributedProcessModel(String _code, String _currentExecutionId, DistributedProcessState _state)
    {
        setCode(_code);
        setCurrentExecutionId(_currentExecutionId);
        setState(_state);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerDistributedProcessModel(int _batchSize, String _code, String _currentExecutionId, String _handlerBeanId, String _nodeGroup, ItemModel _owner, DistributedProcessState _state)
    {
        setBatchSize(_batchSize);
        setCode(_code);
        setCurrentExecutionId(_currentExecutionId);
        setHandlerBeanId(_handlerBeanId);
        setNodeGroup(_nodeGroup);
        setOwner(_owner);
        setState(_state);
    }


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.GETTER)
    public String getFacetSearchConfig()
    {
        return (String)getPersistenceContext().getPropertyValue("facetSearchConfig");
    }


    @Accessor(qualifier = "index", type = Accessor.Type.GETTER)
    public String getIndex()
    {
        return (String)getPersistenceContext().getPropertyValue("index");
    }


    @Accessor(qualifier = "indexedProperties", type = Accessor.Type.GETTER)
    public Collection<String> getIndexedProperties()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("indexedProperties");
    }


    @Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
    public String getIndexedType()
    {
        return (String)getPersistenceContext().getPropertyValue("indexedType");
    }


    @Accessor(qualifier = "indexerHints", type = Accessor.Type.GETTER)
    public Map<String, String> getIndexerHints()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("indexerHints");
    }


    @Accessor(qualifier = "indexOperation", type = Accessor.Type.GETTER)
    public IndexerOperationValues getIndexOperation()
    {
        return (IndexerOperationValues)getPersistenceContext().getPropertyValue("indexOperation");
    }


    @Accessor(qualifier = "indexOperationId", type = Accessor.Type.GETTER)
    public long getIndexOperationId()
    {
        return toPrimitive((Long)getPersistenceContext().getPropertyValue("indexOperationId"));
    }


    @Accessor(qualifier = "sessionCurrency", type = Accessor.Type.GETTER)
    public String getSessionCurrency()
    {
        return (String)getPersistenceContext().getPropertyValue("sessionCurrency");
    }


    @Accessor(qualifier = "sessionLanguage", type = Accessor.Type.GETTER)
    public String getSessionLanguage()
    {
        return (String)getPersistenceContext().getPropertyValue("sessionLanguage");
    }


    @Accessor(qualifier = "sessionUser", type = Accessor.Type.GETTER)
    public String getSessionUser()
    {
        return (String)getPersistenceContext().getPropertyValue("sessionUser");
    }


    @Accessor(qualifier = "externalIndexOperation", type = Accessor.Type.GETTER)
    public boolean isExternalIndexOperation()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("externalIndexOperation"));
    }


    @Accessor(qualifier = "externalIndexOperation", type = Accessor.Type.SETTER)
    public void setExternalIndexOperation(boolean value)
    {
        getPersistenceContext().setPropertyValue("externalIndexOperation", toObject(value));
    }


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.SETTER)
    public void setFacetSearchConfig(String value)
    {
        getPersistenceContext().setPropertyValue("facetSearchConfig", value);
    }


    @Accessor(qualifier = "index", type = Accessor.Type.SETTER)
    public void setIndex(String value)
    {
        getPersistenceContext().setPropertyValue("index", value);
    }


    @Accessor(qualifier = "indexedProperties", type = Accessor.Type.SETTER)
    public void setIndexedProperties(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("indexedProperties", value);
    }


    @Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
    public void setIndexedType(String value)
    {
        getPersistenceContext().setPropertyValue("indexedType", value);
    }


    @Accessor(qualifier = "indexerHints", type = Accessor.Type.SETTER)
    public void setIndexerHints(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("indexerHints", value);
    }


    @Accessor(qualifier = "indexOperation", type = Accessor.Type.SETTER)
    public void setIndexOperation(IndexerOperationValues value)
    {
        getPersistenceContext().setPropertyValue("indexOperation", value);
    }


    @Accessor(qualifier = "indexOperationId", type = Accessor.Type.SETTER)
    public void setIndexOperationId(long value)
    {
        getPersistenceContext().setPropertyValue("indexOperationId", toObject(value));
    }


    @Accessor(qualifier = "sessionCurrency", type = Accessor.Type.SETTER)
    public void setSessionCurrency(String value)
    {
        getPersistenceContext().setPropertyValue("sessionCurrency", value);
    }


    @Accessor(qualifier = "sessionLanguage", type = Accessor.Type.SETTER)
    public void setSessionLanguage(String value)
    {
        getPersistenceContext().setPropertyValue("sessionLanguage", value);
    }


    @Accessor(qualifier = "sessionUser", type = Accessor.Type.SETTER)
    public void setSessionUser(String value)
    {
        getPersistenceContext().setPropertyValue("sessionUser", value);
    }
}
