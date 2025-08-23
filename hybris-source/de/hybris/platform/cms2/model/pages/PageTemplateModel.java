package de.hybris.platform.cms2.model.pages;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Set;

public class PageTemplateModel extends CMSItemModel
{
    public static final String _TYPECODE = "PageTemplate";
    public static final String ACTIVE = "active";
    public static final String VELOCITYTEMPLATE = "velocityTemplate";
    public static final String FRONTENDTEMPLATENAME = "frontendTemplateName";
    public static final String CONTENTSLOTS = "contentSlots";
    public static final String PREVIEWICON = "previewIcon";
    public static final String AVAILABLECONTENTSLOTS = "availableContentSlots";
    public static final String RESTRICTEDPAGETYPES = "restrictedPageTypes";


    public PageTemplateModel()
    {
    }


    public PageTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PageTemplateModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PageTemplateModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "availableContentSlots", type = Accessor.Type.GETTER)
    public List<ContentSlotNameModel> getAvailableContentSlots()
    {
        return (List<ContentSlotNameModel>)getPersistenceContext().getPropertyValue("availableContentSlots");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "contentSlots", type = Accessor.Type.GETTER)
    public List<ContentSlotForTemplateModel> getContentSlots()
    {
        return (List<ContentSlotForTemplateModel>)getPersistenceContext().getPropertyValue("contentSlots");
    }


    @Accessor(qualifier = "frontendTemplateName", type = Accessor.Type.GETTER)
    public String getFrontendTemplateName()
    {
        return (String)getPersistenceContext().getPropertyValue("frontendTemplateName");
    }


    @Accessor(qualifier = "previewIcon", type = Accessor.Type.GETTER)
    public MediaModel getPreviewIcon()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("previewIcon");
    }


    @Accessor(qualifier = "restrictedPageTypes", type = Accessor.Type.GETTER)
    public Set<CMSPageTypeModel> getRestrictedPageTypes()
    {
        return (Set<CMSPageTypeModel>)getPersistenceContext().getPropertyValue("restrictedPageTypes");
    }


    @Accessor(qualifier = "velocityTemplate", type = Accessor.Type.GETTER)
    public String getVelocityTemplate()
    {
        return (String)getPersistenceContext().getPropertyValue("velocityTemplate");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "availableContentSlots", type = Accessor.Type.SETTER)
    public void setAvailableContentSlots(List<ContentSlotNameModel> value)
    {
        getPersistenceContext().setPropertyValue("availableContentSlots", value);
    }


    @Accessor(qualifier = "frontendTemplateName", type = Accessor.Type.SETTER)
    public void setFrontendTemplateName(String value)
    {
        getPersistenceContext().setPropertyValue("frontendTemplateName", value);
    }


    @Accessor(qualifier = "previewIcon", type = Accessor.Type.SETTER)
    public void setPreviewIcon(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("previewIcon", value);
    }


    @Accessor(qualifier = "restrictedPageTypes", type = Accessor.Type.SETTER)
    public void setRestrictedPageTypes(Set<CMSPageTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictedPageTypes", value);
    }


    @Accessor(qualifier = "velocityTemplate", type = Accessor.Type.SETTER)
    public void setVelocityTemplate(String value)
    {
        getPersistenceContext().setPropertyValue("velocityTemplate", value);
    }
}
