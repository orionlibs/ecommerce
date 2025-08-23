package de.hybris.bootstrap.codegenerator.platformwebservices.dto;

import de.hybris.bootstrap.codegenerator.platformwebservices.DtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.typesystem.YComposedType;

@Deprecated(since = "1818", forRemoval = true)
public class CollectionDtoConfig extends AbstractDtoConfig
{
    private boolean isChildCfgResolved = false;
    private DtoConfig childDtoConfig = null;


    public CollectionDtoConfig(YComposedType type, String modelClassName, String dtoClassName, String plural, WebservicesConfig provider)
    {
        super(type, modelClassName, dtoClassName, plural, provider);
    }


    public DtoConfig getCollectionElementConfig()
    {
        if(!this.isChildCfgResolved)
        {
            this.isChildCfgResolved = true;
            this.childDtoConfig = (DtoConfig)this.provider.getSingleDtoConfig(getType());
        }
        return this.childDtoConfig;
    }
}
