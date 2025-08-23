package de.hybris.platform.promotions.util;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.PromotionPriceRow;
import de.hybris.platform.promotions.result.PromotionOrderEntry;
import java.util.Comparator;

public class Comparators
{
    public static final Comparator<PromotionPriceRow> promotionPriceRowComparator = (Comparator<PromotionPriceRow>)new PromotionPriceRowComparator();
    public static final Comparator<Currency> currencyComparator = (Comparator<Currency>)new CurrencyComparator();
    public static final Comparator<Double> doubleComparator = (Comparator<Double>)new DoubleComparator();
    public static final Comparator<String> stringComparator = (Comparator<String>)new StringComparator();
    public static final Comparator<Product> productComparator = (Comparator<Product>)new ProductComparator();
    public static final Comparator<CatalogVersion> catalogVersionComparator = (Comparator<CatalogVersion>)new CatalogVersionComparator();
    public static final Comparator<Catalog> catalogComparator = (Comparator<Catalog>)new CatalogComparator();
    public static final Comparator<Category> categoryComparator = (Comparator<Category>)new CategoryComparator();
    public static final Comparator<PromotionOrderEntry> promotionOrderEntryByPriceComparator = (Comparator<PromotionOrderEntry>)new PromotionOrderEntryByPriceComparator();
    private static final int A_B_NOT_NULL = 2;


    private static int internalCompare(Object a, Object b)
    {
        if(a == null && b == null)
        {
            return 0;
        }
        if(a == null)
        {
            return -1;
        }
        if(b == null)
        {
            return 1;
        }
        return 2;
    }
}
