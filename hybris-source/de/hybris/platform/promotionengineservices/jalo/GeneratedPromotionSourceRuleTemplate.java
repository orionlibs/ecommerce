package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.ruleengineservices.jalo.SourceRuleTemplate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionSourceRuleTemplate extends SourceRuleTemplate
{
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SourceRuleTemplate.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }
}
