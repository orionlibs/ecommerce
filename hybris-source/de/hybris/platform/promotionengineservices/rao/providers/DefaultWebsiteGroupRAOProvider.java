package de.hybris.platform.promotionengineservices.rao.providers;

import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengineservices.rao.WebsiteGroupRAO;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Collections;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWebsiteGroupRAOProvider implements RAOProvider
{
    private Converter<PromotionGroupModel, WebsiteGroupRAO> websiteGroupRaoConverter;


    protected WebsiteGroupRAO createRAO(PromotionGroupModel source)
    {
        return (WebsiteGroupRAO)getWebsiteGroupRaoConverter().convert(source);
    }


    protected Converter<PromotionGroupModel, WebsiteGroupRAO> getWebsiteGroupRaoConverter()
    {
        return this.websiteGroupRaoConverter;
    }


    @Required
    public void setWebsiteGroupRaoConverter(Converter<PromotionGroupModel, WebsiteGroupRAO> websiteGroupRaoConverter)
    {
        this.websiteGroupRaoConverter = websiteGroupRaoConverter;
    }


    public Set<?> expandFactModel(Object modelFact)
    {
        if(modelFact instanceof PromotionGroupModel)
        {
            return Collections.singleton(createRAO((PromotionGroupModel)modelFact));
        }
        return Collections.emptySet();
    }
}
