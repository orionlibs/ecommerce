package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Set;

public interface ValueService
{
    Object getValue(TypedObject paramTypedObject, PropertyDescriptor paramPropertyDescriptor) throws ValueHandlerException;


    void setValue(TypedObject paramTypedObject, PropertyDescriptor paramPropertyDescriptor, Object paramObject) throws ValueHandlerException;


    void setValue(TypedObject paramTypedObject, PropertyDescriptor paramPropertyDescriptor, Object paramObject, String paramString) throws ValueHandlerException;


    ObjectValueContainer getValues(TypedObject paramTypedObject, Set<PropertyDescriptor> paramSet, Set<String> paramSet1) throws ValueHandlerException;


    void setValues(TypedObject paramTypedObject, ObjectValueContainer paramObjectValueContainer) throws ValueHandlerException;


    void updateValues(TypedObject paramTypedObject, ObjectValueContainer paramObjectValueContainer) throws ValueHandlerException;
}
