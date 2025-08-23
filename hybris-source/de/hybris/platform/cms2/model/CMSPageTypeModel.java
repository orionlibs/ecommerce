package de.hybris.platform.cms2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

public class CMSPageTypeModel extends ComposedTypeModel
{
    public static final String _TYPECODE = "CMSPageType";
    public static final String _APPLICABLERESTRICTIONTYPEFORPAGETYPES = "ApplicableRestrictionTypeForPageTypes";
    public static final String _VALIDPAGETYPESFORTEMPLATES = "ValidPageTypesForTemplates";
    public static final String PREVIEWDISABLED = "previewDisabled";
    public static final String RESTRICTIONTYPES = "restrictionTypes";
    public static final String TEMPLATES = "templates";


    public CMSPageTypeModel()
    {
    }


    public CMSPageTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSPageTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSPageTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, ItemModel _owner, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Accessor(qualifier = "restrictionTypes", type = Accessor.Type.GETTER)
    public Collection<RestrictionTypeModel> getRestrictionTypes()
    {
        return (Collection<RestrictionTypeModel>)getPersistenceContext().getPropertyValue("restrictionTypes");
    }


    @Accessor(qualifier = "templates", type = Accessor.Type.GETTER)
    public Set<PageTemplateModel> getTemplates()
    {
        return (Set<PageTemplateModel>)getPersistenceContext().getPropertyValue("templates");
    }


    @Accessor(qualifier = "previewDisabled", type = Accessor.Type.GETTER)
    public boolean isPreviewDisabled()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("previewDisabled"));
    }


    @Accessor(qualifier = "previewDisabled", type = Accessor.Type.SETTER)
    public void setPreviewDisabled(boolean value)
    {
        getPersistenceContext().setPropertyValue("previewDisabled", toObject(value));
    }


    @Accessor(qualifier = "restrictionTypes", type = Accessor.Type.SETTER)
    public void setRestrictionTypes(Collection<RestrictionTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictionTypes", value);
    }


    @Accessor(qualifier = "templates", type = Accessor.Type.SETTER)
    public void setTemplates(Set<PageTemplateModel> value)
    {
        getPersistenceContext().setPropertyValue("templates", value);
    }
}
