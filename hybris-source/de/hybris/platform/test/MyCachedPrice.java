package de.hybris.platform.test;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.jalo.product.Product;

class MyCachedPrice extends AbstractCacheUnit
{
    private final Product product;


    public MyCachedPrice(Product product, Cache cache)
    {
        super(cache);
        this.product = product;
    }


    public Object compute() throws Exception
    {
        System.out.println("..computing price for " + this.product.getCode());
        Double price = (Double)this.product.getProperty("baseprice");
        Thread.sleep(1000L);
        return price;
    }


    public Object[] createKey()
    {
        return new Object[] {"myprice", this.product
                        .getPK()};
    }
}
