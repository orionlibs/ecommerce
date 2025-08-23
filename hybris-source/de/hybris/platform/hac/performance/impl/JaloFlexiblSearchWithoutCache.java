package de.hybris.platform.hac.performance.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import java.util.Collections;
import java.util.List;

public class JaloFlexiblSearchWithoutCache extends AbstractPerformanceTest
{
    public void executeBlock()
    {
        FlexibleSearch flexsearch = JaloSession.getCurrentSession().getFlexibleSearch();
        List<Product> res = flexsearch.search("SELECT {PK} FROM {Product}", Collections.EMPTY_MAP, Collections.singletonList(Product.class), false, true, 0, 200).getResult();
        for(Product prod : res)
        {
            prod.getCode();
        }
    }


    public String getTestName()
    {
        return "Jalo FlexibleSearch - 200 Products without cache";
    }


    public void cleanup()
    {
        Registry.getCurrentTenant().getCache().setEnabled(true);
    }


    public void prepare()
    {
        Registry.getCurrentTenant().getCache().setEnabled(false);
    }
}
