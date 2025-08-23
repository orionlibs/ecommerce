package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractAsBoostItemConfiguration extends GeneratedAbstractAsBoostItemConfiguration
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        updateUniqueIdx((AbstractAsBoostItemConfiguration)item);
        return item;
    }


    public void setAttribute(SessionContext ctx, String qualifier, Object value) throws JaloBusinessException
    {
        super.setAttribute(ctx, qualifier, value);
        if("uniqueIdx".equalsIgnoreCase(qualifier) || "item"
                        .equalsIgnoreCase(qualifier))
        {
            updateUniqueIdx(this);
        }
    }


    protected void updateUniqueIdx(AbstractAsBoostItemConfiguration boostItemConfiguration)
    {
        if(!LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.SYNC))
        {
            return;
        }
        AbstractAsSearchConfiguration searchConfiguration = (AbstractAsSearchConfiguration)boostItemConfiguration.getProperty("searchConfiguration");
        String previousUniqueIdx = boostItemConfiguration.getUniqueIdx();
        String uniqueIdx = generateItemIdentifier((Item)searchConfiguration) + "_" + generateItemIdentifier((Item)searchConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx) && isUniqueIdxChangeAllowed(previousUniqueIdx, uniqueIdx))
        {
            boostItemConfiguration.setUniqueIdx(uniqueIdx);
        }
    }


    protected boolean isUniqueIdxChangeAllowed(String previousUniqueIdx, String uniqueIdx)
    {
        if(StringUtils.isBlank(previousUniqueIdx))
        {
            return true;
        }
        String previousItemIdentifier = StringUtils.substringAfterLast(previousUniqueIdx, "_");
        String itemIdentifier = StringUtils.substringAfterLast(uniqueIdx, "_");
        boolean isPreviousItemNullIdentifier = StringUtils.equals(previousItemIdentifier, "null");
        boolean isItemNullIdentifier = StringUtils.equals(itemIdentifier, "null");
        return (isPreviousItemNullIdentifier == isItemNullIdentifier);
    }
}
