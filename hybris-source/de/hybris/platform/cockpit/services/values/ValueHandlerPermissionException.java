package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.Set;

public class ValueHandlerPermissionException extends ValueHandlerException
{
    public ValueHandlerPermissionException(String message, Set<? extends PropertyDescriptor> relatedProperties)
    {
        super(message, relatedProperties);
    }


    public ValueHandlerPermissionException(String message, Throwable cause, Set<PropertyDescriptor> relatedProperties)
    {
        super(message, cause, relatedProperties);
    }
}
