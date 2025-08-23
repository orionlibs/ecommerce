package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.TypeManager;

public class TaxValuesTranslator extends CollectionValueTranslator
{
    public TaxValuesTranslator()
    {
        super((CollectionType)TypeManager.getInstance().getType("TaxValueCollection"), (AbstractValueTranslator)new TaxValueTranslator());
    }
}
