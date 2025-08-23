package de.hybris.platform.personalizationfacades.trigger.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.TriggerData;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class TriggerReversePopulator implements Populator<TriggerData, CxAbstractTriggerModel>
{
    public void populate(TriggerData source, CxAbstractTriggerModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        if(StringUtils.isEmpty(target.getCode()))
        {
            target.setCode(source.getCode());
        }
    }
}
