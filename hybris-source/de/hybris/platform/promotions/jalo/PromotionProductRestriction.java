package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.util.Helper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public class PromotionProductRestriction extends GeneratedPromotionProductRestriction
{
    private static final Logger LOG = Logger.getLogger(PromotionProductRestriction.class.getName());


    public AbstractPromotionRestriction.RestrictionResult evaluate(SessionContext ctx, Collection<Product> products, Date date, AbstractOrder order)
    {
        Collection<Product> restrictedProducts = getProducts(ctx);
        if(restrictedProducts != null && products != null && !restrictedProducts.isEmpty() && !products.isEmpty())
        {
            ArrayList<Product> productsToRemove = new ArrayList<>();
            for(Product testProduct : products)
            {
                if(isRestrictedProduct(restrictedProducts, testProduct))
                {
                    productsToRemove.add(testProduct);
                }
            }
            if(!productsToRemove.isEmpty())
            {
                products.removeAll(productsToRemove);
                return AbstractPromotionRestriction.RestrictionResult.ADJUSTED_PRODUCTS;
            }
        }
        return AbstractPromotionRestriction.RestrictionResult.ALLOW;
    }


    protected boolean isRestrictedProduct(Collection<Product> restrictedProducts, Product testProduct)
    {
        boolean result = restrictedProducts.contains(testProduct);
        if(!result)
        {
            List<Product> baseProducts = Helper.getBaseProducts(getSession().getSessionContext(), testProduct);
            for(Product baseProduct : baseProducts)
            {
                if(restrictedProducts.contains(baseProduct))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        Collection<Product> products = getProducts(ctx);
        if(products != null && !products.isEmpty())
        {
            for(Product p : products)
            {
                builder.append(p.getCode(ctx)).append(',');
            }
        }
        builder.append('|');
    }


    protected Object[] getDescriptionPatternArguments(SessionContext ctx)
    {
        return new Object[] {getRestrictionType(ctx), getProductNames(ctx)};
    }


    public String getProductNames(SessionContext ctx)
    {
        StringBuilder productNames = new StringBuilder();
        Collection<Product> products = getProducts(ctx);
        if(products != null && !products.isEmpty())
        {
            for(Iterator<Product> iterator = products.iterator(); iterator.hasNext(); )
            {
                Product product = iterator.next();
                productNames.append(product.getName());
                if(iterator.hasNext())
                {
                    productNames.append(", ");
                }
            }
        }
        return productNames.toString();
    }
}
