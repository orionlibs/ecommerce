package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import java.util.List;

public interface CustomEditorSectionConfiguration extends EditorSectionConfiguration
{
    void initialize(EditorConfiguration paramEditorConfiguration, ObjectType paramObjectType, TypedObject paramTypedObject);


    void allInitialized(EditorConfiguration paramEditorConfiguration, ObjectType paramObjectType, TypedObject paramTypedObject);


    void loadValues(EditorConfiguration paramEditorConfiguration, ObjectType paramObjectType, TypedObject paramTypedObject, ObjectValueContainer paramObjectValueContainer);


    void saveValues(EditorConfiguration paramEditorConfiguration, ObjectType paramObjectType, TypedObject paramTypedObject, ObjectValueContainer paramObjectValueContainer);


    List<EditorSectionConfiguration> getAdditionalSections();


    SectionRenderer getCustomRenderer();
}
