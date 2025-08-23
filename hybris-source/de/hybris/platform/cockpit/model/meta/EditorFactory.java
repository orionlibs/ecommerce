package de.hybris.platform.cockpit.model.meta;

import java.util.Collection;

public interface EditorFactory
{
    Collection<PropertyEditorDescriptor> getMatchingEditorDescriptors(String paramString);


    Collection<PropertyEditorDescriptor> getAllEditorDescriptors();
}
