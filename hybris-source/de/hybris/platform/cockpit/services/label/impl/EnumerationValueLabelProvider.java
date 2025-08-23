package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.enumeration.EnumerationValue;

@Deprecated
public class EnumerationValueLabelProvider extends AbstractObjectLabelProvider<EnumerationValue>
{
    protected String getItemLabel(EnumerationValue enumValue)
    {
        return enumValue.getName();
    }


    protected String getItemLabel(EnumerationValue enumValue, String languageIso)
    {
        return getItemLabel(enumValue);
    }


    protected String getIconPath(EnumerationValue item)
    {
        return null;
    }


    protected String getIconPath(EnumerationValue item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(EnumerationValue item)
    {
        return "";
    }


    protected String getItemDescription(EnumerationValue item, String languageIso)
    {
        return "";
    }
}
