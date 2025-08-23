package de.hybris.platform.ruleengineservices.ruleengine.impl;

public class CartRaoBuilder
{
    public static CartRaoDraft newCart(String code)
    {
        return (new CartRaoDraft()).setCode(code);
    }
}
