package de.hybris.platform.processengine.definition;

import org.springframework.core.io.Resource;

public class ProcessDefinitionResource
{
    private Resource resource;


    public void setResource(Resource resource)
    {
        this.resource = resource;
    }


    public Resource getResource()
    {
        return this.resource;
    }
}
