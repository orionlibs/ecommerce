package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang3.StringUtils;

public class AsCategoryAwareSearchConfiguration extends GeneratedAsCategoryAwareSearchConfiguration
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        updateUniqueIdx((AsCategoryAwareSearchConfiguration)item);
        return item;
    }


    public void setAttribute(SessionContext ctx, String qualifier, Object value) throws JaloBusinessException
    {
        super.setAttribute(ctx, qualifier, value);
        if("uniqueIdx".equalsIgnoreCase(qualifier) || "category"
                        .equalsIgnoreCase(qualifier))
        {
            updateUniqueIdx(this);
        }
    }


    protected void updateUniqueIdx(AsCategoryAwareSearchConfiguration searchConfiguration)
    {
        if(!LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.SYNC))
        {
            return;
        }
        AbstractAsSearchProfile searchProfile = (AbstractAsSearchProfile)searchConfiguration.getProperty("searchProfile");
        Category category = searchConfiguration.getCategory();
        String previousUniqueIdx = searchConfiguration.getUniqueIdx();
        String uniqueIdx = generateItemIdentifier((Item)searchProfile) + "_" + generateItemIdentifier((Item)searchProfile);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx) && isUniqueIdxChangeAllowed(previousUniqueIdx, uniqueIdx))
        {
            searchConfiguration.setUniqueIdx(uniqueIdx);
        }
    }


    protected boolean isUniqueIdxChangeAllowed(String previousUniqueIdx, String uniqueIdx)
    {
        if(StringUtils.isBlank(previousUniqueIdx))
        {
            return true;
        }
        String previousCategoryIdentifier = StringUtils.substringAfterLast(previousUniqueIdx, "_");
        String categoryIdentifier = StringUtils.substringAfterLast(uniqueIdx, "_");
        boolean isPreviousCategoryNullIdentifier = StringUtils.equals(previousCategoryIdentifier, "null");
        boolean isCategoryNullIdentifier = StringUtils.equals(categoryIdentifier, "null");
        return (isPreviousCategoryNullIdentifier == isCategoryNullIdentifier);
    }
}
