package de.hybris.platform.voucher.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.variants.jalo.VariantProduct;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

public class ProductCategoryRestriction extends GeneratedProductCategoryRestriction
{
    private static final Logger LOG = Logger.getLogger(ProductCategoryRestriction.class.getName());


    public Collection getCategories(SessionContext ctx)
    {
        Collection categories = super.getCategories(ctx);
        return (categories != null) ? categories : Collections.emptyList();
    }


    protected String getCategoryNames()
    {
        StringBuilder categoryNames = new StringBuilder();
        for(Iterator<Category> iterator = getCategories().iterator(); iterator.hasNext(); )
        {
            Category category = iterator.next();
            categoryNames.append(category.getName());
            if(iterator.hasNext())
            {
                categoryNames.append(", ");
            }
        }
        return categoryNames.toString();
    }


    public VoucherEntrySet getApplicableEntries(AbstractOrder anOrder)
    {
        Set<Product> fulfilledProducts = new HashSet<>();
        Set<Product> unfulfilledProducts = new HashSet<>();
        Collection<Category> restrictCategories = getCategories();
        VoucherEntrySet entries = new VoucherEntrySet();
        for(AbstractOrderEntry entry : anOrder.getEntries())
        {
            Product product = entry.getProduct();
            if(fulfilledProducts.contains(product))
            {
                entries.add(new VoucherEntry(entry, entry.getQuantity().longValue(), entry.getUnit()));
                continue;
            }
            if(unfulfilledProducts.contains(product))
            {
                continue;
            }
            boolean fulfilledProduct = isFulfilled(product, restrictCategories);
            if(fulfilledProduct)
            {
                entries.add(new VoucherEntry(entry, entry.getQuantity().longValue(), entry.getUnit()));
                fulfilledProducts.add(product);
                continue;
            }
            unfulfilledProducts.add(product);
        }
        return entries;
    }


    protected boolean isFulfilledInternal(Product product)
    {
        Collection<Category> restrictCategories = getCategories();
        return isFulfilled(product, restrictCategories);
    }


    private boolean isFulfilled(Product product, Collection<Category> restrictCategories)
    {
        boolean contained = containsProductCategory(product, restrictCategories);
        return (contained == isPositiveAsPrimitive());
    }


    private boolean containsProductCategory(Product product, Collection<Category> restrictCategories)
    {
        Collection<Category> categories;
        CategoryManager catManager = CategoryManager.getInstance();
        if(product instanceof VariantProduct)
        {
            Product baseProduct = ((VariantProduct)product).getBaseProduct();
            categories = new HashSet<>(catManager.getSupercategories(baseProduct));
            categories.addAll(catManager.getSupercategories(product));
        }
        else
        {
            categories = catManager.getSupercategories(product);
        }
        return containsCategory(categories, restrictCategories);
    }


    private boolean containsCategory(Collection<Category> categories, Collection<Category> restrictCategories)
    {
        if(categories.isEmpty())
        {
            return false;
        }
        boolean result = false;
        for(Category category : categories)
        {
            if(restrictCategories.contains(category))
            {
                result = true;
                break;
            }
        }
        if(!result)
        {
            for(Category category : categories)
            {
                result = containsCategory(category.getSupercategories(), restrictCategories);
                if(result)
                {
                    break;
                }
            }
        }
        return result;
    }


    protected String[] getMessageAttributeValues()
    {
        return new String[] {Localization.getLocalizedString("type.restriction.positive." + isPositiveAsPrimitive()), getCategoryNames()};
    }


    public Collection getProducts(SessionContext ctx)
    {
        return getProducts(ctx, new HashSet(), getCategories(ctx));
    }


    private Collection getProducts(SessionContext ctx, Collection<Category> crawledCategories, Collection uncrawledCategories)
    {
        Collection products = new HashSet();
        if(uncrawledCategories != null)
        {
            for(Iterator<Category> iterator = uncrawledCategories.iterator(); iterator.hasNext(); )
            {
                Category nextCategory = iterator.next();
                if(crawledCategories.add(nextCategory))
                {
                    products.addAll(nextCategory.getProducts(ctx));
                    products.addAll(getProducts(ctx, crawledCategories, nextCategory.getSubcategories(ctx)));
                }
            }
        }
        return products;
    }
}
