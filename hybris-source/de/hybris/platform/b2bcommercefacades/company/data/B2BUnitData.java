package de.hybris.platform.b2bcommercefacades.company.data;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CompanyData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.commercefacades.user.data.UserGroupData;
import java.util.Collection;
import java.util.List;

public class B2BUnitData extends CompanyData
{
    private B2BUnitData unit;
    private boolean active;
    private boolean selected;
    private boolean selectable;
    private B2BUnitData reportingOrganization;
    private List<B2BBudgetData> budgets;
    private Collection<AbstractOrderData> orders;
    private List<B2BCostCenterData> costCenters;
    private Collection<B2BUserGroupData> userGroups;
    private Collection<UserGroupData> accountManagerGroups;
    private List<B2BUnitData> children;
    private Collection<CustomerData> administrators;
    private Collection<CustomerData> customers;
    private List<CustomerData> managers;
    private List<PrincipalData> accountManagers;
    private List<AddressData> addresses;
    private String approvalProcessCode;
    private String approvalProcessName;
    private Collection<CustomerData> approvers;
    private Collection<B2BUserGroupData> approverGroups;
    private boolean defaultUnit;


    public void setUnit(B2BUnitData unit)
    {
        this.unit = unit;
    }


    public B2BUnitData getUnit()
    {
        return this.unit;
    }


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    public boolean isSelected()
    {
        return this.selected;
    }


    public void setSelectable(boolean selectable)
    {
        this.selectable = selectable;
    }


    public boolean isSelectable()
    {
        return this.selectable;
    }


    public void setReportingOrganization(B2BUnitData reportingOrganization)
    {
        this.reportingOrganization = reportingOrganization;
    }


    public B2BUnitData getReportingOrganization()
    {
        return this.reportingOrganization;
    }


    public void setBudgets(List<B2BBudgetData> budgets)
    {
        this.budgets = budgets;
    }


    public List<B2BBudgetData> getBudgets()
    {
        return this.budgets;
    }


    public void setOrders(Collection<AbstractOrderData> orders)
    {
        this.orders = orders;
    }


    public Collection<AbstractOrderData> getOrders()
    {
        return this.orders;
    }


    public void setCostCenters(List<B2BCostCenterData> costCenters)
    {
        this.costCenters = costCenters;
    }


    public List<B2BCostCenterData> getCostCenters()
    {
        return this.costCenters;
    }


    public void setUserGroups(Collection<B2BUserGroupData> userGroups)
    {
        this.userGroups = userGroups;
    }


    public Collection<B2BUserGroupData> getUserGroups()
    {
        return this.userGroups;
    }


    public void setAccountManagerGroups(Collection<UserGroupData> accountManagerGroups)
    {
        this.accountManagerGroups = accountManagerGroups;
    }


    public Collection<UserGroupData> getAccountManagerGroups()
    {
        return this.accountManagerGroups;
    }


    public void setChildren(List<B2BUnitData> children)
    {
        this.children = children;
    }


    public List<B2BUnitData> getChildren()
    {
        return this.children;
    }


    public void setAdministrators(Collection<CustomerData> administrators)
    {
        this.administrators = administrators;
    }


    public Collection<CustomerData> getAdministrators()
    {
        return this.administrators;
    }


    public void setCustomers(Collection<CustomerData> customers)
    {
        this.customers = customers;
    }


    public Collection<CustomerData> getCustomers()
    {
        return this.customers;
    }


    public void setManagers(List<CustomerData> managers)
    {
        this.managers = managers;
    }


    public List<CustomerData> getManagers()
    {
        return this.managers;
    }


    public void setAccountManagers(List<PrincipalData> accountManagers)
    {
        this.accountManagers = accountManagers;
    }


    public List<PrincipalData> getAccountManagers()
    {
        return this.accountManagers;
    }


    public void setAddresses(List<AddressData> addresses)
    {
        this.addresses = addresses;
    }


    public List<AddressData> getAddresses()
    {
        return this.addresses;
    }


    public void setApprovalProcessCode(String approvalProcessCode)
    {
        this.approvalProcessCode = approvalProcessCode;
    }


    public String getApprovalProcessCode()
    {
        return this.approvalProcessCode;
    }


    public void setApprovalProcessName(String approvalProcessName)
    {
        this.approvalProcessName = approvalProcessName;
    }


    public String getApprovalProcessName()
    {
        return this.approvalProcessName;
    }


    public void setApprovers(Collection<CustomerData> approvers)
    {
        this.approvers = approvers;
    }


    public Collection<CustomerData> getApprovers()
    {
        return this.approvers;
    }


    public void setApproverGroups(Collection<B2BUserGroupData> approverGroups)
    {
        this.approverGroups = approverGroups;
    }


    public Collection<B2BUserGroupData> getApproverGroups()
    {
        return this.approverGroups;
    }


    public void setDefaultUnit(boolean defaultUnit)
    {
        this.defaultUnit = defaultUnit;
    }


    public boolean isDefaultUnit()
    {
        return this.defaultUnit;
    }
}
