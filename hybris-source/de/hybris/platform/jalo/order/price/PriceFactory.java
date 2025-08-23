package de.hybris.platform.jalo.order.price;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.PriceValue;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Deprecated(since = "ages", forRemoval = false)
public interface PriceFactory
{
    @Deprecated(since = "ages", forRemoval = false)
    ProductPriceInformations getAllPriceInformations(SessionContext paramSessionContext, Product paramProduct, Date paramDate, boolean paramBoolean) throws JaloPriceFactoryException;


    @Deprecated(since = "ages", forRemoval = false)
    List getProductPriceInformations(SessionContext paramSessionContext, Product paramProduct, Date paramDate, boolean paramBoolean) throws JaloPriceFactoryException;


    @Deprecated(since = "ages", forRemoval = false)
    List getProductTaxInformations(SessionContext paramSessionContext, Product paramProduct, Date paramDate) throws JaloPriceFactoryException;


    @Deprecated(since = "ages", forRemoval = false)
    List getProductDiscountInformations(SessionContext paramSessionContext, Product paramProduct, Date paramDate, boolean paramBoolean) throws JaloPriceFactoryException;


    @Deprecated(since = "ages", forRemoval = false)
    boolean isNetUser(User paramUser);


    @Deprecated(since = "ages", forRemoval = false)
    Collection getTaxValues(AbstractOrderEntry paramAbstractOrderEntry) throws JaloPriceFactoryException;


    @Deprecated(since = "ages", forRemoval = false)
    PriceValue getBasePrice(AbstractOrderEntry paramAbstractOrderEntry) throws JaloPriceFactoryException;


    @Deprecated(since = "ages", forRemoval = false)
    List getDiscountValues(AbstractOrderEntry paramAbstractOrderEntry) throws JaloPriceFactoryException;


    @Deprecated(since = "ages", forRemoval = false)
    List getDiscountValues(AbstractOrder paramAbstractOrder) throws JaloPriceFactoryException;
}
