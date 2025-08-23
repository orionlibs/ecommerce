package com.hybris.backoffice.search.setup.impl;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractBackofficeSearchSystemSetupConfigTest
{
    private static final String CONFIGUR_KEY = "key";
    private static final String CONFIGUR_VALUE = "value";
    private ConfigStringResolver resolver = (ConfigStringResolver)Mockito.mock(ConfigStringResolver.class);
    private AbstractBackofficeSearchSystemSetupConfig config = new AbstractBackofficeSearchSystemSetupConfig(this.resolver);


    @Test
    public void shouldConfigStringResolverInitialized()
    {
        Assertions.assertThat(this.config.getConfigStringResolver()).isNotNull();
    }


    @Test
    public void shouldReturnTargetConfiguredValueAccordingToCofiguredKey()
    {
        Mockito.when(this.resolver.resolveConfigStringParameter("key")).thenReturn("value");
        Assertions.assertThat(this.config.getConfigurationRoots("key")).isEqualTo("value");
    }
}
