package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.Set;

public class ValueHandlerWorkflowException extends ValueHandlerException
{
    public ValueHandlerWorkflowException(String message, Throwable cause, Set<PropertyDescriptor> relatedProperties)
    {
        super(message, cause, relatedProperties);
    }


    public ValueHandlerWorkflowException(String message, Set<PropertyDescriptor> relatedProperties)
    {
        super(message, relatedProperties);
    }
}
