package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.jalo.Item;
import java.util.Collection;
import java.util.Map;

public interface ValueLineTranslator
{
    Map<StandardColumnDescriptor, Object> translateColumnValues(Collection<StandardColumnDescriptor> paramCollection, ValueLine paramValueLine, Item paramItem) throws InsufficientDataException;
}
