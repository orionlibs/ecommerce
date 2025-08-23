package de.hybris.platform.returns.impl;

import de.hybris.platform.returns.ReturnActionAdapter;
import de.hybris.platform.returns.model.ReturnRequestModel;
import org.apache.log4j.Logger;

public class DefaultReturnActionAdapter implements ReturnActionAdapter
{
    private static final Logger LOG = Logger.getLogger(DefaultReturnActionAdapter.class.getName());


    public void requestReturnApproval(ReturnRequestModel returnRequest)
    {
        LOG.info("Return approval requested. Default implementation is empty, please provide your own implementation");
    }


    public void requestReturnReception(ReturnRequestModel returnRequest)
    {
        LOG.info("Return reception requested. Default implementation is empty, please provide your own implementation");
    }


    public void requestReturnCancellation(ReturnRequestModel returnRequest)
    {
        LOG.info("Return cancellation requested. Default implementation is empty, please provide your own implementation");
    }


    public void requestManualPaymentReversalForReturnRequest(ReturnRequestModel returnRequest)
    {
        LOG.info("Return manual payment reversal requested. Default implementation is empty, please provide your own implementation");
    }


    public void requestManualTaxReversalForReturnRequest(ReturnRequestModel returnRequest)
    {
        LOG.info("Return manual tax reversal requested. Default implementation is empty, please provide your own implementation");
    }
}
