package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.model.PriceRowModel;

public class PriceRowBuilder
{
    private final PriceRowModel model = new PriceRowModel();


    public static PriceRowBuilder aModel()
    {
        return new PriceRowBuilder();
    }


    private PriceRowModel getModel()
    {
        return this.model;
    }


    public PriceRowModel build()
    {
        return getModel();
    }


    public PriceRowBuilder withCurrency(CurrencyModel currency)
    {
        getModel().setCurrency(currency);
        return this;
    }


    public PriceRowBuilder withPrice(Double price)
    {
        getModel().setPrice(price);
        return this;
    }


    public PriceRowBuilder withProduct(ProductModel product)
    {
        getModel().setProduct(product);
        return this;
    }


    public PriceRowBuilder withUnit(UnitModel unit)
    {
        getModel().setUnit(unit);
        return this;
    }


    public PriceRowBuilder withCatalogVersion(CatalogVersionModel catalogVersion)
    {
        getModel().setCatalogVersion(catalogVersion);
        return this;
    }


    public PriceRowBuilder withUser(UserModel user)
    {
        getModel().setUser(user);
        return this;
    }


    public PriceRowBuilder withProductId(String productId)
    {
        getModel().setProductId(productId);
        return this;
    }
}
