package de.hybris.platform.commerceservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrgUnitModel extends CompanyModel
{
    public static final String _TYPECODE = "OrgUnit";
    public static final String ACTIVE = "active";
    public static final String PATH = "path";


    public OrgUnitModel()
    {
    }


    public OrgUnitModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrgUnitModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrgUnitModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "path", type = Accessor.Type.GETTER)
    public String getPath()
    {
        return (String)getPersistenceContext().getPropertyValue("path");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "path", type = Accessor.Type.SETTER)
    public void setPath(String value)
    {
        getPersistenceContext().setPropertyValue("path", value);
    }
}
