package de.hybris.platform.commercefacades.user.data;

import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CustomerData extends PrincipalData
{
    private AddressData defaultBillingAddress;
    private AddressData defaultShippingAddress;
    private String titleCode;
    private String title;
    private String firstName;
    private String lastName;
    private CurrencyData currency;
    private LanguageData language;
    private String displayUid;
    private String customerId;
    private Date deactivationDate;
    private String normalizedUid;
    private B2BUnitData unit;
    private String email;
    private String contactNumber;
    private boolean active;
    private boolean selected;
    private Collection<String> roles;
    private Collection<String> displayRoles;
    private List<B2BUserGroupData> permissionGroups;
    private List<CustomerData> approvers;
    private List<B2BUserGroupData> approverGroups;
    private List<B2BPermissionData> permissions;
    private List<B2BUnitData> units;
    private String accountPreferences;
    private AddressData defaultAddress;
    private String latestCartId;
    private Boolean hasOrder;
    private ImageData profilePicture;


    public void setDefaultBillingAddress(AddressData defaultBillingAddress)
    {
        this.defaultBillingAddress = defaultBillingAddress;
    }


    public AddressData getDefaultBillingAddress()
    {
        return this.defaultBillingAddress;
    }


    public void setDefaultShippingAddress(AddressData defaultShippingAddress)
    {
        this.defaultShippingAddress = defaultShippingAddress;
    }


    public AddressData getDefaultShippingAddress()
    {
        return this.defaultShippingAddress;
    }


    public void setTitleCode(String titleCode)
    {
        this.titleCode = titleCode;
    }


    public String getTitleCode()
    {
        return this.titleCode;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getTitle()
    {
        return this.title;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    public String getFirstName()
    {
        return this.firstName;
    }


    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }


    public String getLastName()
    {
        return this.lastName;
    }


    public void setCurrency(CurrencyData currency)
    {
        this.currency = currency;
    }


    public CurrencyData getCurrency()
    {
        return this.currency;
    }


    public void setLanguage(LanguageData language)
    {
        this.language = language;
    }


    public LanguageData getLanguage()
    {
        return this.language;
    }


    public void setDisplayUid(String displayUid)
    {
        this.displayUid = displayUid;
    }


    public String getDisplayUid()
    {
        return this.displayUid;
    }


    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }


    public String getCustomerId()
    {
        return this.customerId;
    }


    public void setDeactivationDate(Date deactivationDate)
    {
        this.deactivationDate = deactivationDate;
    }


    public Date getDeactivationDate()
    {
        return this.deactivationDate;
    }


    public void setNormalizedUid(String normalizedUid)
    {
        this.normalizedUid = normalizedUid;
    }


    public String getNormalizedUid()
    {
        return this.normalizedUid;
    }


    public void setUnit(B2BUnitData unit)
    {
        this.unit = unit;
    }


    public B2BUnitData getUnit()
    {
        return this.unit;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setContactNumber(String contactNumber)
    {
        this.contactNumber = contactNumber;
    }


    public String getContactNumber()
    {
        return this.contactNumber;
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


    public void setRoles(Collection<String> roles)
    {
        this.roles = roles;
    }


    public Collection<String> getRoles()
    {
        return this.roles;
    }


    public void setDisplayRoles(Collection<String> displayRoles)
    {
        this.displayRoles = displayRoles;
    }


    public Collection<String> getDisplayRoles()
    {
        return this.displayRoles;
    }


    public void setPermissionGroups(List<B2BUserGroupData> permissionGroups)
    {
        this.permissionGroups = permissionGroups;
    }


    public List<B2BUserGroupData> getPermissionGroups()
    {
        return this.permissionGroups;
    }


    public void setApprovers(List<CustomerData> approvers)
    {
        this.approvers = approvers;
    }


    public List<CustomerData> getApprovers()
    {
        return this.approvers;
    }


    public void setApproverGroups(List<B2BUserGroupData> approverGroups)
    {
        this.approverGroups = approverGroups;
    }


    public List<B2BUserGroupData> getApproverGroups()
    {
        return this.approverGroups;
    }


    public void setPermissions(List<B2BPermissionData> permissions)
    {
        this.permissions = permissions;
    }


    public List<B2BPermissionData> getPermissions()
    {
        return this.permissions;
    }


    public void setUnits(List<B2BUnitData> units)
    {
        this.units = units;
    }


    public List<B2BUnitData> getUnits()
    {
        return this.units;
    }


    public void setAccountPreferences(String accountPreferences)
    {
        this.accountPreferences = accountPreferences;
    }


    public String getAccountPreferences()
    {
        return this.accountPreferences;
    }


    public void setDefaultAddress(AddressData defaultAddress)
    {
        this.defaultAddress = defaultAddress;
    }


    public AddressData getDefaultAddress()
    {
        return this.defaultAddress;
    }


    public void setLatestCartId(String latestCartId)
    {
        this.latestCartId = latestCartId;
    }


    public String getLatestCartId()
    {
        return this.latestCartId;
    }


    public void setHasOrder(Boolean hasOrder)
    {
        this.hasOrder = hasOrder;
    }


    public Boolean getHasOrder()
    {
        return this.hasOrder;
    }


    public void setProfilePicture(ImageData profilePicture)
    {
        this.profilePicture = profilePicture;
    }


    public ImageData getProfilePicture()
    {
        return this.profilePicture;
    }
}
