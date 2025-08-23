package de.hybris.platform.cms2.model.relations;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ContentSlotForTemplateModel extends CMSRelationModel
{
    public static final String _TYPECODE = "ContentSlotForTemplate";
    public static final String ALLOWOVERWRITE = "allowOverwrite";
    public static final String POSITION = "position";
    public static final String PAGETEMPLATE = "pageTemplate";
    public static final String CONTENTSLOT = "contentSlot";


    public ContentSlotForTemplateModel()
    {
    }


    public ContentSlotForTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentSlotForTemplateModel(CatalogVersionModel _catalogVersion, ContentSlotModel _contentSlot, PageTemplateModel _pageTemplate, String _position, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setContentSlot(_contentSlot);
        setPageTemplate(_pageTemplate);
        setPosition(_position);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentSlotForTemplateModel(CatalogVersionModel _catalogVersion, ContentSlotModel _contentSlot, ItemModel _owner, PageTemplateModel _pageTemplate, String _position, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setContentSlot(_contentSlot);
        setOwner(_owner);
        setPageTemplate(_pageTemplate);
        setPosition(_position);
        setUid(_uid);
    }


    @Accessor(qualifier = "allowOverwrite", type = Accessor.Type.GETTER)
    public Boolean getAllowOverwrite()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("allowOverwrite");
    }


    @Accessor(qualifier = "contentSlot", type = Accessor.Type.GETTER)
    public ContentSlotModel getContentSlot()
    {
        return (ContentSlotModel)getPersistenceContext().getPropertyValue("contentSlot");
    }


    @Accessor(qualifier = "pageTemplate", type = Accessor.Type.GETTER)
    public PageTemplateModel getPageTemplate()
    {
        return (PageTemplateModel)getPersistenceContext().getPropertyValue("pageTemplate");
    }


    @Accessor(qualifier = "position", type = Accessor.Type.GETTER)
    public String getPosition()
    {
        return (String)getPersistenceContext().getPropertyValue("position");
    }


    @Accessor(qualifier = "allowOverwrite", type = Accessor.Type.SETTER)
    public void setAllowOverwrite(Boolean value)
    {
        getPersistenceContext().setPropertyValue("allowOverwrite", value);
    }


    @Accessor(qualifier = "contentSlot", type = Accessor.Type.SETTER)
    public void setContentSlot(ContentSlotModel value)
    {
        getPersistenceContext().setPropertyValue("contentSlot", value);
    }


    @Accessor(qualifier = "pageTemplate", type = Accessor.Type.SETTER)
    public void setPageTemplate(PageTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("pageTemplate", value);
    }


    @Accessor(qualifier = "position", type = Accessor.Type.SETTER)
    public void setPosition(String value)
    {
        getPersistenceContext().setPropertyValue("position", value);
    }
}
