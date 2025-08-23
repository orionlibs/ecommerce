package de.hybris.platform.promotionengineservices.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengineservices.rao.WebsiteGroupRAO;

public class WebsiteGroupRaoPopulator implements Populator<PromotionGroupModel, WebsiteGroupRAO>
{
    public void populate(PromotionGroupModel source, WebsiteGroupRAO target)
    {
        target.setId(source.getIdentifier());
    }
}
