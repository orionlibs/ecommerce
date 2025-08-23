package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.model.actions.AbstractCMSActionModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class AbstractCMSComponentModel extends CMSItemModel
{
    public static final String _TYPECODE = "AbstractCMSComponent";
    public static final String _ELEMENTSFORSLOT = "ElementsForSlot";
    public static final String _CMSCOMPONENTCHILDRENFORCMSCOMPONENT = "CMSComponentChildrenForCMSComponent";
    public static final String VISIBLE = "visible";
    public static final String ONLYONERESTRICTIONMUSTAPPLY = "onlyOneRestrictionMustApply";
    public static final String STYLECLASSES = "styleClasses";
    public static final String TYPE = "type";
    public static final String TYPECODE = "typeCode";
    public static final String CONTAINER = "container";
    public static final String RESTRICTED = "restricted";
    public static final String SLOTS = "slots";
    public static final String PARENTS = "parents";
    public static final String CHILDREN = "children";
    public static final String RESTRICTIONS = "restrictions";
    public static final String ACTIONS = "actions";


    public AbstractCMSComponentModel()
    {
    }


    public AbstractCMSComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractCMSComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractCMSComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
    public List<AbstractCMSActionModel> getActions()
    {
        return (List<AbstractCMSActionModel>)getPersistenceContext().getPropertyValue("actions");
    }


    @Accessor(qualifier = "children", type = Accessor.Type.GETTER)
    public List<AbstractCMSComponentModel> getChildren()
    {
        return (List<AbstractCMSComponentModel>)getPersistenceContext().getPropertyValue("children");
    }


    @Accessor(qualifier = "parents", type = Accessor.Type.GETTER)
    public List<AbstractCMSComponentModel> getParents()
    {
        return (List<AbstractCMSComponentModel>)getPersistenceContext().getPropertyValue("parents");
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public List<AbstractRestrictionModel> getRestrictions()
    {
        return (List<AbstractRestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "slots", type = Accessor.Type.GETTER)
    public Collection<ContentSlotModel> getSlots()
    {
        return (Collection<ContentSlotModel>)getPersistenceContext().getPropertyValue("slots");
    }


    @Accessor(qualifier = "styleClasses", type = Accessor.Type.GETTER)
    public String getStyleClasses()
    {
        return (String)getPersistenceContext().getPropertyValue("styleClasses");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public String getType()
    {
        return getType(null);
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public String getType(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("type", loc);
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
    public String getTypeCode()
    {
        return (String)getPersistenceContext().getPropertyValue("typeCode");
    }


    @Accessor(qualifier = "visible", type = Accessor.Type.GETTER)
    public Boolean getVisible()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("visible");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "container", type = Accessor.Type.GETTER)
    public boolean isContainer()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("container"));
    }


    @Accessor(qualifier = "onlyOneRestrictionMustApply", type = Accessor.Type.GETTER)
    public boolean isOnlyOneRestrictionMustApply()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("onlyOneRestrictionMustApply"));
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "restricted", type = Accessor.Type.GETTER)
    public boolean isRestricted()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("restricted"));
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
    public void setActions(List<AbstractCMSActionModel> value)
    {
        getPersistenceContext().setPropertyValue("actions", value);
    }


    @Accessor(qualifier = "children", type = Accessor.Type.SETTER)
    public void setChildren(List<AbstractCMSComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("children", value);
    }


    @Accessor(qualifier = "onlyOneRestrictionMustApply", type = Accessor.Type.SETTER)
    public void setOnlyOneRestrictionMustApply(boolean value)
    {
        getPersistenceContext().setPropertyValue("onlyOneRestrictionMustApply", toObject(value));
    }


    @Accessor(qualifier = "parents", type = Accessor.Type.SETTER)
    public void setParents(List<AbstractCMSComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("parents", value);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(List<AbstractRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }


    @Accessor(qualifier = "slots", type = Accessor.Type.SETTER)
    public void setSlots(Collection<ContentSlotModel> value)
    {
        getPersistenceContext().setPropertyValue("slots", value);
    }


    @Accessor(qualifier = "styleClasses", type = Accessor.Type.SETTER)
    public void setStyleClasses(String value)
    {
        getPersistenceContext().setPropertyValue("styleClasses", value);
    }


    @Accessor(qualifier = "visible", type = Accessor.Type.SETTER)
    public void setVisible(Boolean value)
    {
        getPersistenceContext().setPropertyValue("visible", value);
    }
}
