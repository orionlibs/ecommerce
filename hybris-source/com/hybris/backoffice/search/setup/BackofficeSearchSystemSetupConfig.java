package com.hybris.backoffice.search.setup;

import java.util.Collection;

public interface BackofficeSearchSystemSetupConfig
{
    Collection<String> getLocalizedRootNames();


    Collection<String> getNonLocalizedRootNames();


    String getFileEncoding();


    String getRootNameLanguageSeparator();


    String getListSeparator();
}
