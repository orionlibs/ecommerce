package de.hybris.platform.scripting.engine.repository.impl;

import de.hybris.platform.scripting.engine.repository.ScriptRepositoriesRegistry;
import de.hybris.platform.scripting.engine.repository.ScriptsRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultScriptRepositoriesRegistry implements ScriptRepositoriesRegistry, InitializingBean
{
    @Autowired
    private Set<ScriptsRepository> repositories;
    private Map<String, ScriptsRepository> protocol2repoMap;


    public ScriptsRepository getRepositoryByProtocol(String protocol)
    {
        if(protocol == null)
        {
            throw new IllegalArgumentException("protocol is required for checking registered repositories");
        }
        ScriptsRepository repo = this.protocol2repoMap.get(protocol);
        if(repo == null)
        {
            throw new IllegalArgumentException("No repository registered for protocol: " + protocol);
        }
        return repo;
    }


    public void afterPropertiesSet() throws Exception
    {
        this.protocol2repoMap = new HashMap<>(this.repositories.size() * 2);
        for(ScriptsRepository repo : this.repositories)
        {
            for(String proto : repo.getSupportedProtocols())
            {
                this.protocol2repoMap.put(proto, repo);
            }
        }
    }
}
