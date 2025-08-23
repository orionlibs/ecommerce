package com.hybris.backoffice.searchservices.setup.impl;

import com.hybris.backoffice.search.setup.impl.ConfigStringResolver;
import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DefaultBackofficeSearchservicesSystemSetupConfigTest
{
    private static final String BACKOFFICE_SEARCH_SERVICES_NON_LOCALIZED_ROOTS_KEY = "backoffice.search.services.nonlocalized.files";
    private static final String BACKOFFICE_SEARCH_SERVICES_LOCALIZED_ROOTS_KEY = "backoffice.search.services.localized.roots";
    private static final String BACKOFFICE_SEARCH_SERVICES_FACET_NON_LOCALIZED_ROOTS_KEY = "backoffice.search.services.facet.nonlocalized.files";
    private static final String BACKOFFICE_SEARCH_SERVICES_FACET_LOCALIZED_ROOTS_KEY = "backoffice.search.services.facet.localized.roots";
    private static final String BACKOFFICE_SEARCH_SERVICES_ROOTS_SEPARATOR_KEY = "backoffice.search.services.roots.separator";
    private static final String BACKOFFICE_SEARCH_SERVICES_ROOTS_FILE_ENCODING_KEY = "backoffice.search.services.roots.file.encoding";
    private static final String BACKOFFICE_SEARCH_SERVICES_ROOTS_LANGUAGE_SEPARATOR_KEY = "backoffice.search.services.roots.language.separator";
    private static final String CUSTOMIZED_FILE_LIST_SEPARATOR = ";";
    private static final String CUSTOMIZED_FILE_ENCODING = "GBK";
    private static final String CUSTOMIZED_LANGUAGE_SEPARATOR = "-";
    private static final String SEARCH_SERVICE_CONFIGURED_FILE_LIST = "file1;file2";
    private static final String SEARCH_SERVICE_FACET_CONFIGURED_FILE_LIST = "file3;file4";
    private static final String FILE_1 = "file1";
    private static final String FILE_2 = "file2";
    private static final String FILE_3 = "file3";
    private static final String FILE_4 = "file4";
    private static final String SEARCH_SERVICE_MODULE = "searchservices";
    private static final String ADAPTIVE_SEARCH_MODULE = "adaptivesearch";
    private final ConfigStringResolver configStringResolver = (ConfigStringResolver)Mockito.mock(ConfigStringResolver.class);
    private final DefaultBackofficeSearchservicesSystemSetupConfig config = new DefaultBackofficeSearchservicesSystemSetupConfig(this.configStringResolver);


    @Before
    public void setUp()
    {
        mockConfigResolverWithNonEmptyKeys();
        if(this.config.isExtensionLoaded("searchservices"))
        {
            mockRoots("backoffice.search.services.nonlocalized.files", "file1;file2");
            mockRoots("backoffice.search.services.localized.roots", "file1;file2");
        }
        if(this.config.isExtensionLoaded("adaptivesearch"))
        {
            mockRoots("backoffice.search.services.facet.nonlocalized.files", "file3;file4");
            mockRoots("backoffice.search.services.facet.localized.roots", "file3;file4");
        }
    }


    @Test
    public void shouldReturnCustomizedFileListSeparatorWhenCustomized()
    {
        Assertions.assertThat(this.config.getListSeparator()).isEqualTo(";");
    }


    @Test
    public void shouldReturnCustomizedFileEncodingWhenCustomized()
    {
        Assertions.assertThat(this.config.getFileEncoding()).isEqualTo("GBK");
    }


    @Test
    public void shouldReturnCustomizedLanguageSeparatorWhenCustomized()
    {
        Assertions.assertThat(this.config.getRootNameLanguageSeparator()).isEqualTo("-");
    }


    @Test
    public void shouldReturnCollectionOfLocalizedImpexFilesRootsAccordingToLoadedExtensions()
    {
        Collection<String> roots = this.config.getLocalizedRootNames();
        if(this.config.isExtensionLoaded("searchservices"))
        {
            Assertions.assertThat(roots).contains((Object[])new String[] {"file1", "file2"});
        }
        if(this.config.isExtensionLoaded("adaptivesearch"))
        {
            Assertions.assertThat(roots).contains((Object[])new String[] {"file3", "file4"});
        }
        if(!this.config.isExtensionLoaded("adaptivesearch") && !this.config.isExtensionLoaded("searchservices"))
        {
            Assertions.assertThat(roots).isEmpty();
        }
    }


    @Test
    public void shouldReturnCollectionOfNonLocalizedImpexFilesRootsAccordingToLoadedExtensions()
    {
        Collection<String> roots = this.config.getNonLocalizedRootNames();
        if(this.config.isExtensionLoaded("searchservices"))
        {
            Assertions.assertThat(roots).contains((Object[])new String[] {"file1", "file2"});
        }
        if(this.config.isExtensionLoaded("adaptivesearch"))
        {
            Assertions.assertThat(roots).contains((Object[])new String[] {"file3", "file4"});
        }
        if(!this.config.isExtensionLoaded("adaptivesearch") && !this.config.isExtensionLoaded("searchservices"))
        {
            Assertions.assertThat(roots).isEmpty();
        }
    }


    private void mockConfigResolverWithNonEmptyKeys()
    {
        Mockito.when(this.configStringResolver.resolveConfigStringParameter("backoffice.search.services.roots.separator"))
                        .thenReturn(";");
        Mockito.when(this.configStringResolver.resolveConfigStringParameter("backoffice.search.services.roots.file.encoding"))
                        .thenReturn("GBK");
        Mockito.when(this.configStringResolver.resolveConfigStringParameter("backoffice.search.services.roots.language.separator"))
                        .thenReturn("-");
    }


    private void mockRoots(String rootsKey, String files)
    {
        Mockito.when(this.configStringResolver.resolveConfigStringParameter(rootsKey)).thenReturn(files);
    }
}
