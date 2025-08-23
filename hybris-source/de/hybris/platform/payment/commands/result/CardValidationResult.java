package de.hybris.platform.payment.commands.result;

import de.hybris.platform.core.enums.CreditCardType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

public class CardValidationResult
{
    private Set<CardValidationError> validationErrors;
    private String issuer;
    private String issueCountryCode;
    private CreditCardType cardType;


    public void setIssuer(String issuer)
    {
        this.issuer = issuer;
    }


    public String getIssuer()
    {
        return this.issuer;
    }


    public void setIssueCountryCode(String issueCountryCode)
    {
        this.issueCountryCode = issueCountryCode;
    }


    public String getIssueCountryCode()
    {
        return this.issueCountryCode;
    }


    public void setValidationErrors(Set<CardValidationError> validationErrors)
    {
        this.validationErrors = validationErrors;
    }


    public Set<CardValidationError> getValidationErrors()
    {
        return this.validationErrors;
    }


    public void addValidationError(CardValidationError validationError)
    {
        if(this.validationErrors == null)
        {
            this.validationErrors = new HashSet<>(1);
        }
        this.validationErrors.add(validationError);
    }


    public void addValidationErrors(Collection<CardValidationError> validationErrors)
    {
        for(CardValidationError error : validationErrors)
        {
            addValidationError(error);
        }
    }


    public boolean isSuccess()
    {
        return (this.validationErrors == null || this.validationErrors.isEmpty());
    }


    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


    public void setCardType(CreditCardType cardType)
    {
        this.cardType = cardType;
    }


    public CreditCardType getCardType()
    {
        return this.cardType;
    }
}
