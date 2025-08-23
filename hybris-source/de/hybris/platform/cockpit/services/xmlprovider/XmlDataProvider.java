package de.hybris.platform.cockpit.services.xmlprovider;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;

public interface XmlDataProvider
{
    Object generateAsXml(EditorSectionConfiguration paramEditorSectionConfiguration, TypedObject paramTypedObject);


    Object generateAsXml(EditorRowConfiguration paramEditorRowConfiguration, TypedObject paramTypedObject);
}
