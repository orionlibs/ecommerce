package de.hybris.platform.persistence.polyglot.config;

import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.model.Identity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class PolyglotRepositoriesConfigProviderBuilder
{
    private final List<RepositoryConfig> configs = new ArrayList<>();
    private ItemStateRepository currentRepository = null;
    private Set<MoreSpecificCondition> currentConditions = new HashSet<>();
    private Map<Integer, Set<MoreSpecificCondition>> currentConfig = new HashMap<>();
    private int currentTypeCode = 0;


    public PolyglotRepositoriesConfigProviderBuilder newRepository(ItemStateRepository repository)
    {
        addRepository();
        this.currentRepository = repository;
        this.currentConditions = new HashSet<>();
        this.currentTypeCode = 0;
        this.currentConfig = new HashMap<>();
        return this;
    }


    private void addRepository()
    {
        if(this.currentRepository != null)
        {
            putTypeCode();
            DefaultRepositoryConfig defaultRepositoryConfig = new DefaultRepositoryConfig(this.currentConfig, this.currentRepository);
            this.configs.add(defaultRepositoryConfig);
        }
    }


    public List<RepositoryConfig> build()
    {
        addRepository();
        return this.configs;
    }


    public PolyglotRepositoriesConfigProviderBuilder withTypecode(int newTypecode)
    {
        if(this.currentRepository == null)
        {
            throw new IllegalArgumentException("set repository first");
        }
        putTypeCode();
        this.currentTypeCode = newTypecode;
        this.currentConditions = new HashSet<>();
        return this;
    }


    private void putTypeCode()
    {
        if(this.currentTypeCode > 0)
        {
            this.currentConfig.put(Integer.valueOf(this.currentTypeCode), this.currentConditions);
        }
    }


    public PolyglotRepositoriesConfigProviderBuilder withCondition(long identity, String condition)
    {
        if(this.currentTypeCode < 1)
        {
            throw new IllegalArgumentException("set typecode first");
        }
        this.currentConditions.add(new MoreSpecificCondition(getIdentity(identity), condition, null));
        return this;
    }


    private Identity getIdentity(long id)
    {
        if(id >= 0L)
        {
            return PolyglotPersistence.identityFromLong(id);
        }
        return PolyglotPersistence.unknownIdentity();
    }
}
