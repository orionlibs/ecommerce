package de.hybris.platform.promotions.util.impex;

import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.CollectionValueTranslator;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.promotions.jalo.OrderThresholdPerfectPartnerPromotion;
import java.util.ArrayList;
import java.util.List;

public class PromotionPriceRowsTranslator extends CollectionValueTranslator
{
    public PromotionPriceRowsTranslator()
    {
        super(getCollectionType(), (AbstractValueTranslator)new PriceRowTranslator());
    }


    protected static CollectionType getCollectionType()
    {
        ComposedType ct = TypeManager.getInstance().getComposedType(OrderThresholdPerfectPartnerPromotion.class);
        AttributeDescriptor ad = ct.getAttributeDescriptor("thresholdTotals");
        Type t = ad.getAttributeType();
        return (CollectionType)t;
    }


    protected List splitAndUnescape(String valueExpr)
    {
        List<String> tokens = super.splitAndUnescape(valueExpr);
        if(tokens == null || tokens.size() < 2)
        {
            return tokens;
        }
        List<String> result = new ArrayList<>(tokens.size());
        result.add(tokens.get(0));
        int i = 1;
        for(int s = tokens.size(); i < s; i++)
        {
            String prev = tokens.get(i - 1);
            String current = tokens.get(i);
            if(Character.isDigit(prev.charAt(prev.length() - 1)))
            {
                int lastReaultPosition = result.size() - 1;
                String lastResultTokenText = result.get(lastReaultPosition);
                result.set(lastReaultPosition, lastResultTokenText + lastResultTokenText + getCollectionValueDelimiter());
            }
            else
            {
                result.add(current);
            }
        }
        return result;
    }
}
