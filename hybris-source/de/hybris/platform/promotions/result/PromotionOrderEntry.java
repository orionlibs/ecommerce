package de.hybris.platform.promotions.result;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.util.Helper;
import java.util.List;
import java.util.Objects;

public class PromotionOrderEntry implements Comparable
{
    private final PromotionEvaluationContext.ConsumptionLogger logger;
    private final AbstractOrderEntry baseEntry;


    public PromotionOrderEntry(AbstractOrderEntry orderEntry, PromotionEvaluationContext.ConsumptionLogger logger)
    {
        this.baseEntry = orderEntry;
        this.logger = logger;
    }


    public PromotionOrderEntryConsumed consume(SessionContext ctx, AbstractPromotion promotion, long quantity)
    {
        if(quantity == 0L)
        {
            throw new PromotionException("Cannot consume zero products from an OrderEntry");
        }
        long resultingQuantity = getQuantity(ctx) - quantity;
        if(resultingQuantity < 0L)
        {
            throw new PromotionException("Cannot remove " + quantity + " items.  There is not a sufficient quantity of this product remaining.");
        }
        PromotionOrderEntryConsumed consumed = PromotionsManager.getInstance().createPromotionOrderEntryConsumed(ctx, "", this.baseEntry, quantity);
        this.logger.logOperation(consumed);
        return consumed;
    }


    public long getQuantity(SessionContext ctx)
    {
        long resultingQuantity = this.baseEntry.getQuantity(ctx).longValue();
        for(PromotionOrderEntryConsumed poec : this.logger.getAllOperations())
        {
            if(poec.isRemovedFromOrder() && poec.getOrderEntry(ctx).equals(this.baseEntry))
            {
                resultingQuantity -= poec.getQuantity().longValue();
            }
        }
        return resultingQuantity;
    }


    public Product getProduct(SessionContext ctx)
    {
        return this.baseEntry.getProduct(ctx);
    }


    public List<Product> getBaseProducts(SessionContext ctx)
    {
        return Helper.getBaseProducts(ctx, this.baseEntry.getProduct(ctx));
    }


    public Double getBasePrice(SessionContext ctx)
    {
        return this.baseEntry.getBasePrice(ctx);
    }


    public int compareTo(Object o)
    {
        return this.baseEntry.compareTo(o);
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        PromotionOrderEntry that = (PromotionOrderEntry)o;
        return this.baseEntry.equals(that.baseEntry);
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.baseEntry});
    }


    public AbstractOrderEntry getBaseOrderEntry()
    {
        return this.baseEntry;
    }


    protected PromotionEvaluationContext.ConsumptionLogger getLogger()
    {
        return this.logger;
    }
}
