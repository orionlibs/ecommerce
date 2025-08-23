package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.constants.AdaptivesearchConstants;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.converters.AsSearchConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsSearchProfile;
import de.hybris.platform.adaptivesearch.data.AsConfigurableSearchConfiguration;
import de.hybris.platform.adaptivesearch.data.AsGenericSearchProfile;
import de.hybris.platform.adaptivesearch.data.AsReference;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsCacheKey;
import de.hybris.platform.adaptivesearch.strategies.AsCacheScope;
import de.hybris.platform.adaptivesearch.strategies.AsCacheStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileLoadStrategy;
import de.hybris.platform.adaptivesearch.util.ContextAwareConverter;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractAsSearchProfileLoadStrategy<T extends AbstractAsSearchProfileModel, R extends AbstractAsSearchProfile> implements AsSearchProfileLoadStrategy<T, R>
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractAsSearchProfileLoadStrategy.class);
    private ModelService modelService;
    private AsCacheStrategy asCacheStrategy;
    private ContextAwareConverter<AbstractAsConfigurableSearchConfigurationModel, AsConfigurableSearchConfiguration, AsSearchConfigurationConverterContext> asConfigurableSearchConfigurationConverter;


    public Serializable getCacheKeyFragment(AsSearchProfileContext context, T searchProfile)
    {
        return null;
    }


    protected Map<String, AsConfigurableSearchConfiguration> loadSearchConfigurations(AsSearchProfileContext context, AsGenericSearchProfile source)
    {
        Map<String, AsReference> availableSearchConfigurations = source.getAvailableSearchConfigurations();
        if(MapUtils.isEmpty(availableSearchConfigurations))
        {
            return Collections.emptyMap();
        }
        Map<String, AsConfigurableSearchConfiguration> searchConfigurations = new HashMap<>();
        AsSearchConfigurationConverterContext converterContext = new AsSearchConfigurationConverterContext();
        converterContext.setSearchProfileCode(source.getCode());
        AsReference defaultSearchConfigurationReference = availableSearchConfigurations.get(AdaptivesearchConstants.DEFAULT_QUALIFIER);
        if(defaultSearchConfigurationReference != null)
        {
            loadSearchConfiguration(searchConfigurations, AdaptivesearchConstants.DEFAULT_QUALIFIER, defaultSearchConfigurationReference, converterContext);
        }
        if(StringUtils.isNotBlank(source.getQualifierType()))
        {
            List<String> qualifiers = (List<String>)context.getQualifiers().get(source.getQualifierType());
            for(String qualifier : CollectionUtils.emptyIfNull(qualifiers))
            {
                AsReference searchConfigurationReference = availableSearchConfigurations.get(qualifier);
                if(searchConfigurationReference != null)
                {
                    loadSearchConfiguration(searchConfigurations, qualifier, searchConfigurationReference, converterContext);
                }
            }
        }
        return searchConfigurations;
    }


    protected void loadSearchConfiguration(Map<String, AsConfigurableSearchConfiguration> searchConfigurations, String qualifier, AsReference searchConfigurationReference, AsSearchConfigurationConverterContext converterContext)
    {
        try
        {
            Long baseKey = searchConfigurationReference.getPk().getLong();
            Long baseKeyVersion = Long.valueOf(searchConfigurationReference.getVersion());
            DefaultAsCacheKey defaultAsCacheKey = new DefaultAsCacheKey(AsCacheScope.LOAD, new Serializable[] {baseKey, baseKeyVersion});
            AsConfigurableSearchConfiguration searchConfiguration = (AsConfigurableSearchConfiguration)this.asCacheStrategy.getWithLoader((AsCacheKey)defaultAsCacheKey, key -> doLoadSearchConfiguration(searchConfigurationReference, converterContext));
            if(searchConfiguration != null)
            {
                searchConfigurations.put(qualifier, searchConfiguration);
            }
        }
        catch(RuntimeException e)
        {
            LOG.error(String.format("Could not load search configuration with pk %s", new Object[] {searchConfigurationReference.getPk()}), e);
        }
    }


    protected AsConfigurableSearchConfiguration doLoadSearchConfiguration(AsReference searchConfigurationReference, AsSearchConfigurationConverterContext converterContext)
    {
        AbstractAsConfigurableSearchConfigurationModel source = (AbstractAsConfigurableSearchConfigurationModel)this.modelService.get(searchConfigurationReference.getPk());
        if(source.isCorrupted())
        {
            return null;
        }
        return (AsConfigurableSearchConfiguration)this.asConfigurableSearchConfigurationConverter.convert(source, converterContext);
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


    public AsCacheStrategy getAsCacheStrategy()
    {
        return this.asCacheStrategy;
    }


    @Required
    public void setAsCacheStrategy(AsCacheStrategy asCacheStrategy)
    {
        this.asCacheStrategy = asCacheStrategy;
    }


    public ContextAwareConverter<AbstractAsConfigurableSearchConfigurationModel, AsConfigurableSearchConfiguration, AsSearchConfigurationConverterContext> getAsConfigurableSearchConfigurationConverter()
    {
        return this.asConfigurableSearchConfigurationConverter;
    }


    @Required
    public void setAsConfigurableSearchConfigurationConverter(ContextAwareConverter<AbstractAsConfigurableSearchConfigurationModel, AsConfigurableSearchConfiguration, AsSearchConfigurationConverterContext> asConfigurableSearchConfigurationConverter)
    {
        this.asConfigurableSearchConfigurationConverter = asConfigurableSearchConfigurationConverter;
    }
}
