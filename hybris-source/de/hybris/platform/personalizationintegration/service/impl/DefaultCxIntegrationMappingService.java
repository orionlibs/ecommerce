package de.hybris.platform.personalizationintegration.service.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationintegration.mapping.MappingData;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import de.hybris.platform.personalizationintegration.service.CxIntegrationMappingService;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentSessionService;
import de.hybris.platform.personalizationservices.segment.converters.CxUserSegmentConversionHelper;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.site.BaseSiteService;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DefaultCxIntegrationMappingService implements CxIntegrationMappingService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxIntegrationMappingService.class);
    protected static final BigDecimal DEFAULT_AFFINITY = BigDecimal.ONE;
    private Map<String, Converter<Object, MappingData>> converters;
    private CxSegmentService cxSegmentService;
    private CxUserSegmentService cxUserSegmentService;
    private CxUserSegmentSessionService cxUserSegmentSessionService;
    private CxUserSegmentConversionHelper cxUserSegmentConversionHelper;
    private ModelService modelService;
    private BaseSiteService baseSiteService;
    private List<Validator> validators;


    public Optional<MappingData> mapExternalData(Object externalData, String strategyId)
    {
        Converter<Object, MappingData> strategy = this.converters.get(strategyId);
        if(strategy == null)
        {
            LOG.error("no strategy found for id {}", strategyId);
            return Optional.empty();
        }
        MappingData result = new MappingData();
        result.setSegments(new ArrayList());
        strategy.convert(externalData, result);
        return Optional.of(result);
    }


    public void assignSegmentsToUser(UserModel user, MappingData mappingData, boolean createSegment, CxCalculationContext context)
    {
        if(user != null && mappingData != null && mappingData.getSegments() != null)
        {
            List<SegmentMappingData> newUserSegments = mappingData.getSegments();
            Map<String, CxSegmentModel> segmentsModelMap = createSegmentModelMap(newUserSegments);
            Collection<SegmentMappingData> userSegmentsData = ((Map)newUserSegments.stream().filter(this::validateSegment).filter(us -> segmentExists(us, segmentsModelMap, createSegment)).map(this::fixSegmentMappingData)
                            .collect(Collectors.toMap(this::getMappingKey, Function.identity(), this::mergeSegmentMappingData))).values();
            if(this.cxUserSegmentSessionService.isUserSegmentStoredInSession(user))
            {
                this.cxUserSegmentSessionService.setUserSegmentsInSession(user, userSegmentsData, context);
            }
            else
            {
                this.cxUserSegmentService.setUserSegments(user, this.baseSiteService.getCurrentBaseSite(), this.cxUserSegmentConversionHelper
                                .convertToModel(user, userSegmentsData), context);
            }
        }
    }


    public void assignSegmentsToUser(UserModel user, MappingData mappingData, boolean createSegment)
    {
        assignSegmentsToUser(user, mappingData, createSegment, null);
    }


    protected String getMappingKey(SegmentMappingData mapping)
    {
        return mapping.getCode() + mapping.getCode();
    }


    protected Map<String, CxSegmentModel> createSegmentModelMap(Collection<SegmentMappingData> userSegmentsData)
    {
        if(CollectionUtils.isNotEmpty(userSegmentsData))
        {
            Collection<CxSegmentModel> segmentsModel = this.cxSegmentService.getSegmentsForCodes((Collection)userSegmentsData.stream().map(BaseSegmentData::getCode).collect(Collectors.toList()));
            return createValueMap(segmentsModel, CxSegmentModel::getCode, (a, b) -> b);
        }
        return Collections.emptyMap();
    }


    protected boolean segmentExists(SegmentMappingData segmentData, Map<String, CxSegmentModel> segmentsModelMap, boolean createSegment)
    {
        if(StringUtils.isNotEmpty(segmentData.getCode()) && segmentsModelMap.containsKey(segmentData.getCode()))
        {
            return true;
        }
        if(createSegment)
        {
            try
            {
                segmentsModelMap.put(segmentData.getCode(), createSegment(segmentData));
                return true;
            }
            catch(ModelSavingException e)
            {
                if(isSegmentStoredInDatabase(segmentData.getCode()))
                {
                    LOG.debug("Trying to save already existing segment: {}", segmentData.getCode());
                    return true;
                }
                LOG.warn("Problem when saving segment to database: {}", segmentData.getCode(), e);
                return false;
            }
        }
        return false;
    }


    private boolean isSegmentStoredInDatabase(String segmentCode)
    {
        try
        {
            return this.cxSegmentService.getSegment(segmentCode).isPresent();
        }
        catch(FlexibleSearchException e)
        {
            LOG.warn("Problem when fetching segment: {}", segmentCode, e);
            return false;
        }
    }


    protected SegmentMappingData fixSegmentMappingData(SegmentMappingData userSegment)
    {
        if(userSegment.getAffinity() == null)
        {
            userSegment.setAffinity(DEFAULT_AFFINITY);
        }
        return userSegment;
    }


    protected SegmentMappingData mergeSegmentMappingData(SegmentMappingData s1, SegmentMappingData s2)
    {
        return (s1.getAffinity().compareTo(s2.getAffinity()) > 0) ? s1 : s2;
    }


    private static <K, V> Map<K, V> createValueMap(Collection<V> valueCollection, Function<? super V, ? extends K> keyMapper, BinaryOperator<V> mergeFunction)
    {
        return (Map<K, V>)valueCollection.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(keyMapper,
                                        (Function)Function.identity(), mergeFunction, java.util.HashMap::new));
    }


    protected CxSegmentModel createSegment(SegmentMappingData segmentData)
    {
        CxSegmentModel segment = (CxSegmentModel)this.modelService.create(CxSegmentModel.class);
        segment.setCode(segmentData.getCode());
        segment.setDescription(segmentData.getDescription());
        segment.setAutoCreated(true);
        if(segmentData.getProvider() != null)
        {
            Set<String> providers = new HashSet<>(Collections.singletonList(segmentData.getProvider()));
            segment.setProviders(providers);
        }
        this.modelService.save(segment);
        return segment;
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


    protected Map<String, Converter<Object, MappingData>> getConverters()
    {
        return this.converters;
    }


    @Required
    public void setConverters(Map<String, Converter<Object, MappingData>> converters)
    {
        this.converters = converters;
    }


    protected CxSegmentService getCxSegmentService()
    {
        return this.cxSegmentService;
    }


    @Required
    public void setCxSegmentService(CxSegmentService cxSegmentService)
    {
        this.cxSegmentService = cxSegmentService;
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


    protected CxUserSegmentService getCxUserSegmentService()
    {
        return this.cxUserSegmentService;
    }


    @Required
    public void setCxUserSegmentService(CxUserSegmentService cxUserSegmentService)
    {
        this.cxUserSegmentService = cxUserSegmentService;
    }


    protected CxUserSegmentSessionService getCxUserSegmentSessionService()
    {
        return this.cxUserSegmentSessionService;
    }


    @Required
    public void setCxUserSegmentSessionService(CxUserSegmentSessionService cxUserSegmentSessionService)
    {
        this.cxUserSegmentSessionService = cxUserSegmentSessionService;
    }


    protected CxUserSegmentConversionHelper getCxUserSegmentConversionHelper()
    {
        return this.cxUserSegmentConversionHelper;
    }


    @Required
    public void setCxUserSegmentConversionHelper(CxUserSegmentConversionHelper cxUserSegmentConversionHelper)
    {
        this.cxUserSegmentConversionHelper = cxUserSegmentConversionHelper;
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
