package de.hybris.platform.commerceservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CustomerListModel extends UserGroupModel
{
    public static final String _TYPECODE = "CustomerList";
    public static final String IMPLEMENTATIONTYPE = "implementationType";
    public static final String PRIORITY = "priority";
    public static final String ADDITIONALCOLUMNSKEYS = "additionalColumnsKeys";
    public static final String SEARCHBOXENABLED = "searchBoxEnabled";


    public CustomerListModel()
    {
    }


    public CustomerListModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CustomerListModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CustomerListModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "additionalColumnsKeys", type = Accessor.Type.GETTER)
    public Collection<String> getAdditionalColumnsKeys()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("additionalColumnsKeys");
    }


    @Accessor(qualifier = "implementationType", type = Accessor.Type.GETTER)
    public String getImplementationType()
    {
        return (String)getPersistenceContext().getPropertyValue("implementationType");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "searchBoxEnabled", type = Accessor.Type.GETTER)
    public boolean isSearchBoxEnabled()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("searchBoxEnabled"));
    }


    @Accessor(qualifier = "additionalColumnsKeys", type = Accessor.Type.SETTER)
    public void setAdditionalColumnsKeys(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("additionalColumnsKeys", value);
    }


    @Accessor(qualifier = "implementationType", type = Accessor.Type.SETTER)
    public void setImplementationType(String value)
    {
        getPersistenceContext().setPropertyValue("implementationType", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "searchBoxEnabled", type = Accessor.Type.SETTER)
    public void setSearchBoxEnabled(boolean value)
    {
        getPersistenceContext().setPropertyValue("searchBoxEnabled", toObject(value));
    }
}
