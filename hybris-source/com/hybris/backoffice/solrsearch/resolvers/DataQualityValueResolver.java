package com.hybris.backoffice.solrsearch.resolvers;

import com.hybris.backoffice.proxy.DataQualityCalculationServiceProxy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DataQualityValueResolver extends AbstractValueResolver<ItemModel, Object, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(DataQualityValueResolver.class);
    private static final int DATA_QUALITY_VALUE_SCALE = 2;
    private DataQualityCalculationServiceProxy dataQualityCalculationServiceProxy;
    private String domainGroupId;


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ItemModel model, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
    {
        Optional<Double> qualityIndex = this.dataQualityCalculationServiceProxy.calculate(model, getDomainGroupId());
        if(qualityIndex.isPresent())
        {
            document.addField(indexedProperty,
                            Double.valueOf(BigDecimal.valueOf(Math.max(0.0D, ((Double)qualityIndex.get()).doubleValue())).setScale(2, RoundingMode.HALF_UP).doubleValue()), resolverContext
                                            .getFieldQualifier());
        }
        else
        {
            LOG.warn("Could not calculate quality for {} and domanGroupId {}!", model, this.domainGroupId);
        }
    }


    public void setDataQualityCalculationServiceProxy(DataQualityCalculationServiceProxy dataQualityCalculationServiceProxy)
    {
        this.dataQualityCalculationServiceProxy = dataQualityCalculationServiceProxy;
    }


    public DataQualityCalculationServiceProxy getDataQualityCalculationServiceProxy()
    {
        return this.dataQualityCalculationServiceProxy;
    }


    @Required
    public void setDomainGroupId(String domainGroupId)
    {
        this.domainGroupId = domainGroupId;
    }


    public String getDomainGroupId()
    {
        return this.domainGroupId;
    }
}
