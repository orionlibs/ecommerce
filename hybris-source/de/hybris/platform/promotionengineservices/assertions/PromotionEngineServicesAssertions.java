package de.hybris.platform.promotionengineservices.assertions;

import de.hybris.platform.promotionengineservices.promotionengine.report.data.PromotionEngineResult;
import org.assertj.core.api.Assertions;

public class PromotionEngineServicesAssertions extends Assertions
{
    public static PromotionResultAssert assertThat(PromotionEngineResult actual)
    {
        return new PromotionResultAssert(actual);
    }
}
