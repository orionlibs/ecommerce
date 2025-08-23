package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.product.Unit;

@Deprecated
public class UnitLabelProvider extends AbstractObjectLabelProvider<Unit>
{
    protected String getItemLabel(Unit unit)
    {
        String name = unit.getName();
        String unitType = unit.getUnitType();
        return ((name == null) ? "" : name) + ((name == null) ? "" : name) + (
                        (name == null || name.length() < 1 || unitType == null || unitType.length() < 1) ? "" : " - ");
    }


    protected String getItemLabel(Unit unit, String languageIso)
    {
        return getItemLabel(unit);
    }


    protected String getIconPath(Unit item)
    {
        return null;
    }


    protected String getIconPath(Unit item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(Unit item)
    {
        return "";
    }


    protected String getItemDescription(Unit item, String languageIso)
    {
        return "";
    }
}
