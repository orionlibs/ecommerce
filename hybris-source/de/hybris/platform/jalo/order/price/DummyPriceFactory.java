package de.hybris.platform.jalo.order.price;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.PriceValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DummyPriceFactory extends AbstractPriceFactory
{
    public static final String BEAN_NAME = "core.dummyPriceFactory";


    public static AbstractPriceFactory getInstance()
    {
        return (AbstractPriceFactory)Registry.getApplicationContext().getBean("core.dummyPriceFactory");
    }


    public PriceValue getBasePrice(AbstractOrderEntry entry) throws JaloPriceFactoryException
    {
        return new PriceValue(null, 0.0D, true);
    }


    public List getProductDiscountInformations(SessionContext ctx, Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return new ArrayList();
    }


    public List getProductPriceInformations(SessionContext ctx, Product product, Date date, boolean net) throws JaloPriceFactoryException
    {
        return new ArrayList();
    }


    public List getProductTaxInformations(SessionContext ctx, Product product, Date date) throws JaloPriceFactoryException
    {
        return new ArrayList();
    }


    public String getName()
    {
        return "dummypricefactory";
    }


    public void createProjectData(Map params, JspContext jspc)
    {
    }


    public void createEssentialData(Map params, JspContext jspc)
    {
    }
}
