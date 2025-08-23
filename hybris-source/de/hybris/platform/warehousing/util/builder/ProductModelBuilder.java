package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import java.util.Locale;

public class ProductModelBuilder
{
    private final ProductModel model = new ProductModel();


    private ProductModel getModel()
    {
        return this.model;
    }


    public static ProductModelBuilder aModel()
    {
        return new ProductModelBuilder();
    }


    public ProductModel build()
    {
        return getModel();
    }


    public ProductModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public ProductModelBuilder withCatalogVersion(CatalogVersionModel catalogVersion)
    {
        getModel().setCatalogVersion(catalogVersion);
        return this;
    }


    public ProductModelBuilder withName(String name, Locale locale)
    {
        getModel().setName(name, locale);
        return this;
    }


    public ProductModelBuilder withUnit(UnitModel unit)
    {
        getModel().setUnit(unit);
        return this;
    }


    public ProductModelBuilder withPrice(Double price)
    {
        getModel().setPriceQuantity(price);
        return this;
    }


    public ProductModelBuilder withApprovalStatus(ArticleApprovalStatus approvalStatus)
    {
        getModel().setApprovalStatus(approvalStatus);
        return this;
    }


    public ProductModelBuilder withStartLineNumber(Integer startLineNumber)
    {
        getModel().setStartLineNumber(startLineNumber);
        return this;
    }


    public ProductModelBuilder withOwner(ItemModel owner)
    {
        getModel().setOwner(owner);
        return this;
    }
}
