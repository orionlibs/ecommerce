package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.List;

public interface AvailableValuesProvider
{
    List<? extends Object> getAvailableValues(PropertyDescriptor paramPropertyDescriptor);
}
