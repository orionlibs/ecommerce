package com.hybris.backoffice.solrsearch.setup;

import java.util.Collection;

@Deprecated(since = "2105")
public interface BackofficeSolrSearchSystemSetupConfig
{
    Collection<String> getLocalizedRootNames();


    Collection<String> getNonLocalizedRootNames();


    String getFileEncoding();


    String getRootNameLanguageSeparator();


    String getListSeparator();
}
