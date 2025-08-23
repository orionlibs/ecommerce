package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class AuthReplyData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer ccAuthReplyReasonCode;
    private String ccAuthReplyAuthorizationCode;
    private String ccAuthReplyCvCode;
    private Boolean cvnDecision;
    private String ccAuthReplyAvsCodeRaw;
    private String ccAuthReplyAvsCode;
    private BigDecimal ccAuthReplyAmount;
    private String ccAuthReplyProcessorResponse;
    private String ccAuthReplyAuthorizedDateTime;


    public void setCcAuthReplyReasonCode(Integer ccAuthReplyReasonCode)
    {
        this.ccAuthReplyReasonCode = ccAuthReplyReasonCode;
    }


    public Integer getCcAuthReplyReasonCode()
    {
        return this.ccAuthReplyReasonCode;
    }


    public void setCcAuthReplyAuthorizationCode(String ccAuthReplyAuthorizationCode)
    {
        this.ccAuthReplyAuthorizationCode = ccAuthReplyAuthorizationCode;
    }


    public String getCcAuthReplyAuthorizationCode()
    {
        return this.ccAuthReplyAuthorizationCode;
    }


    public void setCcAuthReplyCvCode(String ccAuthReplyCvCode)
    {
        this.ccAuthReplyCvCode = ccAuthReplyCvCode;
    }


    public String getCcAuthReplyCvCode()
    {
        return this.ccAuthReplyCvCode;
    }


    public void setCvnDecision(Boolean cvnDecision)
    {
        this.cvnDecision = cvnDecision;
    }


    public Boolean getCvnDecision()
    {
        return this.cvnDecision;
    }


    public void setCcAuthReplyAvsCodeRaw(String ccAuthReplyAvsCodeRaw)
    {
        this.ccAuthReplyAvsCodeRaw = ccAuthReplyAvsCodeRaw;
    }


    public String getCcAuthReplyAvsCodeRaw()
    {
        return this.ccAuthReplyAvsCodeRaw;
    }


    public void setCcAuthReplyAvsCode(String ccAuthReplyAvsCode)
    {
        this.ccAuthReplyAvsCode = ccAuthReplyAvsCode;
    }


    public String getCcAuthReplyAvsCode()
    {
        return this.ccAuthReplyAvsCode;
    }


    public void setCcAuthReplyAmount(BigDecimal ccAuthReplyAmount)
    {
        this.ccAuthReplyAmount = ccAuthReplyAmount;
    }


    public BigDecimal getCcAuthReplyAmount()
    {
        return this.ccAuthReplyAmount;
    }


    public void setCcAuthReplyProcessorResponse(String ccAuthReplyProcessorResponse)
    {
        this.ccAuthReplyProcessorResponse = ccAuthReplyProcessorResponse;
    }


    public String getCcAuthReplyProcessorResponse()
    {
        return this.ccAuthReplyProcessorResponse;
    }


    public void setCcAuthReplyAuthorizedDateTime(String ccAuthReplyAuthorizedDateTime)
    {
        this.ccAuthReplyAuthorizedDateTime = ccAuthReplyAuthorizedDateTime;
    }


    public String getCcAuthReplyAuthorizedDateTime()
    {
        return this.ccAuthReplyAuthorizedDateTime;
    }
}
