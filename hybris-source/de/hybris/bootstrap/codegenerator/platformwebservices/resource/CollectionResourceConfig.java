package de.hybris.bootstrap.codegenerator.platformwebservices.resource;

import de.hybris.bootstrap.codegenerator.platformwebservices.DtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.ResourceConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.dto.CollectionDtoConfig;
import java.util.Collection;
import java.util.Collections;

@Deprecated(since = "1818", forRemoval = true)
public class CollectionResourceConfig extends AbstractResourceConfig
{
    public CollectionResourceConfig(CollectionDtoConfig dtoConfig, String resourcePackage, WebservicesConfig provider)
    {
        super((DtoConfig)dtoConfig, resourcePackage, provider);
        setResourceClassName(getPackageName() + "." + getPackageName() + "Resource");
        setGetSupport(true);
        setPostSupport(false);
        setPutSupport(false);
        setDeleteSupport(false);
    }


    public boolean isPostSupport()
    {
        return (super.isPostSupport() || "pk"
                        .equals(getResourceConfigProvider().getUidResover().getUniqueIdentifier(getDTOConfig().getType())));
    }


    public Collection<ResourceConfig> getSubResources()
    {
        SingleResourceConfig singleResourceConfig = this.provider.getSingleResourceConfig(getDTOConfig().getType());
        return (Collection)Collections.singletonList(singleResourceConfig);
    }


    public CollectionDtoConfig getDTOConfig()
    {
        return (CollectionDtoConfig)super.getDTOConfig();
    }
}
