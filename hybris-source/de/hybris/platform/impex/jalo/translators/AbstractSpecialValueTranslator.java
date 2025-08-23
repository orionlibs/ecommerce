package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.jalo.Item;

public class AbstractSpecialValueTranslator implements SpecialValueTranslator
{
    public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
    }


    public void validate(String expr) throws HeaderValidationException
    {
    }


    public String performExport(Item item) throws ImpExException
    {
        throw new ImpExException("The export functionality is not supported by translator " + getClass().getName());
    }


    public void performImport(String cellValue, Item processedItem) throws ImpExException
    {
        throw new ImpExException("The import functionality is not supported by translator " + getClass().getName());
    }


    public boolean isEmpty(String cellValue)
    {
        return (cellValue == null || cellValue.length() == 0);
    }
}
