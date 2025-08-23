package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import java.util.Collection;
import java.util.Map;

public interface InitialPropertyConfiguration
{
    ObjectTemplate getSourceObjectTemplate();


    Collection<PropertyMappingConfiguration> getPropertyMappingConfigurations();


    Map<String, Object> getInitialProperties(TypedObject paramTypedObject, TypeService paramTypeService);
}
