package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.Map;

public interface DefaultPropertySettings
{
    PropertyDescriptor getPropertyDescriptor();


    boolean isBaseProperty();


    String getDefaultEditorCode();


    Map<String, String> getParameters();
}
