package de.hybris.platform.persistence.polyglot.config;

import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ItemStateRepositoryFactory
{
    private final PolyglotRepositoriesConfigProvider configProvider;


    public ItemStateRepositoryFactory(PolyglotRepositoriesConfigProvider configProvider)
    {
        this.configProvider = configProvider;
    }


    public RepositoryResult getRepository(TypeInfo typeInfo)
    {
        if(typeInfo == null)
        {
            return RepositoryResult.empty();
        }
        List<RepositoryConfig> repositoriesConfigs = this.configProvider.getConfigs();
        Map<ItemStateRepository, PolyglotRepoSupportType> repositories = new HashMap<>();
        for(RepositoryConfig repositoryConfig : repositoriesConfigs)
        {
            PolyglotRepoSupportType supported = repositoryConfig.isSupportedBy(typeInfo);
            if(supported != PolyglotRepoSupportType.NONE)
            {
                repositories.put(repositoryConfig.getRepository(), supported);
            }
        }
        if(repositories.isEmpty())
        {
            return RepositoryResult.empty();
        }
        if(repositories.entrySet().stream().anyMatch(e -> (e.getValue() == PolyglotRepoSupportType.PARTIAL)))
        {
            return new RepositoryResult(repositories.keySet(), PolyglotRepoSupportType.PARTIAL);
        }
        return new RepositoryResult(repositories.keySet(), PolyglotRepoSupportType.FULL);
    }


    public Optional<RepositoryConfig> getRepositoryConfig(ItemStateRepository repository)
    {
        Objects.requireNonNull(repository);
        return this.configProvider.getConfigs()
                        .stream()
                        .filter(c -> c.getRepository().equals(repository))
                        .findFirst();
    }


    public boolean isKeyUsedInConfig(TypeInfo typeInfo, String qualifier)
    {
        return RepositoryConfigUtils.isKeyUsedInConfig(this, typeInfo, qualifier);
    }
}
