package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2lib.model.components.AbstractBannerComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SimpleBannerComponentModel extends AbstractBannerComponentModel
{
    public static final String _TYPECODE = "SimpleBannerComponent";
    public static final String _SIMPLEBANNERCOMPONENTSFORCONTENTPAGE = "SimpleBannerComponentsForContentPage";
    public static final String _SIMPLEBANNERCOMPONENTSFORPRODUCT = "SimpleBannerComponentsForProduct";
    public static final String _SIMPLEBANNERCOMPONENTSFORCATEGORY = "SimpleBannerComponentsForCategory";
    public static final String CONTENTPAGEPOS = "contentPagePOS";
    public static final String CONTENTPAGE = "contentPage";
    public static final String PRODUCTPOS = "productPOS";
    public static final String PRODUCT = "product";
    public static final String CATEGORYPOS = "categoryPOS";
    public static final String CATEGORY = "category";


    public SimpleBannerComponentModel()
    {
    }


    public SimpleBannerComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleBannerComponentModel(CatalogVersionModel _catalogVersion, boolean _external, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleBannerComponentModel(CatalogVersionModel _catalogVersion, boolean _external, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "category", type = Accessor.Type.GETTER)
    public CategoryModel getCategory()
    {
        return (CategoryModel)getPersistenceContext().getPropertyValue("category");
    }


    @Accessor(qualifier = "contentPage", type = Accessor.Type.GETTER)
    public ContentPageModel getContentPage()
    {
        return (ContentPageModel)getPersistenceContext().getPropertyValue("contentPage");
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public ProductModel getProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("product");
    }


    @Accessor(qualifier = "category", type = Accessor.Type.SETTER)
    public void setCategory(CategoryModel value)
    {
        getPersistenceContext().setPropertyValue("category", value);
    }


    @Accessor(qualifier = "contentPage", type = Accessor.Type.SETTER)
    public void setContentPage(ContentPageModel value)
    {
        getPersistenceContext().setPropertyValue("contentPage", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }
}
