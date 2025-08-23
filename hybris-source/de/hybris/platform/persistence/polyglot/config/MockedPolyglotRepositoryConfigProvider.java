package de.hybris.platform.persistence.polyglot.config;

import java.util.List;

public class MockedPolyglotRepositoryConfigProvider implements PolyglotRepositoriesConfigProvider
{
    private final List<RepositoryConfig> configs;


    public MockedPolyglotRepositoryConfigProvider(List<RepositoryConfig> configs)
    {
        this.configs = configs;
    }


    public List<RepositoryConfig> getConfigs()
    {
        return this.configs;
    }
}
