package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.List;

public interface EditorConfiguration extends UIComponentConfiguration, Cloneable
{
    List<EditorSectionConfiguration> getSections();


    void setSections(List<EditorSectionConfiguration> paramList);


    List<PropertyDescriptor> getAllPropertyDescriptors();


    EditorConfiguration clone() throws CloneNotSupportedException;
}
