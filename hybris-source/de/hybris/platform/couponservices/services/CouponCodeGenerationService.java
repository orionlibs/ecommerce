package de.hybris.platform.couponservices.services;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import java.util.Optional;

public interface CouponCodeGenerationService
{
    String generateCouponSignature();


    String generateCouponAlphabet();


    String generateCouponCode(MultiCodeCouponModel paramMultiCodeCouponModel);


    String extractCouponPrefix(String paramString);


    boolean verifyCouponCode(MultiCodeCouponModel paramMultiCodeCouponModel, String paramString);


    boolean isValidCodeSeparator(String paramString);


    Optional<MediaModel> generateCouponCodes(MultiCodeCouponModel paramMultiCodeCouponModel, int paramInt);
}
