package de.hybris.platform.promotions.result;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.util.Helper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

public class PromotionEvaluationContext
{
    private static final Logger LOG = Logger.getLogger(PromotionEvaluationContext.class.getName());
    private final List<PromotionOrderEntry> orderEntries = new ArrayList<>();
    private final ConsumptionLogger logger = new ConsumptionLogger();
    private AbstractOrder order = null;
    private boolean observeRestrictions = true;
    private final Date date;


    public PromotionEvaluationContext(AbstractOrder order, boolean observeRestrictions, Date date)
    {
        this.order = order;
        this.observeRestrictions = observeRestrictions;
        this.date = date;
        for(AbstractOrderEntry entry : this.order.getAllEntries())
        {
            this.orderEntries.add(new PromotionOrderEntry(entry, this.logger));
        }
    }


    public void startLoggingConsumed(AbstractPromotion promo)
    {
        getLogger().startLogging(promo);
    }


    public List<PromotionOrderEntryConsumed> finishLoggingAndGetConsumed(AbstractPromotion promo, boolean removeFromOrder)
    {
        return getLogger().completeOperation(promo, removeFromOrder);
    }


    public void abandonLogging(AbstractPromotion promo)
    {
        getLogger().abandonOperation(promo);
    }


    protected ConsumptionLogger getLogger()
    {
        return this.logger;
    }


    public AbstractOrder getOrder()
    {
        return this.order;
    }


    public boolean getObserveRestrictions()
    {
        return this.observeRestrictions;
    }


    public PromotionOrderView createView(SessionContext ctx, AbstractPromotion promotion, List<Product> validProducts)
    {
        List<PromotionOrderEntry> validEntries = new ArrayList<>();
        for(PromotionOrderEntry entry : this.orderEntries)
        {
            Product product = entry.getProduct(ctx);
            List<Product> baseProducts = Helper.getBaseProducts(ctx, product);
            boolean validProductsContainsOneBaseProduct = false;
            if(baseProducts != null && validProducts != null)
            {
                for(Product baseProduct : baseProducts)
                {
                    if(validProducts.contains(baseProduct))
                    {
                        validProductsContainsOneBaseProduct = true;
                        break;
                    }
                }
            }
            boolean productIsInBaseProducts = (baseProducts != null) ? baseProducts.contains(product) : false;
            if(validProducts == null || validProductsContainsOneBaseProduct || (!productIsInBaseProducts && validProducts
                            .contains(product)))
            {
                validEntries.add(entry);
            }
        }
        return new PromotionOrderView(promotion, validEntries);
    }


    public Date getDate()
    {
        return this.date;
    }


    public static Comparator<PromotionOrderEntry> createPriceComparator(SessionContext ctx)
    {
        return (Comparator<PromotionOrderEntry>)new Object(ctx);
    }
}
