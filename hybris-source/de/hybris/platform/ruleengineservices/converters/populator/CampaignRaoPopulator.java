package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ruleengineservices.rao.CampaignRAO;

public class CampaignRaoPopulator implements Populator<CampaignModel, CampaignRAO>
{
    public void populate(CampaignModel source, CampaignRAO target)
    {
        target.setCode(source.getCode());
    }
}
