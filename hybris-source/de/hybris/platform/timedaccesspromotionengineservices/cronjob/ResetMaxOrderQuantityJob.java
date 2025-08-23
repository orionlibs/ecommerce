package de.hybris.platform.timedaccesspromotionengineservices.cronjob;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.timedaccesspromotionengineservices.FlashBuyService;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCronJobModel;
import java.util.List;

public class ResetMaxOrderQuantityJob extends AbstractJobPerformable<FlashBuyCronJobModel>
{
    private ProductDao productDao;
    private FlashBuyService flashBuyService;


    public PerformResult perform(FlashBuyCronJobModel cronJobModel)
    {
        ServicesUtil.validateParameterNotNull(cronJobModel, "Parameter cronJobModel must not be null");
        FlashBuyCouponModel coupon = cronJobModel.getFlashBuyCoupon();
        PromotionSourceRuleModel sourceRule = coupon.getRule();
        if(sourceRule != null)
        {
            List<ProductForPromotionSourceRuleModel> productForPromotionSourceRules = getFlashBuyService().getProductForPromotionSourceRule(sourceRule);
            productForPromotionSourceRules.forEach(rule -> {
                String productCode = rule.getProductCode();
                List<ProductModel> products = this.productDao.findProductsByCode(productCode);
                products.forEach(());
                this.modelService.saveAll(products);
            });
            getFlashBuyService().undeployFlashBuyPromotion(sourceRule);
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    protected ProductDao getProductDao()
    {
        return this.productDao;
    }


    public void setProductDao(ProductDao productDao)
    {
        this.productDao = productDao;
    }


    protected FlashBuyService getFlashBuyService()
    {
        return this.flashBuyService;
    }


    public void setFlashBuyService(FlashBuyService flashBuyService)
    {
        this.flashBuyService = flashBuyService;
    }
}
