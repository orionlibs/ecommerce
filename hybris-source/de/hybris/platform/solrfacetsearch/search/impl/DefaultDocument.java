package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.search.Document;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultDocument implements Document, Serializable
{
    private static final long serialVersionUID = 1L;
    private final transient Map<String, Object> fields = new HashMap<>();
    private final Set<String> tags = new HashSet<>();


    public Collection<String> getFieldNames()
    {
        return getFields().keySet();
    }


    public Object getFieldValue(String fieldName)
    {
        return getFields().get(fieldName);
    }


    public Map<String, Object> getFields()
    {
        return this.fields;
    }


    public Set<String> getTags()
    {
        return this.tags;
    }
}
