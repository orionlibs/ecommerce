package de.hybris.platform.jalo.order;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.type.ComposedType;

public interface CloneCartStrategy<T extends Cart>
{
    T clone(ComposedType paramComposedType1, ComposedType paramComposedType2, AbstractOrder paramAbstractOrder, String paramString, OrderManager paramOrderManager) throws ConsistencyCheckException;
}
