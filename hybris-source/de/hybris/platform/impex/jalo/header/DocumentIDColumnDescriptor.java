package de.hybris.platform.impex.jalo.header;

import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;

public class DocumentIDColumnDescriptor extends AbstractColumnDescriptor
{
    private final String documentIDQualifier;
    private final DocumentIDRegistry documentIDRegistry;


    public DocumentIDColumnDescriptor(int position, HeaderDescriptor header, String expr) throws HeaderValidationException
    {
        super(position, header, expr);
        if(header.getDocumentIDRegistry() == null)
        {
            throw new HeaderValidationException("A document id attribute is used but no registry is given", 5);
        }
        this.documentIDQualifier = expr.substring(1);
        this.documentIDRegistry = header.getDocumentIDRegistry();
    }


    public void registerIDForItem(String cellValue, Item item) throws ImpExException
    {
        registerIDForItem(cellValue, item.getPK());
    }


    public void registerIDForItem(String cellValue, PK itemPK) throws ImpExException
    {
        if(cellValue != null && cellValue.length() != 0)
        {
            this.documentIDRegistry.registerID(this.documentIDQualifier, cellValue, itemPK.getLongValue());
        }
    }


    public String generateIDForItem(Item item)
    {
        return this.documentIDRegistry.registerPK(this.documentIDQualifier, item.getPK().getLongValue());
    }


    protected void validate() throws HeaderValidationException
    {
        super.validate();
        if(this.documentIDQualifier != null && this.documentIDRegistry == null)
        {
            throw new HeaderValidationException("A document id attribute is used but no registry is given", 5);
        }
    }
}
