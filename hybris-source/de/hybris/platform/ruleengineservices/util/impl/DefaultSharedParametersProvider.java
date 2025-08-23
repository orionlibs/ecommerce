package de.hybris.platform.ruleengineservices.util.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.ruleengineservices.util.SharedParametersProvider;
import java.util.Set;

public class DefaultSharedParametersProvider implements SharedParametersProvider
{
    public Set<String> getAll()
    {
        return (Set<String>)ImmutableSet.of("cart_threshold", "cart_total_operator", "is_discounted_price_included");
    }
}
