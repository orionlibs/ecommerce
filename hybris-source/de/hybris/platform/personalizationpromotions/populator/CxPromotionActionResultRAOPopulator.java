package de.hybris.platform.personalizationpromotions.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationpromotions.data.CxPromotionActionResult;
import de.hybris.platform.personalizationpromotions.rao.CxPromotionActionResultRAO;

public class CxPromotionActionResultRAOPopulator implements Populator<CxPromotionActionResult, CxPromotionActionResultRAO>
{
    public void populate(CxPromotionActionResult source, CxPromotionActionResultRAO target)
    {
        target.setPromotionId(source.getPromotionId());
    }
}
