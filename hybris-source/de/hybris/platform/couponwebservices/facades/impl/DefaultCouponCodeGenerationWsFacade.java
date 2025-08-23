package de.hybris.platform.couponwebservices.facades.impl;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import de.hybris.platform.couponwebservices.CouponCodesNotFoundWsException;
import de.hybris.platform.couponwebservices.facades.CouponCodeGenerationWsFacade;
import de.hybris.platform.couponwebservices.util.CouponWsUtils;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCouponCodeGenerationWsFacade implements CouponCodeGenerationWsFacade
{
    private static final String NO_COUPON_CODE_FOUND_TEMPLATE = "No codes found for couponId [%s] and batchCode [%s]";
    public static final String COUPON_ID = "couponId";
    private CouponCodeGenerationService couponCodeGenerationService;
    private CouponDao couponDao;
    private MediaService mediaService;
    private CouponWsUtils couponWsUtils;


    public Optional<MediaModel> generateCouponCodes(String couponId, int batchsize)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("couponId", couponId);
        MultiCodeCouponModel multiCodeCoupon = getCouponWsUtils().getValidMultiCodeCoupon(couponId);
        return getCouponCodeGenerationService().generateCouponCodes(multiCodeCoupon, batchsize);
    }


    public Collection<MediaModel> getCouponCodeBatches(String couponId)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("couponId", couponId);
        return getCouponWsUtils().getValidMultiCodeCoupon(couponId).getGeneratedCodes();
    }


    public byte[] getCouponCodes(String couponId, String batchCode)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("couponId", couponId);
        ServicesUtil.validateParameterNotNullStandardMessage("batchCode", batchCode);
        Collection<MediaModel> couponCodeBatches = getCouponCodeBatches(couponId);
        if(CollectionUtils.isNotEmpty(couponCodeBatches))
        {
            MediaModel couponCodesMediaModel = (MediaModel)couponCodeBatches.stream().filter(b -> batchCode.equals(b.getCode())).findFirst()
                            .orElseThrow(() -> new CouponCodesNotFoundWsException(String.format("No codes found for couponId [%s] and batchCode [%s]", new Object[] {couponId, batchCode})));
            return getMediaService().getDataFromMedia(couponCodesMediaModel);
        }
        throw new CouponCodesNotFoundWsException(String.format("No codes found for couponId [%s] and batchCode [%s]", new Object[] {couponId, batchCode}));
    }


    protected CouponCodeGenerationService getCouponCodeGenerationService()
    {
        return this.couponCodeGenerationService;
    }


    @Required
    public void setCouponCodeGenerationService(CouponCodeGenerationService couponCodeGenerationService)
    {
        this.couponCodeGenerationService = couponCodeGenerationService;
    }


    protected CouponDao getCouponDao()
    {
        return this.couponDao;
    }


    @Required
    public void setCouponDao(CouponDao couponDao)
    {
        this.couponDao = couponDao;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    protected CouponWsUtils getCouponWsUtils()
    {
        return this.couponWsUtils;
    }


    @Required
    public void setCouponWsUtils(CouponWsUtils couponWsUtils)
    {
        this.couponWsUtils = couponWsUtils;
    }
}
