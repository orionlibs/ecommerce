package de.hybris.platform.persistence.polyglot.config;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class RepositoryConfigUtils
{
    public static boolean isKeyUsedInConfig(ItemStateRepositoryFactory itemStateRepositoryFactory, TypeInfo typeInfo, String qualifier)
    {
        Objects.requireNonNull(itemStateRepositoryFactory);
        return itemStateRepositoryFactory.getRepository(typeInfo).getRepositories().stream().map(itemStateRepositoryFactory::getRepositoryConfig)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(RepositoryConfig::getConditions)
                        .filter(Objects::nonNull)
                        .flatMap(c -> ((Set)c.getOrDefault(Integer.valueOf(typeInfo.getTypeCode()), Set.of())).stream())
                        .filter(Objects::nonNull)
                        .anyMatch(con -> con.getQualifier().equalsIgnoreCase(qualifier));
    }
}
