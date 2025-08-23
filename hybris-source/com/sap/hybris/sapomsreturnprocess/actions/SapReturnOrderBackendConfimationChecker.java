/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapomsreturnprocess.actions;

import com.sap.hybris.sapomsreturnprocess.enums.SAPReturnRequestOrderStatus;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

public class SapReturnOrderBackendConfimationChecker extends AbstractSimpleDecisionAction<ReturnProcessModel>
{
    @Override
    public Transition executeAction(final ReturnProcessModel returnProcess)
    {
        final ReturnRequestModel returnRequest = returnProcess.getReturnRequest();
        // check for each order creation status
        if(returnRequest.getSapReturnRequests().stream().allMatch(returnStatus -> returnStatus.getSapReturnRequestOrderStatus()
                        .equals(SAPReturnRequestOrderStatus.CONFIRMED_FROM_BACKEND)))
        {
            return Transition.OK;
        }
        else
        {
            return Transition.NOK;
        }
    }
}
