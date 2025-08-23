package de.hybris.platform.cms2.model.relations;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ContentSlotForPageModel extends CMSRelationModel
{
    public static final String _TYPECODE = "ContentSlotForPage";
    public static final String POSITION = "position";
    public static final String PAGE = "page";
    public static final String CONTENTSLOT = "contentSlot";


    public ContentSlotForPageModel()
    {
    }


    public ContentSlotForPageModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentSlotForPageModel(CatalogVersionModel _catalogVersion, ContentSlotModel _contentSlot, AbstractPageModel _page, String _position, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setContentSlot(_contentSlot);
        setPage(_page);
        setPosition(_position);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentSlotForPageModel(CatalogVersionModel _catalogVersion, ContentSlotModel _contentSlot, ItemModel _owner, AbstractPageModel _page, String _position, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setContentSlot(_contentSlot);
        setOwner(_owner);
        setPage(_page);
        setPosition(_position);
        setUid(_uid);
    }


    @Accessor(qualifier = "contentSlot", type = Accessor.Type.GETTER)
    public ContentSlotModel getContentSlot()
    {
        return (ContentSlotModel)getPersistenceContext().getPropertyValue("contentSlot");
    }


    @Accessor(qualifier = "page", type = Accessor.Type.GETTER)
    public AbstractPageModel getPage()
    {
        return (AbstractPageModel)getPersistenceContext().getPropertyValue("page");
    }


    @Accessor(qualifier = "position", type = Accessor.Type.GETTER)
    public String getPosition()
    {
        return (String)getPersistenceContext().getPropertyValue("position");
    }


    @Accessor(qualifier = "contentSlot", type = Accessor.Type.SETTER)
    public void setContentSlot(ContentSlotModel value)
    {
        getPersistenceContext().setPropertyValue("contentSlot", value);
    }


    @Accessor(qualifier = "page", type = Accessor.Type.SETTER)
    public void setPage(AbstractPageModel value)
    {
        getPersistenceContext().setPropertyValue("page", value);
    }


    @Accessor(qualifier = "position", type = Accessor.Type.SETTER)
    public void setPosition(String value)
    {
        getPersistenceContext().setPropertyValue("position", value);
    }
}
