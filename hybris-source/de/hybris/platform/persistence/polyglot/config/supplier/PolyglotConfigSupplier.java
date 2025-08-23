package de.hybris.platform.persistence.polyglot.config.supplier;

import java.util.Set;

public interface PolyglotConfigSupplier
{
    Set<String> getRepositoryNames();


    String getBeanName(String paramString);


    Set<PropertyTypeCodeDefinition> getTypeCodeDefinitions(String paramString);
}
