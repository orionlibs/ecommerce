package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.core.model.product.UnitModel;

public class UnitModelLabelProvider extends AbstractModelLabelProvider<UnitModel>
{
    protected String getItemLabel(UnitModel unit)
    {
        String name = unit.getName();
        String unitType = unit.getUnitType();
        return ((name == null) ? "" : name) + ((name == null) ? "" : name) + (
                        (name == null || name.length() < 1 || unitType == null || unitType.length() < 1) ? "" : " - ");
    }


    protected String getItemLabel(UnitModel unit, String languageIso)
    {
        return getItemLabel(unit);
    }


    protected String getIconPath(UnitModel item)
    {
        return null;
    }


    protected String getIconPath(UnitModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(UnitModel item)
    {
        return "";
    }


    protected String getItemDescription(UnitModel item, String languageIso)
    {
        return "";
    }
}
