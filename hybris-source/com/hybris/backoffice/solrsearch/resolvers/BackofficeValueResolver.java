package com.hybris.backoffice.solrsearch.resolvers;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import java.util.Collection;
import java.util.Locale;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeValueResolver extends AbstractValueResolver<ItemModel, Object, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeValueResolver.class);
    private LabelServiceProxy labelServiceProxy;
    private UserService userService;
    private FieldNameProvider fieldNameProvider;


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ItemModel model, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
    {
        Qualifier qualifier = resolverContext.getQualifier();
        Locale locale = (Locale)qualifier.getValueForType(Locale.class);
        Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, qualifier.toFieldQualifier());
        if(locale != null || CollectionUtils.isNotEmpty(fieldNames))
        {
            addBackofficeSpecificFields(document, model, locale, fieldNames);
        }
        else
        {
            throw new IllegalStateException("Locale value for qualifier " + qualifier + " could not be resolved");
        }
    }


    protected void addBackofficeSpecificFields(InputDocument document, ItemModel model, Locale language, Collection<String> fieldNames) throws FieldValueProviderException
    {
        String objectLabel = getLabelServiceProxy().getObjectLabel(model, language);
        if(StringUtils.isBlank(objectLabel))
        {
            String exceptionMessage = String.format("Couldn't retrieve %s for fields: %s", new Object[] {fieldNames, model.getItemtype()});
            throw new FieldValueProviderException(exceptionMessage);
        }
        try
        {
            for(String fieldName : fieldNames)
            {
                document.addField(fieldName, objectLabel);
            }
        }
        catch(FieldValueProviderException e)
        {
            LOG.error("Couldn't add backoffice specific field", (Throwable)e);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug(objectLabel);
        }
    }


    public LabelServiceProxy getLabelServiceProxy()
    {
        return this.labelServiceProxy;
    }


    public void setLabelServiceProxy(LabelServiceProxy labelServiceProxy)
    {
        this.labelServiceProxy = labelServiceProxy;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public FieldNameProvider getFieldNameProvider()
    {
        return this.fieldNameProvider;
    }


    @Required
    public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
    {
        this.fieldNameProvider = fieldNameProvider;
    }
}
