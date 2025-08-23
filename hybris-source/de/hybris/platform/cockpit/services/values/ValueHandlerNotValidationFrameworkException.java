package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import java.util.Collections;
import java.util.Set;

public class ValueHandlerNotValidationFrameworkException extends ValueHandlerException
{
    public ValueHandlerNotValidationFrameworkException(String message, Throwable cause, Set<CockpitValidationDescriptor> properties)
    {
        super(message, cause);
        setProperties((properties == null) ? Collections.emptySet() : properties);
    }
}
