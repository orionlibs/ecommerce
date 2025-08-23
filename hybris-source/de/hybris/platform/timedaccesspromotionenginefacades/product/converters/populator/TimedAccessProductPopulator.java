package de.hybris.platform.timedaccesspromotionenginefacades.product.converters.populator;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.timedaccesspromotionengineservices.FlashBuyService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.Assert;

public class TimedAccessProductPopulator implements Populator<ProductModel, ProductData>
{
    private final PromotionsService promotionsService;
    private final TimeService timeService;
    private final BaseSiteService baseSiteService;
    private final FlashBuyService flashBuyService;
    private Converter<AbstractPromotionModel, PromotionData> promotionsConverter;


    public TimedAccessProductPopulator(PromotionsService promotionsService, TimeService timeService, BaseSiteService baseSiteService, FlashBuyService flashBuyService, Converter<AbstractPromotionModel, PromotionData> promotionsConverter)
    {
        this.promotionsService = promotionsService;
        this.timeService = timeService;
        this.baseSiteService = baseSiteService;
        this.flashBuyService = flashBuyService;
        this.promotionsConverter = promotionsConverter;
    }


    public void populate(ProductModel productModel, ProductData productData)
    {
        Assert.notNull(productModel, "Parameter source cannot be null.");
        Assert.notNull(productData, "Parameter target cannot be null.");
        List<PromotionData> promotions = new ArrayList<>();
        List<PromotionData> flashBuyPromotions = new ArrayList<>();
        BaseSiteModel baseSiteModel = getBaseSiteService().getCurrentBaseSite();
        if(baseSiteModel != null)
        {
            PromotionGroupModel defaultPromotionGroup = baseSiteModel.getDefaultPromotionGroup();
            Date currentTimeRoundedToMinute = DateUtils.round(getTimeService().getCurrentTime(), 12);
            if(defaultPromotionGroup != null)
            {
                promotions.addAll(getPromotionsConverter().convertAll(getPromotionsService().getAbstractProductPromotions(
                                Collections.singletonList(defaultPromotionGroup), productModel, true, currentTimeRoundedToMinute)));
            }
        }
        if(CollectionUtils.isNotEmpty(promotions))
        {
            promotions.forEach(p -> getFlashBuyService().getFlashBuyCouponByPromotionCode(p.getCode()).ifPresent(()));
        }
        if(CollectionUtils.isNotEmpty(flashBuyPromotions))
        {
            productData.setTimedAccessPromotion(flashBuyPromotions.get(0));
        }
    }


    protected PromotionsService getPromotionsService()
    {
        return this.promotionsService;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    protected FlashBuyService getFlashBuyService()
    {
        return this.flashBuyService;
    }


    protected Converter<AbstractPromotionModel, PromotionData> getPromotionsConverter()
    {
        return this.promotionsConverter;
    }


    public void setPromotionsConverter(Converter<AbstractPromotionModel, PromotionData> promotionsConverter)
    {
        this.promotionsConverter = promotionsConverter;
    }
}
