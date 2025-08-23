package de.hybris.platform.orderscheduling.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderTemplateToOrderCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "OrderTemplateToOrderCronJob";
    public static final String _ORDER2ORDERTEMPLATETOORDERCRONJOB = "Order2OrderTemplateToOrderCronJob";
    public static final String ORDERTEMPLATE = "orderTemplate";


    public OrderTemplateToOrderCronJobModel()
    {
    }


    public OrderTemplateToOrderCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderTemplateToOrderCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderTemplateToOrderCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "orderTemplate", type = Accessor.Type.GETTER)
    public OrderModel getOrderTemplate()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("orderTemplate");
    }


    @Accessor(qualifier = "orderTemplate", type = Accessor.Type.SETTER)
    public void setOrderTemplate(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("orderTemplate", value);
    }
}
