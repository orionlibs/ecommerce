package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang3.StringUtils;

public class AbstractAsFacetValueConfiguration extends GeneratedAbstractAsFacetValueConfiguration
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        updateUniqueIdx((AbstractAsFacetValueConfiguration)item);
        return item;
    }


    public void setAttribute(SessionContext ctx, String qualifier, Object value) throws JaloBusinessException
    {
        super.setAttribute(ctx, qualifier, value);
        if("uniqueIdx".equalsIgnoreCase(qualifier) || "value"
                        .equalsIgnoreCase(qualifier))
        {
            updateUniqueIdx(this);
        }
    }


    protected void updateUniqueIdx(AbstractAsFacetValueConfiguration facetValueConfiguration)
    {
        if(!LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.SYNC))
        {
            return;
        }
        AbstractAsFacetConfiguration facetConfiguration = (AbstractAsFacetConfiguration)facetValueConfiguration.getProperty("facetConfiguration");
        String previousUniqueIdx = facetValueConfiguration.getUniqueIdx();
        String uniqueIdx = generateItemIdentifier((Item)facetConfiguration) + "_" + generateItemIdentifier((Item)facetConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            facetValueConfiguration.setUniqueIdx(uniqueIdx);
        }
    }
}
