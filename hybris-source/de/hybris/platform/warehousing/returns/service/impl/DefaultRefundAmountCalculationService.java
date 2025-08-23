package de.hybris.platform.warehousing.returns.service.impl;

import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.returns.service.RefundAmountCalculationService;
import java.math.BigDecimal;

public class DefaultRefundAmountCalculationService implements RefundAmountCalculationService
{
    public BigDecimal getCustomRefundAmount(ReturnRequestModel returnRequest)
    {
        ServicesUtil.validateParameterNotNull(returnRequest, "Parameter returnRequest cannot be null");
        ServicesUtil.validateParameterNotNull(returnRequest.getReturnEntries(), "Parameter Return Entries cannot be null");
        BigDecimal refundAmount = returnRequest.getReturnEntries().stream().map(returnEntry -> getCustomRefundEntryAmount(returnEntry)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if(returnRequest.getRefundDeliveryCost().booleanValue())
        {
            refundAmount = refundAmount.add(BigDecimal.valueOf(returnRequest.getOrder().getDeliveryCost().doubleValue()));
        }
        return refundAmount.setScale(getNumberOfDigits(returnRequest), 2);
    }


    public BigDecimal getCustomRefundEntryAmount(ReturnEntryModel returnEntryModel)
    {
        ServicesUtil.validateParameterNotNull(returnEntryModel, "Parameter Return Entry cannot be null");
        BigDecimal itemValue = BigDecimal.ZERO;
        if(returnEntryModel instanceof RefundEntryModel)
        {
            itemValue = getOriginalRefundEntryAmount(returnEntryModel);
        }
        return itemValue;
    }


    public BigDecimal getOriginalRefundAmount(ReturnRequestModel returnRequest)
    {
        ServicesUtil.validateParameterNotNull(returnRequest, "Parameter returnRequest cannot be null");
        ServicesUtil.validateParameterNotNull(returnRequest.getReturnEntries(), "Parameter Return Entries cannot be null");
        BigDecimal refundAmount = returnRequest.getReturnEntries().stream().map(returnEntry -> getOriginalRefundEntryAmount(returnEntry)).reduce(BigDecimal.ZERO, BigDecimal::add);
        if(returnRequest.getRefundDeliveryCost().booleanValue())
        {
            refundAmount = refundAmount.add(BigDecimal.valueOf(returnRequest.getOrder().getDeliveryCost().doubleValue()));
        }
        return refundAmount.setScale(getNumberOfDigits(returnRequest), 2);
    }


    public BigDecimal getOriginalRefundEntryAmount(ReturnEntryModel returnEntryModel)
    {
        ServicesUtil.validateParameterNotNull(returnEntryModel, "Parameter Return Entry cannot be null");
        ReturnRequestModel returnRequest = returnEntryModel.getReturnRequest();
        BigDecimal refundEntryAmount = BigDecimal.ZERO;
        if(returnEntryModel instanceof RefundEntryModel)
        {
            RefundEntryModel refundEntry = (RefundEntryModel)returnEntryModel;
            refundEntryAmount = refundEntry.getAmount();
            refundEntryAmount = refundEntryAmount.setScale(getNumberOfDigits(returnRequest), 5);
        }
        return refundEntryAmount;
    }


    protected int getNumberOfDigits(ReturnRequestModel returnRequest)
    {
        return returnRequest.getOrder().getCurrency().getDigits().intValue();
    }
}
