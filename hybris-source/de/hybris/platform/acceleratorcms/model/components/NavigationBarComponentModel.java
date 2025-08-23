package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.enums.NavigationBarMenuLayout;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class NavigationBarComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "NavigationBarComponent";
    public static final String DROPDOWNLAYOUT = "dropDownLayout";
    public static final String NAVIGATIONNODE = "navigationNode";
    public static final String WRAPAFTER = "wrapAfter";
    public static final String LINK = "link";
    public static final String STYLECLASS = "styleClass";


    public NavigationBarComponentModel()
    {
    }


    public NavigationBarComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public NavigationBarComponentModel(CatalogVersionModel _catalogVersion, CMSLinkComponentModel _link, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setLink(_link);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public NavigationBarComponentModel(CatalogVersionModel _catalogVersion, CMSLinkComponentModel _link, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setLink(_link);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "dropDownLayout", type = Accessor.Type.GETTER)
    public NavigationBarMenuLayout getDropDownLayout()
    {
        return (NavigationBarMenuLayout)getPersistenceContext().getPropertyValue("dropDownLayout");
    }


    @Accessor(qualifier = "link", type = Accessor.Type.GETTER)
    public CMSLinkComponentModel getLink()
    {
        return (CMSLinkComponentModel)getPersistenceContext().getPropertyValue("link");
    }


    @Accessor(qualifier = "navigationNode", type = Accessor.Type.GETTER)
    public CMSNavigationNodeModel getNavigationNode()
    {
        return (CMSNavigationNodeModel)getPersistenceContext().getPropertyValue("navigationNode");
    }


    @Accessor(qualifier = "styleClass", type = Accessor.Type.GETTER)
    public String getStyleClass()
    {
        return (String)getPersistenceContext().getPropertyValue("styleClass");
    }


    @Accessor(qualifier = "wrapAfter", type = Accessor.Type.GETTER)
    public Integer getWrapAfter()
    {
        return (Integer)getPersistenceContext().getPropertyValue("wrapAfter");
    }


    @Accessor(qualifier = "dropDownLayout", type = Accessor.Type.SETTER)
    public void setDropDownLayout(NavigationBarMenuLayout value)
    {
        getPersistenceContext().setPropertyValue("dropDownLayout", value);
    }


    @Accessor(qualifier = "link", type = Accessor.Type.SETTER)
    public void setLink(CMSLinkComponentModel value)
    {
        getPersistenceContext().setPropertyValue("link", value);
    }


    @Accessor(qualifier = "navigationNode", type = Accessor.Type.SETTER)
    public void setNavigationNode(CMSNavigationNodeModel value)
    {
        getPersistenceContext().setPropertyValue("navigationNode", value);
    }


    @Accessor(qualifier = "styleClass", type = Accessor.Type.SETTER)
    public void setStyleClass(String value)
    {
        getPersistenceContext().setPropertyValue("styleClass", value);
    }


    @Accessor(qualifier = "wrapAfter", type = Accessor.Type.SETTER)
    public void setWrapAfter(Integer value)
    {
        getPersistenceContext().setPropertyValue("wrapAfter", value);
    }
}
