package de.hybris.platform.personalizationfacades.action.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.ActionData;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class ActionReversePopulator implements Populator<ActionData, CxAbstractActionModel>
{
    public void populate(ActionData source, CxAbstractActionModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        if(StringUtils.isEmpty(target.getCode()))
        {
            target.setCode(source.getCode());
        }
    }
}
