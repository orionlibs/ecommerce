package de.hybris.platform.payment.commands.result;

import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;

public abstract class AbstractResult
{
    private String merchantTransactionCode;
    private String requestId;
    private String requestToken;
    private String reconciliationId;
    private TransactionStatus transactionStatus;
    private TransactionStatusDetails transactionStatusDetails;


    protected AbstractResult()
    {
    }


    protected AbstractResult(String merchantTransactionCode, String requestId, String requestToken, String reconciliationId, TransactionStatus transactionStatus, TransactionStatusDetails transactionStatusDetails)
    {
        this.merchantTransactionCode = merchantTransactionCode;
        this.requestId = requestId;
        this.requestToken = requestToken;
        this.reconciliationId = reconciliationId;
        this.transactionStatus = transactionStatus;
        this.transactionStatusDetails = transactionStatusDetails;
    }


    public String getMerchantTransactionCode()
    {
        return this.merchantTransactionCode;
    }


    public void setMerchantTransactionCode(String merchantTransactionCode)
    {
        this.merchantTransactionCode = merchantTransactionCode;
    }


    public String getRequestId()
    {
        return this.requestId;
    }


    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }


    public String getRequestToken()
    {
        return this.requestToken;
    }


    public void setRequestToken(String requestToken)
    {
        this.requestToken = requestToken;
    }


    public TransactionStatus getTransactionStatus()
    {
        return this.transactionStatus;
    }


    public void setTransactionStatus(TransactionStatus transactionStatus)
    {
        this.transactionStatus = transactionStatus;
    }


    public TransactionStatusDetails getTransactionStatusDetails()
    {
        return this.transactionStatusDetails;
    }


    public void setTransactionStatusDetails(TransactionStatusDetails transactionStatusDetails)
    {
        this.transactionStatusDetails = transactionStatusDetails;
    }


    public String getReconciliationId()
    {
        return this.reconciliationId;
    }


    public void setReconciliationId(String reconciliationId)
    {
        this.reconciliationId = reconciliationId;
    }
}
