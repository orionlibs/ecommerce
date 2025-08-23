package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexDao;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrIndexNotFoundException;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSolrIndexService implements SolrIndexService
{
    protected static final String FACET_SEARCH_CONFIG_PARAM = "facetSearchConfig";
    protected static final String INDEXED_TYPE_PARAM = "indexedType";
    protected static final String QUALIFIER_PARAM = "qualifier";
    protected static final String INDEX_PARAM = "index";
    private SolrIndexDao solrIndexDao;
    private SolrFacetSearchConfigDao solrFacetSearchConfigDao;
    private ModelService modelService;
    private TimeService timeService;


    public SolrIndexModel createIndex(String facetSearchConfig, String indexedType, String qualifier) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("facetSearchConfig", facetSearchConfig);
        ServicesUtil.validateParameterNotNullStandardMessage("indexedType", indexedType);
        ServicesUtil.validateParameterNotNullStandardMessage("qualifier", qualifier);
        return doInTxWithOptimisticLocking(() -> {
            SolrFacetSearchConfigModel facetSearchConfigModel = findFacetSearchConfig(facetSearchConfig);
            SolrIndexedTypeModel indexedTypeModel = findIndexedType(facetSearchConfigModel, indexedType);
            return createIndex(facetSearchConfigModel, indexedTypeModel, qualifier);
        });
    }


    public List<SolrIndexModel> getAllIndexes() throws SolrServiceException
    {
        return this.solrIndexDao.findAllIndexes();
    }


    public List<SolrIndexModel> getIndexesForConfigAndType(String facetSearchConfig, String indexedType) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("facetSearchConfig", facetSearchConfig);
        ServicesUtil.validateParameterNotNullStandardMessage("indexedType", indexedType);
        SolrFacetSearchConfigModel facetSearchConfigModel = findFacetSearchConfig(facetSearchConfig);
        SolrIndexedTypeModel indexedTypeModel = findIndexedType(facetSearchConfigModel, indexedType);
        return this.solrIndexDao.findIndexesByConfigAndType(facetSearchConfigModel, indexedTypeModel);
    }


    public SolrIndexModel getIndex(String facetSearchConfig, String indexedType, String qualifier) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("facetSearchConfig", facetSearchConfig);
        ServicesUtil.validateParameterNotNullStandardMessage("indexedType", indexedType);
        ServicesUtil.validateParameterNotNullStandardMessage("qualifier", qualifier);
        SolrFacetSearchConfigModel facetSearchConfigModel = findFacetSearchConfig(facetSearchConfig);
        SolrIndexedTypeModel indexedTypeModel = findIndexedType(facetSearchConfigModel, indexedType);
        return findIndex(facetSearchConfigModel, indexedTypeModel, qualifier);
    }


    public SolrIndexModel getOrCreateIndex(String facetSearchConfig, String indexedType, String qualifier) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("facetSearchConfig", facetSearchConfig);
        ServicesUtil.validateParameterNotNullStandardMessage("indexedType", indexedType);
        ServicesUtil.validateParameterNotNullStandardMessage("qualifier", qualifier);
        return doInTxWithOptimisticLocking(() -> {
            SolrFacetSearchConfigModel facetSearchConfigModel = findFacetSearchConfig(facetSearchConfig);
            SolrIndexedTypeModel indexedTypeModel = findIndexedType(facetSearchConfigModel, indexedType);
            SolrIndexModel indexModel = null;
            try
            {
                indexModel = this.solrIndexDao.findIndexByConfigAndTypeAndQualifier(facetSearchConfigModel, indexedTypeModel, qualifier);
            }
            catch(UnknownIdentifierException e)
            {
                indexModel = createIndex(facetSearchConfigModel, indexedTypeModel, qualifier);
            }
            return indexModel;
        });
    }


    public void deleteIndex(String facetSearchConfig, String indexedType, String qualifier) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("facetSearchConfig", facetSearchConfig);
        ServicesUtil.validateParameterNotNullStandardMessage("indexedType", indexedType);
        ServicesUtil.validateParameterNotNullStandardMessage("qualifier", qualifier);
        doInTxWithOptimisticLocking(() -> {
            SolrFacetSearchConfigModel facetSearchConfigModel = findFacetSearchConfig(facetSearchConfig);
            SolrIndexedTypeModel indexedTypeModel = findIndexedType(facetSearchConfigModel, indexedType);
            SolrIndexModel index = findIndex(facetSearchConfigModel, indexedTypeModel, qualifier);
            try
            {
                this.modelService.remove(index);
            }
            catch(ModelRemovalException e)
            {
                throw new SolrServiceException(e);
            }
            return null;
        });
    }


    public SolrIndexModel activateIndex(String facetSearchConfig, String indexedType, String qualifier) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("facetSearchConfig", facetSearchConfig);
        ServicesUtil.validateParameterNotNullStandardMessage("indexedType", indexedType);
        ServicesUtil.validateParameterNotNullStandardMessage("qualifier", qualifier);
        return doInTxWithOptimisticLocking(() -> doActivateIndex(facetSearchConfig, indexedType, qualifier));
    }


    protected SolrIndexModel doActivateIndex(String facetSearchConfig, String indexedType, String qualifier) throws SolrServiceException
    {
        SolrFacetSearchConfigModel facetSearchConfigModel = findFacetSearchConfig(facetSearchConfig);
        SolrIndexedTypeModel indexedTypeModel = findIndexedType(facetSearchConfigModel, indexedType);
        SolrIndexModel activeIndex = null;
        List<SolrIndexModel> indexes = this.solrIndexDao.findIndexesByConfigAndType(facetSearchConfigModel, indexedTypeModel);
        for(SolrIndexModel index : indexes)
        {
            if(Objects.equals(index.getQualifier(), qualifier))
            {
                index.setActive(true);
                activeIndex = index;
                continue;
            }
            index.setActive(false);
        }
        if(activeIndex == null)
        {
            throw new SolrIndexNotFoundException("index not found: facetSearchConfig=" + facetSearchConfig + ", indexedType=" + indexedType + ", qualifier=" + qualifier);
        }
        try
        {
            this.modelService.saveAll(indexes);
        }
        catch(ModelSavingException e)
        {
            throw new SolrServiceException(e);
        }
        return activeIndex;
    }


    public SolrIndexModel getActiveIndex(String facetSearchConfig, String indexedType) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("facetSearchConfig", facetSearchConfig);
        ServicesUtil.validateParameterNotNullStandardMessage("indexedType", indexedType);
        SolrFacetSearchConfigModel facetSearchConfigModel = findFacetSearchConfig(facetSearchConfig);
        SolrIndexedTypeModel indexedTypeModel = findIndexedType(facetSearchConfigModel, indexedType);
        try
        {
            return this.solrIndexDao.findActiveIndexByConfigAndType(facetSearchConfigModel, indexedTypeModel);
        }
        catch(UnknownIdentifierException e)
        {
            throw new SolrIndexNotFoundException(e);
        }
    }


    protected SolrFacetSearchConfigModel findFacetSearchConfig(String facetSearchConfig) throws SolrServiceException
    {
        try
        {
            return this.solrFacetSearchConfigDao.findFacetSearchConfigByName(facetSearchConfig);
        }
        catch(UnknownIdentifierException e)
        {
            throw new SolrServiceException("Facet search config not found: facetSearchConfig=" + facetSearchConfig, e);
        }
    }


    protected SolrIndexedTypeModel findIndexedType(SolrFacetSearchConfigModel facetSearchConfigModel, String indexedType) throws SolrServiceException
    {
        List<SolrIndexedTypeModel> indexedTypeModels = facetSearchConfigModel.getSolrIndexedTypes();
        if(CollectionUtils.isNotEmpty(indexedTypeModels))
        {
            for(SolrIndexedTypeModel indexedTypeModel : indexedTypeModels)
            {
                if(Objects.equals(indexedTypeModel.getIdentifier(), indexedType))
                {
                    return indexedTypeModel;
                }
            }
        }
        throw new SolrServiceException("Indexed type not found: facetSearchConfig=" + facetSearchConfigModel
                        .getName() + ", indexedType=" + indexedType);
    }


    protected SolrIndexModel findIndex(SolrFacetSearchConfigModel facetSearchConfig, SolrIndexedTypeModel indexedType, String qualifier) throws SolrServiceException
    {
        try
        {
            return this.solrIndexDao.findIndexByConfigAndTypeAndQualifier(facetSearchConfig, indexedType, qualifier);
        }
        catch(UnknownIdentifierException e)
        {
            throw new SolrIndexNotFoundException("index not found: facetSearchConfig=" + facetSearchConfig + ", indexedType=" + indexedType + ", qualifier=" + qualifier, e);
        }
    }


    protected SolrIndexModel createIndex(SolrFacetSearchConfigModel facetSearchConfig, SolrIndexedTypeModel indexedType, String qualifier) throws SolrServiceException
    {
        SolrIndexModel indexModel = (SolrIndexModel)this.modelService.create(SolrIndexModel.class);
        indexModel.setFacetSearchConfig(facetSearchConfig);
        indexModel.setIndexedType(indexedType);
        indexModel.setQualifier(qualifier);
        try
        {
            this.modelService.save(indexModel);
        }
        catch(ModelSavingException e)
        {
            throw new SolrServiceException(e);
        }
        return indexModel;
    }


    protected <T> T doInTxWithOptimisticLocking(ExecutionBody<T, SolrServiceException> action) throws SolrServiceException
    {
        Objects.requireNonNull(action, "action must not be null");
        try
        {
            return (T)Transaction.current().execute((TransactionBody)new Object(this, action));
        }
        catch(SolrServiceException | RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new SolrServiceException(e);
        }
    }


    public SolrIndexDao getSolrIndexDao()
    {
        return this.solrIndexDao;
    }


    @Required
    public void setSolrIndexDao(SolrIndexDao solrIndexDao)
    {
        this.solrIndexDao = solrIndexDao;
    }


    public SolrFacetSearchConfigDao getSolrFacetSearchConfigDao()
    {
        return this.solrFacetSearchConfigDao;
    }


    @Required
    public void setSolrFacetSearchConfigDao(SolrFacetSearchConfigDao solrFacetSearchConfigDao)
    {
        this.solrFacetSearchConfigDao = solrFacetSearchConfigDao;
    }


    public TimeService getTimeService()
    {
        return this.timeService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }
}
