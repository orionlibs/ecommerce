package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexOperationDao;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationStatus;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.SolrIndexOperationModel;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexOperationService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrIndexOperationNotFoundException;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSolrIndexOperationService implements SolrIndexOperationService
{
    private SolrIndexOperationDao solrIndexOperationDao;
    private ModelService modelService;
    private TimeService timeService;


    public SolrIndexOperationModel getOperationForId(long id) throws SolrServiceException
    {
        try
        {
            return this.solrIndexOperationDao.findIndexOperationById(id);
        }
        catch(UnknownIdentifierException e)
        {
            throw new SolrIndexOperationNotFoundException("index not found: id=" + id, e);
        }
    }


    public SolrIndexOperationModel startOperation(SolrIndexModel index, long id, IndexOperation operation, boolean external) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        ServicesUtil.validateParameterNotNullStandardMessage("operation", operation);
        SolrIndexOperationModel indexOperationModel = (SolrIndexOperationModel)this.modelService.create(SolrIndexOperationModel.class);
        indexOperationModel.setId(id);
        indexOperationModel.setIndex(index);
        indexOperationModel.setOperation(IndexerOperationValues.valueOf(operation.toString()));
        indexOperationModel.setStatus(IndexerOperationStatus.RUNNING);
        indexOperationModel.setStartTime(this.timeService.getCurrentTime());
        indexOperationModel.setExternal(external);
        try
        {
            this.modelService.save(indexOperationModel);
        }
        catch(ModelSavingException e)
        {
            throw new SolrServiceException(e);
        }
        return indexOperationModel;
    }


    public SolrIndexOperationModel endOperation(long id, boolean indexError) throws SolrServiceException
    {
        SolrIndexOperationModel indexOperation = getOperationForId(id);
        indexOperation.setStatus(indexError ? IndexerOperationStatus.FAILED : IndexerOperationStatus.SUCCESS);
        indexOperation.setEndTime(this.timeService.getCurrentTime());
        try
        {
            this.modelService.save(indexOperation);
        }
        catch(ModelSavingException e)
        {
            throw new SolrServiceException(e);
        }
        return indexOperation;
    }


    public SolrIndexOperationModel cancelOperation(long id) throws SolrServiceException
    {
        SolrIndexOperationModel indexOperation = getOperationForId(id);
        indexOperation.setStatus(IndexerOperationStatus.ABORTED);
        indexOperation.setEndTime(this.timeService.getCurrentTime());
        try
        {
            this.modelService.save(indexOperation);
        }
        catch(ModelSavingException e)
        {
            throw new SolrServiceException(e);
        }
        return indexOperation;
    }


    public Date getLastIndexOperationTime(SolrIndexModel index) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        Optional<SolrIndexOperationModel> operation = this.solrIndexOperationDao.findLastSuccesfulIndexOperation(index);
        if(operation.isPresent())
        {
            return ((SolrIndexOperationModel)operation.get()).getStartTime();
        }
        return new Date(0L);
    }


    public SolrIndexOperationDao getSolrIndexOperationDao()
    {
        return this.solrIndexOperationDao;
    }


    @Required
    public void setSolrIndexOperationDao(SolrIndexOperationDao solrIndexOperationDao)
    {
        this.solrIndexOperationDao = solrIndexOperationDao;
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


    public TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }
}
