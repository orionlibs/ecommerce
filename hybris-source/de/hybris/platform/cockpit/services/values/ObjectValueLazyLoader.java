package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;

public interface ObjectValueLazyLoader
{
    void loadValue(ObjectValueContainer paramObjectValueContainer, PropertyDescriptor paramPropertyDescriptor, String paramString);
}
