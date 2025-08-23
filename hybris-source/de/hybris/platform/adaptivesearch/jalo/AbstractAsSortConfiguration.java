package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractAsSortConfiguration extends GeneratedAbstractAsSortConfiguration
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        updateUniqueIdx((AbstractAsSortConfiguration)item);
        return item;
    }


    public void setAttribute(SessionContext ctx, String qualifier, Object value) throws JaloBusinessException
    {
        super.setAttribute(ctx, qualifier, value);
        if("uniqueIdx".equalsIgnoreCase(qualifier) || "code"
                        .equalsIgnoreCase(qualifier))
        {
            updateUniqueIdx(this);
        }
    }


    protected void updateUniqueIdx(AbstractAsSortConfiguration sortConfiguration)
    {
        if(!LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.SYNC))
        {
            return;
        }
        AbstractAsSearchConfiguration searchConfiguration = (AbstractAsSearchConfiguration)sortConfiguration.getProperty("searchConfiguration");
        String previousUniqueIdx = sortConfiguration.getUniqueIdx();
        String uniqueIdx = generateItemIdentifier((Item)searchConfiguration) + "_" + generateItemIdentifier((Item)searchConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            sortConfiguration.setUniqueIdx(uniqueIdx);
        }
    }
}
