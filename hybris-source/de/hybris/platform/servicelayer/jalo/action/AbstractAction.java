package de.hybris.platform.servicelayer.jalo.action;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public abstract class AbstractAction extends GeneratedAbstractAction
{
    private static final Logger LOG = Logger.getLogger(AbstractAction.class.getName());


    @SLDSafe(portingClass = "UniqueAttributesInterceptor,MandatoryAttributesValidator")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set<String> missing = new HashSet<>();
        if(((!checkMandatoryAttribute("code", allAttributes, missing) ? 1 : 0) | (!checkMandatoryAttribute("type", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("target", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " to create a new " + type.getCode(), 0);
        }
        return super.createItem(ctx, type, allAttributes);
    }
}
