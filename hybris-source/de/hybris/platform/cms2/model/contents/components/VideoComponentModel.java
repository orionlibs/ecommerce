package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.enums.ThumbnailSelectorOptions;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class VideoComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "VideoComponent";
    public static final String _VIDEOCOMPONENTSFORCONTENTPAGE = "VideoComponentsForContentPage";
    public static final String _VIDEOCOMPONENTSFORPRODUCT = "VideoComponentsForProduct";
    public static final String _VIDEOCOMPONENTSFORCATEGORY = "VideoComponentsForCategory";
    public static final String VIDEO = "video";
    public static final String AUTOPLAY = "autoPlay";
    public static final String LOOP = "loop";
    public static final String MUTE = "mute";
    public static final String THUMBNAILSELECTOR = "thumbnailSelector";
    public static final String THUMBNAIL = "thumbnail";
    public static final String URL = "url";
    public static final String CONTENTPAGEPOS = "contentPagePOS";
    public static final String CONTENTPAGE = "contentPage";
    public static final String PRODUCTPOS = "productPOS";
    public static final String PRODUCT = "product";
    public static final String CATEGORYPOS = "categoryPOS";
    public static final String CATEGORY = "category";


    public VideoComponentModel()
    {
    }


    public VideoComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VideoComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VideoComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "autoPlay", type = Accessor.Type.GETTER)
    public Boolean getAutoPlay()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("autoPlay");
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


    @Accessor(qualifier = "loop", type = Accessor.Type.GETTER)
    public Boolean getLoop()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("loop");
    }


    @Accessor(qualifier = "mute", type = Accessor.Type.GETTER)
    public Boolean getMute()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("mute");
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public ProductModel getProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("product");
    }


    @Accessor(qualifier = "thumbnail", type = Accessor.Type.GETTER)
    public MediaContainerModel getThumbnail()
    {
        return getThumbnail(null);
    }


    @Accessor(qualifier = "thumbnail", type = Accessor.Type.GETTER)
    public MediaContainerModel getThumbnail(Locale loc)
    {
        return (MediaContainerModel)getPersistenceContext().getLocalizedValue("thumbnail", loc);
    }


    @Accessor(qualifier = "thumbnailSelector", type = Accessor.Type.GETTER)
    public ThumbnailSelectorOptions getThumbnailSelector()
    {
        return (ThumbnailSelectorOptions)getPersistenceContext().getPropertyValue("thumbnailSelector");
    }


    @Accessor(qualifier = "url", type = Accessor.Type.GETTER)
    public String getUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("url");
    }


    @Accessor(qualifier = "video", type = Accessor.Type.GETTER)
    public MediaModel getVideo()
    {
        return getVideo(null);
    }


    @Accessor(qualifier = "video", type = Accessor.Type.GETTER)
    public MediaModel getVideo(Locale loc)
    {
        return (MediaModel)getPersistenceContext().getLocalizedValue("video", loc);
    }


    @Accessor(qualifier = "autoPlay", type = Accessor.Type.SETTER)
    public void setAutoPlay(Boolean value)
    {
        getPersistenceContext().setPropertyValue("autoPlay", value);
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


    @Accessor(qualifier = "loop", type = Accessor.Type.SETTER)
    public void setLoop(Boolean value)
    {
        getPersistenceContext().setPropertyValue("loop", value);
    }


    @Accessor(qualifier = "mute", type = Accessor.Type.SETTER)
    public void setMute(Boolean value)
    {
        getPersistenceContext().setPropertyValue("mute", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }


    @Accessor(qualifier = "thumbnail", type = Accessor.Type.SETTER)
    public void setThumbnail(MediaContainerModel value)
    {
        setThumbnail(value, null);
    }


    @Accessor(qualifier = "thumbnail", type = Accessor.Type.SETTER)
    public void setThumbnail(MediaContainerModel value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("thumbnail", loc, value);
    }


    @Accessor(qualifier = "thumbnailSelector", type = Accessor.Type.SETTER)
    public void setThumbnailSelector(ThumbnailSelectorOptions value)
    {
        getPersistenceContext().setPropertyValue("thumbnailSelector", value);
    }


    @Accessor(qualifier = "url", type = Accessor.Type.SETTER)
    public void setUrl(String value)
    {
        getPersistenceContext().setPropertyValue("url", value);
    }


    @Accessor(qualifier = "video", type = Accessor.Type.SETTER)
    public void setVideo(MediaModel value)
    {
        setVideo(value, null);
    }


    @Accessor(qualifier = "video", type = Accessor.Type.SETTER)
    public void setVideo(MediaModel value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("video", loc, value);
    }
}
