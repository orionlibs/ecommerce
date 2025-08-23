package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import org.springframework.beans.factory.annotation.Required;

public class EnumerationDelegate
{
    private EnumerationManager enumerationManager;


    @Required
    public void setEnumerationManager(EnumerationManager enumerationManager)
    {
        this.enumerationManager = enumerationManager;
    }


    public PK getPK(HybrisEnumValue enumeration) throws IllegalArgumentException
    {
        EnumerationValue value = this.enumerationManager.getEnumerationValue(enumeration.getType(), enumeration.getCode());
        if(value != null)
        {
            return value.getPK();
        }
        throw new IllegalArgumentException("Could not get value for Enumeration: " + enumeration.toString());
    }
}
