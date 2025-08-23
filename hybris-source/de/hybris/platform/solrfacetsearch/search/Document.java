package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Document extends Serializable
{
    Collection<String> getFieldNames();


    Object getFieldValue(String paramString);


    Map<String, Object> getFields();


    Set<String> getTags();
}
