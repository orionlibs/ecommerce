package de.hybris.platform.personalizationpromotionsweb.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationpromotionsweb.data.PromotionRuleWsDTO;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;

public class CxPromotionRulePopulator implements Populator<PromotionSourceRuleModel, PromotionRuleWsDTO>
{
    public void populate(PromotionSourceRuleModel source, PromotionRuleWsDTO target)
    {
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setStatus(source.getStatus().getCode());
        if(source.getWebsite() != null)
        {
            target.setPromotionGroup(source.getWebsite().getIdentifier());
        }
    }
}
