package de.hybris.platform.personalizationintegration.strategies.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationintegration.segment.SegmentsProvider;
import de.hybris.platform.personalizationservices.CxUpdateSegmentContext;
import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.segment.dao.CxSegmentDao;
import de.hybris.platform.personalizationservices.strategies.UpdateSegmentStrategy;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DefaultCxUpdateSegmentStrategy extends AbstractCxSegmentStrategy implements UpdateSegmentStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxUpdateSegmentStrategy.class.getName());
    private static final String FAILED_TO_SAVE_MESSAGE = "Failed to save segment with code: ";
    private static final int DEFAULT_BATCH_SIZE = 10;
    private int batchSize = 10;
    private CxSegmentDao cxSegmentDao;
    private ModelService modelService;
    private List<SegmentsProvider> providers;
    private List<Validator> validators;
    private BaseSiteService baseSiteService;


    public DefaultCxUpdateSegmentStrategy()
    {
        this.providers = Collections.emptyList();
    }


    public void updateSegments(CxUpdateSegmentContext context)
    {
        List<SegmentsProvider> updateProviders = getUpdateSegmentsProviders(this.providers, context);
        boolean fullUpdate = (context != null && context.isFullUpdate());
        updateSegments(context, updateProviders, fullUpdate);
    }


    protected void updateSegments(CxUpdateSegmentContext context, List<SegmentsProvider> updateProviders, boolean fullUpdate)
    {
        for(SegmentsProvider provider : updateProviders)
        {
            List<BaseSegmentData> segmentsList = new ArrayList<>();
            if(context == null || CollectionUtils.isEmpty(context.getBaseSites()))
            {
                LOG.debug("BaseSite is null for update segments");
                segmentsList.addAll(provider.getSegments().orElse(Collections.emptyList()));
            }
            else
            {
                for(BaseSiteModel baseSite : context.getBaseSites())
                {
                    this.baseSiteService.setCurrentBaseSite(baseSite, true);
                    segmentsList.addAll(provider.getSegments().orElse(Collections.emptyList()));
                }
            }
            HashSet<String> existingSegments = new HashSet<>();
            segmentsList.removeIf(s -> !existingSegments.add(s.getCode()));
            if(CollectionUtils.isNotEmpty(segmentsList))
            {
                List<BaseSegmentData> segmentsToProcess = (List<BaseSegmentData>)segmentsList.stream().filter(s -> validateSegment(s)).collect(Collectors.toList());
                processProviderSegments(provider, segmentsToProcess);
                if(fullUpdate)
                {
                    processRemoveSegments(provider, segmentsToProcess);
                }
            }
        }
    }


    protected List<SegmentsProvider> getUpdateSegmentsProviders(List<SegmentsProvider> providers, CxUpdateSegmentContext context)
    {
        if(context == null || context.getSegmentProviders() == null)
        {
            return providers;
        }
        Set<String> providersIds = context.getSegmentProviders();
        return (List<SegmentsProvider>)providers.stream()
                        .filter(p -> providersIds.contains(p.getProviderId()))
                        .collect(Collectors.toList());
    }


    protected void processProviderSegments(SegmentsProvider provider, List<BaseSegmentData> segments)
    {
        addProviderPrefixAndSeparatorToSegments(provider.getProviderId(), segments);
        for(int cursor = 0; cursor < segments.size(); cursor += this.batchSize)
        {
            int batchEnd = (cursor + this.batchSize > segments.size()) ? segments.size() : (cursor + this.batchSize);
            List<BaseSegmentData> providerSegmentsBatch = segments.subList(cursor, batchEnd);
            Collection<String> providerSegmentCodes = (Collection<String>)providerSegmentsBatch.stream().map(s -> s.getCode()).collect(Collectors.toList());
            Collection<CxSegmentModel> segmentModelsBatch = this.cxSegmentDao.findSegmentsByCodes(providerSegmentCodes);
            Map<String, CxSegmentModel> segmentModelsMap = (Map<String, CxSegmentModel>)segmentModelsBatch.stream().collect(Collectors.toMap(CxSegmentModel::getCode, s -> s));
            providerSegmentsBatch.forEach(segment -> {
                if(segmentModelsMap.containsKey(segment.getCode()))
                {
                    updateSegment(provider, segment, (CxSegmentModel)segmentModelsMap.get(segment.getCode()), false);
                }
                else
                {
                    createSegment(provider, segment);
                }
            });
        }
    }


    protected void processRemoveSegments(SegmentsProvider provider, List<BaseSegmentData> segments)
    {
        SearchPageData<CxSegmentModel> searchPageData = new SearchPageData();
        PaginationData pagination = new PaginationData();
        pagination.setCurrentPage(0);
        pagination.setPageSize(this.batchSize);
        searchPageData.setPagination(pagination);
        Set<String> providerSegmentsCodes = (Set<String>)segments.stream().map(s -> s.getCode()).collect(Collectors.toSet());
        Set<CxSegmentModel> segmentsToRemove = new HashSet<>();
        do
        {
            searchPageData = this.cxSegmentDao.findSegments(MapUtils.EMPTY_MAP, searchPageData);
            for(CxSegmentModel segment : searchPageData.getResults())
            {
                if(isSegmentToRemove(provider, segment, providerSegmentsCodes))
                {
                    segmentsToRemove.add(segment);
                }
                else if(shouldRemoveProviderFromSegment(provider, segment, providerSegmentsCodes))
                {
                    removeProviderFromSegment(provider, segment);
                }
                if(shouldUpdateSegment(segment))
                {
                    Optional<BaseSegmentData> segmentData = segments.stream().filter(s -> s.getCode().equals(segment.getCode())).findFirst();
                    updateSegment(provider, segmentData.orElse(null), segment, true);
                }
            }
            searchPageData.getPagination().setCurrentPage(searchPageData.getPagination().getCurrentPage() + 1);
        }
        while(searchPageData.getPagination().getHasNext().booleanValue());
        try
        {
            this.modelService.removeAll(segmentsToRemove);
        }
        catch(ModelRemovalException mre)
        {
            LOG.debug("Failed to remove segments", (Throwable)mre);
        }
    }


    protected boolean validateSegment(BaseSegmentData segmentData)
    {
        BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(segmentData, "baseSegmentData");
        if(CollectionUtils.isNotEmpty(this.validators))
        {
            for(Validator validator : this.validators)
            {
                validator.validate(segmentData, (Errors)beanPropertyBindingResult);
            }
        }
        if(beanPropertyBindingResult.hasErrors())
        {
            String segmentCode = segmentData.getCode();
            List<String> errorMessages = (List<String>)beanPropertyBindingResult.getAllErrors().stream().filter(e -> (e.getDefaultMessage() != null)).map(e -> MessageFormat.format(e.getDefaultMessage(), e.getArguments())).collect(Collectors.toList());
            LOG.warn("Validation failed for segment with code: {}\n{}", segmentCode, errorMessages);
            return false;
        }
        return true;
    }


    protected boolean isSegmentToRemove(SegmentsProvider provider, CxSegmentModel segment, Set<String> providerSegmentsCodes)
    {
        if(!segment.isAutoCreated())
        {
            return false;
        }
        if(segment.getProviders() == null || segment.getProviders().size() != 1)
        {
            return false;
        }
        if(!segment.getProviders().contains(provider.getProviderId()) || providerSegmentsCodes.contains(segment.getCode()))
        {
            return false;
        }
        if(CollectionUtils.isNotEmpty(segment.getExpressionTriggers()) || CollectionUtils.isNotEmpty(segment.getTriggers()))
        {
            return false;
        }
        return true;
    }


    protected boolean shouldRemoveProviderFromSegment(SegmentsProvider provider, CxSegmentModel segment, Set<String> providerSegmentsCodes)
    {
        return (segment.getProviders() != null && segment.getProviders().size() > 1 && segment
                        .getProviders().contains(provider.getProviderId()) && !providerSegmentsCodes.contains(segment.getCode()));
    }


    protected boolean shouldUpdateSegment(CxSegmentModel segment)
    {
        return (segment.getProviders() != null && segment.getProviders().size() == 1 && segment.isAutoCreated());
    }


    protected void removeProviderFromSegment(SegmentsProvider provider, CxSegmentModel segment)
    {
        Set<String> segmentProviders = new HashSet<>(segment.getProviders());
        segmentProviders.remove(provider.getProviderId());
        segment.setProviders(segmentProviders);
        try
        {
            this.modelService.save(segment);
        }
        catch(ModelSavingException e)
        {
            LOG.debug("Failed to save segment with code: " + segment.getCode(), (Throwable)e);
        }
    }


    protected void updateSegment(SegmentsProvider provider, BaseSegmentData segmentData, CxSegmentModel segmentModel, boolean fullUpdate)
    {
        if(segmentData == null || segmentModel == null)
        {
            return;
        }
        if(segmentModel.getProviders() == null)
        {
            segmentModel.setProviders(new HashSet());
        }
        Set<String> segmentModelProviders = new HashSet<>(segmentModel.getProviders());
        segmentModelProviders.add(provider.getProviderId());
        segmentModel.setProviders(segmentModelProviders);
        if(fullUpdate)
        {
            segmentModel.setDescription(segmentData.getDescription());
        }
        try
        {
            this.modelService.save(segmentModel);
        }
        catch(ModelSavingException mse)
        {
            LOG.debug("Failed to save segment with code: " + segmentData.getCode(), (Throwable)mse);
        }
    }


    protected void createSegment(SegmentsProvider provider, BaseSegmentData segmentData)
    {
        if(segmentData == null)
        {
            return;
        }
        CxSegmentModel segmentModel = (CxSegmentModel)this.modelService.create(CxSegmentModel.class);
        segmentModel.setCode(segmentData.getCode());
        segmentModel.setDescription(segmentData.getDescription());
        segmentModel.setProviders(Collections.singleton(provider.getProviderId()));
        segmentModel.setAutoCreated(true);
        try
        {
            this.modelService.save(segmentModel);
        }
        catch(ModelSavingException mse)
        {
            LOG.debug("Failed to save segment with code: " + segmentData.getCode(), (Throwable)mse);
        }
    }


    protected List<SegmentsProvider> getProviders()
    {
        return this.providers;
    }


    @Autowired(required = false)
    public void setProviders(List<SegmentsProvider> providers)
    {
        this.providers = providers;
        LOG.debug("Providers count : {}", Integer.valueOf(this.providers.size()));
    }


    protected CxSegmentDao getCxSegmentDao()
    {
        return this.cxSegmentDao;
    }


    @Required
    public void setCxSegmentDao(CxSegmentDao segmentDao)
    {
        this.cxSegmentDao = segmentDao;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected int getBatchSize()
    {
        return this.batchSize;
    }


    public void setBatchSize(int batchSize)
    {
        this.batchSize = batchSize;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    protected List<Validator> getValidators()
    {
        return this.validators;
    }


    public void setValidators(List<Validator> validators)
    {
        this.validators = validators;
    }
}
