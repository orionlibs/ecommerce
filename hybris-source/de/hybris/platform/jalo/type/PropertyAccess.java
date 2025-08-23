package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;

public class PropertyAccess implements AttributeAccess
{
    protected final String qualifier;


    public PropertyAccess(String qualifier)
    {
        this.qualifier = qualifier.intern();
    }


    public void setValue(SessionContext ctx, Item item, Object value) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException
    {
        ((ExtensibleItem)item).setProperty(ctx, this.qualifier, value);
    }


    public Object getValue(SessionContext ctx, Item item) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException
    {
        return ((ExtensibleItem)item).getProperty(ctx, this.qualifier);
    }
}
