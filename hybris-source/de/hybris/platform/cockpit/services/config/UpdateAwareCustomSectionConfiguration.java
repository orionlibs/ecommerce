package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.Set;

public interface UpdateAwareCustomSectionConfiguration extends CustomEditorSectionConfiguration
{
    Set<PropertyDescriptor> getUpdateTriggerProperties();


    Set<ObjectType> getUpdateTriggerTypes();
}
