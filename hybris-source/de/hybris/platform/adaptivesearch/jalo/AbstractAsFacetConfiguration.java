package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang3.StringUtils;

public class AbstractAsFacetConfiguration extends GeneratedAbstractAsFacetConfiguration
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        updateUniqueIdx((AbstractAsFacetConfiguration)item);
        return item;
    }


    public void setAttribute(SessionContext ctx, String qualifier, Object value) throws JaloBusinessException
    {
        super.setAttribute(ctx, qualifier, value);
        if("uniqueIdx".equalsIgnoreCase(qualifier) || "indexProperty"
                        .equalsIgnoreCase(qualifier))
        {
            updateUniqueIdx(this);
        }
    }


    protected void updateUniqueIdx(AbstractAsFacetConfiguration facetConfiguration)
    {
        if(!LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.SYNC))
        {
            return;
        }
        AbstractAsSearchConfiguration searchConfiguration = (AbstractAsSearchConfiguration)facetConfiguration.getProperty("searchConfiguration");
        String previousUniqueIdx = facetConfiguration.getUniqueIdx();
        String uniqueIdx = generateItemIdentifier((Item)searchConfiguration) + "_" + generateItemIdentifier((Item)searchConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            facetConfiguration.setUniqueIdx(uniqueIdx);
        }
    }
}
