package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;

public class OrderPageConfirmationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String cancelLinkText;
    private String cancelResponseUrl;
    private String declineLinkText;
    private String declineResponseUrl;
    private String emailFromAddress;
    private String emailFromName;
    private String merchantEmailAddress;
    private String receiptLinkText;
    private String receiptResponseUrl;
    private Boolean returnCardNumber;
    private Boolean returnBinInCardNumber;
    private Boolean returnCardBin;
    private Boolean sendCustomerReceiptEmail;
    private Boolean sendMerchantEmailPost;
    private String merchantEmailPostAddress;
    private Boolean sendMerchantReceiptEmail;
    private Boolean sendMerchantUrlPost;
    private String merchantUrlPostAddress;


    public void setCancelLinkText(String cancelLinkText)
    {
        this.cancelLinkText = cancelLinkText;
    }


    public String getCancelLinkText()
    {
        return this.cancelLinkText;
    }


    public void setCancelResponseUrl(String cancelResponseUrl)
    {
        this.cancelResponseUrl = cancelResponseUrl;
    }


    public String getCancelResponseUrl()
    {
        return this.cancelResponseUrl;
    }


    public void setDeclineLinkText(String declineLinkText)
    {
        this.declineLinkText = declineLinkText;
    }


    public String getDeclineLinkText()
    {
        return this.declineLinkText;
    }


    public void setDeclineResponseUrl(String declineResponseUrl)
    {
        this.declineResponseUrl = declineResponseUrl;
    }


    public String getDeclineResponseUrl()
    {
        return this.declineResponseUrl;
    }


    public void setEmailFromAddress(String emailFromAddress)
    {
        this.emailFromAddress = emailFromAddress;
    }


    public String getEmailFromAddress()
    {
        return this.emailFromAddress;
    }


    public void setEmailFromName(String emailFromName)
    {
        this.emailFromName = emailFromName;
    }


    public String getEmailFromName()
    {
        return this.emailFromName;
    }


    public void setMerchantEmailAddress(String merchantEmailAddress)
    {
        this.merchantEmailAddress = merchantEmailAddress;
    }


    public String getMerchantEmailAddress()
    {
        return this.merchantEmailAddress;
    }


    public void setReceiptLinkText(String receiptLinkText)
    {
        this.receiptLinkText = receiptLinkText;
    }


    public String getReceiptLinkText()
    {
        return this.receiptLinkText;
    }


    public void setReceiptResponseUrl(String receiptResponseUrl)
    {
        this.receiptResponseUrl = receiptResponseUrl;
    }


    public String getReceiptResponseUrl()
    {
        return this.receiptResponseUrl;
    }


    public void setReturnCardNumber(Boolean returnCardNumber)
    {
        this.returnCardNumber = returnCardNumber;
    }


    public Boolean getReturnCardNumber()
    {
        return this.returnCardNumber;
    }


    public void setReturnBinInCardNumber(Boolean returnBinInCardNumber)
    {
        this.returnBinInCardNumber = returnBinInCardNumber;
    }


    public Boolean getReturnBinInCardNumber()
    {
        return this.returnBinInCardNumber;
    }


    public void setReturnCardBin(Boolean returnCardBin)
    {
        this.returnCardBin = returnCardBin;
    }


    public Boolean getReturnCardBin()
    {
        return this.returnCardBin;
    }


    public void setSendCustomerReceiptEmail(Boolean sendCustomerReceiptEmail)
    {
        this.sendCustomerReceiptEmail = sendCustomerReceiptEmail;
    }


    public Boolean getSendCustomerReceiptEmail()
    {
        return this.sendCustomerReceiptEmail;
    }


    public void setSendMerchantEmailPost(Boolean sendMerchantEmailPost)
    {
        this.sendMerchantEmailPost = sendMerchantEmailPost;
    }


    public Boolean getSendMerchantEmailPost()
    {
        return this.sendMerchantEmailPost;
    }


    public void setMerchantEmailPostAddress(String merchantEmailPostAddress)
    {
        this.merchantEmailPostAddress = merchantEmailPostAddress;
    }


    public String getMerchantEmailPostAddress()
    {
        return this.merchantEmailPostAddress;
    }


    public void setSendMerchantReceiptEmail(Boolean sendMerchantReceiptEmail)
    {
        this.sendMerchantReceiptEmail = sendMerchantReceiptEmail;
    }


    public Boolean getSendMerchantReceiptEmail()
    {
        return this.sendMerchantReceiptEmail;
    }


    public void setSendMerchantUrlPost(Boolean sendMerchantUrlPost)
    {
        this.sendMerchantUrlPost = sendMerchantUrlPost;
    }


    public Boolean getSendMerchantUrlPost()
    {
        return this.sendMerchantUrlPost;
    }


    public void setMerchantUrlPostAddress(String merchantUrlPostAddress)
    {
        this.merchantUrlPostAddress = merchantUrlPostAddress;
    }


    public String getMerchantUrlPostAddress()
    {
        return this.merchantUrlPostAddress;
    }
}
