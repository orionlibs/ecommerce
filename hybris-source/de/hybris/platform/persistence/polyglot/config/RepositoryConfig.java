package de.hybris.platform.persistence.polyglot.config;

import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import java.util.Map;
import java.util.Set;

public interface RepositoryConfig
{
    PolyglotRepoSupportType isSupportedBy(TypeInfo paramTypeInfo);


    ItemStateRepository getRepository();


    Map<Integer, Set<MoreSpecificCondition>> getConditions();
}
