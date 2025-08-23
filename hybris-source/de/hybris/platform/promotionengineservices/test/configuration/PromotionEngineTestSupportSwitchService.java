package de.hybris.platform.promotionengineservices.test.configuration;

import de.hybris.platform.ruleengineservices.configuration.Switch;
import de.hybris.platform.ruleengineservices.configuration.impl.DefaultSwitchService;

public class PromotionEngineTestSupportSwitchService extends DefaultSwitchService
{
    public void enable(Switch option)
    {
        getStatuses().put(option, Boolean.TRUE);
    }


    public void disable(Switch option)
    {
        getStatuses().put(option, Boolean.FALSE);
    }


    public Boolean set(Switch option, Boolean value)
    {
        return getStatuses().put(option, value);
    }
}
