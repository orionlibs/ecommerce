package de.hybris.platform.cms2lib.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2.jalo.pages.ContentPage;
import de.hybris.platform.cms2lib.components.BannerComponent;
import de.hybris.platform.cms2lib.components.FlashComponent;
import de.hybris.platform.cms2lib.components.ProductCarouselComponent;
import de.hybris.platform.cms2lib.components.ProductDetailComponent;
import de.hybris.platform.cms2lib.components.ProductListComponent;
import de.hybris.platform.cms2lib.components.RotatingImagesComponent;
import de.hybris.platform.cms2lib.constants.GeneratedCms2LibConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCms2LibManager extends Extension
{
    protected static final OneToManyHandler<BannerComponent> BANNERSFORCONTENTPAGEBANNERCOMPONETSHANDLER = new OneToManyHandler(GeneratedCms2LibConstants.TC.BANNERCOMPONENT, false, "page", null, false, true, 2);
    protected static final OneToManyHandler<FlashComponent> FLASHCOMPONENTSFORCONTENTPAGEFLASHCOMPONENTSHANDLER = new OneToManyHandler(GeneratedCms2LibConstants.TC.FLASHCOMPONENT, false, "page", null, false, true, 2);
    protected static String PRODUCTSFORPRODUCTLISTCOMPONENT_SRC_ORDERED = "relation.ProductsForProductListComponent.source.ordered";
    protected static String PRODUCTSFORPRODUCTLISTCOMPONENT_TGT_ORDERED = "relation.ProductsForProductListComponent.target.ordered";
    protected static String PRODUCTSFORPRODUCTLISTCOMPONENT_MARKMODIFIED = "relation.ProductsForProductListComponent.markmodified";
    protected static String PRODUCTSFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED = "relation.ProductsForProductCarouselComponent.source.ordered";
    protected static String PRODUCTSFORPRODUCTCAROUSELCOMPONENT_TGT_ORDERED = "relation.ProductsForProductCarouselComponent.target.ordered";
    protected static String PRODUCTSFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED = "relation.ProductsForProductCarouselComponent.markmodified";
    protected static final OneToManyHandler<ProductDetailComponent> PRODUCTDETAILCOMPONENTSFORPRODUCTPRODUCTDETAILCOMPONENTSHANDLER = new OneToManyHandler(GeneratedCms2LibConstants.TC.PRODUCTDETAILCOMPONENT, false, "product", null, false, true, 2);
    protected static String CATEGORIESFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED = "relation.CategoriesForProductCarouselComponent.source.ordered";
    protected static String CATEGORIESFORPRODUCTCAROUSELCOMPONENT_TGT_ORDERED = "relation.CategoriesForProductCarouselComponent.target.ordered";
    protected static String CATEGORIESFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED = "relation.CategoriesForProductCarouselComponent.markmodified";
    protected static final OneToManyHandler<ProductListComponent> PRODUCTLISTCOMPONENTSFORCATEGORYPRODUCTLISTCOMPONENTSHANDLER = new OneToManyHandler(GeneratedCms2LibConstants.TC.PRODUCTLISTCOMPONENT, false, "category", null, false, true, 2);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public List<BannerComponent> getBannerComponets(SessionContext ctx, ContentPage item)
    {
        return (List<BannerComponent>)BANNERSFORCONTENTPAGEBANNERCOMPONETSHANDLER.getValues(ctx, (Item)item);
    }


    public List<BannerComponent> getBannerComponets(ContentPage item)
    {
        return getBannerComponets(getSession().getSessionContext(), item);
    }


    public void setBannerComponets(SessionContext ctx, ContentPage item, List<BannerComponent> value)
    {
        BANNERSFORCONTENTPAGEBANNERCOMPONETSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setBannerComponets(ContentPage item, List<BannerComponent> value)
    {
        setBannerComponets(getSession().getSessionContext(), item, value);
    }


    public void addToBannerComponets(SessionContext ctx, ContentPage item, BannerComponent value)
    {
        BANNERSFORCONTENTPAGEBANNERCOMPONETSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToBannerComponets(ContentPage item, BannerComponent value)
    {
        addToBannerComponets(getSession().getSessionContext(), item, value);
    }


    public void removeFromBannerComponets(SessionContext ctx, ContentPage item, BannerComponent value)
    {
        BANNERSFORCONTENTPAGEBANNERCOMPONETSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromBannerComponets(ContentPage item, BannerComponent value)
    {
        removeFromBannerComponets(getSession().getSessionContext(), item, value);
    }


    public BannerComponent createBannerComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2LibConstants.TC.BANNERCOMPONENT);
            return (BannerComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating BannerComponent : " + e.getMessage(), 0);
        }
    }


    public BannerComponent createBannerComponent(Map attributeValues)
    {
        return createBannerComponent(getSession().getSessionContext(), attributeValues);
    }


    public FlashComponent createFlashComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2LibConstants.TC.FLASHCOMPONENT);
            return (FlashComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating FlashComponent : " + e.getMessage(), 0);
        }
    }


    public FlashComponent createFlashComponent(Map attributeValues)
    {
        return createFlashComponent(getSession().getSessionContext(), attributeValues);
    }


    public ProductCarouselComponent createProductCarouselComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2LibConstants.TC.PRODUCTCAROUSELCOMPONENT);
            return (ProductCarouselComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductCarouselComponent : " + e.getMessage(), 0);
        }
    }


    public ProductCarouselComponent createProductCarouselComponent(Map attributeValues)
    {
        return createProductCarouselComponent(getSession().getSessionContext(), attributeValues);
    }


    public ProductDetailComponent createProductDetailComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2LibConstants.TC.PRODUCTDETAILCOMPONENT);
            return (ProductDetailComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductDetailComponent : " + e.getMessage(), 0);
        }
    }


    public ProductDetailComponent createProductDetailComponent(Map attributeValues)
    {
        return createProductDetailComponent(getSession().getSessionContext(), attributeValues);
    }


    public ProductListComponent createProductListComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2LibConstants.TC.PRODUCTLISTCOMPONENT);
            return (ProductListComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductListComponent : " + e.getMessage(), 0);
        }
    }


    public ProductListComponent createProductListComponent(Map attributeValues)
    {
        return createProductListComponent(getSession().getSessionContext(), attributeValues);
    }


    public RotatingImagesComponent createRotatingImagesComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2LibConstants.TC.ROTATINGIMAGESCOMPONENT);
            return (RotatingImagesComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RotatingImagesComponent : " + e.getMessage(), 0);
        }
    }


    public RotatingImagesComponent createRotatingImagesComponent(Map attributeValues)
    {
        return createRotatingImagesComponent(getSession().getSessionContext(), attributeValues);
    }


    public List<FlashComponent> getFlashComponents(SessionContext ctx, ContentPage item)
    {
        return (List<FlashComponent>)FLASHCOMPONENTSFORCONTENTPAGEFLASHCOMPONENTSHANDLER.getValues(ctx, (Item)item);
    }


    public List<FlashComponent> getFlashComponents(ContentPage item)
    {
        return getFlashComponents(getSession().getSessionContext(), item);
    }


    public void setFlashComponents(SessionContext ctx, ContentPage item, List<FlashComponent> value)
    {
        FLASHCOMPONENTSFORCONTENTPAGEFLASHCOMPONENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setFlashComponents(ContentPage item, List<FlashComponent> value)
    {
        setFlashComponents(getSession().getSessionContext(), item, value);
    }


    public void addToFlashComponents(SessionContext ctx, ContentPage item, FlashComponent value)
    {
        FLASHCOMPONENTSFORCONTENTPAGEFLASHCOMPONENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToFlashComponents(ContentPage item, FlashComponent value)
    {
        addToFlashComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromFlashComponents(SessionContext ctx, ContentPage item, FlashComponent value)
    {
        FLASHCOMPONENTSFORCONTENTPAGEFLASHCOMPONENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromFlashComponents(ContentPage item, FlashComponent value)
    {
        removeFromFlashComponents(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "cms2lib";
    }


    public Collection<ProductCarouselComponent> getProductCarouselComponents(SessionContext ctx, Product item)
    {
        List<ProductCarouselComponent> items = item.getLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT, "ProductCarouselComponent", null,
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<ProductCarouselComponent> getProductCarouselComponents(Product item)
    {
        return getProductCarouselComponents(getSession().getSessionContext(), item);
    }


    public long getProductCarouselComponentsCount(SessionContext ctx, Product item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT, "ProductCarouselComponent", null);
    }


    public long getProductCarouselComponentsCount(Product item)
    {
        return getProductCarouselComponentsCount(getSession().getSessionContext(), item);
    }


    public void setProductCarouselComponents(SessionContext ctx, Product item, Collection<ProductCarouselComponent> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT, null, value,
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void setProductCarouselComponents(Product item, Collection<ProductCarouselComponent> value)
    {
        setProductCarouselComponents(getSession().getSessionContext(), item, value);
    }


    public void addToProductCarouselComponents(SessionContext ctx, Product item, ProductCarouselComponent value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void addToProductCarouselComponents(Product item, ProductCarouselComponent value)
    {
        addToProductCarouselComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromProductCarouselComponents(SessionContext ctx, Product item, ProductCarouselComponent value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void removeFromProductCarouselComponents(Product item, ProductCarouselComponent value)
    {
        removeFromProductCarouselComponents(getSession().getSessionContext(), item, value);
    }


    public Collection<ProductCarouselComponent> getProductCarouselComponents(SessionContext ctx, Category item)
    {
        List<ProductCarouselComponent> items = item.getLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT, "ProductCarouselComponent", null,
                        Utilities.getRelationOrderingOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<ProductCarouselComponent> getProductCarouselComponents(Category item)
    {
        return getProductCarouselComponents(getSession().getSessionContext(), item);
    }


    public long getProductCarouselComponentsCount(SessionContext ctx, Category item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT, "ProductCarouselComponent", null);
    }


    public long getProductCarouselComponentsCount(Category item)
    {
        return getProductCarouselComponentsCount(getSession().getSessionContext(), item);
    }


    public void setProductCarouselComponents(SessionContext ctx, Category item, Collection<ProductCarouselComponent> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT, null, value,
                        Utilities.getRelationOrderingOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void setProductCarouselComponents(Category item, Collection<ProductCarouselComponent> value)
    {
        setProductCarouselComponents(getSession().getSessionContext(), item, value);
    }


    public void addToProductCarouselComponents(SessionContext ctx, Category item, ProductCarouselComponent value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void addToProductCarouselComponents(Category item, ProductCarouselComponent value)
    {
        addToProductCarouselComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromProductCarouselComponents(SessionContext ctx, Category item, ProductCarouselComponent value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void removeFromProductCarouselComponents(Category item, ProductCarouselComponent value)
    {
        removeFromProductCarouselComponents(getSession().getSessionContext(), item, value);
    }


    public List<ProductDetailComponent> getProductDetailComponents(SessionContext ctx, Product item)
    {
        return (List<ProductDetailComponent>)PRODUCTDETAILCOMPONENTSFORPRODUCTPRODUCTDETAILCOMPONENTSHANDLER.getValues(ctx, (Item)item);
    }


    public List<ProductDetailComponent> getProductDetailComponents(Product item)
    {
        return getProductDetailComponents(getSession().getSessionContext(), item);
    }


    public void setProductDetailComponents(SessionContext ctx, Product item, List<ProductDetailComponent> value)
    {
        PRODUCTDETAILCOMPONENTSFORPRODUCTPRODUCTDETAILCOMPONENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setProductDetailComponents(Product item, List<ProductDetailComponent> value)
    {
        setProductDetailComponents(getSession().getSessionContext(), item, value);
    }


    public void addToProductDetailComponents(SessionContext ctx, Product item, ProductDetailComponent value)
    {
        PRODUCTDETAILCOMPONENTSFORPRODUCTPRODUCTDETAILCOMPONENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToProductDetailComponents(Product item, ProductDetailComponent value)
    {
        addToProductDetailComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromProductDetailComponents(SessionContext ctx, Product item, ProductDetailComponent value)
    {
        PRODUCTDETAILCOMPONENTSFORPRODUCTPRODUCTDETAILCOMPONENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromProductDetailComponents(Product item, ProductDetailComponent value)
    {
        removeFromProductDetailComponents(getSession().getSessionContext(), item, value);
    }


    public Collection<ProductListComponent> getProductListComponents(SessionContext ctx, Product item)
    {
        List<ProductListComponent> items = item.getLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT, "ProductListComponent", null,
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<ProductListComponent> getProductListComponents(Product item)
    {
        return getProductListComponents(getSession().getSessionContext(), item);
    }


    public long getProductListComponentsCount(SessionContext ctx, Product item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT, "ProductListComponent", null);
    }


    public long getProductListComponentsCount(Product item)
    {
        return getProductListComponentsCount(getSession().getSessionContext(), item);
    }


    public void setProductListComponents(SessionContext ctx, Product item, Collection<ProductListComponent> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT, null, value,
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_MARKMODIFIED));
    }


    public void setProductListComponents(Product item, Collection<ProductListComponent> value)
    {
        setProductListComponents(getSession().getSessionContext(), item, value);
    }


    public void addToProductListComponents(SessionContext ctx, Product item, ProductListComponent value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_MARKMODIFIED));
    }


    public void addToProductListComponents(Product item, ProductListComponent value)
    {
        addToProductListComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromProductListComponents(SessionContext ctx, Product item, ProductListComponent value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_MARKMODIFIED));
    }


    public void removeFromProductListComponents(Product item, ProductListComponent value)
    {
        removeFromProductListComponents(getSession().getSessionContext(), item, value);
    }


    public List<ProductListComponent> getProductListComponents(SessionContext ctx, Category item)
    {
        return (List<ProductListComponent>)PRODUCTLISTCOMPONENTSFORCATEGORYPRODUCTLISTCOMPONENTSHANDLER.getValues(ctx, (Item)item);
    }


    public List<ProductListComponent> getProductListComponents(Category item)
    {
        return getProductListComponents(getSession().getSessionContext(), item);
    }


    public void setProductListComponents(SessionContext ctx, Category item, List<ProductListComponent> value)
    {
        PRODUCTLISTCOMPONENTSFORCATEGORYPRODUCTLISTCOMPONENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setProductListComponents(Category item, List<ProductListComponent> value)
    {
        setProductListComponents(getSession().getSessionContext(), item, value);
    }


    public void addToProductListComponents(SessionContext ctx, Category item, ProductListComponent value)
    {
        PRODUCTLISTCOMPONENTSFORCATEGORYPRODUCTLISTCOMPONENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToProductListComponents(Category item, ProductListComponent value)
    {
        addToProductListComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromProductListComponents(SessionContext ctx, Category item, ProductListComponent value)
    {
        PRODUCTLISTCOMPONENTSFORCATEGORYPRODUCTLISTCOMPONENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromProductListComponents(Category item, ProductListComponent value)
    {
        removeFromProductListComponents(getSession().getSessionContext(), item, value);
    }
}
