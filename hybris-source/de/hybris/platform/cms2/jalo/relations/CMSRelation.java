package de.hybris.platform.cms2.jalo.relations;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CMSRelation extends GeneratedCMSRelation
{
    private static final Logger log = Logger.getLogger(CMSRelation.class.getName());


    @Deprecated(since = "4.3")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        String cType = type.getCode();
        Object uid = allAttributes.get("uid");
        CatalogVersion catVersion = (CatalogVersion)allAttributes.get("catalogVersion");
        allAttributes.setAttributeMode("uid", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("catalogVersion", Item.AttributeMode.INITIAL);
        if(uid == null)
        {
            String number = NumberSeriesManager.getInstance().getUniqueNumber(cType);
            if(StringUtils.isBlank(number))
            {
                throw new JaloBusinessException("Could not create type: " + cType + " UID=" + number + " catalogVersion=" + catVersion);
            }
            uid = number;
            allAttributes.put(uid, number);
        }
        if(log.isDebugEnabled())
        {
            log.debug("Creating new " + cType + ": UID [" + uid + "]; catalogVersion [" + catVersion.getCatalog().getId() + ":" + catVersion
                            .getVersion() + "]");
        }
        return super.createItem(ctx, type, allAttributes);
    }
}
