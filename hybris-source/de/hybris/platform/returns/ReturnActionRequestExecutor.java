package de.hybris.platform.returns;

import de.hybris.platform.returns.model.ReturnRequestModel;

public interface ReturnActionRequestExecutor
{
    void processApprovingRequest(ReturnRequestModel paramReturnRequestModel) throws OrderReturnException;


    void processReceivingRequest(ReturnRequestModel paramReturnRequestModel) throws OrderReturnException;


    void processCancellingRequest(ReturnRequestModel paramReturnRequestModel) throws OrderReturnException;


    void processManualPaymentReversalForReturnRequest(ReturnActionRequest paramReturnActionRequest) throws OrderReturnException;


    void processManualTaxReversalForReturnRequest(ReturnActionRequest paramReturnActionRequest) throws OrderReturnException;
}
