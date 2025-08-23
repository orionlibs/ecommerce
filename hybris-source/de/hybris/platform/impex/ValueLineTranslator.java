package de.hybris.platform.impex;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.exceptions.InsufficientDataException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import java.util.Collection;
import java.util.Map;

public interface ValueLineTranslator<T>
{
    Map<? extends StandardColumnDescriptor, T> translateColumnValues(Collection<? extends StandardColumnDescriptor> paramCollection, ValueLine paramValueLine, ItemModel paramItemModel) throws InsufficientDataException;
}
