package de.hybris.platform.scripting.engine.repository.impl;

import de.hybris.platform.scripting.engine.repository.ScriptsRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractScriptsRepository implements ScriptsRepository
{
    private Collection<String> protocols;


    @Required
    public void setSupportedProtocols(Collection<String> protocols)
    {
        this.protocols = protocols;
    }


    public Collection<String> getSupportedProtocols()
    {
        return this.protocols;
    }
}
