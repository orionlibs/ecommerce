package de.hybris.platform.personalizationintegration.strategies.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationintegration.mapping.MappingData;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import de.hybris.platform.personalizationintegration.segment.UserSegmentsProvider;
import de.hybris.platform.personalizationintegration.service.CxIntegrationMappingService;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.consent.CxConsentService;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.strategies.UpdateUserSegmentStrategy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxUpdateUserSegmentStrategy extends AbstractCxSegmentStrategy implements UpdateUserSegmentStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxUpdateUserSegmentStrategy.class.getName());
    private CxIntegrationMappingService cxIntegrationMappingService;
    private CxConfigurationService cxConfigurationService;
    private List<UserSegmentsProvider> providers = Collections.emptyList();
    protected CxConsentService cxConsentService;


    public void updateUserSegments(UserModel user)
    {
        updateUserSegments(user, null);
    }


    public void updateUserSegments(UserModel user, CxCalculationContext context)
    {
        if(this.cxConsentService.userHasActiveConsent(user))
        {
            CxCalculationContext localContext = context;
            if(localContext == null && CollectionUtils.isNotEmpty(this.providers))
            {
                localContext = new CxCalculationContext();
                localContext.setSegmentUpdateProviders((Set)this.providers.stream().map(p -> p.getProviderId()).collect(Collectors.toSet()));
            }
            assignSegmentsToUser(user, createSegmentData(user, localContext).orElse(null), localContext);
        }
        else
        {
            this.cxIntegrationMappingService.assignSegmentsToUser(user, new MappingData(), false);
        }
    }


    protected void assignSegmentsToUser(UserModel user, MappingData segmentData, CxCalculationContext context)
    {
        if(segmentData != null)
        {
            if(context == null)
            {
                this.cxIntegrationMappingService.assignSegmentsToUser(user, segmentData, shouldCreateSegments());
            }
            else
            {
                this.cxIntegrationMappingService.assignSegmentsToUser(user, segmentData, shouldCreateSegments(), context);
            }
        }
    }


    protected boolean shouldCreateSegments()
    {
        return ((Boolean)this.cxConfigurationService.getConfiguration()
                        .map(CxConfigModel::getAutoCreateSegments).map(Boolean::booleanValue)
                        .orElseGet(() -> this.configurationService.getConfiguration().getBoolean("personalizationintegration.updateUserSegment.createSegment", Boolean.FALSE))).booleanValue();
    }


    protected Optional<MappingData> createSegmentData(UserModel user, CxCalculationContext context)
    {
        List<UserSegmentsProvider> selectedProviders = getSelectedProviders(context);
        if(CollectionUtils.isNotEmpty(selectedProviders))
        {
            List<List<SegmentMappingData>> segmentMappingFromProviders = getSegmentMappingFromProviders(user, selectedProviders, context);
            if(CollectionUtils.isNotEmpty(segmentMappingFromProviders))
            {
                Map<String, SegmentMappingData> segmentMap = new HashMap<>();
                segmentMappingFromProviders.stream()
                                .flatMap(Collection::stream)
                                .forEach(segment -> addSegment(segmentMap, segment));
                MappingData segmentData = new MappingData();
                segmentData.setSegments(new ArrayList(segmentMap.values()));
                return Optional.of(segmentData);
            }
        }
        return Optional.empty();
    }


    protected List<UserSegmentsProvider> getSelectedProviders(CxCalculationContext context)
    {
        if(context == null || CollectionUtils.isEmpty(context.getSegmentUpdateProviders()))
        {
            return this.providers;
        }
        return (List<UserSegmentsProvider>)this.providers.stream()
                        .filter(p -> context.getSegmentUpdateProviders().contains(p.getProviderId()))
                        .collect(Collectors.toList());
    }


    protected List<List<SegmentMappingData>> getSegmentMappingFromProviders(UserModel user, List<UserSegmentsProvider> providers, CxCalculationContext context)
    {
        return (List<List<SegmentMappingData>>)providers.stream()
                        .map(provider -> getSegmentsFromProvider(user, provider, context))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
    }


    protected List<SegmentMappingData> getSegmentsFromProvider(UserModel user, UserSegmentsProvider provider, CxCalculationContext context)
    {
        try
        {
            List<SegmentMappingData> segments = provider.getUserSegments(user);
            if(segments != null)
            {
                segments.forEach(s -> s.setProvider(provider.getProviderId()));
                addProviderPrefixAndSeparatorToSegments(provider.getProviderId(), segments);
            }
            else if(context != null && CollectionUtils.isNotEmpty(context.getSegmentUpdateProviders()))
            {
                context.getSegmentUpdateProviders().remove(provider.getProviderId());
            }
            return segments;
        }
        catch(RuntimeException e)
        {
            LOG.warn("Failed to get segment data from provider {}. Error message : {} ", provider.getClass().getName(), e
                            .getMessage());
            LOG.debug("Exception", e);
            return Collections.emptyList();
        }
    }


    protected void addSegment(Map<String, SegmentMappingData> segmentMap, SegmentMappingData segmentData)
    {
        segmentMap.merge(getSegmentDataKey(segmentData), segmentData, this::merge);
    }


    protected String getSegmentDataKey(SegmentMappingData segmentData)
    {
        return segmentData.getCode() + "_" + segmentData.getCode();
    }


    protected SegmentMappingData merge(SegmentMappingData existingData, SegmentMappingData newData)
    {
        if(newData.getAffinity() != null && newData.getAffinity().compareTo(existingData.getAffinity()) > 0)
        {
            return newData;
        }
        return existingData;
    }


    protected CxIntegrationMappingService getCxIntegrationMappingService()
    {
        return this.cxIntegrationMappingService;
    }


    @Required
    public void setCxIntegrationMappingService(CxIntegrationMappingService cxIntegrationMappingService)
    {
        this.cxIntegrationMappingService = cxIntegrationMappingService;
    }


    protected List<UserSegmentsProvider> getProviders()
    {
        return this.providers;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    protected CxConsentService getCxConsentService()
    {
        return this.cxConsentService;
    }


    @Required
    public void setCxConsentService(CxConsentService cxConsentService)
    {
        this.cxConsentService = cxConsentService;
    }


    @Autowired(required = false)
    public void setProviders(List<UserSegmentsProvider> providers)
    {
        this.providers = providers;
        LOG.debug("Providers count : {}", Integer.valueOf(this.providers.size()));
    }
}
