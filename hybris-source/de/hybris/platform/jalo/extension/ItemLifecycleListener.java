package de.hybris.platform.jalo.extension;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public interface ItemLifecycleListener
{
    void afterItemCreation(SessionContext paramSessionContext, ComposedType paramComposedType, Item paramItem, Item.ItemAttributeMap paramItemAttributeMap) throws JaloBusinessException;
}
