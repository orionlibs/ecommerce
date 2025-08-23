/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapcpiomsreturnsexchange.service;

import static com.google.common.base.Preconditions.checkArgument;

import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundOrderCancellationModel;
import de.hybris.platform.sap.sapcpiomsreturnsexchange.constants.SapcpiomsreturnsexchangeConstants;
import de.hybris.platform.sap.sapcpireturnsexchange.model.SAPCpiOutboundReturnOrderModel;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import rx.Observable;

/**
 * SapCpiOmsOutboundReturnService
 */
public interface SapCpiOmsOutboundReturnService
{
    /**
     * Send Return Order
     * @param sapCpiOutboundReturnOrderModel SAPCpiOutboundReturnOrderModel
     * @return Observable<ResponseEntity < Map>>
     */
    Observable<ResponseEntity<Map>> sendReturnOrder(SAPCpiOutboundReturnOrderModel sapCpiOutboundReturnOrderModel);


    /**
     * Send Return Order Cancellation
     * @param sapCpiOutboundOrderCancellationModel SAPCpiOutboundOrderCancellationModel
     * @return Observable<ResponseEntity < Map>>
     */
    Observable<ResponseEntity<Map>> sendReturnOrderCancellation(
                    SAPCpiOutboundOrderCancellationModel sapCpiOutboundOrderCancellationModel);


    /**
     * isSentSuccessfully
     * @param responseEntityMap ResponseEntity<Map>
     * @return boolean
     */
    static boolean isSentSuccessfully(final ResponseEntity<Map> responseEntityMap)
    {
        return SapcpiomsreturnsexchangeConstants.SUCCESS
                        .equalsIgnoreCase(getPropertyValue(responseEntityMap, SapcpiomsreturnsexchangeConstants.RESPONSE_STATUS))
                        && responseEntityMap.getStatusCode().is2xxSuccessful();
    }


    /**
     * getPropertyValue
     *
     * @param responseEntityMap ResponseEntity<Map>
     * @param property          String
     * @return String
     */
    static String getPropertyValue(final ResponseEntity<Map> responseEntityMap, final String property)
    {
        final Object next = responseEntityMap.getBody().keySet().iterator().next();
        checkArgument(next != null, String.format("SCPI response entity key set cannot be null for property [%s]!", property));
        final String responseKey = next.toString();
        checkArgument(responseKey != null && !responseKey.isEmpty(),
                        String.format("SCPI response property can neither be null nor empty for property [%s]!", property));
        final Object propertyValue = ((HashMap)responseEntityMap.getBody().get(responseKey)).get(property);
        checkArgument(propertyValue != null, String.format("SCPI response property [%s] value cannot be null!", property));
        return propertyValue.toString();
    }
}
