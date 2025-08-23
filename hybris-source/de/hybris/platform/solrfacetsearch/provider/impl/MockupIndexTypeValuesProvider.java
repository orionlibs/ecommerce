package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.IndexedTypeFieldsValuesProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockupIndexTypeValuesProvider implements IndexedTypeFieldsValuesProvider
{
    public static final String NAME = "arbitraryField1";
    public static final String TARGET_SUPER_CATEGORY = "topseller";
    private ModelService modelService;


    public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, Object model) throws FieldValueProviderException
    {
        List<FieldValue> fieldValues = new ArrayList<>();
        Collection<CategoryModel> categories = null;
        categories = (Collection<CategoryModel>)this.modelService.getAttributeValue(model, "supercategories");
        if(categories != null && !categories.isEmpty())
        {
            String catName = null;
            for(CategoryModel category : categories)
            {
                catName = (String)this.modelService.getAttributeValue(category, "code");
                if("topseller".equals(catName))
                {
                    fieldValues.add(new FieldValue("arbitraryField1_string", "TOP"));
                }
            }
        }
        return fieldValues;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public Set<String> getFacets()
    {
        return Collections.emptySet();
    }


    public Map<String, String> getFieldNamesMapping()
    {
        return Collections.emptyMap();
    }
}
