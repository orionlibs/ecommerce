package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class SAPCpiOutboundB2BCustomerModel extends SAPCpiOutboundCustomerModel
{
    public static final String _TYPECODE = "SAPCpiOutboundB2BCustomer";
    public static final String EMAIL = "email";
    public static final String DEFAULTB2BUNIT = "defaultB2BUnit";
    public static final String GROUPS = "groups";
    public static final String SAPCPIOUTBOUNDB2BCONTACTS = "sapCpiOutboundB2BContacts";


    public SAPCpiOutboundB2BCustomerModel()
    {
    }


    public SAPCpiOutboundB2BCustomerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundB2BCustomerModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "defaultB2BUnit", type = Accessor.Type.GETTER)
    public String getDefaultB2BUnit()
    {
        return (String)getPersistenceContext().getPropertyValue("defaultB2BUnit");
    }


    @Accessor(qualifier = "email", type = Accessor.Type.GETTER)
    public String getEmail()
    {
        return (String)getPersistenceContext().getPropertyValue("email");
    }


    @Accessor(qualifier = "groups", type = Accessor.Type.GETTER)
    public String getGroups()
    {
        return (String)getPersistenceContext().getPropertyValue("groups");
    }


    @Accessor(qualifier = "sapCpiOutboundB2BContacts", type = Accessor.Type.GETTER)
    public Set<SAPCpiOutboundB2BContactModel> getSapCpiOutboundB2BContacts()
    {
        return (Set<SAPCpiOutboundB2BContactModel>)getPersistenceContext().getPropertyValue("sapCpiOutboundB2BContacts");
    }


    @Accessor(qualifier = "defaultB2BUnit", type = Accessor.Type.SETTER)
    public void setDefaultB2BUnit(String value)
    {
        getPersistenceContext().setPropertyValue("defaultB2BUnit", value);
    }


    @Accessor(qualifier = "email", type = Accessor.Type.SETTER)
    public void setEmail(String value)
    {
        getPersistenceContext().setPropertyValue("email", value);
    }


    @Accessor(qualifier = "groups", type = Accessor.Type.SETTER)
    public void setGroups(String value)
    {
        getPersistenceContext().setPropertyValue("groups", value);
    }


    @Accessor(qualifier = "sapCpiOutboundB2BContacts", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundB2BContacts(Set<SAPCpiOutboundB2BContactModel> value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundB2BContacts", value);
    }
}
