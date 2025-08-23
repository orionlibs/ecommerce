package de.hybris.platform.payment.commands.result;

public class EnrollmentCheckResult extends AbstractResult
{
    private String acsURL;
    private String eci;
    private String commerceIndicator;
    private String ucafCollectionIndicator;
    private String xid;
    private String proofXml;
    private String paReq;
    private String proxyPAN;
    private String veresEnrolled;
    private String paymentProvider;


    public EnrollmentCheckResult(String merchantTransactionCode)
    {
        super(merchantTransactionCode, "", "", "", null, null);
    }


    public String getAcsURL()
    {
        return this.acsURL;
    }


    public void setAcsURL(String acsURL)
    {
        this.acsURL = acsURL;
    }


    public String getEci()
    {
        return this.eci;
    }


    public void setEci(String eci)
    {
        this.eci = eci;
    }


    public String getCommerceIndicator()
    {
        return this.commerceIndicator;
    }


    public void setCommerceIndicator(String commerceIndicator)
    {
        this.commerceIndicator = commerceIndicator;
    }


    public String getUcafCollectionIndicator()
    {
        return this.ucafCollectionIndicator;
    }


    public void setUcafCollectionIndicator(String ucafCollectionIndicator)
    {
        this.ucafCollectionIndicator = ucafCollectionIndicator;
    }


    public String getXid()
    {
        return this.xid;
    }


    public void setXid(String xid)
    {
        this.xid = xid;
    }


    public String getProofXml()
    {
        return this.proofXml;
    }


    public void setProofXml(String proofXml)
    {
        this.proofXml = proofXml;
    }


    public String getPaReq()
    {
        return this.paReq;
    }


    public void setPaReq(String paReq)
    {
        this.paReq = paReq;
    }


    public String getProxyPAN()
    {
        return this.proxyPAN;
    }


    public void setProxyPAN(String proxyPAN)
    {
        this.proxyPAN = proxyPAN;
    }


    public String getVeresEnrolled()
    {
        return this.veresEnrolled;
    }


    public void setVeresEnrolled(String veresEnrolled)
    {
        this.veresEnrolled = veresEnrolled;
    }


    public void setPaymentProvider(String paymentProvider)
    {
        this.paymentProvider = paymentProvider;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }
}
