package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.jalo.Item;

public interface SpecialValueTranslator
{
    void init(SpecialColumnDescriptor paramSpecialColumnDescriptor) throws HeaderValidationException;


    void validate(String paramString) throws HeaderValidationException;


    String performExport(Item paramItem) throws ImpExException;


    void performImport(String paramString, Item paramItem) throws ImpExException;


    boolean isEmpty(String paramString);
}
