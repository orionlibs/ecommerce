package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;
import java.util.List;

public interface SearchResultGroupCommand extends Serializable
{
    String getName();


    long getNumberOfMatches();


    long getNumberOfGroups();


    List<SearchResultGroup> getGroups();
}
