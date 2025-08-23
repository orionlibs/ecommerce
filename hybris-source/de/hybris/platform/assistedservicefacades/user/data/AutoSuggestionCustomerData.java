package de.hybris.platform.assistedservicefacades.user.data;

import java.io.Serializable;
import java.util.List;

public class AutoSuggestionCustomerData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String email;
    private String value;
    private String date;
    private String card;
    private List<String> carts;


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }


    public void setDate(String date)
    {
        this.date = date;
    }


    public String getDate()
    {
        return this.date;
    }


    public void setCard(String card)
    {
        this.card = card;
    }


    public String getCard()
    {
        return this.card;
    }


    public void setCarts(List<String> carts)
    {
        this.carts = carts;
    }


    public List<String> getCarts()
    {
        return this.carts;
    }
}
