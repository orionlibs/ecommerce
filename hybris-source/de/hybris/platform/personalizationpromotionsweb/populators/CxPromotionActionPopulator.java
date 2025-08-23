package de.hybris.platform.personalizationpromotionsweb.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationpromotions.model.CxPromotionActionModel;
import de.hybris.platform.personalizationpromotionsweb.data.CxPromotionActionData;

public class CxPromotionActionPopulator implements Populator<CxPromotionActionModel, CxPromotionActionData>
{
    public void populate(CxPromotionActionModel source, CxPromotionActionData target)
    {
        target.setPromotionId(source.getPromotionId());
    }
}
