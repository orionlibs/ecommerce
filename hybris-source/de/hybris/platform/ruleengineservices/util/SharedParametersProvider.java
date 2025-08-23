package de.hybris.platform.ruleengineservices.util;

import java.util.Set;

public interface SharedParametersProvider
{
    public static final String CART_THRESHOLD = "cart_threshold";
    public static final String CART_TOTAL_OPERATOR = "cart_total_operator";
    public static final String IS_DISCOUNTED_PRICE_INCLUDED = "is_discounted_price_included";


    Set<String> getAll();
}
