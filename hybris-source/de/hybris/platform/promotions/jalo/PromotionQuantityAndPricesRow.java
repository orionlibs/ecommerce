package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import org.apache.log4j.Logger;

public class PromotionQuantityAndPricesRow extends GeneratedPromotionQuantityAndPricesRow
{
    private static final Logger LOG = Logger.getLogger(PromotionQuantityAndPricesRow.class.getName());


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        AbstractPromotion.deletePromotionPriceRows(ctx, getPrices(ctx));
        super.remove(ctx);
    }
}
