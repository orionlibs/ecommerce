package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class AbstractRestrictionModel extends CMSItemModel
{
    public static final String _TYPECODE = "AbstractRestriction";
    public static final String _RESTRICTIONSFORPAGES = "RestrictionsForPages";
    public static final String _RESTRICTIONSFORCOMPONENTS = "RestrictionsForComponents";
    public static final String TYPE = "type";
    public static final String TYPECODE = "typeCode";
    public static final String DESCRIPTION = "description";
    public static final String PAGES = "pages";
    public static final String COMPONENTS = "components";
    public static final String INVERSERESTRICTIONS = "inverseRestrictions";


    public AbstractRestrictionModel()
    {
    }


    public AbstractRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRestrictionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRestrictionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "components", type = Accessor.Type.GETTER)
    public Collection<AbstractCMSComponentModel> getComponents()
    {
        return (Collection<AbstractCMSComponentModel>)getPersistenceContext().getPropertyValue("components");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "description");
    }


    @Accessor(qualifier = "inverseRestrictions", type = Accessor.Type.GETTER)
    public Collection<CMSInverseRestrictionModel> getInverseRestrictions()
    {
        return (Collection<CMSInverseRestrictionModel>)getPersistenceContext().getPropertyValue("inverseRestrictions");
    }


    @Accessor(qualifier = "pages", type = Accessor.Type.GETTER)
    public Collection<AbstractPageModel> getPages()
    {
        return (Collection<AbstractPageModel>)getPersistenceContext().getPropertyValue("pages");
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


    @Accessor(qualifier = "components", type = Accessor.Type.SETTER)
    public void setComponents(Collection<AbstractCMSComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("components", value);
    }


    @Accessor(qualifier = "inverseRestrictions", type = Accessor.Type.SETTER)
    public void setInverseRestrictions(Collection<CMSInverseRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("inverseRestrictions", value);
    }


    @Accessor(qualifier = "pages", type = Accessor.Type.SETTER)
    public void setPages(Collection<AbstractPageModel> value)
    {
        getPersistenceContext().setPropertyValue("pages", value);
    }
}
