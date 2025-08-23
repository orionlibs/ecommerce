package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.jalo.security.JaloSecurityException;
import java.util.Map;

public class LocalizableFeatureAccess extends PropertyAccess
{
    public LocalizableFeatureAccess(String qualifier)
    {
        super(qualifier);
    }


    public void setValue(SessionContext ctx, Item item, Object value) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException
    {
        if(ctx.getLanguage() == null)
        {
            if(value != null && !(value instanceof Map))
            {
                throw new JaloInvalidParameterException("setting localized property '" + this.qualifier + "' without language requires Map{ Language -> Serializable } as value ( got value class '" + value
                                .getClass().getName() + "' instead", -1);
            }
            ((LocalizableItem)item).setAllLocalizedProperties(ctx, this.qualifier, (Map)value);
        }
        else
        {
            ((LocalizableItem)item).setLocalizedProperty(ctx, this.qualifier, value);
        }
    }


    public Object getValue(SessionContext ctx, Item item) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException
    {
        return ((LocalizableItem)item).getLocalizedProperty(ctx, this.qualifier);
    }
}
