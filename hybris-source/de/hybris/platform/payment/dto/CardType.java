package de.hybris.platform.payment.dto;

import de.hybris.platform.core.enums.CreditCardType;
import java.io.Serializable;

public class CardType implements Serializable
{
    private final String id;
    private final CreditCardType code;
    private final String description;


    public CardType(String id, CreditCardType code, String description)
    {
        this.id = id;
        this.code = code;
        this.description = description;
    }


    public String getId()
    {
        return this.id;
    }


    public CreditCardType getCode()
    {
        return this.code;
    }


    public String getDescription()
    {
        return this.description;
    }
}
