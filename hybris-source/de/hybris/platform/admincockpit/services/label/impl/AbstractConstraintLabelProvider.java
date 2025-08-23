package de.hybris.platform.admincockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.validation.jalo.constraints.AbstractConstraint;

public class AbstractConstraintLabelProvider extends AbstractObjectLabelProvider<AbstractConstraint>
{
    protected String getItemLabel(AbstractConstraint item)
    {
        return item.getComposedType().getName() + " (" + item.getComposedType().getName() + ")";
    }


    protected String getItemLabel(AbstractConstraint item, String languageIso)
    {
        return getItemLabel(item);
    }


    protected String getIconPath(AbstractConstraint item)
    {
        return null;
    }


    protected String getIconPath(AbstractConstraint item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(AbstractConstraint item)
    {
        return null;
    }


    protected String getItemDescription(AbstractConstraint item, String languageIso)
    {
        return null;
    }
}
