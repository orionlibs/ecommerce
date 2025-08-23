package de.hybris.platform.cms2.model.navigation;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class CMSNavigationNodeModel extends CMSItemModel
{
    public static final String _TYPECODE = "CMSNavigationNode";
    public static final String _CMSNAVIGATIONNODECHILDREN = "CMSNavigationNodeChildren";
    public static final String _CMSLINKSFORNAVNODES = "CMSLinksForNavNodes";
    public static final String _CMSCONTENTPAGESFORNAVNODES = "CMSContentPagesForNavNodes";
    public static final String TITLE = "title";
    public static final String VISIBLE = "visible";
    public static final String PARENTPOS = "parentPOS";
    public static final String PARENT = "parent";
    public static final String CHILDREN = "children";
    public static final String LINKS = "links";
    public static final String PAGES = "pages";
    public static final String ENTRIES = "entries";


    public CMSNavigationNodeModel()
    {
    }


    public CMSNavigationNodeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSNavigationNodeModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSNavigationNodeModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "children", type = Accessor.Type.GETTER)
    public List<CMSNavigationNodeModel> getChildren()
    {
        return (List<CMSNavigationNodeModel>)getPersistenceContext().getPropertyValue("children");
    }


    @Accessor(qualifier = "entries", type = Accessor.Type.GETTER)
    public List<CMSNavigationEntryModel> getEntries()
    {
        return (List<CMSNavigationEntryModel>)getPersistenceContext().getPropertyValue("entries");
    }


    @Deprecated(since = "4.4", forRemoval = true)
    @Accessor(qualifier = "links", type = Accessor.Type.GETTER)
    public List<CMSLinkComponentModel> getLinks()
    {
        return (List<CMSLinkComponentModel>)getPersistenceContext().getPropertyValue("links");
    }


    @Deprecated(since = "4.4", forRemoval = true)
    @Accessor(qualifier = "pages", type = Accessor.Type.GETTER)
    public List<ContentPageModel> getPages()
    {
        return (List<ContentPageModel>)getPersistenceContext().getPropertyValue("pages");
    }


    @Accessor(qualifier = "parent", type = Accessor.Type.GETTER)
    public CMSNavigationNodeModel getParent()
    {
        return (CMSNavigationNodeModel)getPersistenceContext().getPropertyValue("parent");
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle()
    {
        return getTitle(null);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("title", loc);
    }


    @Accessor(qualifier = "visible", type = Accessor.Type.GETTER)
    public boolean isVisible()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("visible"));
    }


    @Accessor(qualifier = "children", type = Accessor.Type.SETTER)
    public void setChildren(List<CMSNavigationNodeModel> value)
    {
        getPersistenceContext().setPropertyValue("children", value);
    }


    @Accessor(qualifier = "entries", type = Accessor.Type.SETTER)
    public void setEntries(List<CMSNavigationEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("entries", value);
    }


    @Deprecated(since = "4.4", forRemoval = true)
    @Accessor(qualifier = "links", type = Accessor.Type.SETTER)
    public void setLinks(List<CMSLinkComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("links", value);
    }


    @Deprecated(since = "4.4", forRemoval = true)
    @Accessor(qualifier = "pages", type = Accessor.Type.SETTER)
    public void setPages(List<ContentPageModel> value)
    {
        getPersistenceContext().setPropertyValue("pages", value);
    }


    @Accessor(qualifier = "parent", type = Accessor.Type.SETTER)
    public void setParent(CMSNavigationNodeModel value)
    {
        getPersistenceContext().setPropertyValue("parent", value);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value)
    {
        setTitle(value, null);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("title", loc, value);
    }


    @Accessor(qualifier = "visible", type = Accessor.Type.SETTER)
    public void setVisible(boolean value)
    {
        getPersistenceContext().setPropertyValue("visible", toObject(value));
    }
}
