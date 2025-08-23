package de.hybris.platform.admincockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.validation.jalo.constraints.ConstraintGroup;
import org.zkoss.util.resource.Labels;

public class ConstraintGroupLabelProvider extends AbstractObjectLabelProvider<ConstraintGroup>
{
    private static final String DEFAULT_CONSTRAINT_LABEL = "ea.constrain_group_default_label";


    protected String getItemLabel(ConstraintGroup group)
    {
        if(group == null)
        {
            return Labels.getLabel("ea.constrain_group_default_label");
        }
        return group.getId();
    }


    protected String getItemLabel(ConstraintGroup group, String languageIso)
    {
        return getItemLabel(group);
    }


    protected String getIconPath(ConstraintGroup item)
    {
        return null;
    }


    protected String getIconPath(ConstraintGroup item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(ConstraintGroup item)
    {
        return "";
    }


    protected String getItemDescription(ConstraintGroup item, String languageIso)
    {
        return "";
    }
}
