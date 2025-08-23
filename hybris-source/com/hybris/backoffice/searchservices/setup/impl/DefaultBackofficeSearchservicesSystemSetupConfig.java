package com.hybris.backoffice.searchservices.setup.impl;

import com.hybris.backoffice.search.setup.impl.AbstractBackofficeSearchSystemSetupConfig;
import com.hybris.backoffice.search.setup.impl.ConfigStringResolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;

public class DefaultBackofficeSearchservicesSystemSetupConfig extends AbstractBackofficeSearchSystemSetupConfig
{
    private static final String BACKOFFICE_SMART_SEARCH_NON_LOCALIZED_ROOTS_KEY = "backoffice.search.services.nonlocalized.files";
    private static final String BACKOFFICE_SMART_SEARCH_FACET_NON_LOCALIZED_ROOTS_KEY = "backoffice.search.services.facet.nonlocalized.files";
    private static final String BACKOFFICE_SMART_SEARCH_LOCALIZED_ROOTS_KEY = "backoffice.search.services.localized.roots";
    private static final String BACKOFFICE_SMART_SEARCH_FACET_LOCALIZED_ROOTS_KEY = "backoffice.search.services.facet.localized.roots";
    private static final String BACKOFFICE_SMART_SEARCH_ROOTS_SEPARATOR_KEY = "backoffice.search.services.roots.separator";
    private static final String BACKOFFICE_SMART_SEARCH_ROOTS_FILE_ENCODING = "backoffice.search.services.roots.file.encoding";
    private static final String BACKOFFICE_SMART_SEARCH_ROOTS_LANGUAGE_SEPARATOR = "backoffice.search.services.roots.language.separator";
    private static final String SEARCH_SERVICE_MODULE = "searchservices";
    private static final String ADAPTIVE_SEARCH_MODULE = "adaptivesearch";


    public DefaultBackofficeSearchservicesSystemSetupConfig(ConfigStringResolver configStringResolver)
    {
        super(configStringResolver);
    }


    public Collection<String> getLocalizedRootNames()
    {
        ArrayList<String> roots = new ArrayList<>();
        if(isExtensionLoaded("searchservices"))
        {
            roots.addAll(readRoots("backoffice.search.services.localized.roots"));
        }
        if(isExtensionLoaded("adaptivesearch"))
        {
            roots.addAll(readRoots("backoffice.search.services.facet.localized.roots"));
        }
        return roots;
    }


    public Collection<String> getNonLocalizedRootNames()
    {
        ArrayList<String> roots = new ArrayList<>();
        if(isExtensionLoaded("searchservices"))
        {
            roots.addAll(readRoots("backoffice.search.services.nonlocalized.files"));
        }
        if(isExtensionLoaded("adaptivesearch"))
        {
            roots.addAll(readRoots("backoffice.search.services.facet.nonlocalized.files"));
        }
        return roots;
    }


    public String getFileEncoding()
    {
        String fileEncoding = getConfigStringResolver().resolveConfigStringParameter("backoffice.search.services.roots.file.encoding");
        return StringUtils.isNotBlank(fileEncoding) ? fileEncoding : super.getFileEncoding();
    }


    public String getRootNameLanguageSeparator()
    {
        String languageSeparator = getConfigStringResolver().resolveConfigStringParameter("backoffice.search.services.roots.language.separator");
        return StringUtils.isNotBlank(languageSeparator) ? languageSeparator : super.getRootNameLanguageSeparator();
    }


    public String getListSeparator()
    {
        String listSeparator = getConfigStringResolver().resolveConfigStringParameter("backoffice.search.services.roots.separator");
        return StringUtils.isNotBlank(listSeparator) ? listSeparator : super.getListSeparator();
    }


    private Collection<String> readRoots(String backofficeSmartSearchImpexRootsKey)
    {
        String configurationRoots = getConfigurationRoots(backofficeSmartSearchImpexRootsKey);
        return StringUtils.isNotBlank(configurationRoots) ? Arrays.<String>asList(configurationRoots.split(getListSeparator())) :
                        Collections.<String>emptyList();
    }
}
