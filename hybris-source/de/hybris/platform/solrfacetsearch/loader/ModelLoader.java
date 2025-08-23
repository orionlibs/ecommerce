package de.hybris.platform.solrfacetsearch.loader;

import java.util.Collection;
import java.util.List;
import org.apache.solr.common.SolrDocument;

public interface ModelLoader<T>
{
    List<T> loadModels(Collection<SolrDocument> paramCollection) throws ModelLoadingException;


    List<String> loadCodes(Collection<SolrDocument> paramCollection) throws ModelLoadingException;
}
