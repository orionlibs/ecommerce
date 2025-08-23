package de.hybris.platform.warehousing.process.strategies;

import de.hybris.platform.acceleratorservices.process.strategies.impl.AbstractProcessContextStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;

public class ConsolidatedPickSlipBusinessProcessContextStrategy extends AbstractProcessContextStrategy
{
    public BaseSiteModel getCmsSite(BusinessProcessModel businessProcessModel)
    {
        BaseSiteModel baseSite = null;
        if(businessProcessModel.getContextParameters().iterator().hasNext())
        {
            BusinessProcessParameterModel param = businessProcessModel.getContextParameters().iterator().next();
            if("ConsolidatedConsignmentModels".equals(param.getName()))
            {
                List<ConsignmentModel> consignmentList = (List<ConsignmentModel>)param.getValue();
                if(CollectionUtils.isNotEmpty(consignmentList))
                {
                    baseSite = ((ConsignmentModel)consignmentList.iterator().next()).getOrder().getSite();
                }
            }
        }
        if(baseSite == null && businessProcessModel instanceof ConsignmentProcessModel)
        {
            baseSite = Optional.<ConsignmentModel>of(((ConsignmentProcessModel)businessProcessModel).getConsignment()).map(ConsignmentModel::getOrder).map(AbstractOrderModel::getSite).orElse(null);
        }
        return baseSite;
    }


    protected CustomerModel getCustomer(BusinessProcessModel businessProcess)
    {
        return null;
    }
}
