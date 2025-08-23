package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class AccountNavigationComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "AccountNavigationComponent";
    public static final String TITLE = "title";
    public static final String STYLECLASS = "styleClass";
    public static final String NAVIGATIONNODE = "navigationNode";


    public AccountNavigationComponentModel()
    {
    }


    public AccountNavigationComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AccountNavigationComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AccountNavigationComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
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
}
