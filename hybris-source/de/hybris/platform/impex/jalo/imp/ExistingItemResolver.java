package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.header.UnresolvedValueException;
import java.util.Collection;

public interface ExistingItemResolver
{
    Collection findExisting(ValueLineTranslator paramValueLineTranslator, ValueLine paramValueLine) throws InsufficientDataException, UnresolvedValueException, AmbiguousItemException;


    void notifyItemCreatedOrRemoved(ValueLineTranslator paramValueLineTranslator, ValueLine paramValueLine);
}
