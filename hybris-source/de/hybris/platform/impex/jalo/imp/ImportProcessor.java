package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;

public interface ImportProcessor
{
    void init(ImpExImportReader paramImpExImportReader);


    Item processItemData(ValueLine paramValueLine) throws ImpExException;
}
