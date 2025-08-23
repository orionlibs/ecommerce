package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public class PromotionGroup extends GeneratedPromotionGroup
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        String id = (String)allAttributes.get("Identifier");
        if(id != null)
        {
            PromotionGroup group = PromotionsManager.getInstance().getPromotionGroup(id);
            if(group != null)
            {
                String msg = "A PromotionGroup with the id " + id + " already exists!";
                throw new ConsistencyCheckException(msg, 100);
            }
        }
        return super.createItem(ctx, type, allAttributes);
    }
}
