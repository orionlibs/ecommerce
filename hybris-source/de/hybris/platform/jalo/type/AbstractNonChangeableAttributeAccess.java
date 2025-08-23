package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;

public abstract class AbstractNonChangeableAttributeAccess implements AttributeAccess
{
    private final String qualifier;


    protected AbstractNonChangeableAttributeAccess(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public void setValue(SessionContext ctx, Item item, Object value) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        throw new JaloInvalidParameterException("attribute " + this.qualifier + " is not changeable", 0);
    }
}
