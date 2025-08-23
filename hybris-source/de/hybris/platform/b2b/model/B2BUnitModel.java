package de.hybris.platform.b2b.model;

import com.olympus.oca.commerce.core.model.ShippingCarrierAccountModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Set;

public class B2BUnitModel extends OrgUnitModel
{
    public static final String _TYPECODE = "B2BUnit";
    public static final String REPORTINGORGANIZATION = "reportingOrganization";
    public static final String USERGROUPS = "UserGroups";
    public static final String COSTCENTERS = "CostCenters";
    public static final String BUDGETS = "Budgets";
    public static final String ORDERS = "Orders";
    public static final String CREDITLIMIT = "CreditLimit";
    public static final String QUOTELIMIT = "QuoteLimit";
    public static final String ACCOUNTMANAGER = "accountManager";
    public static final String ACCOUNTMANAGERGROUPS = "AccountManagerGroups";
    public static final String APPROVALPROCESSCODE = "approvalProcessCode";
    public static final String APPROVERGROUPS = "ApproverGroups";
    public static final String PERMISSIONS = "Permissions";
    public static final String APPROVERS = "Approvers";
    public static final String SHIPPINGCARRIERACCOUNTREFERENCE = "shippingCarrierAccountReference";


    public B2BUnitModel()
    {
    }


    public B2BUnitModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BUnitModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BUnitModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "accountManager", type = Accessor.Type.GETTER)
    public EmployeeModel getAccountManager()
    {
        return (EmployeeModel)getPersistenceContext().getPropertyValue("accountManager");
    }


    @Accessor(qualifier = "AccountManagerGroups", type = Accessor.Type.GETTER)
    public Set<UserGroupModel> getAccountManagerGroups()
    {
        return (Set<UserGroupModel>)getPersistenceContext().getPropertyValue("AccountManagerGroups");
    }


    @Accessor(qualifier = "approvalProcessCode", type = Accessor.Type.GETTER)
    public String getApprovalProcessCode()
    {
        return (String)getPersistenceContext().getPropertyValue("approvalProcessCode");
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


    @Accessor(qualifier = "Budgets", type = Accessor.Type.GETTER)
    public List<B2BBudgetModel> getBudgets()
    {
        return (List<B2BBudgetModel>)getPersistenceContext().getPropertyValue("Budgets");
    }


    @Accessor(qualifier = "CostCenters", type = Accessor.Type.GETTER)
    public List<B2BCostCenterModel> getCostCenters()
    {
        return (List<B2BCostCenterModel>)getPersistenceContext().getPropertyValue("CostCenters");
    }


    @Accessor(qualifier = "CreditLimit", type = Accessor.Type.GETTER)
    public B2BCreditLimitModel getCreditLimit()
    {
        return (B2BCreditLimitModel)getPersistenceContext().getPropertyValue("CreditLimit");
    }


    @Accessor(qualifier = "Orders", type = Accessor.Type.GETTER)
    public Set<AbstractOrderModel> getOrders()
    {
        return (Set<AbstractOrderModel>)getPersistenceContext().getPropertyValue("Orders");
    }


    @Accessor(qualifier = "Permissions", type = Accessor.Type.GETTER)
    public Set<B2BPermissionModel> getPermissions()
    {
        return (Set<B2BPermissionModel>)getPersistenceContext().getPropertyValue("Permissions");
    }


    @Accessor(qualifier = "QuoteLimit", type = Accessor.Type.GETTER)
    public B2BQuoteLimitModel getQuoteLimit()
    {
        return (B2BQuoteLimitModel)getPersistenceContext().getPropertyValue("QuoteLimit");
    }


    @Accessor(qualifier = "reportingOrganization", type = Accessor.Type.GETTER)
    public B2BUnitModel getReportingOrganization()
    {
        return (B2BUnitModel)getPersistenceContext().getPropertyValue("reportingOrganization");
    }


    @Accessor(qualifier = "shippingCarrierAccountReference", type = Accessor.Type.GETTER)
    public ShippingCarrierAccountModel getShippingCarrierAccountReference()
    {
        return (ShippingCarrierAccountModel)getPersistenceContext().getPropertyValue("shippingCarrierAccountReference");
    }


    @Accessor(qualifier = "UserGroups", type = Accessor.Type.GETTER)
    public Set<B2BUserGroupModel> getUserGroups()
    {
        return (Set<B2BUserGroupModel>)getPersistenceContext().getPropertyValue("UserGroups");
    }


    @Accessor(qualifier = "accountManager", type = Accessor.Type.SETTER)
    public void setAccountManager(EmployeeModel value)
    {
        getPersistenceContext().setPropertyValue("accountManager", value);
    }


    @Accessor(qualifier = "AccountManagerGroups", type = Accessor.Type.SETTER)
    public void setAccountManagerGroups(Set<UserGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("AccountManagerGroups", value);
    }


    @Accessor(qualifier = "approvalProcessCode", type = Accessor.Type.SETTER)
    public void setApprovalProcessCode(String value)
    {
        getPersistenceContext().setPropertyValue("approvalProcessCode", value);
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


    @Accessor(qualifier = "Budgets", type = Accessor.Type.SETTER)
    public void setBudgets(List<B2BBudgetModel> value)
    {
        getPersistenceContext().setPropertyValue("Budgets", value);
    }


    @Accessor(qualifier = "CostCenters", type = Accessor.Type.SETTER)
    public void setCostCenters(List<B2BCostCenterModel> value)
    {
        getPersistenceContext().setPropertyValue("CostCenters", value);
    }


    @Accessor(qualifier = "CreditLimit", type = Accessor.Type.SETTER)
    public void setCreditLimit(B2BCreditLimitModel value)
    {
        getPersistenceContext().setPropertyValue("CreditLimit", value);
    }


    @Accessor(qualifier = "Orders", type = Accessor.Type.SETTER)
    public void setOrders(Set<AbstractOrderModel> value)
    {
        getPersistenceContext().setPropertyValue("Orders", value);
    }


    @Accessor(qualifier = "Permissions", type = Accessor.Type.SETTER)
    public void setPermissions(Set<B2BPermissionModel> value)
    {
        getPersistenceContext().setPropertyValue("Permissions", value);
    }


    @Accessor(qualifier = "QuoteLimit", type = Accessor.Type.SETTER)
    public void setQuoteLimit(B2BQuoteLimitModel value)
    {
        getPersistenceContext().setPropertyValue("QuoteLimit", value);
    }


    @Accessor(qualifier = "reportingOrganization", type = Accessor.Type.SETTER)
    public void setReportingOrganization(B2BUnitModel value)
    {
        getPersistenceContext().setPropertyValue("reportingOrganization", value);
    }


    @Accessor(qualifier = "shippingCarrierAccountReference", type = Accessor.Type.SETTER)
    public void setShippingCarrierAccountReference(ShippingCarrierAccountModel value)
    {
        getPersistenceContext().setPropertyValue("shippingCarrierAccountReference", value);
    }


    @Accessor(qualifier = "UserGroups", type = Accessor.Type.SETTER)
    public void setUserGroups(Set<B2BUserGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("UserGroups", value);
    }
}
