package de.hybris.bootstrap.codegenerator.platformwebservices.resource;

import de.hybris.bootstrap.codegenerator.platformwebservices.DtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.ResourceConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.typesystem.YType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Deprecated(since = "1818", forRemoval = true)
public class SingleResourceConfig extends AbstractResourceConfig
{
    private final SubResourceResolver subresolver;
    protected Collection<ResourceConfig> subresources = null;


    public SingleResourceConfig(DtoConfig dtoConfig, String resourcePackage, WebservicesConfig provider)
    {
        super(dtoConfig, resourcePackage, provider);
        setResourceClassName(getPackageName() + "." + getPackageName() + "Resource");
        this.subresolver = provider.getSubresourceResolver();
        setGetSupport(true);
        setPostSupport(false);
        setPutSupport(true);
        setDeleteSupport(true);
    }


    public Collection<ResourceConfig> getSubResources()
    {
        if(this.subresources == null)
        {
            this.subresources = new ArrayList<>();
            Map<YType, String> subresources = this.subresolver.getAllSubResources(getDTOConfig().getType(), this.provider);
            for(YType yType : subresources.keySet())
            {
                ResourceConfig cfg = this.provider.getResourceConfig(yType);
                this.subresources.add(cfg);
            }
        }
        return this.subresources;
    }
}
