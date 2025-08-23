package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.Item;

public interface NotifiedSpecialValueTranslator extends SpecialValueTranslator
{
    void notifyTranslationEnd(ValueLine paramValueLine, HeaderDescriptor paramHeaderDescriptor, Item paramItem) throws ImpExException;
}
