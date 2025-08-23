package de.hybris.platform.payment.commands.result;

import de.hybris.platform.payment.dto.AvsStatus;
import de.hybris.platform.payment.dto.CvnStatus;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class AuthorizationResult extends AbstractResult
{
    private Currency currency;
    private BigDecimal totalAmount;
    private AvsStatus avsStatus;
    private CvnStatus cvnStatus;
    private BigDecimal accountBalance;
    private String authorizationCode;
    private Date authorizationTime;
    private String paymentProvider;


    public Currency getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(Currency currency)
    {
        this.currency = currency;
    }


    public BigDecimal getTotalAmount()
    {
        return this.totalAmount;
    }


    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }


    public AvsStatus getAvsStatus()
    {
        return this.avsStatus;
    }


    public void setAvsStatus(AvsStatus avsStatus)
    {
        this.avsStatus = avsStatus;
    }


    public CvnStatus getCvnStatus()
    {
        return this.cvnStatus;
    }


    public void setCvnStatus(CvnStatus cvnStatus)
    {
        this.cvnStatus = cvnStatus;
    }


    public BigDecimal getAccountBalance()
    {
        return this.accountBalance;
    }


    public void setAccountBalance(BigDecimal accountBalance)
    {
        this.accountBalance = accountBalance;
    }


    public String getAuthorizationCode()
    {
        return this.authorizationCode;
    }


    public void setAuthorizationCode(String authorizationCode)
    {
        this.authorizationCode = authorizationCode;
    }


    public Date getAuthorizationTime()
    {
        return this.authorizationTime;
    }


    public void setAuthorizationTime(Date authorizationTime)
    {
        this.authorizationTime = authorizationTime;
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
