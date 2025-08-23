package de.hybris.platform.cms2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.model.CMSActionTypeModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

public class CMSComponentTypeModel extends ComposedTypeModel
{
    public static final String _TYPECODE = "CMSComponentType";
    public static final String _VALIDCOMPONENTTYPESFORSITE = "ValidComponentTypesForSite";
    public static final String _VALIDCOMPONENTTYPESFORCONTENTSLOTS = "ValidComponentTypesForContentSlots";
    public static final String _COMPONENTTYPEGROUPS2COMPONENTTYPE = "ComponentTypeGroups2ComponentType";
    public static final String _APPLICABLECMSACTIONSTYPEFORCMSCOMPONENT = "ApplicableCmsActionsTypeForCmsComponent";
    public static final String CMSSITES = "cmsSites";
    public static final String CONTENTSLOTNAMES = "contentSlotNames";
    public static final String COMPONENTTYPEGROUPS = "componentTypeGroups";
    public static final String ACTIONTYPES = "actionTypes";


    public CMSComponentTypeModel()
    {
    }


    public CMSComponentTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSComponentTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSComponentTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, ItemModel _owner, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Accessor(qualifier = "actionTypes", type = Accessor.Type.GETTER)
    public Collection<CMSActionTypeModel> getActionTypes()
    {
        return (Collection<CMSActionTypeModel>)getPersistenceContext().getPropertyValue("actionTypes");
    }


    @Accessor(qualifier = "cmsSites", type = Accessor.Type.GETTER)
    public Set<CMSSiteModel> getCmsSites()
    {
        return (Set<CMSSiteModel>)getPersistenceContext().getPropertyValue("cmsSites");
    }


    @Accessor(qualifier = "componentTypeGroups", type = Accessor.Type.GETTER)
    public Set<ComponentTypeGroupModel> getComponentTypeGroups()
    {
        return (Set<ComponentTypeGroupModel>)getPersistenceContext().getPropertyValue("componentTypeGroups");
    }


    @Accessor(qualifier = "contentSlotNames", type = Accessor.Type.GETTER)
    public Set<ContentSlotNameModel> getContentSlotNames()
    {
        return (Set<ContentSlotNameModel>)getPersistenceContext().getPropertyValue("contentSlotNames");
    }


    @Accessor(qualifier = "actionTypes", type = Accessor.Type.SETTER)
    public void setActionTypes(Collection<CMSActionTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("actionTypes", value);
    }


    @Accessor(qualifier = "cmsSites", type = Accessor.Type.SETTER)
    public void setCmsSites(Set<CMSSiteModel> value)
    {
        getPersistenceContext().setPropertyValue("cmsSites", value);
    }


    @Accessor(qualifier = "componentTypeGroups", type = Accessor.Type.SETTER)
    public void setComponentTypeGroups(Set<ComponentTypeGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("componentTypeGroups", value);
    }


    @Accessor(qualifier = "contentSlotNames", type = Accessor.Type.SETTER)
    public void setContentSlotNames(Set<ContentSlotNameModel> value)
    {
        getPersistenceContext().setPropertyValue("contentSlotNames", value);
    }
}
