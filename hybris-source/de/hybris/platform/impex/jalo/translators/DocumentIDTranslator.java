package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.AttributeDescriptor;

public class DocumentIDTranslator extends AtomicValueTranslator
{
    String pattern;
    DocumentIDRegistry registry;


    public DocumentIDTranslator(AttributeDescriptor attributeDescriptor, String pattern)
    {
        super(attributeDescriptor, PK.class);
        this.pattern = pattern.substring(1);
    }


    public void setRegistry(DocumentIDRegistry registry)
    {
        this.registry = registry;
    }


    protected Object convertToJalo(String expr, Item forItem)
    {
        long pk = this.registry.lookupID(this.pattern, expr);
        if(pk <= 0L)
        {
            setError();
            return null;
        }
        return PK.fromLong(pk);
    }


    protected String convertToString(Object value)
    {
        return this.registry.lookupPK(this.pattern, ((Item)value).getPK().getLongValue());
    }
}
