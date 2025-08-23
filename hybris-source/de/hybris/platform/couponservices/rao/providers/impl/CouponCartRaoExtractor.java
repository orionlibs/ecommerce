package de.hybris.platform.couponservices.rao.providers.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.providers.RAOFactsExtractor;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class CouponCartRaoExtractor implements RAOFactsExtractor
{
    public static final String EXPAND_COUPONS = "EXPAND_COUPONS";


    public Set expandFact(Object fact)
    {
        Preconditions.checkArgument(fact instanceof CartRAO, "CartRAO type is expected here");
        Set<Object> facts = new HashSet();
        CartRAO cartRAO = (CartRAO)fact;
        if(CollectionUtils.isNotEmpty(cartRAO.getCoupons()))
        {
            facts.addAll(cartRAO.getCoupons());
        }
        return facts;
    }


    public String getTriggeringOption()
    {
        return "EXPAND_COUPONS";
    }


    public boolean isMinOption()
    {
        return false;
    }


    public boolean isDefault()
    {
        return true;
    }
}
