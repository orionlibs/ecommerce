package de.hybris.platform.europe1.jalo.impex;

import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.TypeManager;

public class Europe1ProductDiscountTranslator extends Europe1DiscountsTranslator
{
    public Europe1ProductDiscountTranslator()
    {
        super((CollectionType)TypeManager.getInstance().getType("GlobalDiscountRowCollectionType"), (AbstractValueTranslator)new Europe1DiscountRowTranslator());
    }


    public Europe1ProductDiscountTranslator(AbstractValueTranslator elementTranslator)
    {
        super((CollectionType)TypeManager.getInstance().getType("GlobalDiscountRowCollectionType"), elementTranslator);
    }
}
