package de.hybris.platform.cockpit.services.meta;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;

public interface PropertyService
{
    String getDefaultEditorType(String paramString, boolean paramBoolean);


    String getMultiplicityString(PropertyDescriptor paramPropertyDescriptor);


    boolean isInitial(PropertyDescriptor paramPropertyDescriptor);


    boolean isPartof(PropertyDescriptor paramPropertyDescriptor);


    boolean isMandatory(PropertyDescriptor paramPropertyDescriptor, boolean paramBoolean);
}
