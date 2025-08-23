package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.impl.CategoryCodeValueProvider;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class CategoryPKValueProvider extends CategoryCodeValueProvider
{
    private FieldNameProvider fieldNameProvider;


    protected List<FieldValue> createFieldValue(CategoryModel category, IndexedProperty indexedProperty)
    {
        PK value = (PK)getPropertyValue(category, "pk");
        Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
        return (List<FieldValue>)fieldNames.stream().map(fieldName -> new FieldValue(fieldName, value.getLong())).collect(Collectors.toList());
    }


    protected FieldNameProvider getFieldNameProvider()
    {
        return this.fieldNameProvider;
    }


    @Required
    public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
    {
        super.setFieldNameProvider(fieldNameProvider);
        this.fieldNameProvider = fieldNameProvider;
    }
}
