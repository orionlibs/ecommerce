package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.pages.ContentPage;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedVideoComponent extends SimpleCMSComponent
{
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
    protected static final BidirectionalOneToManyHandler<GeneratedVideoComponent> CONTENTPAGEHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.VIDEOCOMPONENT, false, "contentPage", "contentPagePOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedVideoComponent> PRODUCTHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.VIDEOCOMPONENT, false, "product", "productPOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedVideoComponent> CATEGORYHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.VIDEOCOMPONENT, false, "category", "categoryPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("video", Item.AttributeMode.INITIAL);
        tmp.put("autoPlay", Item.AttributeMode.INITIAL);
        tmp.put("loop", Item.AttributeMode.INITIAL);
        tmp.put("mute", Item.AttributeMode.INITIAL);
        tmp.put("thumbnailSelector", Item.AttributeMode.INITIAL);
        tmp.put("thumbnail", Item.AttributeMode.INITIAL);
        tmp.put("url", Item.AttributeMode.INITIAL);
        tmp.put("contentPagePOS", Item.AttributeMode.INITIAL);
        tmp.put("contentPage", Item.AttributeMode.INITIAL);
        tmp.put("productPOS", Item.AttributeMode.INITIAL);
        tmp.put("product", Item.AttributeMode.INITIAL);
        tmp.put("categoryPOS", Item.AttributeMode.INITIAL);
        tmp.put("category", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAutoPlay(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "autoPlay");
    }


    public Boolean isAutoPlay()
    {
        return isAutoPlay(getSession().getSessionContext());
    }


    public boolean isAutoPlayAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAutoPlay(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAutoPlayAsPrimitive()
    {
        return isAutoPlayAsPrimitive(getSession().getSessionContext());
    }


    public void setAutoPlay(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "autoPlay", value);
    }


    public void setAutoPlay(Boolean value)
    {
        setAutoPlay(getSession().getSessionContext(), value);
    }


    public void setAutoPlay(SessionContext ctx, boolean value)
    {
        setAutoPlay(ctx, Boolean.valueOf(value));
    }


    public void setAutoPlay(boolean value)
    {
        setAutoPlay(getSession().getSessionContext(), value);
    }


    public Category getCategory(SessionContext ctx)
    {
        return (Category)getProperty(ctx, "category");
    }


    public Category getCategory()
    {
        return getCategory(getSession().getSessionContext());
    }


    public void setCategory(SessionContext ctx, Category value)
    {
        CATEGORYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCategory(Category value)
    {
        setCategory(getSession().getSessionContext(), value);
    }


    Integer getCategoryPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "categoryPOS");
    }


    Integer getCategoryPOS()
    {
        return getCategoryPOS(getSession().getSessionContext());
    }


    int getCategoryPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getCategoryPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getCategoryPOSAsPrimitive()
    {
        return getCategoryPOSAsPrimitive(getSession().getSessionContext());
    }


    void setCategoryPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "categoryPOS", value);
    }


    void setCategoryPOS(Integer value)
    {
        setCategoryPOS(getSession().getSessionContext(), value);
    }


    void setCategoryPOS(SessionContext ctx, int value)
    {
        setCategoryPOS(ctx, Integer.valueOf(value));
    }


    void setCategoryPOS(int value)
    {
        setCategoryPOS(getSession().getSessionContext(), value);
    }


    public ContentPage getContentPage(SessionContext ctx)
    {
        return (ContentPage)getProperty(ctx, "contentPage");
    }


    public ContentPage getContentPage()
    {
        return getContentPage(getSession().getSessionContext());
    }


    public void setContentPage(SessionContext ctx, ContentPage value)
    {
        CONTENTPAGEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setContentPage(ContentPage value)
    {
        setContentPage(getSession().getSessionContext(), value);
    }


    Integer getContentPagePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "contentPagePOS");
    }


    Integer getContentPagePOS()
    {
        return getContentPagePOS(getSession().getSessionContext());
    }


    int getContentPagePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getContentPagePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getContentPagePOSAsPrimitive()
    {
        return getContentPagePOSAsPrimitive(getSession().getSessionContext());
    }


    void setContentPagePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "contentPagePOS", value);
    }


    void setContentPagePOS(Integer value)
    {
        setContentPagePOS(getSession().getSessionContext(), value);
    }


    void setContentPagePOS(SessionContext ctx, int value)
    {
        setContentPagePOS(ctx, Integer.valueOf(value));
    }


    void setContentPagePOS(int value)
    {
        setContentPagePOS(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CONTENTPAGEHANDLER.newInstance(ctx, allAttributes);
        PRODUCTHANDLER.newInstance(ctx, allAttributes);
        CATEGORYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isLoop(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "loop");
    }


    public Boolean isLoop()
    {
        return isLoop(getSession().getSessionContext());
    }


    public boolean isLoopAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLoop(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLoopAsPrimitive()
    {
        return isLoopAsPrimitive(getSession().getSessionContext());
    }


    public void setLoop(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "loop", value);
    }


    public void setLoop(Boolean value)
    {
        setLoop(getSession().getSessionContext(), value);
    }


    public void setLoop(SessionContext ctx, boolean value)
    {
        setLoop(ctx, Boolean.valueOf(value));
    }


    public void setLoop(boolean value)
    {
        setLoop(getSession().getSessionContext(), value);
    }


    public Boolean isMute(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "mute");
    }


    public Boolean isMute()
    {
        return isMute(getSession().getSessionContext());
    }


    public boolean isMuteAsPrimitive(SessionContext ctx)
    {
        Boolean value = isMute(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isMuteAsPrimitive()
    {
        return isMuteAsPrimitive(getSession().getSessionContext());
    }


    public void setMute(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "mute", value);
    }


    public void setMute(Boolean value)
    {
        setMute(getSession().getSessionContext(), value);
    }


    public void setMute(SessionContext ctx, boolean value)
    {
        setMute(ctx, Boolean.valueOf(value));
    }


    public void setMute(boolean value)
    {
        setMute(getSession().getSessionContext(), value);
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
        PRODUCTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setProduct(Product value)
    {
        setProduct(getSession().getSessionContext(), value);
    }


    Integer getProductPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "productPOS");
    }


    Integer getProductPOS()
    {
        return getProductPOS(getSession().getSessionContext());
    }


    int getProductPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getProductPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getProductPOSAsPrimitive()
    {
        return getProductPOSAsPrimitive(getSession().getSessionContext());
    }


    void setProductPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "productPOS", value);
    }


    void setProductPOS(Integer value)
    {
        setProductPOS(getSession().getSessionContext(), value);
    }


    void setProductPOS(SessionContext ctx, int value)
    {
        setProductPOS(ctx, Integer.valueOf(value));
    }


    void setProductPOS(int value)
    {
        setProductPOS(getSession().getSessionContext(), value);
    }


    public MediaContainer getThumbnail(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedVideoComponent.getThumbnail requires a session language", 0);
        }
        return (MediaContainer)getLocalizedProperty(ctx, "thumbnail");
    }


    public MediaContainer getThumbnail()
    {
        return getThumbnail(getSession().getSessionContext());
    }


    public Map<Language, MediaContainer> getAllThumbnail(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "thumbnail", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, MediaContainer> getAllThumbnail()
    {
        return getAllThumbnail(getSession().getSessionContext());
    }


    public void setThumbnail(SessionContext ctx, MediaContainer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedVideoComponent.setThumbnail requires a session language", 0);
        }
        setLocalizedProperty(ctx, "thumbnail", value);
    }


    public void setThumbnail(MediaContainer value)
    {
        setThumbnail(getSession().getSessionContext(), value);
    }


    public void setAllThumbnail(SessionContext ctx, Map<Language, MediaContainer> value)
    {
        setAllLocalizedProperties(ctx, "thumbnail", value);
    }


    public void setAllThumbnail(Map<Language, MediaContainer> value)
    {
        setAllThumbnail(getSession().getSessionContext(), value);
    }


    public EnumerationValue getThumbnailSelector(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "thumbnailSelector");
    }


    public EnumerationValue getThumbnailSelector()
    {
        return getThumbnailSelector(getSession().getSessionContext());
    }


    public void setThumbnailSelector(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "thumbnailSelector", value);
    }


    public void setThumbnailSelector(EnumerationValue value)
    {
        setThumbnailSelector(getSession().getSessionContext(), value);
    }


    public String getUrl(SessionContext ctx)
    {
        return (String)getProperty(ctx, "url");
    }


    public String getUrl()
    {
        return getUrl(getSession().getSessionContext());
    }


    public void setUrl(SessionContext ctx, String value)
    {
        setProperty(ctx, "url", value);
    }


    public void setUrl(String value)
    {
        setUrl(getSession().getSessionContext(), value);
    }


    public Media getVideo(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedVideoComponent.getVideo requires a session language", 0);
        }
        return (Media)getLocalizedProperty(ctx, "video");
    }


    public Media getVideo()
    {
        return getVideo(getSession().getSessionContext());
    }


    public Map<Language, Media> getAllVideo(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "video", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, Media> getAllVideo()
    {
        return getAllVideo(getSession().getSessionContext());
    }


    public void setVideo(SessionContext ctx, Media value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedVideoComponent.setVideo requires a session language", 0);
        }
        setLocalizedProperty(ctx, "video", value);
    }


    public void setVideo(Media value)
    {
        setVideo(getSession().getSessionContext(), value);
    }


    public void setAllVideo(SessionContext ctx, Map<Language, Media> value)
    {
        setAllLocalizedProperties(ctx, "video", value);
    }


    public void setAllVideo(Map<Language, Media> value)
    {
        setAllVideo(getSession().getSessionContext(), value);
    }
}
