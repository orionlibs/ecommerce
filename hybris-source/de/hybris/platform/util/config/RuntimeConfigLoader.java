package de.hybris.platform.util.config;

import java.util.Optional;
import java.util.Properties;

public interface RuntimeConfigLoader
{
    Optional<Properties> load();
}
