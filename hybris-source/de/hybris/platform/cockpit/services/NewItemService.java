package de.hybris.platform.cockpit.services;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import java.util.Set;

public interface NewItemService
{
    void setDefaultValues(ObjectValueContainer paramObjectValueContainer, ObjectTemplate paramObjectTemplate);


    TypedObject createNewItem(ObjectValueContainer paramObjectValueContainer, ObjectTemplate paramObjectTemplate) throws ValueHandlerException;


    Set<PropertyDescriptor> getMandatoryAttributes(ObjectType paramObjectType);
}
