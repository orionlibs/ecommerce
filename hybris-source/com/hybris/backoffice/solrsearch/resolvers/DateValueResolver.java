package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DateValueResolver extends AbstractValueResolver<ItemModel, Object, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(DateValueResolver.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private ModelService modelService;


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ItemModel model, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
    {
        Object date = null;
        String fieldName = null;
        if(indexedProperty.isLocalized())
        {
            if(resolverContext.getQualifier() != null)
            {
                LanguageModel lang = (LanguageModel)resolverContext.getQualifier().getValueForType(LanguageModel.class);
                Locale locale = (Locale)resolverContext.getQualifier().getValueForType(Locale.class);
                if(lang != null && locale != null)
                {
                    date = this.modelService.getAttributeValue(model, indexedProperty.getName(), locale);
                    fieldName = String.format("%s_%s_%s", new Object[] {indexedProperty.getName(), lang.getIsocode(), indexedProperty.getType()});
                }
                else
                {
                    LOG.warn("Cannot index localized property {} due to missing lang qualifier provider", indexedProperty.getName());
                }
            }
        }
        else
        {
            date = this.modelService.getAttributeValue(model, indexedProperty.getName());
            fieldName = String.format("%s_%s", new Object[] {indexedProperty.getName(), indexedProperty.getType()});
        }
        if(date instanceof Date)
        {
            document.addField(fieldName, DATE_FORMAT.withZone(ZoneOffset.UTC).format(((Date)date).toInstant()));
        }
        else if(date != null)
        {
            LOG.warn("Indexing property {} of {} is not a reference", indexedProperty.getName(), model.getItemtype());
        }
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
}
