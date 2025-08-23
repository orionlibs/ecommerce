package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang3.StringUtils;

public class AsSortExpression extends GeneratedAsSortExpression
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        updateUniqueIdx((AsSortExpression)item);
        return item;
    }


    public void setAttribute(SessionContext ctx, String qualifier, Object value) throws JaloBusinessException
    {
        super.setAttribute(ctx, qualifier, value);
        if("uniqueIdx".equalsIgnoreCase(qualifier) || "expression"
                        .equalsIgnoreCase(qualifier))
        {
            updateUniqueIdx(this);
        }
    }


    protected void updateUniqueIdx(AsSortExpression sortExpression)
    {
        if(!LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.SYNC))
        {
            return;
        }
        AbstractAsSortConfiguration sortConfiguration = sortExpression.getSortConfiguration();
        String previousUniqueIdx = sortExpression.getUniqueIdx();
        String uniqueIdx = generateItemIdentifier((Item)sortConfiguration) + "_" + generateItemIdentifier((Item)sortConfiguration);
        if(!StringUtils.equals(previousUniqueIdx, uniqueIdx))
        {
            sortExpression.setUniqueIdx(uniqueIdx);
        }
    }
}
