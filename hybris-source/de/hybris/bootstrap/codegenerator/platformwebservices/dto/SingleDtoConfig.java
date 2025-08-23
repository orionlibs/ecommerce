package de.hybris.bootstrap.codegenerator.platformwebservices.dto;

import de.hybris.bootstrap.codegenerator.platformwebservices.DtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.typesystem.YComposedType;

@Deprecated(since = "1818", forRemoval = true)
public class SingleDtoConfig extends AbstractDtoConfig
{
    private DtoConfig parentDtoConfig = null;
    private boolean isParentDtoConfigResolved = false;


    public SingleDtoConfig(YComposedType type, String modelClassName, String dtoClassName, String plural, WebservicesConfig provider)
    {
        super(type, modelClassName, dtoClassName, plural, provider);
    }


    public DtoConfig getParentConfig()
    {
        if(!this.isParentDtoConfigResolved)
        {
            this.isParentDtoConfigResolved = true;
            YComposedType superType = getType().getSuperType();
            if(superType != null)
            {
                this.parentDtoConfig = (DtoConfig)this.provider.getSingleDtoConfig(superType);
            }
        }
        return this.parentDtoConfig;
    }
}
