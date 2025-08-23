package de.hybris.platform.personalizationpromotionsweb.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationpromotions.model.CxPromotionActionModel;
import de.hybris.platform.personalizationpromotionsweb.data.CxPromotionActionData;

public class CxPromotionActionReversePopulator implements Populator<CxPromotionActionData, CxPromotionActionModel>
{
    public void populate(CxPromotionActionData source, CxPromotionActionModel target)
    {
        target.setPromotionId(source.getPromotionId());
    }
}
