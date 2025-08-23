package de.hybris.platform.cms2lib.components;

import de.hybris.platform.cms2.jalo.pages.ContentPage;
import de.hybris.platform.cms2lib.constants.GeneratedCms2LibConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedBannerComponent extends AbstractBannerComponent
{
    public static final String HEADLINE = "headline";
    public static final String CONTENT = "content";
    public static final String PAGELABELORID = "pageLabelOrId";
    public static final String ROTATINGCOMPONENT = "rotatingComponent";
    protected static String BANNERSFORROTATINGCOMPONENT_SRC_ORDERED = "relation.BannersForRotatingComponent.source.ordered";
    protected static String BANNERSFORROTATINGCOMPONENT_TGT_ORDERED = "relation.BannersForRotatingComponent.target.ordered";
    protected static String BANNERSFORROTATINGCOMPONENT_MARKMODIFIED = "relation.BannersForRotatingComponent.markmodified";
    public static final String PAGE = "page";
    protected static final BidirectionalOneToManyHandler<GeneratedBannerComponent> PAGEHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2LibConstants.TC.BANNERCOMPONENT, false, "page", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractBannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("headline", Item.AttributeMode.INITIAL);
        tmp.put("content", Item.AttributeMode.INITIAL);
        tmp.put("page", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getContent(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedBannerComponent.getContent requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "content");
    }


    public String getContent()
    {
        return getContent(getSession().getSessionContext());
    }


    public Map<Language, String> getAllContent(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "content", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllContent()
    {
        return getAllContent(getSession().getSessionContext());
    }


    public void setContent(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedBannerComponent.setContent requires a session language", 0);
        }
        setLocalizedProperty(ctx, "content", value);
    }


    public void setContent(String value)
    {
        setContent(getSession().getSessionContext(), value);
    }


    public void setAllContent(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "content", value);
    }


    public void setAllContent(Map<Language, String> value)
    {
        setAllContent(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PAGEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getHeadline(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedBannerComponent.getHeadline requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "headline");
    }


    public String getHeadline()
    {
        return getHeadline(getSession().getSessionContext());
    }


    public Map<Language, String> getAllHeadline(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "headline", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllHeadline()
    {
        return getAllHeadline(getSession().getSessionContext());
    }


    public void setHeadline(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedBannerComponent.setHeadline requires a session language", 0);
        }
        setLocalizedProperty(ctx, "headline", value);
    }


    public void setHeadline(String value)
    {
        setHeadline(getSession().getSessionContext(), value);
    }


    public void setAllHeadline(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "headline", value);
    }


    public void setAllHeadline(Map<Language, String> value)
    {
        setAllHeadline(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("RotatingImagesComponent");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(BANNERSFORROTATINGCOMPONENT_MARKMODIFIED);
        }
        return true;
    }


    public ContentPage getPage(SessionContext ctx)
    {
        return (ContentPage)getProperty(ctx, "page");
    }


    public ContentPage getPage()
    {
        return getPage(getSession().getSessionContext());
    }


    public void setPage(SessionContext ctx, ContentPage value)
    {
        PAGEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setPage(ContentPage value)
    {
        setPage(getSession().getSessionContext(), value);
    }


    public String getPageLabelOrId()
    {
        return getPageLabelOrId(getSession().getSessionContext());
    }


    public Collection<RotatingImagesComponent> getRotatingComponent(SessionContext ctx)
    {
        List<RotatingImagesComponent> items = getLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.BANNERSFORROTATINGCOMPONENT, "RotatingImagesComponent", null,
                        Utilities.getRelationOrderingOverride(BANNERSFORROTATINGCOMPONENT_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<RotatingImagesComponent> getRotatingComponent()
    {
        return getRotatingComponent(getSession().getSessionContext());
    }


    public long getRotatingComponentCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2LibConstants.Relations.BANNERSFORROTATINGCOMPONENT, "RotatingImagesComponent", null);
    }


    public long getRotatingComponentCount()
    {
        return getRotatingComponentCount(getSession().getSessionContext());
    }


    public void setRotatingComponent(SessionContext ctx, Collection<RotatingImagesComponent> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.BANNERSFORROTATINGCOMPONENT, null, value,
                        Utilities.getRelationOrderingOverride(BANNERSFORROTATINGCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(BANNERSFORROTATINGCOMPONENT_MARKMODIFIED));
    }


    public void setRotatingComponent(Collection<RotatingImagesComponent> value)
    {
        setRotatingComponent(getSession().getSessionContext(), value);
    }


    public void addToRotatingComponent(SessionContext ctx, RotatingImagesComponent value)
    {
        addLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.BANNERSFORROTATINGCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(BANNERSFORROTATINGCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(BANNERSFORROTATINGCOMPONENT_MARKMODIFIED));
    }


    public void addToRotatingComponent(RotatingImagesComponent value)
    {
        addToRotatingComponent(getSession().getSessionContext(), value);
    }


    public void removeFromRotatingComponent(SessionContext ctx, RotatingImagesComponent value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2LibConstants.Relations.BANNERSFORROTATINGCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(BANNERSFORROTATINGCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(BANNERSFORROTATINGCOMPONENT_MARKMODIFIED));
    }


    public void removeFromRotatingComponent(RotatingImagesComponent value)
    {
        removeFromRotatingComponent(getSession().getSessionContext(), value);
    }


    public abstract String getPageLabelOrId(SessionContext paramSessionContext);
}
