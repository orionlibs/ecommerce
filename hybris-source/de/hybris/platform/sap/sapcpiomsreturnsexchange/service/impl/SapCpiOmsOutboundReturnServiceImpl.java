/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapcpiomsreturnsexchange.service.impl;

import de.hybris.platform.outboundservices.facade.OutboundServiceFacade;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundOrderCancellationModel;
import de.hybris.platform.sap.sapcpiomsreturnsexchange.service.SapCpiOmsOutboundReturnService;
import de.hybris.platform.sap.sapcpireturnsexchange.model.SAPCpiOutboundReturnOrderModel;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import rx.Observable;

/**
 *
 */
public class SapCpiOmsOutboundReturnServiceImpl implements SapCpiOmsOutboundReturnService
{
    // Return Order Outbound
    private static final String OUTBOUND_RETURN_ORDER_OBJECT = "OutboundOMMReturnOMSReturnOrder";
    private static final String OUTBOUND_RETURN_ORDER_DESTINATION = "scpiReturnOrderDestination";
    // Return Order Cancellation Outbound
    private static final String OUTBOUND_RETURN_ORDER_CANCELLATION_OBJECT = "OutboundCancelOMMReturnOMSReturnOrder";
    private static final String OUTBOUND_RETURN_ORDER_CANCELLATION_DESTINATION = "scpiCancelReturnOrderDestination";
    private OutboundServiceFacade outboundServiceFacade;


    @Override
    public Observable<ResponseEntity<Map>> sendReturnOrder(final SAPCpiOutboundReturnOrderModel sapCpiOutboundReturnOrderModel)
    {
        return getOutboundServiceFacade().send(sapCpiOutboundReturnOrderModel, OUTBOUND_RETURN_ORDER_OBJECT,
                        OUTBOUND_RETURN_ORDER_DESTINATION);
    }


    @Override
    public Observable<ResponseEntity<Map>> sendReturnOrderCancellation(
                    final SAPCpiOutboundOrderCancellationModel sapCpiOutboundOrderCancellationModel)
    {
        return getOutboundServiceFacade().send(sapCpiOutboundOrderCancellationModel, OUTBOUND_RETURN_ORDER_CANCELLATION_OBJECT,
                        OUTBOUND_RETURN_ORDER_CANCELLATION_DESTINATION);
    }


    protected OutboundServiceFacade getOutboundServiceFacade()
    {
        return outboundServiceFacade;
    }


    @Required
    public void setOutboundServiceFacade(final OutboundServiceFacade outboundServiceFacade)
    {
        this.outboundServiceFacade = outboundServiceFacade;
    }
}
