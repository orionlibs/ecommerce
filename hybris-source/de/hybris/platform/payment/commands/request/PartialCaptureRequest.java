package de.hybris.platform.payment.commands.request;

import java.math.BigDecimal;
import java.util.Currency;

public class PartialCaptureRequest extends CaptureRequest
{
    private final String partialPaymentID;


    public PartialCaptureRequest(String merchantTransactionCode, String requestId, String requestToken, Currency currency, BigDecimal totalAmount, String partialPaymentID, String paymentProvider)
    {
        super(merchantTransactionCode, requestId, requestToken, currency, totalAmount, paymentProvider);
        this.partialPaymentID = (partialPaymentID == null) ? String.valueOf(System.currentTimeMillis()) : partialPaymentID;
    }


    public String getPartialPaymentID()
    {
        return this.partialPaymentID;
    }
}
