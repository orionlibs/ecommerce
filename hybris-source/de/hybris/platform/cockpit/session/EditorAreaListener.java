package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;

public interface EditorAreaListener
{
    void currentObjectChanged(TypedObject paramTypedObject1, TypedObject paramTypedObject2);


    void currentObjectUpdated();


    void valuesStored(ObjectValueContainer paramObjectValueContainer);


    void valuesUpdated(ObjectValueContainer paramObjectValueContainer);


    void valueChanged(ObjectValueContainer paramObjectValueContainer, PropertyDescriptor paramPropertyDescriptor);


    void nextItemClicked(TypedObject paramTypedObject);


    void previousItemClicked(TypedObject paramTypedObject);


    void browseItemClicked(TypedObject paramTypedObject);
}
