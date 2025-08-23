package de.hybris.platform.persistence.polyglot.config;

import java.util.List;

public interface PolyglotRepositoriesConfigProvider
{
    List<RepositoryConfig> getConfigs();
}
