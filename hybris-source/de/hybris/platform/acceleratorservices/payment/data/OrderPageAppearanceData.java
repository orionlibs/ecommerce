package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;

public class OrderPageAppearanceData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String orderPageBackgroundImageURL;
    private String orderPageBarColor;
    private String orderPageBarTextColor;
    private String orderPageBuyButtonText;
    private String orderPageColorScheme;
    private String orderPageMerchantDisplayName;
    private String orderPageMessageBoxBackgroundColor;
    private String orderPageRequiredFieldColor;


    public void setOrderPageBackgroundImageURL(String orderPageBackgroundImageURL)
    {
        this.orderPageBackgroundImageURL = orderPageBackgroundImageURL;
    }


    public String getOrderPageBackgroundImageURL()
    {
        return this.orderPageBackgroundImageURL;
    }


    public void setOrderPageBarColor(String orderPageBarColor)
    {
        this.orderPageBarColor = orderPageBarColor;
    }


    public String getOrderPageBarColor()
    {
        return this.orderPageBarColor;
    }


    public void setOrderPageBarTextColor(String orderPageBarTextColor)
    {
        this.orderPageBarTextColor = orderPageBarTextColor;
    }


    public String getOrderPageBarTextColor()
    {
        return this.orderPageBarTextColor;
    }


    public void setOrderPageBuyButtonText(String orderPageBuyButtonText)
    {
        this.orderPageBuyButtonText = orderPageBuyButtonText;
    }


    public String getOrderPageBuyButtonText()
    {
        return this.orderPageBuyButtonText;
    }


    public void setOrderPageColorScheme(String orderPageColorScheme)
    {
        this.orderPageColorScheme = orderPageColorScheme;
    }


    public String getOrderPageColorScheme()
    {
        return this.orderPageColorScheme;
    }


    public void setOrderPageMerchantDisplayName(String orderPageMerchantDisplayName)
    {
        this.orderPageMerchantDisplayName = orderPageMerchantDisplayName;
    }


    public String getOrderPageMerchantDisplayName()
    {
        return this.orderPageMerchantDisplayName;
    }


    public void setOrderPageMessageBoxBackgroundColor(String orderPageMessageBoxBackgroundColor)
    {
        this.orderPageMessageBoxBackgroundColor = orderPageMessageBoxBackgroundColor;
    }


    public String getOrderPageMessageBoxBackgroundColor()
    {
        return this.orderPageMessageBoxBackgroundColor;
    }


    public void setOrderPageRequiredFieldColor(String orderPageRequiredFieldColor)
    {
        this.orderPageRequiredFieldColor = orderPageRequiredFieldColor;
    }


    public String getOrderPageRequiredFieldColor()
    {
        return this.orderPageRequiredFieldColor;
    }
}
