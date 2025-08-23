package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AccountBookmarkComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "AccountBookmarkComponent";
    public static final String STYLECLASS = "styleClass";
    public static final String LINK = "link";


    public AccountBookmarkComponentModel()
    {
    }


    public AccountBookmarkComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AccountBookmarkComponentModel(CatalogVersionModel _catalogVersion, CMSLinkComponentModel _link, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setLink(_link);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AccountBookmarkComponentModel(CatalogVersionModel _catalogVersion, CMSLinkComponentModel _link, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setLink(_link);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "link", type = Accessor.Type.GETTER)
    public CMSLinkComponentModel getLink()
    {
        return (CMSLinkComponentModel)getPersistenceContext().getPropertyValue("link");
    }


    @Accessor(qualifier = "styleClass", type = Accessor.Type.GETTER)
    public String getStyleClass()
    {
        return (String)getPersistenceContext().getPropertyValue("styleClass");
    }


    @Accessor(qualifier = "link", type = Accessor.Type.SETTER)
    public void setLink(CMSLinkComponentModel value)
    {
        getPersistenceContext().setPropertyValue("link", value);
    }


    @Accessor(qualifier = "styleClass", type = Accessor.Type.SETTER)
    public void setStyleClass(String value)
    {
        getPersistenceContext().setPropertyValue("styleClass", value);
    }
}
