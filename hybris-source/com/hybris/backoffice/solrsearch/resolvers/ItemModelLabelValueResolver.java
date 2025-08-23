package com.hybris.backoffice.solrsearch.resolvers;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ItemModelLabelValueResolver extends AbstractValueResolver<ItemModel, Object, Object>
{
    public static final Logger LOG = LoggerFactory.getLogger(ItemModelLabelValueResolver.class);
    private LabelServiceProxy labelServiceProxy;
    private ModelService modelService;


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ItemModel model, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
    {
        ItemModel itemModel = provideModel(model, indexedProperty);
        if(itemModel != null)
        {
            Qualifier qualifier = resolverContext.getQualifier();
            if(qualifier != null)
            {
                LanguageModel language = (LanguageModel)qualifier.getValueForType(LanguageModel.class);
                Locale locale = (Locale)qualifier.getValueForType(Locale.class);
                if(locale != null && language != null)
                {
                    String value = getLabelServiceProxy().getObjectLabel(itemModel, locale);
                    document.addField(resolveIndexKey(indexedProperty, language), value);
                }
                else
                {
                    throw new IllegalStateException(
                                    String.format("Locale cannot be resolved for indexed property %s", new Object[] {indexedProperty.getName()}));
                }
            }
            else
            {
                throw new IllegalStateException(
                                String.format("Indexed property must be localized and of type string/text to use %s while %s is not", new Object[] {ItemModelLabelValueResolver.class.getSimpleName(), indexedProperty.getName()}));
            }
        }
    }


    protected String resolveIndexKey(IndexedProperty indexedProperty, LanguageModel language)
    {
        return String.format("%s_%s_%s", new Object[] {indexedProperty.getName(), language.getIsocode().toLowerCase(Locale.ENGLISH), indexedProperty.getType()});
    }


    protected ItemModel provideModel(ItemModel model, IndexedProperty indexedProperty)
    {
        String providerParameter = indexedProperty.getValueProviderParameter();
        if(StringUtils.isNotEmpty(providerParameter))
        {
            return (ItemModel)getModelService().getAttributeValue(model, providerParameter);
        }
        return provideModel(model);
    }


    protected ItemModel provideModel(ItemModel model)
    {
        return model;
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


    public LabelServiceProxy getLabelServiceProxy()
    {
        return this.labelServiceProxy;
    }


    public void setLabelServiceProxy(LabelServiceProxy labelServiceProxy)
    {
        this.labelServiceProxy = labelServiceProxy;
    }
}
