package de.hybris.platform.cms2.jalo.contents;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public abstract class CMSItem extends GeneratedCMSItem
{
    private static final Logger LOG = Logger.getLogger(CMSItem.class.getName());


    @Deprecated(since = "4.3")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("catalogVersion", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("uid", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new " + type.getCode(), 0);
        }
        allAttributes.setAttributeMode("catalogVersion", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("uid", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getName(SessionContext ctx)
    {
        String name = super.getName(ctx);
        if(StringUtils.isEmpty(name))
        {
            SessionContext lCtx = (ctx != null) ? ctx : getSession().getSessionContext();
            String compTypeName = (lCtx.getLanguage() == null) ? getComposedType().getCode() : getComposedType().getName(lCtx);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Name of CMSItem is empty or null. Returning ComposedType name <" + compTypeName + ">");
            }
            return compTypeName;
        }
        return name;
    }
}
