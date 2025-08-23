/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapcpioaaorderintegration.service.impl;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.sap.hybris.sapcpioaaorderintegration.constants.SapcpioaaorderintegrationConstants;
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundOrderModel;
import de.hybris.platform.sap.sapcpiadapter.service.impl.SapCpiOutboundServiceImpl;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import rx.Observable;

/**
 *
 */
public class SapCpiOaaOutboundServiceImpl extends SapCpiOutboundServiceImpl
{
    //Order Outbound
    private static final String OUTBOUND_OAA_ORDER_OBJECT = "OutboundOaaOrder";
    private static final String OUTBOUND_OAA_ORDER_DESTINATION = "scpiOaaOrderDestination";
    private OutboundServiceFacade outboundServiceFacade;


    @Override
    public Observable<ResponseEntity<Map>> sendOrder(final SAPCpiOutboundOrderModel sapCpiOutboundOrderModel)
    {
        if(!StringUtils.isEmpty(sapCpiOutboundOrderModel.getOaaOrderInUse())
                        && (SapcpioaaorderintegrationConstants.OAA_ORDER_IN_USE_FLAG.equalsIgnoreCase(sapCpiOutboundOrderModel.getOaaOrderInUse())))
        {
            return getOutboundServiceFacade().send(sapCpiOutboundOrderModel, OUTBOUND_OAA_ORDER_OBJECT,
                            OUTBOUND_OAA_ORDER_DESTINATION);
        }
        else
        {
            return super.sendOrder(sapCpiOutboundOrderModel);
        }
    }


    @Override
    protected OutboundServiceFacade getOutboundServiceFacade()
    {
        return outboundServiceFacade;
    }


    @Override
    public void setOutboundServiceFacade(final OutboundServiceFacade outboundServiceFacade)
    {
        this.outboundServiceFacade = outboundServiceFacade;
    }
}
