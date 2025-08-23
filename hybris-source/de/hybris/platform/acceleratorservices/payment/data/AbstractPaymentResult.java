package de.hybris.platform.acceleratorservices.payment.data;

public class AbstractPaymentResult extends HostedOrderPageResult
{
    private String decision;
    private String decisionPublicSignature;
    private Boolean transactionSignatureVerified;


    public void setDecision(String decision)
    {
        this.decision = decision;
    }


    public String getDecision()
    {
        return this.decision;
    }


    public void setDecisionPublicSignature(String decisionPublicSignature)
    {
        this.decisionPublicSignature = decisionPublicSignature;
    }


    public String getDecisionPublicSignature()
    {
        return this.decisionPublicSignature;
    }


    public void setTransactionSignatureVerified(Boolean transactionSignatureVerified)
    {
        this.transactionSignatureVerified = transactionSignatureVerified;
    }


    public Boolean getTransactionSignatureVerified()
    {
        return this.transactionSignatureVerified;
    }
}
