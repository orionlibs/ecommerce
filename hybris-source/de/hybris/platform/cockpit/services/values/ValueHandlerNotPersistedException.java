package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import java.util.Collections;
import java.util.Set;

public class ValueHandlerNotPersistedException extends ValueHandlerException
{
    public ValueHandlerNotPersistedException(String message)
    {
        super(message, Collections.EMPTY_SET);
    }


    public ValueHandlerNotPersistedException(String message, Set<PropertyDescriptor> descriptors)
    {
        super(message, descriptors);
    }


    public ValueHandlerNotPersistedException(Set<CockpitValidationDescriptor> properties)
    {
        this(null, properties);
    }


    public ValueHandlerNotPersistedException(Throwable cause, Set<CockpitValidationDescriptor> properties)
    {
        super(cause, properties);
    }
}
