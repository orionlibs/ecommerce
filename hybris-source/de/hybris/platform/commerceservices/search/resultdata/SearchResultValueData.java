package de.hybris.platform.commerceservices.search.resultdata;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SearchResultValueData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<String, Object> values;
    private Map<ClassAttributeAssignmentModel, Object> featureValues;
    private Collection<SearchResultValueData> variants;
    private Set<String> tags;


    public void setValues(Map<String, Object> values)
    {
        this.values = values;
    }


    public Map<String, Object> getValues()
    {
        return this.values;
    }


    public void setFeatureValues(Map<ClassAttributeAssignmentModel, Object> featureValues)
    {
        this.featureValues = featureValues;
    }


    public Map<ClassAttributeAssignmentModel, Object> getFeatureValues()
    {
        return this.featureValues;
    }


    public void setVariants(Collection<SearchResultValueData> variants)
    {
        this.variants = variants;
    }


    public Collection<SearchResultValueData> getVariants()
    {
        return this.variants;
    }


    public void setTags(Set<String> tags)
    {
        this.tags = tags;
    }


    public Set<String> getTags()
    {
        return this.tags;
    }
}
