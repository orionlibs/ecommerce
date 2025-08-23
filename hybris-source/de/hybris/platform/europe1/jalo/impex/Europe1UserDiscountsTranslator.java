package de.hybris.platform.europe1.jalo.impex;

import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.TypeManager;

public class Europe1UserDiscountsTranslator extends Europe1DiscountsTranslator
{
    public Europe1UserDiscountsTranslator()
    {
        super((CollectionType)TypeManager.getInstance().getType("GlobalDiscountRowCollectionType"), (AbstractValueTranslator)new Europe1GlobalDiscountRowTranslator());
    }


    public Europe1UserDiscountsTranslator(AbstractValueTranslator elementTranslator)
    {
        super((CollectionType)TypeManager.getInstance().getType("GlobalDiscountRowCollectionType"), elementTranslator);
    }
}
