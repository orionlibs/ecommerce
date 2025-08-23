package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class FacetValueField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private Set<String> values;


    public FacetValueField(String field, String... values)
    {
        this.field = field;
        this.values = new LinkedHashSet<>(Arrays.asList(values));
    }


    public FacetValueField(String field, Set<String> values)
    {
        this.field = field;
        this.values = values;
    }


    public String getField()
    {
        return this.field;
    }


    public void setField(String field)
    {
        this.field = field;
    }


    public Set<String> getValues()
    {
        return this.values;
    }


    public void setValues(Set<String> values)
    {
        this.values = values;
    }
}
