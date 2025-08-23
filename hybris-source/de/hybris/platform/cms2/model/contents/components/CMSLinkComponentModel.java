package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class CMSLinkComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "CMSLinkComponent";
    public static final String _CMSLINKCOMPONENTSFORCONTENTPAGE = "CmsLinkComponentsForContentPage";
    public static final String _CMSLINKCOMPONENTSFORPRODUCT = "CmsLinkComponentsForProduct";
    public static final String _CMSLINKCOMPONENTSFORCATEGORY = "CmsLinkComponentsForCategory";
    public static final String LINKNAME = "linkName";
    public static final String EXTERNAL = "external";
    public static final String URL = "url";
    public static final String CONTENTPAGELABELORID = "contentPageLabelOrId";
    public static final String PRODUCTCODE = "productCode";
    public static final String CATEGORYCODE = "categoryCode";
    public static final String TARGET = "target";
    public static final String NAVIGATIONNODES = "navigationNodes";
    public static final String CONTENTPAGEPOS = "contentPagePOS";
    public static final String CONTENTPAGE = "contentPage";
    public static final String PRODUCTPOS = "productPOS";
    public static final String PRODUCT = "product";
    public static final String CATEGORYPOS = "categoryPOS";
    public static final String CATEGORY = "category";
    public static final String STYLEATTRIBUTES = "styleAttributes";


    public CMSLinkComponentModel()
    {
    }


    public CMSLinkComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSLinkComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSLinkComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "category", type = Accessor.Type.GETTER)
    public CategoryModel getCategory()
    {
        return (CategoryModel)getPersistenceContext().getPropertyValue("category");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
    public String getCategoryCode()
    {
        return (String)getPersistenceContext().getPropertyValue("categoryCode");
    }


    @Accessor(qualifier = "contentPage", type = Accessor.Type.GETTER)
    public ContentPageModel getContentPage()
    {
        return (ContentPageModel)getPersistenceContext().getPropertyValue("contentPage");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "contentPageLabelOrId", type = Accessor.Type.GETTER)
    public String getContentPageLabelOrId()
    {
        return (String)getPersistenceContext().getPropertyValue("contentPageLabelOrId");
    }


    @Accessor(qualifier = "linkName", type = Accessor.Type.GETTER)
    public String getLinkName()
    {
        return getLinkName(null);
    }


    @Accessor(qualifier = "linkName", type = Accessor.Type.GETTER)
    public String getLinkName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("linkName", loc);
    }


    @Deprecated(since = "4.4", forRemoval = true)
    @Accessor(qualifier = "navigationNodes", type = Accessor.Type.GETTER)
    public List<CMSNavigationNodeModel> getNavigationNodes()
    {
        return (List<CMSNavigationNodeModel>)getPersistenceContext().getPropertyValue("navigationNodes");
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public ProductModel getProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("product");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
    public String getProductCode()
    {
        return (String)getPersistenceContext().getPropertyValue("productCode");
    }


    @Accessor(qualifier = "styleAttributes", type = Accessor.Type.GETTER)
    public String getStyleAttributes()
    {
        return (String)getPersistenceContext().getPropertyValue("styleAttributes");
    }


    @Accessor(qualifier = "target", type = Accessor.Type.GETTER)
    public LinkTargets getTarget()
    {
        return (LinkTargets)getPersistenceContext().getPropertyValue("target");
    }


    @Accessor(qualifier = "url", type = Accessor.Type.GETTER)
    public String getUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("url");
    }


    @Accessor(qualifier = "external", type = Accessor.Type.GETTER)
    public boolean isExternal()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("external"));
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


    @Accessor(qualifier = "external", type = Accessor.Type.SETTER)
    public void setExternal(boolean value)
    {
        getPersistenceContext().setPropertyValue("external", toObject(value));
    }


    @Accessor(qualifier = "linkName", type = Accessor.Type.SETTER)
    public void setLinkName(String value)
    {
        setLinkName(value, null);
    }


    @Accessor(qualifier = "linkName", type = Accessor.Type.SETTER)
    public void setLinkName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("linkName", loc, value);
    }


    @Deprecated(since = "4.4", forRemoval = true)
    @Accessor(qualifier = "navigationNodes", type = Accessor.Type.SETTER)
    public void setNavigationNodes(List<CMSNavigationNodeModel> value)
    {
        getPersistenceContext().setPropertyValue("navigationNodes", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }


    @Accessor(qualifier = "styleAttributes", type = Accessor.Type.SETTER)
    public void setStyleAttributes(String value)
    {
        getPersistenceContext().setPropertyValue("styleAttributes", value);
    }


    @Accessor(qualifier = "target", type = Accessor.Type.SETTER)
    public void setTarget(LinkTargets value)
    {
        getPersistenceContext().setPropertyValue("target", value);
    }


    @Accessor(qualifier = "url", type = Accessor.Type.SETTER)
    public void setUrl(String value)
    {
        getPersistenceContext().setPropertyValue("url", value);
    }
}
