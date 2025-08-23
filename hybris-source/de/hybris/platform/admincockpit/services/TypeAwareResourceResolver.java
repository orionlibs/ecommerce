package de.hybris.platform.admincockpit.services;

import de.hybris.platform.cockpit.model.meta.BaseType;

public interface TypeAwareResourceResolver<Resource>
{
    Resource getResourceForType(BaseType paramBaseType);


    Resource getDefaultResource();
}
