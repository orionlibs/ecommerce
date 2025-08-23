package de.hybris.platform.warehousing.document.strategies.impl;

import de.hybris.platform.acceleratorservices.document.strategy.DocumentCatalogFetchStrategy;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

public class WarehousingDocumentCatalogFetchStrategy implements DocumentCatalogFetchStrategy
{
    public CatalogVersionModel fetch(BusinessProcessModel businessProcessModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("businessProcessModel", businessProcessModel);
        OrderModel order = null;
        if(businessProcessModel instanceof OrderProcessModel)
        {
            order = ((OrderProcessModel)businessProcessModel).getOrder();
        }
        else if(businessProcessModel instanceof ConsignmentProcessModel)
        {
            order = (OrderModel)((ConsignmentProcessModel)businessProcessModel).getConsignment().getOrder();
        }
        else if(businessProcessModel instanceof ReturnProcessModel)
        {
            order = ((ReturnProcessModel)businessProcessModel).getReturnRequest().getOrder();
        }
        else if(businessProcessModel.getContextParameters().iterator().hasNext())
        {
            BusinessProcessParameterModel param = businessProcessModel.getContextParameters().iterator().next();
            if("ConsolidatedConsignmentModels".equals(param.getName()))
            {
                List<ConsignmentModel> consignmentList = (List<ConsignmentModel>)param.getValue();
                if(CollectionUtils.isNotEmpty(consignmentList))
                {
                    order = (OrderModel)((ConsignmentModel)consignmentList.iterator().next()).getOrder();
                }
            }
        }
        ServicesUtil.validateParameterNotNullStandardMessage("order", order);
        Assert.isTrue((order != null && order.getPotentiallyFraudulent() != null), "No potentially fraudulent found for the order");
        Assert.isTrue(order.getSite() instanceof CMSSiteModel, "No CMSSite found for the order");
        List<ContentCatalogModel> contentCatalogs = ((CMSSiteModel)order.getSite()).getContentCatalogs();
        Assert.isTrue(CollectionUtils.isNotEmpty(contentCatalogs), "Catalog Version cannot be found for the order");
        return ((ContentCatalogModel)contentCatalogs.iterator().next()).getActiveCatalogVersion();
    }
}
