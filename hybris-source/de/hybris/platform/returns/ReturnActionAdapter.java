package de.hybris.platform.returns;

import de.hybris.platform.returns.model.ReturnRequestModel;

public interface ReturnActionAdapter
{
    void requestReturnApproval(ReturnRequestModel paramReturnRequestModel);


    void requestReturnReception(ReturnRequestModel paramReturnRequestModel);


    void requestReturnCancellation(ReturnRequestModel paramReturnRequestModel);


    void requestManualPaymentReversalForReturnRequest(ReturnRequestModel paramReturnRequestModel);


    void requestManualTaxReversalForReturnRequest(ReturnRequestModel paramReturnRequestModel);
}
