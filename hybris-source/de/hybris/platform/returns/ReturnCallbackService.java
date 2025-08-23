package de.hybris.platform.returns;

public interface ReturnCallbackService
{
    void onReturnApprovalResponse(ReturnActionResponse paramReturnActionResponse) throws OrderReturnException;


    void onReturnCancelResponse(ReturnActionResponse paramReturnActionResponse) throws OrderReturnException;


    void onReturnReceptionResponse(ReturnActionResponse paramReturnActionResponse) throws OrderReturnException;
}
