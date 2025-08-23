package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang3.StringUtils;

public class AsSimpleSearchConfiguration extends GeneratedAsSimpleSearchConfiguration
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        updateUniqueIdx((AsSimpleSearchConfiguration)item);
        return item;
    }


    public void setAttribute(SessionContext ctx, String qualifier, Object value) throws JaloBusinessException
    {
        super.setAttribute(ctx, qualifier, value);
        if("uniqueIdx".equalsIgnoreCase(qualifier))
        {
            updateUniqueIdx(this);
        }
    }


    protected void updateUniqueIdx(AsSimpleSearchConfiguration searchConfiguration)
    {
        if(!LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.SYNC))
        {
            return;
        }
        AbstractAsSearchProfile searchProfile = (AbstractAsSearchProfile)searchConfiguration.getProperty("searchProfile");
        String previousUniqueIdx = searchConfiguration.getUniqueIdx();
        String uniqueIdx = generateItemIdentifier((Item)searchProfile);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            searchConfiguration.setUniqueIdx(uniqueIdx);
        }
    }
}
