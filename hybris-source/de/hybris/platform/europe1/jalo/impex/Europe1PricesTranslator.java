package de.hybris.platform.europe1.jalo.impex;

import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.CollectionValueTranslator;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class Europe1PricesTranslator extends CollectionValueTranslator
{
    public static final char BRUTTO = 'B';
    public static final char NETTO = 'N';
    public static final char GROSS = 'G';
    private static final Logger LOG = Logger.getLogger(Europe1PricesTranslator.class);


    public Europe1PricesTranslator()
    {
        super((CollectionType)TypeManager.getInstance().getType("PriceRowCollectionType"), (AbstractValueTranslator)new Europe1PriceRowTranslator());
    }


    public Europe1PricesTranslator(AbstractValueTranslator elementTranslator)
    {
        super((CollectionType)TypeManager.getInstance().getType("PriceRowCollectionType"), elementTranslator);
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
                String sxx = ret.get(ret.size() - 1);
                ret.set(ret.size() - 1, sxx + sxx + getCollectionValueDelimiter());
            }
            else
            {
                ret.add(current);
            }
        }
        return ret;
    }
}
