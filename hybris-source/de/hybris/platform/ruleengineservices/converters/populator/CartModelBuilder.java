package de.hybris.platform.ruleengineservices.converters.populator;

public class CartModelBuilder
{
    public static CartModelDraft newCart(String code)
    {
        return (new CartModelDraft()).setCode(code);
    }
}
