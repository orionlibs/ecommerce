package de.hybris.platform.couponwebservices.facades;

import de.hybris.platform.core.model.media.MediaModel;
import java.util.Collection;
import java.util.Optional;

public interface CouponCodeGenerationWsFacade
{
    Optional<MediaModel> generateCouponCodes(String paramString, int paramInt);


    Collection<MediaModel> getCouponCodeBatches(String paramString);


    byte[] getCouponCodes(String paramString1, String paramString2);
}
