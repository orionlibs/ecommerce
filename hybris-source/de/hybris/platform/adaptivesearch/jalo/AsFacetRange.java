package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang3.StringUtils;

public class AsFacetRange extends GeneratedAsFacetRange
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        updateUniqueIdx((AsFacetRange)item);
        return item;
    }


    public void setAttribute(SessionContext ctx, String qualifier, Object value) throws JaloBusinessException
    {
        super.setAttribute(ctx, qualifier, value);
        if("uniqueIdx".equalsIgnoreCase(qualifier) || "id".equalsIgnoreCase(qualifier))
        {
            updateUniqueIdx(this);
        }
    }


    protected void updateUniqueIdx(AsFacetRange facetRange)
    {
        if(!LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.SYNC))
        {
            return;
        }
        AbstractAsFacetConfiguration facetConfiguration = facetRange.getFacetConfiguration();
        String previousUniqueIdx = facetRange.getUniqueIdx();
        String uniqueIdx = generateItemIdentifier((Item)facetConfiguration) + "_" + generateItemIdentifier((Item)facetConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            facetRange.setUniqueIdx(uniqueIdx);
        }
    }
}
