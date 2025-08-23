package com.hybris.backoffice.solrsearch.setup.impl;

import com.hybris.backoffice.search.setup.impl.ConfigStringResolver;
import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DefaultBackofficeSolrSearchSystemSetupConfigTest
{
    private static final String FILE_1 = "file1";
    private static final String FILE_2 = "file2";
    private static final String CUSTOMIZED_FILE_LIST_SEPARATOR = ";";
    private static final String CUSTOMIZED_FILE_ENCODING = "GBK";
    private static final String CUSTOMIZED_LANGUAGE_SEPARATOR = "-";
    private static final String BACKOFFICE_SOLR_SEARCH_ROOTS_SEPARATOR_KEY = "backoffice.solr.search.roots.separator";
    private static final String BACKOFFICE_SOLR_SEARCH_LOCALIZED_ROOTS_KEY = "backoffice.solr.search.localized.roots";
    private static final String BACKOFFICE_SOLR_SEARCH_NON_LOCALIZED_ROOTS_KEY = "backoffice.solr.search.nonlocalized.files";
    private static final String BACKOFFICE_SOLR_SEARCH_ROOTS_FILE_ENCODING_KEY = "backoffice.solr.search.roots.file.encoding";
    private static final String BACKOFFICE_SOLR_SEARCH_ROOTS_LANGUAGE_SEPARATOR_KEY = "backoffice.solr.search.roots.language.separator";
    private static final String CONFIGURED_FILE_LIST = "file1;file2";
    private static final String SOLR_FACET_SEARCH_MODULE = "solrfacetsearch";
    private final ConfigStringResolver configStringResolver = (ConfigStringResolver)Mockito.mock(ConfigStringResolver.class);
    private final DefaultBackofficeSolrSearchSystemSetupConfig config = new DefaultBackofficeSolrSearchSystemSetupConfig(this.configStringResolver);


    @Before
    public void setUp()
    {
        mockConfigResolverWithNonEmptyKeys();
        if(this.config.isExtensionLoaded("solrfacetsearch"))
        {
            mockRoots("backoffice.solr.search.localized.roots", "file1;file2");
            mockRoots("backoffice.solr.search.nonlocalized.files", "file1;file2");
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
    public void shouldReturnConfiguredLocalizedImpexFilesRootsWhenConfigurationKeyIsSet()
    {
        Collection<String> roots = this.config.getLocalizedRootNames();
        if(this.config.isExtensionLoaded("solrfacetsearch"))
        {
            Assertions.assertThat(roots).contains((Object[])new String[] {"file1", "file2"});
        }
        else
        {
            Assertions.assertThat(roots).isEmpty();
        }
    }


    @Test
    public void shouldReturnConfiguredNonLocalizedImpexFilesRootsWhenConfigurationKeyIsSet()
    {
        Collection<String> roots = this.config.getNonLocalizedRootNames();
        if(this.config.isExtensionLoaded("solrfacetsearch"))
        {
            Assertions.assertThat(roots).contains((Object[])new String[] {"file1", "file2"});
        }
        else
        {
            Assertions.assertThat(roots).isEmpty();
        }
    }


    private void mockConfigResolverWithNonEmptyKeys()
    {
        Mockito.when(this.configStringResolver.resolveConfigStringParameter("backoffice.solr.search.roots.separator"))
                        .thenReturn(";");
        Mockito.when(this.configStringResolver.resolveConfigStringParameter("backoffice.solr.search.roots.file.encoding"))
                        .thenReturn("GBK");
        Mockito.when(this.configStringResolver.resolveConfigStringParameter("backoffice.solr.search.roots.language.separator"))
                        .thenReturn("-");
    }


    private void mockRoots(String rootsKey, String files)
    {
        Mockito.when(this.configStringResolver.resolveConfigStringParameter(rootsKey)).thenReturn(files);
    }
}
