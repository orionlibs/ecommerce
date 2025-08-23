package com.hybris.backoffice.jalo;

import com.hybris.backoffice.constants.GeneratedBackofficeConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBackofficeObjectCollectionItemReference extends GenericItem
{
    public static final String PRODUCT = "product";
    public static final String COLLECTIONPK = "collectionPk";
    protected static final BidirectionalOneToManyHandler<GeneratedBackofficeObjectCollectionItemReference> COLLECTIONPKHANDLER = new BidirectionalOneToManyHandler(GeneratedBackofficeConstants.TC.BACKOFFICEOBJECTCOLLECTIONITEMREFERENCE, false, "collectionPk", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("product", Item.AttributeMode.INITIAL);
        tmp.put("collectionPk", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public BackofficeObjectSpecialCollection getCollectionPk(SessionContext ctx)
    {
        return (BackofficeObjectSpecialCollection)getProperty(ctx, "collectionPk");
    }


    public BackofficeObjectSpecialCollection getCollectionPk()
    {
        return getCollectionPk(getSession().getSessionContext());
    }


    public void setCollectionPk(SessionContext ctx, BackofficeObjectSpecialCollection value)
    {
        COLLECTIONPKHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCollectionPk(BackofficeObjectSpecialCollection value)
    {
        setCollectionPk(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        COLLECTIONPKHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Product getProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "product");
    }


    public Product getProduct()
    {
        return getProduct(getSession().getSessionContext());
    }


    public void setProduct(SessionContext ctx, Product value)
    {
        setProperty(ctx, "product", value);
    }


    public void setProduct(Product value)
    {
        setProduct(getSession().getSessionContext(), value);
    }
}
