package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.TypeManager;

public class DiscountValuesTranslator extends CollectionValueTranslator
{
    public DiscountValuesTranslator()
    {
        super((CollectionType)TypeManager.getInstance().getType("DiscountValueCollection"), (AbstractValueTranslator)new DiscountValueTranslator());
    }
}
