package com.hybris.backoffice.searchservices.providers.impl;

import com.hybris.backoffice.proxy.DataQualityCalculationServiceProxy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.impl.AbstractSnIndexerValueProvider;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataQualitySnIndexerValueProvider extends AbstractSnIndexerValueProvider<ItemModel, Void>
{
    private static final Logger LOG = LoggerFactory.getLogger(DataQualitySnIndexerValueProvider.class);
    private static final int DATA_QUALITY_VALUE_SCALE = 2;
    protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(Locale.class);
    private DataQualityCalculationServiceProxy dataQualityCalculationServiceProxy;
    private String domainGroupId;


    public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
    {
        return SUPPORTED_QUALIFIER_CLASSES;
    }


    protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, ItemModel source, Void data) throws SnIndexerException
    {
        Optional<Double> qualityIndex = this.dataQualityCalculationServiceProxy.calculate(source, getDomainGroupId());
        if(qualityIndex.isPresent())
        {
            return Double.valueOf(BigDecimal.valueOf(Math.max(0.0D, ((Double)qualityIndex.get()).doubleValue())).setScale(2, RoundingMode.HALF_UP)
                            .doubleValue());
        }
        LOG.warn("Could not calculate quality for {} and domanGroupId {}!", source, this.domainGroupId);
        throw new SnIndexerException(
                        String.format("Could not calculate quality for %s and domanGroupId %s!", new Object[] {source, this.domainGroupId}));
    }


    public void setDataQualityCalculationServiceProxy(DataQualityCalculationServiceProxy dataQualityCalculationServiceProxy)
    {
        this.dataQualityCalculationServiceProxy = dataQualityCalculationServiceProxy;
    }


    public DataQualityCalculationServiceProxy getDataQualityCalculationServiceProxy()
    {
        return this.dataQualityCalculationServiceProxy;
    }


    public void setDomainGroupId(String domainGroupId)
    {
        this.domainGroupId = domainGroupId;
    }


    public String getDomainGroupId()
    {
        return this.domainGroupId;
    }
}
