package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;

public interface AttributeAccess
{
    void setValue(SessionContext paramSessionContext, Item paramItem, Object paramObject) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException, JaloBusinessException;


    Object getValue(SessionContext paramSessionContext, Item paramItem) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException;
}
