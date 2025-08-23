package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationCatalogValueProvider implements FieldValueProvider
{
    private FieldNameProvider fieldNameProvider;


    public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model) throws FieldValueProviderException
    {
        if(model instanceof ProductModel)
        {
            List<Long> classificationCatalogsIds = (List<Long>)((ProductModel)model).getClassificationClasses().stream().map(classificationClass -> classificationClass.getCatalogVersion().getCatalog().getPk().getLong()).distinct().collect(Collectors.toList());
            Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
            List<FieldValue> fieldsValues = new ArrayList<>();
            for(String fieldName : fieldNames)
            {
                for(Long catalogId : classificationCatalogsIds)
                {
                    fieldsValues.add(new FieldValue(fieldName, catalogId));
                }
            }
            return fieldsValues;
        }
        return Collections.emptyList();
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
