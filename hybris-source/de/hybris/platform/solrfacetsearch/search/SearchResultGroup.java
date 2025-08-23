package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;
import java.util.List;

public interface SearchResultGroup extends Serializable
{
    @Deprecated(since = "2105", forRemoval = true)
    long getNumberOfResults();


    String getGroupValue();


    List<Document> getDocuments();
}
