package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;

public interface ValueHandler
{
    public static final Object NOT_READABLE_VALUE = new NotReadableValue();


    void setValue(TypedObject paramTypedObject, Object paramObject) throws ValueHandlerException;


    void setValue(TypedObject paramTypedObject, Object paramObject, String paramString) throws ValueHandlerException;


    Object getValue(TypedObject paramTypedObject) throws ValueHandlerException;


    Object getValue(TypedObject paramTypedObject, String paramString) throws ValueHandlerException;
}
