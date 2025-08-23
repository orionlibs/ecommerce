package de.hybris.platform.europe1.jalo.impex;

import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.CollectionValueTranslator;
import de.hybris.platform.jalo.type.CollectionType;
import java.util.ArrayList;
import java.util.List;

public abstract class Europe1DiscountsTranslator extends CollectionValueTranslator
{
    public Europe1DiscountsTranslator(CollectionType targetType, AbstractValueTranslator elementTranslator)
    {
        super(targetType, elementTranslator);
    }


    protected List splitAndUnescape(String valueExpr)
    {
        List<String> tokens = super.splitAndUnescape(valueExpr);
        if(tokens == null || tokens.size() < 2)
        {
            return tokens;
        }
        List<String> ret = new ArrayList(tokens.size());
        ret.add(tokens.get(0));
        for(int i = 1; i < tokens.size(); i++)
        {
            String prev = tokens.get(i - 1);
            String current = tokens.get(i);
            if(Character.isDigit(prev.charAt(prev.length() - 1)))
            {
                String previous = ret.get(ret.size() - 1);
                ret.set(ret.size() - 1, previous + previous + getCollectionValueDelimiter());
            }
            else
            {
                ret.add(current);
            }
        }
        return ret;
    }
}
