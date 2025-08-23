package de.hybris.platform.returns.impl.executors;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.returns.OrderReturnException;
import de.hybris.platform.returns.ReturnActionAdapter;
import de.hybris.platform.returns.ReturnActionRequest;
import de.hybris.platform.returns.ReturnActionRequestExecutor;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultReturnActionRequestExecutor implements ReturnActionRequestExecutor
{
    private static final Logger LOG = Logger.getLogger(DefaultReturnActionRequestExecutor.class.getName());
    private ModelService modelService;
    private ReturnActionAdapter returnActionAdapter;


    public void processApprovingRequest(ReturnRequestModel returnRequest) throws OrderReturnException
    {
        returnRequest.setStatus(ReturnStatus.APPROVING);
        this.modelService.save(returnRequest);
        getReturnActionAdapter().requestReturnApproval(returnRequest);
        LOG.info("Return request: " + returnRequest.getCode() + " is being approved");
    }


    public void processReceivingRequest(ReturnRequestModel returnRequest) throws OrderReturnException
    {
        returnRequest.setStatus(ReturnStatus.RECEIVING);
        this.modelService.save(returnRequest);
        getReturnActionAdapter().requestReturnReception(returnRequest);
        LOG.info("Return request: " + returnRequest.getCode() + " is being received");
    }


    public void processCancellingRequest(ReturnRequestModel returnRequest) throws OrderReturnException
    {
        returnRequest.setStatus(ReturnStatus.CANCELLING);
        this.modelService.save(returnRequest);
        getReturnActionAdapter().requestReturnCancellation(returnRequest);
        LOG.info("Return request: " + returnRequest.getCode() + " is being cancelled");
    }


    public void processManualPaymentReversalForReturnRequest(ReturnActionRequest returnActionRequest) throws OrderReturnException
    {
        Assert.notNull(returnActionRequest, "ReturnActionRequest cannot be null");
        ReturnRequestModel returnRequest = returnActionRequest.getReturnRequest();
        returnRequest.setStatus(ReturnStatus.REVERSING_PAYMENT);
        this.modelService.save(returnRequest);
        getReturnActionAdapter().requestManualPaymentReversalForReturnRequest(returnRequest);
        LOG.info("Manually reversing the payment for Return request: " + returnRequest.getCode());
    }


    public void processManualTaxReversalForReturnRequest(ReturnActionRequest returnActionRequest) throws OrderReturnException
    {
        Assert.notNull(returnActionRequest, "ReturnActionRequest cannot be null");
        ReturnRequestModel returnRequest = returnActionRequest.getReturnRequest();
        returnRequest.setStatus(ReturnStatus.REVERSING_TAX);
        this.modelService.save(returnRequest);
        getReturnActionAdapter().requestManualTaxReversalForReturnRequest(returnRequest);
        LOG.info("Manually reversing the tax for Return request: " + returnRequest.getCode());
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ReturnActionAdapter getReturnActionAdapter()
    {
        return this.returnActionAdapter;
    }


    @Required
    public void setReturnActionAdapter(ReturnActionAdapter returnActionAdapter)
    {
        this.returnActionAdapter = returnActionAdapter;
    }
}
