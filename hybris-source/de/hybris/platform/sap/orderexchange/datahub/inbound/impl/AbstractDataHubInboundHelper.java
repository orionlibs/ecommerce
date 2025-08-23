/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.orderexchange.datahub.inbound.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.sap.sapmodel.model.SAPOrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Required;

/**
 * Abstract root class for inbound processing of order related notifications
 * from Data Hub
 */
public abstract class AbstractDataHubInboundHelper
{
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;
    private BusinessProcessService businessProcessService;


    @SuppressWarnings("javadoc")
    public BusinessProcessService getBusinessProcessService()
    {
        return businessProcessService;
    }


    @SuppressWarnings("javadoc")
    @Required
    public void setBusinessProcessService(final BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }


    @SuppressWarnings("javadoc")
    public ModelService getModelService()
    {
        return modelService;
    }


    @SuppressWarnings("javadoc")
    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }


    @SuppressWarnings("javadoc")
    public FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }


    @SuppressWarnings("javadoc")
    @Required
    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected OrderModel readOrder(final String orderCode)
    {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(
                        "SELECT {o:pk} FROM {Order AS o} WHERE  {o.code} = ?code AND {o.versionID} IS NULL");
        flexibleSearchQuery.addQueryParameter("code", orderCode);
        final OrderModel order = getFlexibleSearchService().searchUnique(flexibleSearchQuery);
        if(order == null)
        {
            throw new IllegalArgumentException(String.format(
                            "Error while processing inbound IDoc with order code [%s] that does not exist!", orderCode));
        }
        return order;
    }


    protected SAPOrderModel readSapOrder(final String sapOrderNumber)
    {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(
                        "SELECT {o:pk} FROM {SAPOrder AS o} WHERE  {o.code} = ?code");
        flexibleSearchQuery.addQueryParameter("code", sapOrderNumber);
        final SAPOrderModel sapOrder = getFlexibleSearchService().searchUnique(flexibleSearchQuery);
        if(sapOrder == null)
        {
            throw new IllegalArgumentException(String.format(
                            "Error while processing inbound IDoc with SAP order number [%s] that does not exist!", sapOrderNumber));
        }
        return sapOrder;
    }
}
