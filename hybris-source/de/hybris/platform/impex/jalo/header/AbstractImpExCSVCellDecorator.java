package de.hybris.platform.impex.jalo.header;

import de.hybris.platform.util.CSVCellDecorator;

public abstract class AbstractImpExCSVCellDecorator implements CSVCellDecorator
{
    private AbstractColumnDescriptor column;


    public void init(AbstractColumnDescriptor column) throws HeaderValidationException
    {
        this.column = column;
    }


    protected AbstractColumnDescriptor getColumnDescriptor()
    {
        return this.column;
    }
}
