package de.hybris.platform.b2b.model;

import com.olympus.oca.commerce.core.model.AccountPreferencesModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

public class B2BCustomerModel extends CustomerModel
{
    public static final String _TYPECODE = "B2BCustomer";
    public static final String ACTIVE = "active";
    public static final String EMAIL = "email";
    public static final String DEFAULTB2BUNIT = "defaultB2BUnit";
    public static final String APPROVERGROUPS = "ApproverGroups";
    public static final String APPROVERS = "Approvers";
    public static final String PERMISSIONGROUPS = "PermissionGroups";
    public static final String PERMISSIONS = "Permissions";
    public static final String ACCOUNTPREFERENCES = "accountPreferences";
    public static final String SAPBUSINESSPARTNERID = "sapBusinessPartnerID";


    public B2BCustomerModel()
    {
    }


    public B2BCustomerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCustomerModel(String _email, boolean _loginDisabled, String _uid)
    {
        setEmail(_email);
        setLoginDisabled(_loginDisabled);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BCustomerModel(Collection<CustomerReviewModel> _customerReviews, String _email, boolean _loginDisabled, ItemModel _owner, String _uid)
    {
        setCustomerReviews(_customerReviews);
        setEmail(_email);
        setLoginDisabled(_loginDisabled);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "accountPreferences", type = Accessor.Type.GETTER)
    public AccountPreferencesModel getAccountPreferences()
    {
        return (AccountPreferencesModel)getPersistenceContext().getPropertyValue("accountPreferences");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "ApproverGroups", type = Accessor.Type.GETTER)
    public Set<B2BUserGroupModel> getApproverGroups()
    {
        return (Set<B2BUserGroupModel>)getPersistenceContext().getPropertyValue("ApproverGroups");
    }


    @Accessor(qualifier = "Approvers", type = Accessor.Type.GETTER)
    public Set<B2BCustomerModel> getApprovers()
    {
        return (Set<B2BCustomerModel>)getPersistenceContext().getPropertyValue("Approvers");
    }


    @Accessor(qualifier = "defaultB2BUnit", type = Accessor.Type.GETTER)
    public B2BUnitModel getDefaultB2BUnit()
    {
        return (B2BUnitModel)getPersistenceContext().getPropertyValue("defaultB2BUnit");
    }


    @Accessor(qualifier = "email", type = Accessor.Type.GETTER)
    public String getEmail()
    {
        return (String)getPersistenceContext().getPropertyValue("email");
    }


    @Accessor(qualifier = "PermissionGroups", type = Accessor.Type.GETTER)
    public Set<B2BUserGroupModel> getPermissionGroups()
    {
        return (Set<B2BUserGroupModel>)getPersistenceContext().getPropertyValue("PermissionGroups");
    }


    @Accessor(qualifier = "Permissions", type = Accessor.Type.GETTER)
    public Set<B2BPermissionModel> getPermissions()
    {
        return (Set<B2BPermissionModel>)getPersistenceContext().getPropertyValue("Permissions");
    }


    @Accessor(qualifier = "sapBusinessPartnerID", type = Accessor.Type.GETTER)
    public String getSapBusinessPartnerID()
    {
        return (String)getPersistenceContext().getPropertyValue("sapBusinessPartnerID");
    }


    @Accessor(qualifier = "accountPreferences", type = Accessor.Type.SETTER)
    public void setAccountPreferences(AccountPreferencesModel value)
    {
        getPersistenceContext().setPropertyValue("accountPreferences", value);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "ApproverGroups", type = Accessor.Type.SETTER)
    public void setApproverGroups(Set<B2BUserGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("ApproverGroups", value);
    }


    @Accessor(qualifier = "Approvers", type = Accessor.Type.SETTER)
    public void setApprovers(Set<B2BCustomerModel> value)
    {
        getPersistenceContext().setPropertyValue("Approvers", value);
    }


    @Accessor(qualifier = "defaultB2BUnit", type = Accessor.Type.SETTER)
    public void setDefaultB2BUnit(B2BUnitModel value)
    {
        getPersistenceContext().setPropertyValue("defaultB2BUnit", value);
    }


    @Accessor(qualifier = "email", type = Accessor.Type.SETTER)
    public void setEmail(String value)
    {
        getPersistenceContext().setPropertyValue("email", value);
    }


    @Accessor(qualifier = "PermissionGroups", type = Accessor.Type.SETTER)
    public void setPermissionGroups(Set<B2BUserGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("PermissionGroups", value);
    }


    @Accessor(qualifier = "Permissions", type = Accessor.Type.SETTER)
    public void setPermissions(Set<B2BPermissionModel> value)
    {
        getPersistenceContext().setPropertyValue("Permissions", value);
    }


    @Accessor(qualifier = "sapBusinessPartnerID", type = Accessor.Type.SETTER)
    public void setSapBusinessPartnerID(String value)
    {
        getPersistenceContext().setPropertyValue("sapBusinessPartnerID", value);
    }
}
