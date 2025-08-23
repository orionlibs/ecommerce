package de.hybris.platform.jalo.order;

import de.hybris.platform.jalo.type.ComposedType;

public interface CloneOrderStrategy<T extends Order>
{
    T clone(ComposedType paramComposedType1, ComposedType paramComposedType2, AbstractOrder paramAbstractOrder, OrderManager paramOrderManager);
}
