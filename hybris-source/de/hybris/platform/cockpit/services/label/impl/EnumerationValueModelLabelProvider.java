package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;

public class EnumerationValueModelLabelProvider extends AbstractModelLabelProvider<EnumerationValueModel>
{
    protected String getItemLabel(EnumerationValueModel enumValue)
    {
        return enumValue.getName();
    }


    protected String getItemLabel(EnumerationValueModel enumValue, String languageIso)
    {
        return getItemLabel(enumValue);
    }


    protected String getIconPath(EnumerationValueModel item)
    {
        return null;
    }


    protected String getIconPath(EnumerationValueModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(EnumerationValueModel item)
    {
        return "";
    }


    protected String getItemDescription(EnumerationValueModel item, String languageIso)
    {
        return "";
    }
}
