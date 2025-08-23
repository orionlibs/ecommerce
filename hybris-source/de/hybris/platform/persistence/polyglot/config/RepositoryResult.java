package de.hybris.platform.persistence.polyglot.config;

import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import java.util.Collections;
import java.util.Set;

public class RepositoryResult
{
    private final Set<ItemStateRepository> repositories;
    private final PolyglotRepoSupportType repositoriesSupportType;


    public RepositoryResult(Set<ItemStateRepository> repositories, PolyglotRepoSupportType repositoriesSupportType)
    {
        this.repositories = repositories;
        this.repositoriesSupportType = repositoriesSupportType;
    }


    public static RepositoryResult empty()
    {
        return new RepositoryResult(Collections.emptySet(), PolyglotRepoSupportType.NONE);
    }


    public Set<ItemStateRepository> getRepositories()
    {
        return this.repositories;
    }


    public ItemStateRepository requireSingleRepositoryOnly()
    {
        if(hasSingleRepositoryOnly())
        {
            return this.repositories.iterator().next();
        }
        throw new NoStrictPolyglotPersistenceRepositoryException("Single strict repository required");
    }


    private boolean hasSingleRepositoryOnly()
    {
        return (getRepositories() != null && this.repositories.size() == 1 &&
                        getRepositoriesSupportType() == PolyglotRepoSupportType.FULL);
    }


    public boolean isFullySupported()
    {
        return (getRepositoriesSupportType() == PolyglotRepoSupportType.FULL);
    }


    public boolean isPartiallySupported()
    {
        return (getRepositoriesSupportType() == PolyglotRepoSupportType.PARTIAL);
    }


    public boolean isNotSupported()
    {
        return (getRepositoriesSupportType() == PolyglotRepoSupportType.NONE);
    }


    private PolyglotRepoSupportType getRepositoriesSupportType()
    {
        return this.repositoriesSupportType;
    }
}
