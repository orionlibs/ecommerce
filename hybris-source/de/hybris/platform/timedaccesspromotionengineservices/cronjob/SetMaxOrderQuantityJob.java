package de.hybris.platform.timedaccesspromotionengineservices.cronjob;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCronJobModel;

public class SetMaxOrderQuantityJob extends AbstractJobPerformable<FlashBuyCronJobModel>
{
    private ProductDao productDao;


    public PerformResult perform(FlashBuyCronJobModel cronJobModel)
    {
        ServicesUtil.validateParameterNotNull(cronJobModel, "Parameter cronJobModel must not be null");
        FlashBuyCouponModel coupon = cronJobModel.getFlashBuyCoupon();
        ProductModel product = coupon.getProduct();
        coupon.setOriginalMaxOrderQuantity(product.getMaxOrderQuantity());
        this.modelService.save(coupon);
        getProductDao().findProductsByCode(product.getCode()).forEach(p -> {
            p.setMaxOrderQuantity(coupon.getMaxProductQuantityPerOrder());
            this.modelService.save(p);
        });
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
}
