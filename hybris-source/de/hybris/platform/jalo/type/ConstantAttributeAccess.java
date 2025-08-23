package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;

public class ConstantAttributeAccess extends AbstractNonChangeableAttributeAccess
{
    private final Object value;


    public ConstantAttributeAccess(String qualifier, Object value)
    {
        super(qualifier);
        this.value = value;
    }


    public Object getValue(SessionContext ctx, Item item) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException
    {
        return this.value;
    }
}
