package com.hybris.backoffice.solrsearch.setup.impl;

import com.hybris.backoffice.search.setup.impl.AbstractBackofficeSearchSystemSetupConfig;
import com.hybris.backoffice.search.setup.impl.ConfigStringResolver;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;

public class DefaultBackofficeSolrSearchSystemSetupConfig extends AbstractBackofficeSearchSystemSetupConfig
{
    private static final String BACKOFFICE_SOLR_SEARCH_NON_LOCALIZED_ROOTS_KEY = "backoffice.solr.search.nonlocalized.files";
    private static final String BACKOFFICE_SOLR_SEARCH_LOCALIZED_ROOTS_KEY = "backoffice.solr.search.localized.roots";
    private static final String BACKOFFICE_SOLR_SEARCH_ROOTS_SEPARATOR_KEY = "backoffice.solr.search.roots.separator";
    private static final String BACKOFFICE_SOLR_SEARCH_ROOTS_FILE_ENCODING = "backoffice.solr.search.roots.file.encoding";
    private static final String BACKOFFICE_SOLR_SEARCH_ROOTS_LANGUAGE_SEPARATOR = "backoffice.solr.search.roots.language.separator";
    private static final String SOLR_FACET_SEARCH_MODULE = "solrfacetsearch";


    public DefaultBackofficeSolrSearchSystemSetupConfig(ConfigStringResolver configStringResolver)
    {
        super(configStringResolver);
    }


    public Collection<String> getLocalizedRootNames()
    {
        if(!isExtensionLoaded("solrfacetsearch"))
        {
            return Collections.emptyList();
        }
        return readRoots("backoffice.solr.search.localized.roots");
    }


    public Collection<String> getNonLocalizedRootNames()
    {
        if(!isExtensionLoaded("solrfacetsearch"))
        {
            return Collections.emptyList();
        }
        return readRoots("backoffice.solr.search.nonlocalized.files");
    }


    public String getFileEncoding()
    {
        String fileEncoding = getConfigStringResolver().resolveConfigStringParameter("backoffice.solr.search.roots.file.encoding");
        return StringUtils.isNotBlank(fileEncoding) ? fileEncoding : super.getFileEncoding();
    }


    public String getRootNameLanguageSeparator()
    {
        String languageSeparator = getConfigStringResolver().resolveConfigStringParameter("backoffice.solr.search.roots.language.separator");
        return StringUtils.isNotBlank(languageSeparator) ? languageSeparator : super.getRootNameLanguageSeparator();
    }


    public String getListSeparator()
    {
        String listSeparator = getConfigStringResolver().resolveConfigStringParameter("backoffice.solr.search.roots.separator");
        return StringUtils.isNotBlank(listSeparator) ? listSeparator : super.getListSeparator();
    }


    private Collection<String> readRoots(String backofficeSolrSearchImpexRootsKey)
    {
        String configurationRoots = getConfigurationRoots(backofficeSolrSearchImpexRootsKey);
        return StringUtils.isNotBlank(configurationRoots) ? Arrays.<String>asList(configurationRoots.split(getListSeparator())) :
                        Collections.<String>emptyList();
    }
}
