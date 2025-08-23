package de.hybris.platform.warehousing.validation.validators;

import de.hybris.platform.core.Registry;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import de.hybris.platform.warehousing.validation.annotations.AdvancedShippingNoticeValid;
import de.hybris.platform.warehousing.warehouse.service.WarehousingWarehouseService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AdvancedShippingNoticeValidator implements ConstraintValidator<AdvancedShippingNoticeValid, AdvancedShippingNoticeModel>
{
    private WarehousingWarehouseService warehousingWarehouseService;


    public void initialize(AdvancedShippingNoticeValid advancedShippingNoticeValid)
    {
        this
                        .warehousingWarehouseService = (WarehousingWarehouseService)Registry.getApplicationContext().getBean("warehousingWarehouseService", WarehousingWarehouseService.class);
    }


    public boolean isValid(AdvancedShippingNoticeModel advancedShippingNotice, ConstraintValidatorContext constraintValidatorContext)
    {
        PointOfServiceModel pointOfService = advancedShippingNotice.getPointOfService();
        WarehouseModel warehouse = advancedShippingNotice.getWarehouse();
        return (pointOfService != null && (warehouse == null || getWarehousingWarehouseService()
                        .isWarehouseInPoS(warehouse, pointOfService)));
    }


    protected WarehousingWarehouseService getWarehousingWarehouseService()
    {
        return this.warehousingWarehouseService;
    }
}
