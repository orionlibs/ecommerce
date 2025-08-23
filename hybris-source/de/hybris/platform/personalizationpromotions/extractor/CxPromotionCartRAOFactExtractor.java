package de.hybris.platform.personalizationpromotions.extractor;

import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.UserRAO;
import de.hybris.platform.ruleengineservices.rao.providers.RAOFactsExtractor;
import java.util.HashSet;
import java.util.Set;

public class CxPromotionCartRAOFactExtractor implements RAOFactsExtractor
{
    public static final String EXPAND_PROMOTION_ACTION_RESULTS = "EXPAND_PROMOTION_ACTION_RESULTS";


    public Set<?> expandFact(Object raoFact)
    {
        Set<Object> facts = new HashSet();
        if(raoFact instanceof CartRAO)
        {
            UserRAO user = ((CartRAO)raoFact).getUser();
            if(user != null && user.getCxPromotionActionResults() != null)
            {
                facts.addAll(user.getCxPromotionActionResults());
            }
        }
        return facts;
    }


    public String getTriggeringOption()
    {
        return "EXPAND_PROMOTION_ACTION_RESULTS";
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
