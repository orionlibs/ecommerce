package de.hybris.platform.commercewebservices.core.validation.data;

import java.io.Serializable;

public class CartVoucherValidationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String subject;


    public void setSubject(String subject)
    {
        this.subject = subject;
    }


    public String getSubject()
    {
        return this.subject;
    }
}
