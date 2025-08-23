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
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationCatalogVersionValueProvider implements FieldValueProvider
{
    private FieldNameProvider fieldNameProvider;


    public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model) throws FieldValueProviderException
    {
        if(model instanceof ProductModel)
        {
            ProductModel product = (ProductModel)model;
            Set<Long> classificationCatalogVersionsIds = (Set<Long>)product.getClassificationClasses().stream().map(classificationClass -> classificationClass.getCatalogVersion().getPk().getLong()).collect(Collectors.toSet());
            if(product.getCatalogVersion() instanceof de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel)
            {
                classificationCatalogVersionsIds.add(product.getCatalogVersion().getPk().getLong());
            }
            Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
            List<FieldValue> fieldsValues = new ArrayList<>();
            for(String fieldName : fieldNames)
            {
                for(Long catalogVersionId : classificationCatalogVersionsIds)
                {
                    fieldsValues.add(new FieldValue(fieldName, catalogVersionId));
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
