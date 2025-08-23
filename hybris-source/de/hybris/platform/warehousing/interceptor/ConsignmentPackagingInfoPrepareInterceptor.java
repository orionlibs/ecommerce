package de.hybris.platform.warehousing.interceptor;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.warehousing.model.PackagingInfoModel;
import org.springframework.beans.factory.annotation.Required;

public class ConsignmentPackagingInfoPrepareInterceptor implements PrepareInterceptor<ConsignmentModel>
{
    protected static final String DEFAULT_DIMENSION_UNIT = "cm";
    protected static final String DEFAULT_VALUE = "0";
    protected static final String DEFAULT_WEIGHT_UNIT = "kg";
    private ModelService modelService;
    private TimeService timeService;


    public void onPrepare(ConsignmentModel consignment, InterceptorContext context) throws InterceptorException
    {
        if(context.isNew(consignment))
        {
            PackagingInfoModel packagingInfo = (PackagingInfoModel)getModelService().create(PackagingInfoModel.class);
            packagingInfo.setConsignment(consignment);
            packagingInfo.setGrossWeight("0");
            packagingInfo.setHeight("0");
            packagingInfo.setInsuredValue("0");
            packagingInfo.setLength("0");
            packagingInfo.setWidth("0");
            packagingInfo.setDimensionUnit("cm");
            packagingInfo.setWeightUnit("kg");
            packagingInfo.setCreationtime(getTimeService().getCurrentTime());
            packagingInfo.setModifiedtime(getTimeService().getCurrentTime());
            context.registerElementFor(packagingInfo, PersistenceOperation.SAVE);
            consignment.setPackagingInfo(packagingInfo);
        }
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }
}
